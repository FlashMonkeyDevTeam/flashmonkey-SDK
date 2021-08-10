package type.tools.calculator;

import queue.ArrayQueue;
import java.util.Stack;


/**
 * Enum assigning Start of an Enclosure actions
 * Symbols, and precedence
 * End of expression component.
 * Executed Immediately!
 */
public enum Open_Enclosure implements OperatorInterface {

    OPEN_BRACE("[", 0) {
        // @Override
        // double execute(double x, double y) { return x; }// not used
    },
    OPEN_BRACK("{", 0) {
        // @Override
        // double execute(double x, double y) { return x; }// not used
    },
    OPEN_PARAN("(", 0) {
        // @Override
        // double execute(double x, double y) { return x; }// not used
    },
    OPEN_PIPE("|", 0) {

    },
    /** Multiplies by -1 **/
    OPEN_BRACE_NEG("-[", 0) {

        @Override
        public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int i) {
            negStackAction(previous, op, outQueue, opStack, i);
        }

        @Override
        public String getStrExpr(String x, String y, OperatorInterface op) {
            return x + " " + op;
        }
    },
    /** Multiplies by -1 **/
    OPEN_BRACK_NEG("-{", 0) {

        @Override
        public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int i) {
            negStackAction(previous, op, outQueue, opStack, i);
        }

        @Override
        public String getStrExpr(String x, String y, OperatorInterface op) {
            return x + " " + op;
        }

    },
    /** Multiplies by -1 **/
    OPEN_PARAN_NEG("-(", 0) {

        @Override
        public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int i) {
            negStackAction(previous, op, outQueue, opStack, i);
        }

        @Override
        public String getStrExpr(String x, String y, OperatorInterface op) {
            return x + " " + op;
        }

    },
    /** Multiplies by -1 **/
    OPEN_PIPE_NEG("-|", 0) {

        @Override
        public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int i) {
            negStackAction(previous, op, outQueue, opStack, i);
        }

        @Override
        public String getStrExpr(String x, String y, OperatorInterface op) {
            return x + " " + op;
        }
    };

    // Fields
    private String symbol;
    private int priority;

    // GETTERS
    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    /**
     * Constructor
     * @param sym
     * @param p
     */
    Open_Enclosure(String sym, int p) {
        symbol = sym;
        priority = p;
    }

    /**
     * - Open_Enclosures are always placed on the opStack, they do not
     * reOrganize the stack. They act as a stop for close_enclosures.
     * - If EncryptedUser.EncryptedUser did not insert an operator previous to
     * the opening enclosure. Inserts a Multiply operator prior to
     * the Open_Enclosure.
     * pre-condition: Expects the previous item excluding any white space.
     * @param previous The previous item, excluding any white
     *                 space.
     * @param exp ExpNode the ExpNode for this operator
     * @param outQueue The outQueue
     * @param opStack, The opStack
     * @param index The current
     */
    @Override
    public void stackAction(Object previous, ExpNode exp, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {

        //System.out.println(" in open_enclosure stackAction() Name of class: " + previous.getClass().getName() + " " +
        //        "\n and previous operator is: " + previous);

        if( ! previous.getClass().getName().contains("Exp") && ! previous.toString().equals(")") ) {
            // Index should be the current size of the writerList
            ExpNode exp1 = new ExpNode(Operator.MULTIPLY,  " * ", index);
            cPriorityNPush(opStack, outQueue, exp1);
        }
        // Insert this opening enclosure onto the opStack
        opStack.push(exp);
    }

    private static void negStackAction(Object previous, ExpNode exp, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {

        // If there is not an operator previous to the negitive opening enclosure
        // add a multiply prior to the -1
        if(! previous.getClass().getName().contains("Exp")) {

            ExpNode expLocal = new ExpNode(Operator.MULTIPLY,  " * ", index);
            cPriorityNPush(opStack, outQueue, expLocal);
        }
        outQueue.offer(-1.0);

        // Create multiply ExpNode, get index
        ExpNode exp1 = new ExpNode(Operator.MULTIPLY,  "-1 * ", index);
        // Check prioirty and push multiply ExpNode onto opStack
        cPriorityNPush(opStack, outQueue, exp1);
        // Insert this opening enclosure onto opStack
        opStack.push(exp);
    }

    // shouldn't be called
    @Override
    public double execute(ExpNode exp, double x, double value) {
        // not used
        //System.err.println("ERROR: called execute in Open_Enclosure");
        return .77;
    }

    /**
     * Removes operators that have a higher or equal priority from the top of the opStack and offers them to the outQueue.
     * Note that all operators have a higher priority.
     * @param opStack
     * @param outQueue
     * @param exp
     */
    private static void cPriorityNPush(Stack<ExpNode> opStack, ArrayQueue outQueue, ExpNode exp) {
        // System.out.println("checkPriority and push called for: " + exp.getOp().getSymbol());
        // OperatorInterface stackOp = opStack.peek().getOp();
        while( ! opStack.isEmpty() && opStack.peek().getOp().getPriority() >= exp.getOp().getPriority() ) {
            // remove the higher priority operator from the opStack and offer it to
            // the outQueue
            outQueue.offer( opStack.pop());
        }
        opStack.push(exp);
    }

    @Override
    public String getStrExpr(String x, String y, OperatorInterface op) {
        return x + " " + op + " " + y;
    }

    @Override
    public String toString() {
        return this.getSymbol();
    }

    @Override
    public boolean isUnaryOp() {
        return false;
    }
}
