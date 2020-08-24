package src;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Function implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String color = "Black";
    private double minRange;
    private double maxRange;

    public Function(double minRange, double maxRange) {
    	this.minRange = minRange;
        this.maxRange = maxRange;
    }

    /** Evaluates the function at every value in its range
     * @param step The stepping amount for the next point
     * @return A mapping of doubles rounded to the nearest tenth to the output point
     */
    public Map<Double, Point> evaluateFunction(double step) {
        Map<Double, Point> mapping = new HashMap<>();
        for (double i = minRange; i <= maxRange; i += step) {
            i = ((i*10)) / 10;
            mapping.put(i, this.calc(i));
        }
        return mapping;
    }
	/**
	 * Calculates f(x)
	 * @param x x value
	 * @return (x, f(x)), or (x, NaN) if the equation is invalid
	 */
	abstract Point calc(double x);

	String getColor() {
	    return this.color;
    }

	void setColor(String color) {
	    this.color = color;
    }

	double getMinRange() {
	    return this.minRange;
    }

	double getMaxRange() {
	    return  this.maxRange;
    }

	void setMinRange(double minRange) {
	    this.minRange = minRange;
    }

	void setMaxRange(double maxRange) {
	    this.maxRange = maxRange;
    }
	
	@Override
	public String toString() {
		return "";
	}
}