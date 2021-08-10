package type.tools.calculator;

import queue.ArrayQueue;
import java.util.Stack;

public enum Close_Enclosure implements OperatorInterface {

    /**
     * Enum assigning End of an Enclosure actions
     * Symbols, and precedence
     * End of expression component.
     * Executed Immediately!
     */

        CLOSE_BRACE("]", 4) {

        },
        CLOSE_BRACK("}", 4) {

        },
        CLOSE_PARAN(")", 4) {

        },
        CLOSE_PIPE("|", 4) {

            // Although absolute value is a unary operator, we do
            // not need to set it to true here, Set
            // ABS in operator as a unary operator

            // Insert the ABS operator into the outQueue
            @Override
            public void stackAction(Object previous, ExpNode notUsed, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {

                super.stackAction(previous, notUsed, outQueue, opStack, index);
                ExpNode exp = new ExpNode(Operator.ABS,  " abs ", index);
                outQueue.offer(exp);
            }
        },
        END("END", 5) {

        };

        private String symbol;
        private int priority;

        /**
         * Constructor
         * @param sym
         * @param p
         */
        Close_Enclosure(String sym, int p) {
            symbol = sym;
            priority = p;
        }

        @Override
        public String getSymbol() {
            return this.symbol;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

    /**
     * Reshuffles the operators from the opStack and offers them to the outQueue
     * until the opening enclosure is reached. Closing Operator is not pushed
     * onto the stack.
     * @param previous
     * @param notUsed
     * @param outQueue
     * @param opStack
     */
        @Override
        public void stackAction(Object previous, ExpNode notUsed, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {

            //int index not used
            //System.out.println(" in Close_Enclosure stackAction()");

            ExpNode expReshuffle;
            while( ! opStack.isEmpty()) {
                expReshuffle = opStack.pop();
                // Effectively removing the open_closure
                if(expReshuffle.getOp().getPriority() == 0) {
                    break;
                }
                outQueue.offer(expReshuffle);
            }
        }

        @Override
        public String getStrExpr(String x, String y, OperatorInterface op) {
            return " " + op + " ";
        }

        @Override
        public String toString() {
            return this.getSymbol();
        }

        @Override
        public boolean isUnaryOp() {
            return false;
        }

        // Not used.
        public double execute(ExpNode exp, double x, double value) {
            System.err.println("ERROR: Called execute() in Close_Enclosure\n");
            // not used
            return 0.77;
        }

}
