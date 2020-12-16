package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Node.*;
import nl.Koen02.ScarLang.Type.*;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.Function.FunctionType;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Interpreter {
    public Type visit(Node node, Context context) throws Exception {
        if (node instanceof IntegerNode) {
            return visitIntegerNode((IntegerNode) node, context);
        } else if (node instanceof FloatNode) {
            return visitFloatNode((FloatNode) node, context);
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
        } else if (node instanceof ArrayNode) {
            return visitArrayNode((ArrayNode) node, context);
        }
        return null;
    }

    public Type visitVarAccessNode(VarAccessNode node, Context context) throws Exception {
        String varName = node.varNameToken.value;
        Type value = context.symbolTable.get(varName);

        if (value == null) throw new RunTimeError(node.posStart, node.posEnd, String.format("'%s' is not defined", varName), context);

        value = value.getCopy().setPos(node.posStart, node.posEnd).setContext(context);
        return value;
    }

    public Type visitVarAssignNode(VarAssignNode node, Context context) throws Exception {
        String varName = node.varNameToken.value;
        Type value = visit(node.valueNode, context);

        context.symbolTable.set(varName, value);
        return value;
    }

    private StringType visitStringNode(StringNode node, Context context) {
        return (StringType) new StringType(node.tok.value).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public IntegerType visitIntegerNode(IntegerNode node, Context context) {
        return (IntegerType) new IntegerType(Integer.parseInt(node.tok.value)).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public FloatType visitFloatNode(FloatNode node, Context context) {
        return (FloatType) new FloatType(Float.parseFloat(node.tok.value)).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public ArrayType visitArrayNode(ArrayNode node, Context context) throws Exception {
        ArrayList<Type> elements = new ArrayList<>();
        for (Node elementNode : node.elementNodes) {
            elements.add(visit(elementNode, context));
        }
        return (ArrayType) new ArrayType(elements).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public Type visitBinOpNode(BinOpNode node, Context context) throws Exception {
        Type left = visit(node.leftNode, context);
        Type right = visit(node.rightNode, context);

        Type type = null;
        if (TT_PLUS.equals(node.opTok.type)) {
            type = left.addedTo(right);
        } else if (TT_MIN.equals(node.opTok.type)) {
            type = left.subtractedBy(right);
        } else if (TT_MUL.equals(node.opTok.type)) {
            type = left.multipliedBy(right);
        } else if (TT_DIV.equals(node.opTok.type)) {
            type = left.dividedBy(right);
        } else if (TT_POW.equals(node.opTok.type)) {
            type = left.poweredBy(right);
        } else if (TT_EE.equals(node.opTok.type)) {
            type = left.getComparisonEe(right);
        } else if (TT_NE.equals(node.opTok.type)) {
            type = left.getComparisonNe(right);
        } else if (TT_LT.equals(node.opTok.type)) {
            type = left.getComparisonLt(right);
        } else if (TT_GT.equals(node.opTok.type)) {
            type = left.getComparisonGt(right);
        } else if (TT_LTE.equals(node.opTok.type)) {
            type = left.getComparisonLte(right);
        } else if (TT_GTE.equals(node.opTok.type)) {
            type = left.getComparisonGte(right);
        } else if (node.opTok.matches(TT_KEYWORD, "and")) {
            type = left.andOperated(right);
        } else if (node.opTok.matches(TT_KEYWORD, "or")) {
            type = left.orOperated(right);
        }

        if (type == null) return null;
        return type.setPos(node.posStart, node.posEnd);
    }

    public Type visitUnaryOpNode(UnaryOpNode node, Context context) throws Exception {
        Type number = visit(node.node, context);
        if (node.opTok.type.equals(TT_MIN)) {
            number = number.multipliedByMinOne();
        } else if (node.opTok.matches(TT_KEYWORD, "not")) {
            number = number.notOperated();
        }
        return number.setPos(node.posStart, node.posEnd);
    }

    private Type visitIfNode(IfNode node, Context context) throws Exception {
        for (ArrayList<Node> condExpr : node.cases) {
            Node condition = condExpr.get(0);
            Node expression = condExpr.get(1);
            IntegerType conditionValue = (IntegerType) visit(condition, context);
            if (conditionValue.isTrue()) {
                Type res = visit(expression, context);
                return node.shouldReturnNull ? IntegerType.zero : res;
            }
        }
        if (node.elseCase != null) {
            {
                Type res = visit(node.elseCase, context);
                return node.shouldReturnNull ? IntegerType.zero : res;
            }
        }
        return IntegerType.zero;
    }

    private Type visitForNode(ForNode node, Context context) throws Exception {
        ArrayList<Type> elements = new ArrayList<>();
        Type startValue = visit(node.startValueNode, context);
        Type endValue = visit(node.endValueNode, context);
        Type stepValue = visit(node.stepValueNode, context);

        if (stepValue.isPositive()) {
            while (startValue.getComparisonLt(endValue).isTrue()) {
                context.symbolTable.set(node.varNameTok.value, startValue.getCopy());
                startValue = startValue.addedTo(stepValue);
                elements.add(visit(node.bodyNode, context));
            }
        } else {
            while (startValue.getComparisonGt(endValue).isTrue()) {
                context.symbolTable.set(node.varNameTok.value, startValue.getCopy());
                startValue = startValue.addedTo(stepValue);
                elements.add(visit(node.bodyNode, context));
            }
        }
        return node.shouldReturnNull ? IntegerType.zero : (ArrayType) new ArrayType(elements).setContext(context).setPos(node.posStart, node.posEnd);
    }

    private Type visitWhileNode(WhileNode node, Context context) throws Exception {
        ArrayList<Type> elements = new ArrayList<>();
        while (true) {
            Type condition = visit(node.conditionNode, context);
            if (!condition.isTrue()) break;
            elements.add(visit(node.bodyNode, context));
        }
        return node.shouldReturnNull ? IntegerType.zero : (ArrayType) new ArrayType(elements).setContext(context).setPos(node.posStart, node.posEnd);
    }

    private FunctionType visitFuncDefNode(FuncDefNode node, Context context) {
        String funcName = node.varNameTok != null ? node.varNameTok.value : null;
        Node bodyNode = node.bodyNode;
        ArrayList<String> argNames = (ArrayList<String>) node.argNameToks.stream().map(argName -> argName.value).collect(Collectors.toList());
        FunctionType func_value = (FunctionType) new FunctionType(funcName, bodyNode, argNames, node.shouldReturnNull).setContext(context).setPos(node.posStart, node.posEnd);

        if (node.varNameTok != null) context.symbolTable.set(funcName, func_value);
        return func_value;
    }

    private Type visitCallNode(CallNode node, Context context) throws Exception {
        ArrayList<Type> args = new ArrayList<>();
        BaseFunction valueToCall = (BaseFunction) visit(node.nodeToCall, context);
        valueToCall = (BaseFunction) valueToCall.getCopy().setPos(node.posStart, node.posEnd);

        for (Node argNode : node.argNodes) {
            args.add(visit(argNode, context));
        }

        Type ret = valueToCall.execute(args);
        ret.getCopy().setPos(node.posStart, node.posEnd).setContext(context);

        return ret;
    }
}
