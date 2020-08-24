package src;

/**
 * Represents an error in the evaluation of an Expression
 */
public class ExpressionException extends Exception {

    public ExpressionException(String msg) {
        super(msg);
    }
}
