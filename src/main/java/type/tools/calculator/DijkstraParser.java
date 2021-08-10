package type.tools.calculator;


import queue.ArrayQueue;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**********************************************************************************************************************
 *
 *
 * Parses an expression based on Edsgar W. Dijkstra's Shunting Yard
 * algorithm. The class prioritizes operators and sub-expressions ensuring that the
 * expression maintains PERMDAS order. Allows the EncryptedUser.EncryptedUser to use pipes for
 * Absolute Value when entering a string expression.
 *
 * *** NOTE:
 * !!! To print the problem for the EncryptedUser.EncryptedUser in a simple and understandable format, We first create an object for each element
 *  *   that can be referanced and display a result to the EncryptedUser.EncryptedUser. Otherwise if we printed the results in the order they
 *  *   are executed, although they are in left to right PERMDAS order, a 9 * 9 may not provide a good hint if there isn't
 *  *   a 9 * 9 in the original string. ie in the problem, 9 * 3 ^ 2 - 2 -{ 3 ^ 2 ( 5 + 1 ) } } / ...
 *  *   9 * 9 does not appear in the original string. Niether would 2 * -1 which is used to calcuate the results of the
 *  *   2 -{ ...}. To create an easy to understand display when the EncryptedUser.EncryptedUser gets the answer wrong, due to ordering, or to
 *  *   help the EncryptedUser.EncryptedUser understand the order that is used to solve problems in this calculator, the expression is
 *  *   parsed into a expNode to be used as a referance. The referance may be used later for displaying and creating
 *  *   behaviors that assist with understanding.
 *
 * Algorithm:
 *   - Determine if the equation is balanced… based on parantheseis , brackets, and braces. Simply go through the
 *   expression and place opening enclosures on a stack, and closing enclosures remove opening enclosures.
 *
 *   When a string expression is read (parsed) from beginning to end it is fed onto a queue in PERMDAS order using
 *   Dijkstra's Shunting Yard algorithm to reorder the expression from in-Fix notation, to end-fix notation.
 *   As the expression is parsed, each token is either an operator or a number.
 *
 * -	If the token is a number, it is offered (placed) to the outQueue and waits for the next token
 * -	If the token is an operator,
 *      - It checks the operator on the top of the opStack.
 *          - If the operator on the top of the stack is higher than it's precedence. The operator on the top of the
 *          opStack is pooped from the opStack and offered to the outQueue until it can be placed on the opStack.
 *          - If the operator is an open enclosure it is always placed on the opStack.
 *              - If it is a negitive enclosure, a -1 is offered to the outQueue and a multiply is pushed onto the
 *              opQueue with the same rules for precedence.
 *          - If the operator is a closing enclosure, it poops the operators from the opStack until it has pooped the
 *          opening enclosure
 *
 *  After the string is read, the outQueue is evaluated in postFix order.
 * - If an element of the outQueue is a number, it is placed on an evaluation stack.
 * - If an element is an operator, the evaluation stack is popped and the first number
 *   becomes Y and the second becomes X and the expression is evaluated in X then Y order.
 *   The result is pushed back onto the evaluation stack. The remaining number on the evaluation
 *   stack is the result.
 *
 *
 *      -	Set priority
 *          o	Immidiate = 3
 *          	Power = execute immidiatley –
 *              •	Create thread, hold any processing if this ref is needed until it’s finished
 *              •	Pop last token, set it to left integer
 *              •	Get next token, (maybe place it on the stack), remove it from the original string/structure
 *              •	Evaluate and place on the stack
 *          	Rigth brackets, paran, brace: “ ) “ or “ ] “ or “ } “
 *              •	Create new thread, hold any processing if this ref is needed
 *              •	Execute operators from the top of the stack until the next left bracket, paran, brace “ ( “ or “ [ “ or “ { “
 *          	Pipe “ | “ Absolute Value
 *              •	If parser encounters a pipe for Absolute Value it checks the absValueBool.
 *                  o	If it is true it evaluates the operators and values until it reaches the first AbsValue pipe.
 *                  And enters the evaluation result.
 *                  o	Set absValueBool to false
 *              •	Else it is false it sets absValueBool to true
 *          o   High = 2
 *              	Multiply, Divide, and modulus * / %
 *          o	Low = 1
 *              	Add and subtract + -
 *          o	Wild Stop
 *              	Left paran, bracket, brace “ ( “ or “ [ “ or “ { “
 *                  •	As stated above, Acts as a stop for previous right brackets
 *                  •	When evaluated
 *                      o	If preceeded by an integer, it is multiplied
 *                      o	If preceeded by an operator, it defaults to the operator
 *                      o	If preceeded by a “-“ it is multiplied by negative 1
 *              	1st Pipe / Absolute value left “ | ”
 *                  *   As stated above, acts as a stop for previous right pipe
 *
 **********************************************************************************************************************/


public class DijkstraParser implements OperatorInterface{

    private double result;
    // Combination of numbers and ExpNodes
    private static ArrayQueue outQueue;
    // ExpNodes
    private static Stack<ExpNode> opStack;
    // Combination of numbers and ExpNodes
    private static ArrayList subExpList;
    // Message if input is illegal
    private static String message;
    // Flag for invalid input
    private static boolean invalidInput;
    // Size of subExpList
    private static int size;


    /**
     * default constructor
     */
    public DijkstraParser() {
        outQueue = new ArrayQueue();
        opStack = new Stack<>();
        size = 0;
        //invalidInput = true;
    }

    public DijkstraParser(String expression) {
        outQueue = new ArrayQueue();
        subExpList = new ArrayList();
        opStack = new Stack<>();
        result = evaluate(expression);
        size = 0;
        //invalidInput = true;
    }

    /** GETTERS **/

    public static ArrayQueue getOutQueue() {
        return outQueue;
    }

    public static Stack<ExpNode> getOpStack() {
        return opStack;
    }

    public double getResult() { return this.result; }

    public static ArrayList getWriterList() {
        return subExpList;
    }

    public static int getListSize() { return size; }


    /** METHODS **/


    /**
     * Evaluates the String expresion in the argument
     * @param string The string expression to be evaluated
     * @return Returns the result
     */
    private double evaluate(String string) {

       // Working on sending a message to the EncryptedUser.EncryptedUser when they do not provide
       //    clean input. Try catch currently implemented but is not very
       //    good.

        try {
            if (parseStrExpression(string)) {
                parseIntoRPN(subExpList);
                return execute(outQueue);
            }

        } catch (Exception e) {

            // @todo Exception for invalid operator. Handle this error in DijkstraParser line 164 evaluate
            clearStructures();
        }
        return 0;
    }

    /**
     * Executes an endfix expression given in a queue
     * @param outQ
     * @return returns the solution
     */
    public double execute(ArrayQueue outQ) {

       //System.out.println("\n *!*!* in Parser.execute() *!*!* ");
        Stack<Double> resultStack = new Stack<>();

        double x;
        double y;
        while ( ! outQ.isEmpty() ) {

            if(outQ.peek().getClass().isInstance(1.1)) {

               //System.out.println("\n\t inserting: " + outQ.peek() + " into resultStack");

                // queue element is a number
                resultStack.push((double) outQ.poll());

            } else {
                // queue element is an operator

               //System.out.println("\n\t OPERATOR: " + ((ExpNode) outQ.peek()).getOp().getSymbol());

                if( ! ((ExpNode) outQ.peek()).getOp().isUnaryOp() ) {

                    y = resultStack.pop();
                    x = resultStack.pop();
                    ExpNode exp = (ExpNode) outQ.poll();

                    double result = exp.getOp().execute(exp, x, y);

                   //System.out.println("binary operator inserting: " + result + " into resultStack");
                   //System.out.println("ResultStack size is now: " + resultStack.size());

                    resultStack.push(result);

                } else {

                   //System.out.println(" !~!~! in DijkstraParser execute() !~!~! \n " +
                   //         "symbol is " + ((ExpNode) outQ.peek()).getOp().getSymbol());

                    x = resultStack.pop();
                    ExpNode exp = (ExpNode) outQ.poll();

                    double result = exp.getOp().execute(exp, x, 0);

                    resultStack.push(result);

                   //System.out.println("unary operator inserting: " + result + " into resultStack");
                   //System.out.println("ResultStack size is now: " + resultStack.size());
                }
            }
        }

        if(resultStack.size() == 1) {

            return resultStack.pop();

        } else {

            System.err.println("\nERROR: DijkstraParser resultStack has more than one" +
                    "\n answer left in the stack \n");
            return 99999999;
        }
    }

    /**
     * Parses the String expression, and
     * of String elements containing each element of the
     * expression.
     * @param expression The string expresion
     * @return
     * @throws Exception if operator input is invalid.
     */
    public boolean parseStrExpression(String expression) throws Exception {

        // clean whitespace from start
        expression = expression.trim();
        String element;
        String[] elements = expression.split(" ");

        for(int i = 0; i < elements.length; i++) {
            element = elements[i];
            element.trim();
            // Expression length,
            // used to determine if number or op
            int length = element.length();
            char c;

           //System.out.println("parsing string, element is: " + element);

            // classify the element as a number or operator
            // and add it to the subExpList.
            // - Check last char in case it's a negitive number
            if (length > 1) {
                c = element.charAt(length - 1);
            } else {
                c = element.charAt(0);
            }

            // Classify, seperate, and add to subExpList
            if( Character.isDigit(c) ) {
                double num = Double.parseDouble(element);
                subExpList.add(num);
                size++;

            } else { // it's an operator

                /**
                 * Add the binomial operator and its values (in an unexecuted state)
                 * as strings into the ExpNode expOrigin
                 */
                String x = "";
                String y = "";
                OperatorInterface operator = getOperator(element);

                if(operator.getPriority() == 8) { // DEFAULT OPERATOR

                    System.err.println("parseStrExp is return false !!!");
                    return false;
                }
                if(i < 0 ) {
                    System.err.println("ERROR: There are not enough numbers for the " + operator.getSymbol() +
                            " operation. \nCheck the format of the expression.");
                    if (operator.getPriority() != 0) {
                        throw new EmptyStackException();
                    }
                }
                if(operator.isUnaryOp()) { // sqrt, abs,

                    // for printing values prior to execution
                    x = elements[i + 1];

                   //System.out.println("op " + operator + "is unary, x: " + x);

                }
                else if(i > 1 && i < elements.length - 1) {
                    // for printing values prior to execution
                    x = elements[i - 1];
                    y = elements[i + 1];
                } else {
                    if (operator.getPriority() != 0) {
                    // throw new EmptyStackException();
                    }
                }

                // for display when answered incorrectly
                String strOrigSubExp = getStrExpr(x, y, operator);
                ExpNode exp = new ExpNode(operator, strOrigSubExp, i);

                // add the expression to the sub-expession list
                subExpList.add(exp);
                size++;
            }
        }
        return true; // The number of elements in subExpList
    }


    /**
     * Parses an arrayList of numbers and operators into RPN order.
     * @param subExpressList
     */
    public void parseIntoRPN(ArrayList subExpressList) throws Exception {

        // - Work on parsing larger doubles including other real numbers.
       //System.out.println("\n\n ~*~*~ parseIntoRPN ~*~*~");

        outQueue.clear();
        opStack.clear();

        /**
         * Set prevObject to + to prevent MULTIPLY from being added to the opStack
         * at the start
         **/

        OperatorInterface operator = getOperator("+");
        ExpNode exp = new ExpNode(operator, "", 0);

        Object prevObject = exp;

        for(int i = 0; i < subExpressList.size(); i++)
        {
            Object ob = subExpressList.get(i);
            // if object is a double
            if(ob.getClass().isInstance(1.1))
            {

                outQueue.offer(ob);
                // Set the prevObject to the number
                prevObject = ob;
            } else {
                // Build the outQueue in RPN order using the Shunting yard algorithm.
                // Calls methods and values from enums using the OperatorInterface. :)
                exp = (ExpNode) ob;
                operator = exp.getOp();
                // The reOrdering call
                operator.stackAction(prevObject, exp, outQueue, opStack, i);
                // Set the prevObject to the operator
                prevObject = exp;
            }
        }
        while(!opStack.isEmpty())
        {
            outQueue.offer(opStack.pop());
        }
        // testing
        //outQueue.print();
    }

    /**
     * Flag is set when a pipe (absolute value symbol) is opened and indicates
     * that the next pipe is the closing pipe.
     * Explanation. Open and closing pipes are identical. A true flag indicates
     * that the openPipe has been processed and the next pipe is the closing
     * abs value operation.
     */
    private static boolean pipeOpen = false;

    /**
     * Returns the Operator object based on the string provided in
     * the parameter.
     * @param stOperator
     * @return
     */
    public static OperatorInterface getOperator(String stOperator) throws Exception {

        /** Priority from 0 to 4. Higher number == higher priority **/
        invalidInput = false;

        switch (stOperator) {
            case "-":    {
                return Operator.SUBTRACT; // priority 1
            }
            case "+":    {
                return Operator.ADD; // priority 1
            }
            case "*":    {
                return Operator.MULTIPLY; // priority 2
            }
            case "/":    {
                return Operator.DIVIDE; // priority 2
            }
            case "%":    {
                return Operator.MODULUS; // priority 2
            }
            case "^":    {
                return Operator.POWER; // priority 3
            }
            case "cos":  {
                return Operator.COS; // priority 2
            }
            case "acos": {
                return Operator.ACOS;
            }
            case "cosh": {
                return Operator.COSH;
            }
            case "sin":  {
                return Operator.SIN; // priority 2
            }
            case "asin": {
                return Operator.ASIN;
            }
            case "sinh": {
                return Operator.SINH;
            }
            case "tan":  {
                return Operator.TAN; // priority 2
            }
            case "tanh": {
                return Operator.TANH;
            }
            case "atan": {
                return Operator.ATAN;
            }
            case "atan2": {
                return Operator.ATAN2;
            }
            case "sqrt": {
                return Operator.SQRT; // priority 3
            }
            case "root": {
                return Operator.ROOT; // priority 3
            }
            case "log":  {
                return Operator.LOG; // priority 3???
            }

            /** --------------- Open enclosures --------------- **/

            case "[":  {
                return Open_Enclosure.OPEN_BRACE; // priority 0
            }
            case "{":  {
                return Open_Enclosure.OPEN_BRACK; // priority 0
            }
            case "(":  {
                return Open_Enclosure.OPEN_PARAN; // priority 0
            }
            case "-[": {
                return Open_Enclosure.OPEN_BRACE_NEG; // priority 0
            }
            case "-{": {
                return Open_Enclosure.OPEN_BRACK_NEG; // priority 0
            }
            case "-(": {
                return Open_Enclosure.OPEN_PARAN_NEG; // priority 0
            }
            case "-|": {
                // Need to set pipeOpen for the closing pipe.
                // "-|" is always an "open_pipe_negitive"
                pipeOpen = true;
                return Open_Enclosure.OPEN_PIPE_NEG; // priority 0
            }

            /** --------------- close enclosures --------------- **/

            case "]": {
                return Close_Enclosure.CLOSE_BRACE; // priority 4
            }
            case "}": {
                return Close_Enclosure.CLOSE_BRACK; // priority 4
            }
            case ")": {
                return Close_Enclosure.CLOSE_PARAN; // priority 4
            }
            case "|": {
                // Need to check how many pipes exist already since they all look the same

                if(pipeOpen) {
                    pipeOpen = false;
                    return Close_Enclosure.CLOSE_PIPE; // priority 4
                } else {
                    pipeOpen = true;
                    return Open_Enclosure.OPEN_PIPE;
                }
            }
            default: {
                // Default is filtered out in DijkstraParser parseStrExpression()
                System.err.println("\n *~*~* in DijkstraParser.getOperator default. Setting invalidInput to true\n\n");
                // @todo problem with string entries being recieved in parse instead of doubles... needs to be handled

                invalidInput = true;
                message = "ERROR: Input not recognized. "
                + "\nPlease replace \"" + stOperator + "\" with valid input.";
                throw new Exception("ERROR: Operator Not recognized " + stOperator);
                //return Operator.DEFAULT;
            }
        }
    }

    /**
     * Returns an invalid input message set by getOperator()
     * @return
     */
    public String getErrorMessage() {
        return message;
    }

    /**
     * Returns the invalidInput flag
     * @return
     */
    public static boolean isInvalidInput() {
        return invalidInput;
    }

    /**
     * Clears the data structures used in this
     * class. Call this method before beginning
     * a new operation
     */
    public void clearStructures() {

        if( ! outQueue.isEmpty()) {
            outQueue.clear();
        }

        if( ! opStack.isEmpty())  {
            opStack.clear();
        }

        if( ! subExpList.isEmpty()) {
            subExpList.clear();
        }
    }


    /** ************************************************************************************************************ **/
    /**                                         FOR THE OPERATOR INTERFACE                                           **/
    /**                                                 not used                                                     **/
    /** ************************************************************************************************************ **/

    @Override
    // For OperatorInterface... Method not used
    public String getSymbol() {
        System.err.println("ERROR: Dijkstra Parser wrong getSymbol()");
        return "not the getSymbol you wanted";
    }
    @Override
    // For OperatorInterface... Method not used
    public int getPriority()  {
        System.err.println("ERROR: Dijkstra Parser, wrong getPriority()");
        return 999;
    }
    @Override
    // For OperatorInterface... Method not used
    public double execute(ExpNode exp, double x, double value){
        // do nothing
        return .77;
    }
    @Override
    // For OperatorInterface... Method not used
    public void stackAction(Object previous, ExpNode op, ArrayQueue outQueue, Stack<ExpNode> opStack, int index) {
        // do nothing
    }
    @Override
    public String getStrExpr(String x, String y, OperatorInterface op) {
        return x + " " + op.getSymbol() + " " + y;
    }

    @Override
    public boolean isUnaryOp() {
        return false;
    }
}
