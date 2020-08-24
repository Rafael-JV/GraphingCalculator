package src;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ControllerTest {

	@Test
	void testCalculation() {
		CalculatorController controller = new CalculatorController(new CalculatorModel());
		
		Assertions.assertTrue(controller.doCalculation("7 + 6").equals("13.0"));
		Assertions.assertTrue(controller.doCalculation("5 * (4 + 2)").equals("30.0"));
		Assertions.assertTrue(controller.doCalculation("5 / 2").equals("2.5"));
		Assertions.assertTrue(controller.doCalculation("log(9)").equals("0.9542425094393249"));
	}
	
	@Test
	void testTangents() {
		CalculatorController controller = new CalculatorController(new CalculatorModel());
		
		Assertions.assertTrue(controller.makeTangent("x^2", "0.0").equals("0.1 * x + 0.0"));
		Assertions.assertTrue(controller.makeTangent("x^2", "1.0").equals("2.1 * x + -1.1"));
		Assertions.assertTrue(controller.makeTangent("x^2", "-3.0").equals("-5.9 * x + -8.7"));
	}
	
	@Test
	void testVariates() {
		CalculatorModel model = new CalculatorModel();
		CalculatorController controller = new CalculatorController(model);
		
		controller.addSingleVariate(0, "x + 5");
		controller.addSingleVariate(1, "x^2");
		
		System.out.println(model.getFunctionList().toString());
		System.out.println("\"" + model.getFunctionAtIndex(0).toString() + "\"");
		System.out.println("\"" + model.getFunctionAtIndex(1).toString() + "\"");
		
		Assertions.assertTrue(model.getFunctionAtIndex(0).toString().equals("x + 5"));
		Assertions.assertTrue(model.getFunctionAtIndex(1).toString().equals("x^2"));
		
		controller.removeEquation(1);
		Assertions.assertTrue(model.getFunctionAtIndex(1) == null);
		
		controller.cleanList();
		Assertions.assertTrue(model.getFunctionList().size() == 1);
		
		controller.addParametric("t", "t^2", 0, 5);
		Assertions.assertTrue(model.getFunctionAtIndex(1).toString().equals("t;t^2"));
	}
	
	@Test
	void testBounds() {
		CalculatorModel model = new CalculatorModel();
		CalculatorController controller = new CalculatorController(model);
		
		controller.addSingleVariate(0, "x + 5");
		
		Assertions.assertTrue(model.getFunctionAtIndex(0).getColor().equals("Black"));
		Assertions.assertTrue(model.getStep() == 0.1);
		Assertions.assertTrue(model.getXMax() == 10.0);
		Assertions.assertTrue(model.getXMin() == -10.0);
		
		controller.changeColor(0,"Red");
		controller.changeMinX(-5.0);
		controller.changeMaxX(5.0);
		controller.changeMinY(-5.0);
		controller.changeMaxY(5.0);
		controller.changeStep(0.5);
		
		Assertions.assertTrue(model.getXMin() == -5.0);
		Assertions.assertTrue(model.getXMax() == 5.0);
		Assertions.assertTrue(model.getYMin() == -5.0);
		Assertions.assertTrue(model.getYMax() == 5.0);
		Assertions.assertTrue(model.getStep() == 0.5);
	}
}
