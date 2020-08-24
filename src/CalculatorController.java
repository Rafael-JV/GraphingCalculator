package src;

public class CalculatorController {

	private CalculatorModel model;

	public CalculatorController(CalculatorModel model) {
		this.model = model;
	}
	
	/**
	 * Does calculation using the expression inputted
	 * @param expression expression entered by the user
	 * @return String representation of the result. "Invalid expression" if an exception occured
	 */
	public String doCalculation(String expression) {
		try {
			return "" + (new Expression(expression)).evaluate();
		} catch (ExpressionException e) {
			return "Invalid Expression";
		}
	}
	
	/**
	 * Makes the tangent equation of expression at s1
	 * @param expression Expression
	 * @param s1 x value
	 * @return Tangent equation of expression at s1 or error string
	 */
	public String makeTangent(String expression, String s1) {
		String result = "";
		try {
			double x1 = Double.parseDouble(s1);
			double x2 = x1+ model.getStep();
			Expression exp = new Expression(expression);
			double y1 = exp.evaluate(x1);
			double y2 = exp.evaluate(x2);
			double m = (y2 - y1) / (x2 - x1);
			double b = y1 - (m * x1);
			m = Math.round((m*100))/100.0;
			b = Math.round((b*1000))/1000.0;
			result = m + " * x + " + b;
		} catch (Exception e) {
			result = "Invalid Expression";
		}
		return result;
	}

	/**
	 * Adds a new equation to the model
	 * @param index index in the GUI list of equations
	 * @param equation text in the equation's TextField
	 */
	public void addSingleVariate(int index, String equation) {
		model.addSingleVariateFunction(index, equation);
	}

	/**
	 * Adds a new parametric equation to the model
	 * @param xExpr
	 * @param yExpr
	 * @param tMin
	 * @param tMax
	 */
	public void addParametric(String xExpr, String yExpr, double tMin, double tMax) {
	    model.addParametricFunction(xExpr, yExpr, tMin, tMax);
    }
	
	/**
	 * Removes any empty equations from the function list
	 * @return the new size of the 
	 */
	public int cleanList() {
		int i;
		for (i = 0; i < model.getFunctionList().size(); i++) {
			if (model.getFunctionAtIndex(i) == null) {
				model.getFunctionList().remove(i);
				i--;
			}
		}
		return i;
	}

	/**
	 * Removes equation from the model
	 * @param index index in the GUI list of equations
	 */
	public void removeEquation(int index) {
		model.removeEquation(index);
	}
	
	/**
	 * Changes the model's minX value
	 * @param newX new minX value
	 */
	public void changeMinX(double newX) {
		model.setXMin(newX);
	}

	/**
	 * Changes the model's maxX value
	 * @param newX new maxX value
	 */
	public void changeMaxX(double newX) {
		model.setXMax(newX);
	}
	
	/**
	 * Changes the model's minY value
	 * @param newY new minY value
	 */
	public void changeMinY(double newY) {
		model.setYMin(newY);
	}
	
	/**
	 * Changes the model's maxY value
	 * @param newY new maxY value
	 */
	public void changeMaxY(double newY) {
		model.setYMax(newY);
	}
	
	/**
	 * Changes the model's step value
	 * @param newStep new step value
	 */
	public void changeStep(double newStep) {
		model.setStep(newStep);
	}
	
	public void changeColor(int index, String newColor) {
		model.setColor(index, newColor);
	}
	
	/**
	 * Updates the pane
	 */
	public void updatePane() {
		model.updatePane();

	}
}