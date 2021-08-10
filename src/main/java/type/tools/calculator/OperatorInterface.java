package type.tools.calculator;

import queue.ArrayQueue;
import java.util.Stack;

public interface OperatorInterface {

    public abstract String getSymbol();

    public abstract int getPriority();

    public abstract void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int index);

    public abstract double execute(ExpNode exp, double x, double value);

    public abstract String getStrExpr(String x, String y, OperatorInterface op);

    public abstract boolean isUnaryOp();
}
