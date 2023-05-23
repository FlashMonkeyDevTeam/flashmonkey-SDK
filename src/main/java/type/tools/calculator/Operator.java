package type.tools.calculator;

import queue.ArrayQueue;
import java.util.Stack;

public enum Operator implements OperatorInterface {


    /**
     * Enum assigning priority and execution to operators
     */


    // ------------- LOWEST priority -------------


    ADD ("+", 1) {
        @Override
        public double execute(ExpNode exp, double x, double y) {

           //System.out.println("Executing \"+\"");
            exp.setExpSolved("ADD: " + x + " " + this.getSymbol() + " " + y + " = " + (x + y));
           //System.out.println(exp.getExpSolved());
            ansComponents.offer(exp);
            return x + y;
        }
    },
    SUBTRACT ("-", 1) {
        @Override
        public double execute(ExpNode exp, double x, double y) {

           //System.out.println("Executing \"-\"");
            exp.setExpSolved("SUBTRACT: " + x + " " + this.getSymbol() + " " + y + " = " + (x - y));
           //System.out.println(exp.getExpSolved());
            ansComponents.offer(exp);
            return x - y;
        }
    },


    // ------------- MEDIUM priority -------------


    MULTIPLY ("*", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {
           //System.out.println("Executing \"*\"");
            exp.setExpSolved("MULTIPLY: " + x + " " + this.getSymbol() + " " + y + " = " + (x * y));
           //System.out.println(exp.getExpSolved());
            ansComponents.offer(exp);
            return x * y;
        }
    },
    DIVIDE   ("/", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {

            //System.out.println("Executing \"/\"");

            exp.setExpSolved("DIVIDE: " + x + " " + this.getSymbol() + " " + y + " = " + (x / y));
            ansComponents.offer(exp);
            return x / y;
        }
    },
    MODULUS  ("%", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {
            exp.setExpSolved("MODULUS: " + x + " " + this.getSymbol() + " " + y + " = " + (x % y));
            ansComponents.offer(exp);
            return x % y;
        }
    },
    ABS ("abs", 3) {

        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

           //System.out.println(" *** Called ABS.execute() ***");
            double result;
            result = StrictMath.abs(x);
           //System.out.println("\t x is: " + x);
            exp.setExpSolved("ABS: " + this.getSymbol() + " " + x + " = " + result);
            ansComponents.offer(exp);

            return result;
        }

        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    LOG ("log", 3) {

        // for small roots log1p is closer to truth but using log().
        @Override
        public double execute(ExpNode exp, double base, double exponent) {

            double result;
           //System.out.println(" *** Called LOG.execute() ***");
            result = Math.log(exponent) / Math.log(base);
            exp.setExpSolved("LOG: " + this.getSymbol() + " base " + base + " log exp: " + exponent + " = " + result);
           //System.out.println(exp.getExpSolved());
            ansComponents.offer(exp);
            return result;
        }
    },
    POWER ("^", 3) {
        @Override
        public double execute(ExpNode exp, double x, double exponent) {
            exp.setExpSolved("EXPONENT: " + x + " " + this.getSymbol() + " " + exponent + " = " + Math.pow(x, exponent));
            ansComponents.offer(exp);
            return Math.pow(x, exponent);
        }
    },
    ROOT ("root", 3) {
        @Override
        public double execute(ExpNode exp, double root, double x) {

            if(x < 0) {
                return 0;
            }

            // convert root to integer
            int intRoot = (int) root;

            switch(intRoot) {

                //case -1: {
                //    return x * -1;
                //}
                case 0: {
                    return 0;
                }
                case 1: {
                    return x;
                }
                case 2: { // square root

                    exp.setExpSolved("SQRT: " + this.getSymbol() + " " + x + " = " + Math.sqrt(x));
                    ansComponents.offer(exp);
                    return StrictMath.sqrt(x);
                }
                default: {

                    // @todo Need to do something with negitive roots < 2 in Operator root

                   //System.out.println(" x: " + x + " and root: " + root);
                   //System.out.println("Root: " +  (-1 / root));
                   //System.out.println("Solution: " + Math.pow(x ,  (-1 / root)));

                    double ans = StrictMath.pow(x, (1 / root));
                    double ansCeil = Math.ceil(ans);

                    //System.out.println("ansCiel: " + ansCeil + " - ciel: " + ans + " = " + (ansCeil - ans));

                    if ( (root > 2 && (ansCeil - ans < .00000001)) || (root < -1 && (ansCeil - ans > .09)) ){
                        exp.setExpSolved("ROOT true: " + root + " " + this.getSymbol() + " " + x + " = " + ansCeil);
                        ansComponents.offer(exp);
                        return ansCeil;
                    } else {
                        exp.setExpSolved("ROOT false: " + root + " " + this.getSymbol() + " " + x + " = " + ans);
                        ansComponents.offer(exp);
                        return ans;
                    }
                }
            }
        }
    },
    SQRT ("sqrt", 3) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {
            // ROOT from SQRT is the reverse of other operators
            // return ROOT.execute(exp, root, x);
            if(x <= 0) {
                return 0;
            }

            double solved = StrictMath.sqrt(x);
            exp.setExpSolved("SQRT: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }

        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },


    // ------------- COS SIN TAN & SIMILAR FUNCTIONS (Priority 2 ) -------------


    COS ("cos", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            double radians = Math.toRadians(x);
            double solved = StrictMath.cos(radians);
            exp.setExpSolved("COS: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    COSH ("cosh", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            double solved = StrictMath.cosh(x);
            exp.setExpSolved("COSH: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    ACOS ("acos", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            //double radians = Math.toRadians(x);
            double solved = StrictMath.acos(x);
            exp.setExpSolved("ACOS: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    SIN ("sin", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            double radians = Math.toRadians(x);
            double solved = StrictMath.sin(radians);
            exp.setExpSolved("SIN: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    SINH ("sinh", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            //double radians = Math.toRadians(x);
            double solved = StrictMath.sinh(x);
            exp.setExpSolved("SINH: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    ASIN ("asin", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            //double radians = Math.toRadians(x);
            double solved = StrictMath.asin(x);
            exp.setExpSolved("ASIN: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    TAN ("tan", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            double radians = Math.toRadians(x);
            double solved = StrictMath.tan(radians);
            exp.setExpSolved("TAN: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    TANH ("tanh", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            //double radians = Math.toRadians(x);
            double solved = StrictMath.tanh(x);
            exp.setExpSolved("TANH: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    ATAN ("atan", 2) {
        @Override
        public double execute(ExpNode exp, double x, double notUsed) {

            //double radians = Math.toRadians(x);
            double solved = StrictMath.atan(x);
            exp.setExpSolved("ATAN: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    },
    ATAN2 ("atan2", 2) {
        @Override
        public double execute(ExpNode exp, double x, double y) {

            //double radians = Math.toRadians(x);
            double solved = StrictMath.atan2(x, y);
            exp.setExpSolved("ATAN2: " + this.getSymbol() + " " + x + " = " + solved);
           //System.out.println("exp: " + exp);
            ansComponents.offer(exp);
            return solved;
        }
    },
    DEFAULT ("def", 8) {
        @Override
        public double execute(ExpNode exp, double notUsed, double notUsedEither) {

            System.err.println(" *** Called DEFAULT.execute() ***");

            return 0;
        }
        @Override
        public boolean isUnaryOp() {
            return true;
        }
    };

    // --------------------------------- --------------------------------- //
    // Common fields
    // --------------------------------- --------------------------------- //

    private String symbol;
    private int priority;
    private static ArrayQueue<ExpNode> ansComponents = new ArrayQueue<>(10);

    Operator(String sym, int p) {
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

    @Override
    public abstract double execute(ExpNode exp, double x, double y);


    /**
     * if this Operator has a lower or equal priority than the operator on
     * the top of the opStack. Pop the operator on the opStack
     * and push it onto the outQueue until this operator can be
     * pushed onto the opStack. (this operator is higher than the
     * operator on the opStack)
     * @param outQueue
     * @param opStack
     */
    public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {

        // index not used.

        while( ! opStack.isEmpty() && opStack.peek().getOp().getPriority() >= this.priority ) {
           outQueue.offer( opStack.pop()); // pushing the enum onto the numStack
        }
        opStack.push(op);
    }

    public static ArrayQueue<ExpNode> getAnsComponents() {
        return ansComponents;
    }

    @Override
    public boolean isUnaryOp() {
        return false;
    }

    @Override
    public String toString() {
        return this.getSymbol();
    }

    @Override
    public String getStrExpr(String x, String y, OperatorInterface op) {
        return x + " " + op + " " + y;
    }
}