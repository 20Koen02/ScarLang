package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Node.*;
import nl.Koen02.ScarLang.Type.FunctionType;
import nl.Koen02.ScarLang.Type.NumberType;
import nl.Koen02.ScarLang.Type.StringType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Interpreter {
    public Type visit(Node node, Context context) throws Exception {
        if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node, context);
        } else if (node instanceof BinOpNode) {
            return visitBinOpNode((BinOpNode) node, context);
        } else if (node instanceof UnaryOpNode) {
            return visitUnaryOpNode((UnaryOpNode) node, context);
        } else if (node instanceof VarAccessNode) {
            return visitVarAccessNode((VarAccessNode) node, context);
        } else if (node instanceof VarAssignNode) {
            return visitVarAssignNode((VarAssignNode) node, context);
        } else if (node instanceof IfNode) {
            return visitIfNode((IfNode) node, context);
        } else if (node instanceof ForNode) {
            return visitForNode((ForNode) node, context);
        } else if (node instanceof WhileNode) {
            return visitWhileNode((WhileNode) node, context);
        } else if (node instanceof FuncDefNode) {
            return visitFuncDefNode((FuncDefNode) node, context);
        } else if (node instanceof CallNode) {
            return visitCallNode((CallNode) node, context);
        } else if (node instanceof StringNode) {
            return visitStringNode((StringNode) node, context);
        }
        return null;
    }

    public Type visitVarAccessNode(VarAccessNode node, Context context) throws Exception {
        String varName = node.varNameToken.value;
        Type value = context.symbolTable.get(varName);

        if (value == null) throw new RunTimeError(node.posStart, node.posEnd, String.format("'%s' is not defined", varName), context);

        value = value.getCopy().setPos(node.posStart, node.posEnd);
        return value;
    }

    public Type visitVarAssignNode(VarAssignNode node, Context context) throws Exception {
        String varName = node.varNameToken.value;
        Type value = visit(node.valueNode, context);

        context.symbolTable.set(varName, value);
        return value;
    }

    private Type visitStringNode(StringNode node, Context context) {
        return new StringType(node.tok.value).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public NumberType visitNumberNode(NumberNode node, Context context) {
        return (NumberType) new NumberType(Double.parseDouble(node.tok.value)).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public Type visitBinOpNode(BinOpNode node, Context context) throws Exception {
        Type left = visit(node.leftNode, context);
        Type right = visit(node.rightNode, context);

        Type number = null;
        if (TT_PLUS.equals(node.opTok.type)) {
            number = left.addedTo(right);
        } else if (TT_MIN.equals(node.opTok.type)) {
            number = left.subtractedBy(right);
        } else if (TT_MUL.equals(node.opTok.type)) {
            number = left.multipliedBy(right);
        } else if (TT_DIV.equals(node.opTok.type)) {
            number = left.dividedBy(right);
        } else if (TT_POW.equals(node.opTok.type)) {
            number = left.poweredBy(right);
        } else if (TT_EE.equals(node.opTok.type)) {
            number = left.getComparisonEe(right);
        } else if (TT_NE.equals(node.opTok.type)) {
            number = left.getComparisonNe(right);
        } else if (TT_LT.equals(node.opTok.type)) {
            number = left.getComparisonLt(right);
        } else if (TT_GT.equals(node.opTok.type)) {
            number = left.getComparisonGt(right);
        } else if (TT_LTE.equals(node.opTok.type)) {
            number = left.getComparisonLte(right);
        } else if (TT_GTE.equals(node.opTok.type)) {
            number = left.getComparisonGte(right);
        } else if (node.opTok.matches(TT_KEYWORD, "and")) {
            number = left.andOperated(right);
        } else if (node.opTok.matches(TT_KEYWORD, "or")) {
            number = left.orOperated(right);
        }

        if (number == null) return null;
        return number.setPos(node.posStart, node.posEnd);
    }

    public NumberType visitUnaryOpNode(UnaryOpNode node, Context context) throws Exception {
        NumberType number = (NumberType) visit(node.node, context);
        if (node.opTok.type.equals(TT_MIN)) {
            number = number.multipliedBy(new NumberType((double) -1));
        } else if (node.opTok.matches(TT_KEYWORD, "not")) {
            number = number.notOperated();
        }
        return (NumberType) number.setPos(node.posStart, node.posEnd);
    }

    private NumberType visitIfNode(IfNode node, Context context) throws Exception {
        for (ArrayList<Node> condExpr : node.cases) {
            Node condition = condExpr.get(0);
            Node expression = condExpr.get(1);
            NumberType conditionValue = (NumberType) visit(condition, context);
            if (conditionValue.is_true()) return (NumberType) visit(expression, context);
        }
        if (node.elseCase != null) return (NumberType) visit(node.elseCase, context);
        return null;
    }

    private NumberType visitForNode(ForNode node, Context context) throws Exception {
        NumberType startValue = (NumberType) visit(node.startValueNode, context);
        NumberType endValue = (NumberType) visit(node.endValueNode, context);
        NumberType stepValue = node.stepValueNode != null ? (NumberType) visit(node.stepValueNode, context) : new NumberType(1d);
        Double i = startValue.value;
        if (stepValue.value >= 0) {
            while (i < endValue.value) {
                context.symbolTable.set(node.varNameTok.value, new NumberType(i));
                i += stepValue.value;
                visit(node.bodyNode, context);
            }
        } else {
            while (i > endValue.value) {
                context.symbolTable.set(node.varNameTok.value, new NumberType(i));
                i += stepValue.value;
                visit(node.bodyNode, context);
            }
        }
        return null;
    }

    private NumberType visitWhileNode(WhileNode node, Context context) throws Exception {
        while (true) {
            NumberType condition = (NumberType) visit(node.conditionNode, context);
            if (!condition.is_true()) break;
            visit(node.bodyNode, context);
        }
        return null;
    }

    private FunctionType visitFuncDefNode(FuncDefNode node, Context context) {
        String funcName = node.varNameTok != null ? node.varNameTok.value : null;
        Node bodyNode = node.bodyNode;
        ArrayList<String> argNames = (ArrayList<String>) node.argNameToks.stream().map(argName -> argName.value).collect(Collectors.toList());
        FunctionType func_value = (FunctionType) new FunctionType(funcName, bodyNode, argNames).setContext(context).setPos(node.posStart, node.posEnd);

        if (node.varNameTok != null) context.symbolTable.set(funcName, func_value);
        return func_value;
    }

    private Type visitCallNode(CallNode node, Context context) throws Exception {
        ArrayList<Type> args = new ArrayList<>();
        FunctionType valueToCall = (FunctionType) visit(node.nodeToCall, context);
        valueToCall = (FunctionType) valueToCall.getCopy().setPos(node.posStart, node.posEnd);

        for (Node argNode : node.argNodes) {
            args.add(visit(argNode, context));
        }

        return valueToCall.execute(args);
    }
}
