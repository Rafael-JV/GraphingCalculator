package src;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
/**
 * This class is a testing class for the model 
 * that will display the outputs of various class
 * based methods onto a text UI.
 * @author zackleibowitz
 *
 */
class ModelTest {
	
	
	
	@Test
	void testAddFunction() {
		CalculatorModel model= new CalculatorModel();
		String expression= "2 * x";
		
		model.addSingleVariateFunction(0, expression);
		Assertions.assertTrue(model.getFunctionAtIndex(0).equals("2 * x"));
	}
	
	@Test
	void testRemoveFunction() {
		CalculatorModel model= new CalculatorModel();
		model.addSingleVariateFunction(0, "2 * x");
		Assertions.assertTrue(model.getFunctionAtIndex(0).equals("2 * x"));
		model.removeEquation(0);
		Assertions.assertTrue(model.getFunctionAtIndex(0) == null);
	}
	
	
	@Test
	void testClearFunctions() {
		CalculatorModel model= new CalculatorModel();
		
		model.addSingleVariateFunction(0, "x + 5");
		model.addSingleVariateFunction(1, "2 * x");
		model.addSingleVariateFunction(2, "2 * x - 3");
		
		ArrayList<Function> temp= model.getFunctionList();
		
		model.clearFunctions();
		
		Assertions.assertFalse(model.getFunctionList().equals(temp));
	}
	
	@Test
	void testGetFunctionList() {
		CalculatorModel model= new CalculatorModel();
		
		String expression= "X+5";
		
		model.addSingleVariateFunction(0, expression);
		
	
	}
	
	@Test 
	void testGetXMin() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= -10.0;
		
		double actual= model.getXMin();
		
		Assertions.assertEquals(expected, actual);
		
		
	}
	
	@Test 
	void testGetYMin() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= -10.0;
		
		double actual= model.getYMin();
		
		Assertions.assertEquals(expected, actual);
		
		
	}
	
	@Test 
	void testGetXMax() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= 10.0;
		
		double actual= model.getXMax();
		
		Assertions.assertEquals(expected, actual);
		
		
	}
	
	@Test 
	void testGetYMax() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= 10.0;
		
		double actual= model.getYMax();
		
		Assertions.assertEquals(expected, actual);
		
		
	}
	
	@Test 
	void testSetXMin() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= -20;
		
		model.setXMin(-20);
		
		double actual= model.getXMin();
		
		Assertions.assertTrue(expected == actual);
		
	}
	
	@Test
	void testSetXMax() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= 20;
		
		model.setXMax(20);
		
		double actual= model.getXMax();
		
		Assertions.assertTrue(expected == actual);
		
	}
	
	@Test
	void testSetYMin() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= -20;
		
		model.setYMin(-20);
		
		double actual= model.getYMin();
		
		Assertions.assertTrue(expected == actual);
		
	}
	
	@Test
	void testSetYMax() {
		CalculatorModel model= new CalculatorModel();
				
		double expected= 20;
		
		model.setYMax(20);
		
		double actual= model.getYMax();
		
		Assertions.assertTrue(expected == actual);
		
	}
	
	@Test 
	void testSetColor() {
		CalculatorModel model= new CalculatorModel();
		String expression= "3 * x - 2";
		
		model.addSingleVariateFunction(0, expression);
		
		model.setColor(0, "Blue");
		Assertions.assertTrue(model.getFunctionAtIndex(0).getColor().equals("Blue"));
	}
	
	@Test
	void testSetStep() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= 0.5;
		
		model.setStep(0.5);
		
		double actual= model.getStep();
		
		Assertions.assertTrue(expected == actual);
		
	}
	
	void testGetStep() {
		CalculatorModel model= new CalculatorModel();
		
		double expected= 0.1;
		
		double actual= model.getStep();
		
		Assertions.assertTrue(actual == expected);
		
	}
	
}
