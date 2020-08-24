package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class CalculatorModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;    // UID for serializable
	
    private ArrayList<Function> functionHolder;

    private double xMax = 10.0;
    private double xMin = -10.0;
    private double yMax = 10.0;
    private double yMin = -10.0;
    private double step = 0.1;


    /**
     * Constructor
     */
    public CalculatorModel() {
        functionHolder = new ArrayList<Function>();
    }

    public void clearFunctions() {
        for (int i = functionHolder.size(); i >= 0; i--) {
            this.removeEquation(i);
        }
    }

    /**
     * Adds an single variate function to functionHolder at position index
     *
     * @param index      position in the GUI list of equations
     * @param expression String representation of the expression
     */
    public void addSingleVariateFunction(int index, String expression) {
        if (index < functionHolder.size()) {
            functionHolder.remove(index);
            functionHolder.add(index, new SingleVariate(expression, this.xMin, this.xMax));
        } else {
            for (int i = functionHolder.size(); i < index; i++) {
                functionHolder.add(null);
            }

            functionHolder.add(new SingleVariate(expression, this.xMin, this.xMax));
        }
        updatePane();
    }

    public void addParametricFunction(String xExpr, String yExpr, double tMin, double tMax) {
        this.clearFunctions();
        this.functionHolder.add(new Parametric(xExpr, yExpr, tMin, tMax));
        updatePane();
    }

    /**
     * Removes the equation at index from functionHolder
     *
     * @param index the index of the Function to be removed
     *              from the list.
     */
    public void removeEquation(int index) {
        if (index < functionHolder.size()) {
            functionHolder.set(index, null);
            updatePane();
        }
    }

    /**
     * retrieves the list containing the Function
     * objects that will be plotted onto the graph
     *
     * @return the ArrayList of Function holding all of
     * the Function objects being written in on the view.
     */
    public ArrayList<Function> getFunctionList() {
        return functionHolder;
    }

    /**
     * gets the Function object contained in
     * functionHolder at a specified index
     *
     * @param index represents the index that will
     *              be checked to see if there is a Function at
     *              that index
     * @return the Function onject at index in functionHolder
     */
    public Function getFunctionAtIndex(int index) {
        return functionHolder.get(index);
    }

    /**
     * gets the minimum bound of the x-axis
     *
     * @return the integer representing the smallest
     * value of the x-axis shown on the plot
     */
    public double getXMin() {
        return this.xMin;
    }

    /**
     * gets the maximum bound of the x-axis
     *
     * @return the integer representing the biggest
     * value of the x-axis shown on the plot
     */
    public double getXMax() {
        return this.xMax;
    }

    /**
     * gets the minimum bound of the y-axis
     *
     * @return the integer representing the smallest
     * value of the y-axis shown on the plot
     */
    public double getYMin() {
        return this.yMin;
    }

    /**
     * gets the maximum bound of the y-axis
     *
     * @return the integer representing the smallest
     * value of the y-axis shown on the plot
     */
    public double getYMax() {
        return this.yMax;
    }

    /**
     * sets the value of the maximum bound of the x-axis
     * to a specified value
     *
     * @param max represents the value that xMax will
     *            be set to
     */
    public void setXMax(double max) {
        this.xMax = max;
        for (Function f : functionHolder) {
        	if(f != null) {
        		f.setMaxRange(max);
        	}
        }
    }


    /**
     * sets the value of the minimum bound of the x-axis
     * to a specified value
     *
     * @param min represents the value that xMin will
     *            be set to
     */
    public void setXMin(double min) {
        this.xMin = min;
        for (Function f : functionHolder) {
        	if(f != null) {
        		f.setMinRange(min);
        	}
        }
    }

    /**
     * sets the value of the maximum bound of the y-axis
     * to a specified value
     *
     * @param max represents the value that yMax will
     *            be set to.
     */
    public void setYMax(double max) {
        this.yMax = max;
    }

    /**
     * sets the value of the minimum bound of the y-axis
     * to a specified value
     *
     * @param min represents the value that yMin will
     *            be set to.
     */
    public void setYMin(double min) {
        this.yMin = min;
    }


    /**
     * Returns the value of the step variable
     *
     * @return returns the current value of the
     * step variable
     */
    public double getStep() {
        return this.step;
    }

    /**
     * sets the value of step to a specified value
     *
     * @param step represents the value that the
     *             instance variable step will be set to.
     */
    public void setStep(double step) {
        this.step = step;
        updatePane();
    }

    public void setColor(int index, String color) {
        functionHolder.get(index).setColor(color);
        updatePane();
    }

    public void updatePane() {
        setChanged();
        notifyObservers(functionHolder);
    }

}