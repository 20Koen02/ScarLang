package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Node.*;
import nl.Koen02.ScarLang.Type.*;
import nl.Koen02.ScarLang.Type.Function.FunctionType;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Interpreter {
    public RunTimeResult visit(Node node, Context context) throws Exception {
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
        } else if (node instanceof ReturnNode) {
            return visitReturnNode((ReturnNode) node, context);
        } else if (node instanceof ContinueNode) {
            return visitContinueNode();
        } else if (node instanceof BreakNode) {
            return visitBreakNode();
        }
        return null;
    }

    public RunTimeResult visitVarAccessNode(VarAccessNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();
        String varName = node.varNameToken.value;
        Type value = context.symbolTable.get(varName);

        if (value == null) throw new RunTimeError(node.posStart, node.posEnd, String.format("'%s' is not defined", varName), context);

        value = value.getCopy().setPos(node.posStart, node.posEnd).setContext(context);
        return res.success(value);
    }

    public RunTimeResult visitVarAssignNode(VarAssignNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();
        String varName = node.varNameToken.value;
        Type value = res.register(visit(node.valueNode, context));
        if (res.shouldReturn()) return res;

        context.symbolTable.set(varName, value);
        return res.success(value);
    }

    private RunTimeResult visitStringNode(StringNode node, Context context) {
        return new RunTimeResult().success(new StringType(node.tok.value).setContext(context).setPos(node.posStart, node.posEnd));
    }

    public RunTimeResult visitIntegerNode(IntegerNode node, Context context) {
        return new RunTimeResult().success(new IntegerType(Integer.parseInt(node.tok.value)).setContext(context).setPos(node.posStart, node.posEnd));
    }

    public RunTimeResult visitFloatNode(FloatNode node, Context context) {
        return new RunTimeResult().success(new FloatType(Float.parseFloat(node.tok.value)).setContext(context).setPos(node.posStart, node.posEnd));
    }

    public RunTimeResult visitArrayNode(ArrayNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();
        ArrayList<Type> elements = new ArrayList<>();
        for (Node elementNode : node.elementNodes) {
            elements.add(res.register(visit(elementNode, context)));
            if (res.shouldReturn()) return res;
        }
        return res.success(new ArrayType(elements).setContext(context).setPos(node.posStart, node.posEnd));
    }

    public RunTimeResult visitBinOpNode(BinOpNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Type left = res.register(visit(node.leftNode, context));
        if (res.shouldReturn()) return res;
        Type right = res.register(visit(node.rightNode, context));
        if (res.shouldReturn()) return res;

        Type type = null;
        if (TT_PLUS.equals(node.opTok.type)) {
            type = left.addedTo(right);
        } else if (TT_MIN.equals(node.opTok.type)) {
            type = left.subtractedBy(right);
        } else if (TT_MUL.equals(node.opTok.type)) {
            type = left.multipliedBy(right);
        } else if (TT_MOD.equals(node.opTok.type)) {
            type = left.moduloBy(right);
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
        return res.success(type.setPos(node.posStart, node.posEnd));
    }

    public RunTimeResult visitUnaryOpNode(UnaryOpNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Type number = res.register(visit(node.node, context));
        if (res.shouldReturn()) return res;
        if (node.opTok.type.equals(TT_MIN)) {
            number = number.multipliedByMinOne();
        } else if (node.opTok.matches(TT_KEYWORD, "not")) {
            number = number.notOperated();
        }
        return res.success(number.setPos(node.posStart, node.posEnd));
    }

    private RunTimeResult visitIfNode(IfNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        for (ArrayList<Node> condExpr : node.cases) {
            Node condition = condExpr.get(0);
            Node expression = condExpr.get(1);
            Type conditionValue = res.register(visit(condition, context));
            if (res.shouldReturn()) return res;
            if (conditionValue.isTrue()) {
                Type v = res.register(visit(expression, context));
                if (res.shouldReturn()) return res;
                return res.success(node.shouldReturnNull ? IntegerType.zero : v);
            }
        }
        if (node.elseCase != null) {
            {
                Type v = res.register(visit(node.elseCase, context));
                if (res.shouldReturn()) return res;
                return res.success(node.shouldReturnNull ? IntegerType.zero : v);
            }
        }
        return res.success(IntegerType.zero);
    }

    private RunTimeResult visitForNode(ForNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        ArrayList<Type> elements = new ArrayList<>();
        Type startValue = res.register(visit(node.startValueNode, context));
        if (res.shouldReturn()) return res;
        Type endValue = res.register(visit(node.endValueNode, context));
        if (res.shouldReturn()) return res;
        Type stepValue = res.register(visit(node.stepValueNode, context));

        while (stepValue.isPositive() ? startValue.getComparisonLt(endValue).isTrue() : startValue.getComparisonGt(endValue).isTrue()) {
            context.symbolTable.set(node.varNameTok.value, startValue.getCopy());
            startValue = startValue.addedTo(stepValue);

            Type value = res.register(visit(node.bodyNode, context));
            if (res.shouldReturn() && !res.loopShouldContinue && !res.loopShouldBreak) return res;
            if (res.loopShouldContinue) continue;
            if (res.loopShouldBreak) break;
            elements.add(value);
        }

        return res.success(node.shouldReturnNull ? IntegerType.zero : new ArrayType(elements).setContext(context).setPos(node.posStart, node.posEnd));
    }

    private RunTimeResult visitWhileNode(WhileNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        ArrayList<Type> elements = new ArrayList<>();
        while (true) {
            Type condition = res.register(visit(node.conditionNode, context));
            if (res.shouldReturn()) return res;
            if (!condition.isTrue()) break;

            Type value = res.register(visit(node.bodyNode, context));
            if (res.shouldReturn() && !res.loopShouldContinue && !res.loopShouldBreak) return res;
            if (res.loopShouldContinue) continue;
            if (res.loopShouldBreak) break;
            elements.add(value);
        }
        return res.success(node.shouldReturnNull ? IntegerType.zero : new ArrayType(elements).setContext(context).setPos(node.posStart, node.posEnd));
    }

    private RunTimeResult visitFuncDefNode(FuncDefNode node, Context context) {
        RunTimeResult res = new RunTimeResult();

        String funcName = node.varNameTok != null ? node.varNameTok.value : null;
        Node bodyNode = node.bodyNode;
        ArrayList<String> argNames = (ArrayList<String>) node.argNameToks.stream().map(argName -> argName.value).collect(Collectors.toList());
        FunctionType func_value = (FunctionType) new FunctionType(funcName, bodyNode, argNames, node.shouldAutoReturn).setContext(context).setPos(node.posStart, node.posEnd);

        if (node.varNameTok != null) context.symbolTable.set(funcName, func_value);
        return res.success(func_value);
    }

    private RunTimeResult visitCallNode(CallNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        ArrayList<Type> args = new ArrayList<>();
        Type valueToCall = res.register(visit(node.nodeToCall, context));
        if (res.shouldReturn()) return res;
        valueToCall = valueToCall.getCopy().setPos(node.posStart, node.posEnd);

        for (Node argNode : node.argNodes) {
            args.add(res.register(visit(argNode, context)));
            if (res.shouldReturn()) return res;
        }

        Type ret = res.register(valueToCall.execute(args));
        if (res.shouldReturn()) return res;
        ret = ret.getCopy().setPos(node.posStart, node.posEnd).setContext(context);

        return res.success(ret);
    }

    private RunTimeResult visitReturnNode(ReturnNode node, Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Type value;
        if (node.nodeToReturn != null) {
            value = res.register(visit(node.nodeToReturn, context));
        } else {
            value = IntegerType.zero;
        }
        return res.successReturn(value);
    }

    private RunTimeResult visitContinueNode() {
        return new RunTimeResult().successContinue();
    }

    private RunTimeResult visitBreakNode() {
        return new RunTimeResult().successBreak();
    }
}
