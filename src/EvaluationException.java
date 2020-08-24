package src;

/**
 * Represents an error in the evaluation of an Expression
 */
public class EvaluationException extends Exception {

    public EvaluationException(String msg) {
        super(msg);
    }
}
