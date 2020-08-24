package src;

public class SingleVariate extends Function {

    private Expression expr;

    public SingleVariate(String expr, double minRange, double maxRange) {
        super(minRange, maxRange);
        this.expr = new Expression(expr);
    }

    @Override
    public Point calc(double x) {
        try {
            return new Point(x, expr.evaluate(x));
        } catch (ExpressionException expressionException) {
            return new Point(x, Double.NaN);
        }
    }

    @Override
    public String toString() {
    	return expr.toString();
    }
}
