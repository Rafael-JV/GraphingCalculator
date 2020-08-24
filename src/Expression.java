package src;

import java.io.Serializable;

/**
 * Expression implements a mathematical expression to evaluate
 */
public class Expression implements Serializable {

    private static final long serialVersionUID = 1L;
    private ASTNode expr;
    private String expression;

    /** Constructor for the expression
     * @param expression The string of the expression
     */
    public Expression(String expression) {
        this.expression = expression;
        Lexer lex = new Lexer();
        Parser parser = new Parser(lex.tokenize(expression));
        this.expr = parser.parseTokens();
    }

    /** Evaluates the Syntax tree
     * @return The result of the evaluation
     * @throws ExpressionException
     */
    public double evaluate() throws ExpressionException {
        return evaluateNode(this.expr);
    }

    /** Evaluates the Syntax tree with the given variable value
     * @param val The value to evaluate the variables at
     * @return The result of the Evaluation
     * @throws ExpressionException
     */
    public double evaluate(double val) throws ExpressionException {
        return evaluateNode(this.expr, val);
    }

    /** Returns the value of the string as a double
     * @param num The value to parse
     * @return The numeric value represented by the string
     */
    private static double numValue(String num) {
        return Double.parseDouble(num);
    }

    /** Evaluates the Syntax tree
     * @param node The Syntax tree to evaluate
     * @return The result of the evaluation
     * @throws ExpressionException
     */
    private static double evaluateNode(ASTNode node) throws ExpressionException {
        double left, right;
        switch (node.getTokenType()) {
            case NUM:
                return numValue(node.getTokenVal());
            case PLUS:
                left = evaluateNode(node.getLeft());
                right = evaluateNode(node.getRight());
                return left + right;
            case MINUS:
                left = evaluateNode(node.getLeft());
                right = evaluateNode(node.getRight());
                return left - right;
            case MUL:
                left = evaluateNode(node.getLeft());
                right = evaluateNode(node.getRight());
                return left * right;
            case DIV:
                left = evaluateNode(node.getLeft()) ;
                right = evaluateNode(node.getRight() );
                return left / right;
            case EXP:
                left = evaluateNode(node.getLeft());
                right = evaluateNode(node.getRight());
                return Math.pow(left, right);
            case VAR:
                throw new ExpressionException("Variable in expression when none expected!");
            case FUNC:
                left = evaluateNode(node.getLeft());
                return evaluateFunc(node.getTokenVal(), left);
            default:
                throw new ExpressionException("Invalid TokenType");
        }
    }
    private static double evaluateNode(ASTNode node, double variable) throws ExpressionException {
        double left, right;
        switch (node.getTokenType()) {
            case NUM:
                return numValue(node.getTokenVal());
            case PLUS:
                left = evaluateNode(node.getLeft(), variable);
                right = evaluateNode(node.getRight(), variable);
                return left + right;
            case MINUS:
                left = evaluateNode(node.getLeft(), variable);
                right = evaluateNode(node.getRight(), variable);
                return left - right;
            case MUL:
                left = evaluateNode(node.getLeft(), variable);
                right = evaluateNode(node.getRight(), variable);
                return left * right;
            case DIV:
                left = evaluateNode(node.getLeft(), variable);
                right = evaluateNode(node.getRight(), variable);
                return left / right;
            case EXP:
                left = evaluateNode(node.getLeft(), variable);
                right = evaluateNode(node.getRight(), variable);
                return Math.pow(left, right);
            case VAR:
                return variable;
            case FUNC:
                left = evaluateNode(node.getLeft(), variable);
                return evaluateFunc(node.getTokenVal(), left);
            default:
                throw new ExpressionException("Invalid TokenType");
        }
    }

    /** Evaluates a named function
     * @param tokenVal The string representing the  function to call
     * @param left The left node
     * @return The result of the function evaluation
     * @throws ExpressionException
     */
    private static double evaluateFunc(String tokenVal, double left) throws ExpressionException {
        switch (tokenVal) {
            case "e^":
                return Math.exp(left);
            case "sin":
                return Math.sin(left);
            case "cos":
                return Math.cos(left);
            case "tan":
                return Math.tan(left);
            case "sin^-1":
                return Math.asin(left);
            case "cos^-1":
                return Math.acos(left);
            case "tan^-1":
                return Math.atan(left);
            case "ln":
                return Math.log(left);
            case "log":
                return Math.log10(left);
            default:
                throw new ExpressionException("UNKNOWN FUNCTION: " + tokenVal);
        }
    }

    /** To string for Expression
     * @return The string version of the expression
     */
    @Override
    public String toString() {
        return this.expression;
    }
}