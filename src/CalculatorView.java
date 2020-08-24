package src;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CalculatorView extends Application implements Observer {
	private final int CANWID = 300;
	private final int CANHIG = 200;
	private final int SCEWID = 500;
	private final int SCEHIG = 350;
	private int numRows = 1;
	private Stage stage;
	private BorderPane BP;
	private Pane canvas = new Pane();
	private String[] panelText = { "clear", "sin", "cos", "tan", "^", "^2", "sin^-1", "cos^-1", "tan^-1", "/", "log",
			"7", "8", "9", "*", "ln", "4", "5", "6", "-", "(", "1", "2", "3", "+", ")", "0", ".", "", "=" };
	private List<String> usableNums = Arrays.asList(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			".", "sin", "cos", "tan", "sin^-1", "cos^-1", "tan^-1", "log", "ln" });
	private List<String> operators = Arrays.asList(new String[] { "+", "-", "/", "*" });
	private String currEquation = "";
	private String storedResult = "0";
	private int LineStart = 0;
	private CalculatorModel model = onOpen();
	private double xMax = model.getXMax();
	private double yMax = model.getYMax();
	private double xMin = model.getXMin();
	private double yMin = model.getYMin();
	private double step = model.getStep();
	private double xRat = CANWID / (xMax - xMin);
	private double yRat = CANHIG / (yMax - yMin);
	private double xCan, yCan, xTrack, yTrack, lastXTrack, lastYTrack = 0;
	
	private CalculatorController controller = new CalculatorController(model);
	private boolean x, y, para = false;
	private double start = -10.0;
	private double minBound = -10.0;
	private double end = 10.0;
	private double maxBound = 10.0;
	private List<Circle> dotHolder = Arrays.asList(null, null, null, null, null, null, null, null, null, null);
	private List<String> tanHolder = Arrays.asList(null, null, null, null, null, null, null, null, null, null);
	private List<Double> dotLocs = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

	/**
	 * Constructs the GUI stage and sets the event handlers for the menu items
	 * 
	 * @param the stage for the GUI to be constructed on
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		model.addObserver(this);
		stage = primaryStage;
		BP = new BorderPane();
		GridPane buttons = new GridPane();

		canvas.setMaxWidth(CANWID);
		canvas.setMaxHeight(CANHIG);
		canvas.setStyle("-fx-background-color: beige;");

		stage.setTitle("TI-8x");
		MenuBar menuBar = new MenuBar();
		Menu graph = new Menu("Graphing Options");
		MenuItem newWindow = new MenuItem("Graph");
		MenuItem window = new MenuItem("Window");
		MenuItem zoom = new MenuItem("Zoom");
		MenuItem parametric = new MenuItem("Parametric");

		zoom.setDisable(true);
		window.setDisable(true);

		newWindow.setOnAction((newGraphWindow) -> {
			canvas.getChildren().clear();
			buttons.setDisable(true);
			newWindow.setDisable(true);
			zoom.setDisable(false);
			window.setDisable(false);
			parametric.setDisable(true);

			Stage graphStage = graphStage();

			graphStage.setOnCloseRequest((graphClose) -> {
				newWindow.setDisable(false);
				zoom.setDisable(true);
				window.setDisable(true);
				canvas.getChildren().clear();
				buttons.setDisable(false);
				parametric.setDisable(false);
				LineStart = 0;
				numRows = controller.cleanList();
			});
		});

		window.setOnAction((windowAction) -> {
			canvas.getChildren().clear();
			setWindow();
		});

		zoom.setOnAction((newZoom) -> {
			if (yMin + 1 != 0) {
				yMin += 1;
			}
			if (yMax - 1 != 0) {
				yMax -= 1;
			}
			if (xMin + 1 != 0) {
				xMin += 1;
			}
			if (xMax - 1 != 0) {
				xMax -= 1;
			}
			if (!para) {
				start += 1;
				end -= 1;
			}
			xRat = Math.round(CANWID / (xMax - xMin));
			yRat = Math.round(CANHIG / (yMax - yMin));

			canvas.getChildren().clear();
			drawBounds();

			controller.changeMaxX(xMax);
			controller.changeMinX(xMin);
			controller.changeMaxY(yMax);
			controller.changeMinY(yMin);
			controller.updatePane();
		});

		parametric.setOnAction((newGraphOptions) -> {
			canvas.getChildren().clear();
			buttons.setDisable(true);
			newWindow.setDisable(true);
			zoom.setDisable(false);
			window.setDisable(false);
			parametric.setDisable(true);

			Stage paraStage = paraStage();
			para = true;

			paraStage.setOnCloseRequest(paraClose -> {
				newWindow.setDisable(false);
				zoom.setDisable(true);
				window.setDisable(true);
				canvas.getChildren().clear();
				buttons.setDisable(false);
				parametric.setDisable(false);
				LineStart = 0;
			});
		});

		graph.getItems().addAll(newWindow, parametric, window, zoom);
		menuBar.getMenus().add(graph);
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		BP.setTop(menuBar);

		BP.setBackground(new Background(new BackgroundFill(Color.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));
		BP.setCenter(canvas);

		buttons.setHgap(10);
		buttons.setVgap(10);
		buttons.setPadding(new Insets(10, 10, 10, 10));

		int count = 0;
		for (int i = 0; i < 6; i++) {
			HBox hbox = new HBox(5);
			for (int j = 0; j < 5; j++) {
				Button newButton = newButton(panelText[count]);
				hbox.getChildren().add(newButton);
				count++;
			}
			buttons.add(hbox, 0, i);
		}

		BP.setBottom(buttons);

		Scene scene = new Scene(BP, SCEHIG, SCEWID);

		stage.setOnCloseRequest((event) -> {
			onClose();
			System.exit(0);
		});

		stage.setScene(scene);
		stage.show();

	}

	/**
	 * This method creates or reads a file upon opening the calculator in order to
	 * store graphed functions
	 */
	private CalculatorModel onOpen() {
		CalculatorModel model;

		try {
			FileInputStream fileIn = new FileInputStream(new File("calculator.ser"));
			ObjectInputStream objIn = new ObjectInputStream(fileIn);
			model = (CalculatorModel) objIn.readObject();
			objIn.close();
			fileIn.close();
		} catch (Exception e) {
			model = new CalculatorModel();
		}
		return model;
	}

	/**
	 * This method writes to the created file on close to store the objects within
	 * the graph
	 */
	private void onClose() {
		try {
			FileOutputStream fileOut = new FileOutputStream(new File("calculator.ser"));
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(model);

			objOut.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The Stage created for the parametric equations to gather the necessary data
	 * 
	 * @return the parametric stage object created in this method
	 */
	public Stage paraStage() {
		drawGrid();

		Stage paraStage = new Stage();
		GridPane grid = new GridPane();

		paraStage.setTitle("Graph What?");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		HBox functBox = new HBox(4);
		Label x = new Label("x(t): ");
		TextField xFunct = new TextField();

		Label y = new Label("y(t): ");
		TextField yFunct = new TextField();

		functBox.getChildren().addAll(x, xFunct, y, yFunct);

		HBox rangeBox = new HBox(4);
		Label min = new Label("Min:");
		TextField tRange = new TextField();

		Label max = new Label("Max:");
		TextField sRange = new TextField();

		rangeBox.getChildren().addAll(min, tRange, max, sRange);

		HBox buttons = new HBox(2);
		Button ok = new Button("Run");
		ok.setPrefWidth(100);
		buttons.getChildren().addAll(ok);
		buttons.setAlignment(Pos.CENTER);

		ok.setOnAction(okEvent -> {
			double[] prevValues = { minBound, start, maxBound, end };
			try {
				SingleVariate startCalc = new SingleVariate(xFunct.getText(), Double.parseDouble(tRange.getText()),
						Double.parseDouble(sRange.getText()));
				minBound = Double.parseDouble(tRange.getText());
				start = startCalc.calc(Double.parseDouble(tRange.getText())).getY();
				SingleVariate endCalc = new SingleVariate(xFunct.getText(), Double.parseDouble(tRange.getText()),
						Double.parseDouble(sRange.getText()));
				maxBound = Double.parseDouble(sRange.getText());
				end = endCalc.calc(Double.parseDouble(sRange.getText())).getY();
				controller.addParametric(xFunct.getText(), yFunct.getText(), Double.parseDouble(tRange.getText()),
						Double.parseDouble(sRange.getText()));
				drawBounds();
				controller.updatePane();
			} catch (Exception e) {
				minBound = prevValues[0];
				start = prevValues[1];
				maxBound = prevValues[2];
				end = prevValues[3];
			}
		});

		grid.add(functBox, 0, 0);
		grid.add(rangeBox, 0, 1);
		grid.add(buttons, 0, 2);

		paraStage.setScene(new Scene(grid));
		paraStage.show();

		return paraStage;
	}

	/**
	 * Constructs the extra windows for inputting the functions to be graphed.
	 * 
	 * @return The constructed stage
	 */
	public Stage graphStage() {
		drawGrid();
		model.updatePane();

		Stage graphStage = new Stage();
		GridPane grid = new GridPane();

		graphStage.setTitle("Graph What?");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		try {

			int k;
			for (k = 0; k <= model.getFunctionList().size(); k++) {
				HBox newHB = newEquationLine(k);

				if (k != model.getFunctionList().size() && model.getFunctionAtIndex(k) != null) {
					((TextField) newHB.getChildren().get(0)).setText(model.getFunctionAtIndex(k).toString());
					((Node) newHB.getChildren().get(2)).setDisable(false);
					((Node) newHB.getChildren().get(3)).setDisable(false);
				}

				grid.add(newHB, 0, k);
			}

			numRows = k;

			HBox addRemove = new HBox(2);
			Button add = new Button("Add Row");
			add.setPrefWidth(100);

			Button remove = new Button("Remove Row");
			remove.setPrefWidth(100);
			remove.setDisable(true);

			add.setOnAction((addRow) -> {
				if (numRows < 10) {
					grid.getChildren().remove(numRows);
					grid.add(newEquationLine(numRows), 0, numRows);
					numRows++;
					grid.add(addRemove, 0, numRows);
					graphStage.getScene().setRoot(grid);
					graphStage.sizeToScene();
				}
				if (numRows > 1) {
					remove.setDisable(false);
				} else if (numRows == 1) {
					remove.setDisable(true);
				}

			});

			remove.setOnAction((removeRow) -> {
				if (numRows != 0) {
					grid.getChildren().remove(numRows);
					grid.getChildren().remove(numRows - 1);
					numRows--;
					grid.add(addRemove, 0, numRows);
					graphStage.getScene().setRoot(grid);
					graphStage.sizeToScene();
				}
				if (numRows == 1) {
					remove.setDisable(true);
				}
			});

			Button runAll = new Button("Run All");
			runAll.setPrefWidth(100);
			runAll.setOnAction((newRunAll) -> {
				for (int i = 0; i < grid.getChildren().size() - 1; i++) {
					Node item = grid.getChildren().get(i);
					HBox currItem = (HBox) item;
					Node run = currItem.getChildren().get(1);
					Button runButton = (Button) run;
					runButton.fire();
				}
			});

			Button clearAll = new Button("Clear all");
			clearAll.setPrefWidth(100);
			clearAll.setOnAction(clearAllEvent -> {
				model.clearFunctions();
				for (int i = 0; i < grid.getChildren().size() - 1; i++) {
					Node item = grid.getChildren().get(i);
					HBox currItem = (HBox) item;
					Node clear = currItem.getChildren().get(2);
					Button clearButton = (Button) clear;
					clearButton.fire();
				}
				drawGrid();
				model.updatePane();
			});

			Button saveImg = new Button("Save as");
			saveImg.setPrefWidth(100);
			saveImg.setOnAction((event) -> {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
				fileChooser.getExtensionFilters().add(filter);

				File file = fileChooser.showSaveDialog(stage);

				if (file != null) {
					try {
						WritableImage writableImage = new WritableImage(CANWID, CANHIG);
						BP.getCenter().snapshot(null, writableImage);
						RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
						ImageIO.write(renderedImage, "png", file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			addRemove.getChildren().addAll(add, remove, runAll, clearAll, saveImg);
			grid.add(addRemove, 0, numRows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		graphStage.setScene(new Scene(grid));
		graphStage.show();

		return graphStage;
	}

	/**
	 * This class makes generic buttons set to the passed in title in order to
	 * construct the grid of buttons on the face of the calculator
	 * 
	 * @param The title and value to be stored in the button
	 * @return The constructed button object
	 */
	public Button newButton(String title) {
		Button tempButton = new Button();
		tempButton.setText(title);
		tempButton.setOnAction(buttonPress -> {
			if (title.equals("clear")) {
				canvas.getChildren().clear();
				LineStart = 0;
				currEquation = "";
			} else if (title.equals("=")) {
				if (currEquation.length() > 0) {
					LineStart += 13;
					storedResult = controller.doCalculation(currEquation);
					currEquation = storedResult;
				} else {
					currEquation = storedResult;
				}

				Label currLine = new Label(currEquation);
				currLine.setFont(Font.font("Times New Roman", 12));
				currLine.relocate(5, LineStart);

				canvas.getChildren().add(currLine);
				currEquation = "";

				LineStart += 13;

				while (LineStart + 17 >= CANHIG) {
					canvas.getChildren().remove(0);
					LineStart = 0;
					for (Object item : canvas.getChildren()) {
						((Label) item).relocate(5, LineStart);
						LineStart += 13;
					}

				}
			} else {
				if (operators.contains(title) && currEquation.isEmpty()) {
					currEquation = currEquation + storedResult + " " + title + " ";
				} else if (usableNums.contains(title)) {
					currEquation = currEquation + title;
					if (currEquation.length() > title.length()) {
						canvas.getChildren().remove(canvas.getChildren().size() - 1);
					}
				} else {
					currEquation = currEquation + " " + title + " ";
					if (currEquation.length() > title.length() + 2) {
						canvas.getChildren().remove(canvas.getChildren().size() - 1);
					}
				}

				Label currLine = new Label(currEquation);
				currLine.setFont(Font.font("Times New Roman", 12));
				currLine.relocate(5, LineStart);

				canvas.getChildren().add(currLine);
			}
		});

		tempButton.setPrefWidth(110);
		HBox.setHgrow(tempButton, Priority.ALWAYS);

		return tempButton;
	}

	/**
	 * This is used within the graphing window to make input lines for equations and
	 * handle the object's events
	 * 
	 * @return the HBox holding the textfield and the two buttons
	 */
	public HBox newEquationLine(int currIndex) {
		HBox hbox = new HBox(3);
		TextField input = new TextField();
		input.setPrefWidth(185);

		Button run = new Button("Run");
		run.setDisable(true);
		run.setPrefWidth(50);

		Button clear = new Button("Clear");
		clear.setDisable(true);
		clear.setPrefWidth(80);

		Button trace = new Button("Trace");
		trace.setDisable(true);
		trace.setPrefWidth(80);

		ComboBox<String> colorBox = new ComboBox<String>();
		colorBox.getItems().addAll("Black", "Red", "Yellow", "Green", "Blue", "Purple", "Pink", "Orange", "White",
				"Cyan");
		colorBox.setPrefWidth(100);
		colorBox.getSelectionModel().select(currIndex);

		clear.setOnAction((clearPress) -> {
			input.clear();
			clear.setDisable(true);
			run.setDisable(true);
			trace.setDisable(true);
			drawBounds();
			controller.removeEquation(currIndex);
		});

		run.setOnAction((runPress) -> {
			String text = input.getText();
			if (!text.isEmpty()) {
				drawBounds();
				controller.addSingleVariate(currIndex, text);
				controller.changeColor(currIndex, colorBox.getSelectionModel().getSelectedItem());
				clear.setDisable(false);
				trace.setDisable(false);

			}
		});

		trace.setOnAction(TracePress -> {
			traceStage(currIndex, input.getText(), colorBox.getSelectionModel().getSelectedItem());
		});

		input.textProperty().addListener(listener -> {
			if (!input.getText().isEmpty()) {
				run.setDisable(false);
			} else {
				run.setDisable(true);
			}
		});

		colorBox.setOnAction((e) -> {
			if (!input.getText().isEmpty()) {
				controller.changeColor(currIndex, colorBox.getSelectionModel().getSelectedItem());
				if (dotHolder.get(currIndex) != null) {
					((Circle) dotHolder.get(currIndex))
							.setFill(Color.valueOf(colorBox.getSelectionModel().getSelectedItem()));
				}
			}
			drawBounds();
			controller.updatePane();
		});

		hbox.getChildren().addAll(input, run, clear, trace, colorBox);
		return hbox;
	}

	/**
	 * This method is made to create the stage for the trace cursor when called
	 * 
	 * @param index    : the index of the current equation
	 * @param equation : the current equation that will be used to calculate y
	 *                 values
	 * @param color    : the color of the current equation
	 */
	public void traceStage(int index, String equation, String color) {
		Function funct = new SingleVariate(equation, xMin, xMax);
		Stage traceStage = new Stage();
		GridPane grid = new GridPane();

		traceStage.setTitle("Trace");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 50, 15, 50));

		Button left = new Button("<-");
		left.setPrefWidth(40);
		Button right = new Button("->");
		right.setPrefWidth(40);

		HBox arrowBox = new HBox(2);
		arrowBox.getChildren().addAll(left, right);

		Label x = new Label("X:");
		Label xVal = new Label(String.valueOf(dotLocs.get(index)));

		Label y = new Label("Y:");
		Label yVal = new Label(String.valueOf(funct.calc(dotLocs.get(index)).getY()));

		HBox valBox = new HBox(4);
		valBox.getChildren().addAll(x, xVal, y, yVal);

		CheckBox tangent = new CheckBox("Find Tangent");
		tangent.setSelected(false);

		Label tangentEq = new Label("");
		HBox tangentBox = new HBox(tangentEq);
		tangentBox.setVisible(false);

		tangent.setOnAction(event -> {
			tangentBox.setVisible(tangent.isSelected());
			tangentEq.setText(controller.makeTangent(equation, String.valueOf(dotLocs.get(index))));
			tanHolder.set(index, controller.makeTangent(equation, String.valueOf(dotLocs.get(index))));
		});
		addDot(index, 0.0, Math.round((funct.calc(0.0).getY() * 100)) / 100.0, color);

		drawBounds();
		controller.updatePane();

		left.setOnAction(moveLeft -> {
			dotLocs.set(index, dotLocs.get(index) - step);
			double xCoord = Math.round((dotLocs.get(index) * 10)) / 10.0;
			double yCoord = Math.round((funct.calc(xCoord).getY() * 100)) / 100.0;
			((Label) (valBox.getChildren().get(1))).setText(String.valueOf((xCoord)));
			((Label) (valBox.getChildren().get(3))).setText(String.valueOf(yCoord));
			addDot(index, xCoord, yCoord, color);
			tangentEq.setText(controller.makeTangent(equation, String.valueOf(dotLocs.get(index))));
			drawBounds();
			controller.updatePane();
		});
		right.setOnAction(moveRight -> {
			dotLocs.set(index, dotLocs.get(index) + step);
			double xCoord = Math.round((dotLocs.get(index) * 10)) / 10.0;
			double yCoord = Math.round((funct.calc(xCoord).getY() * 100)) / 100.0;
			((Label) (valBox.getChildren().get(1))).setText(String.valueOf((xCoord)));
			((Label) (valBox.getChildren().get(3))).setText(String.valueOf(yCoord));
			addDot(index, xCoord, yCoord, color);
			tangentEq.setText(controller.makeTangent(equation, String.valueOf(dotLocs.get(index))));
			drawBounds();
			controller.updatePane();
		});

		grid.add(valBox, 0, 0);
		grid.add(arrowBox, 0, 1);
		grid.add(tangent, 0, 2);
		grid.add(tangentBox, 0, 3);

		traceStage.setScene(new Scene(grid));
		traceStage.show();
		traceStage.setOnCloseRequest((Close) -> {
			if (!canvas.getChildren().isEmpty()) {
				dotHolder.set(index, null);
				dotLocs.set(index, 0.0);
				drawBounds();
				controller.updatePane();
			}
		});
	}

	/**
	 * This method adds the cursor circle to the respective function when trace is
	 * called.
	 * 
	 * @param index  : the function the cursor is tied to
	 * @param xCoord : the x coordinate of the cursor
	 * @param yCoord : the y coordinate of the cursor
	 * @param color  : the color of the cursor
	 */
	public void addDot(int index, double xCoord, double yCoord, String color) {
		Circle dot = new Circle((xRat * (-1 * xMin)) + (xRat * xCoord), (yRat * yMax) - (yRat * yCoord), 2);
		dot.setFill(Color.valueOf(color));
		dotHolder.set(index, dot);
	}

	/**
	 * This method draws the grid on the graphing pane and controls the updating of
	 * the pane when dragged by a mouse or other scaling methods.
	 */
	public void drawGrid() {
		drawBounds();

		canvas.setOnMousePressed(click -> {
			xCan = click.getSceneX();
			yCan = click.getSceneY();
		});
		canvas.setOnMouseDragged((drag) -> {

			double offsetX = drag.getSceneX() - xCan;
			double offsetY = drag.getSceneY() - yCan;

			xTrack = offsetX;
			yTrack = offsetY;

			if (xTrack % (xRat) == 0 && xTrack > lastXTrack) {
				if (!x) {
					xMax--;
					xMin--;
					if (!para) {
						start--;
						end--;
						minBound--;
						maxBound--;
					}
					xTrack = 0;
					x = true;
				}
			} else if (xTrack % (xRat) == 0 && xTrack < lastXTrack) {
				if (!x) {
					xMax++;
					xMin++;
					if (!para) {
						start++;
						end++;
						minBound++;
						maxBound++;
					}
					xTrack = 0;
					x = true;
				}
			} else {
				x = false;
			}
			if (yTrack % (yRat) == 0 && yTrack > lastYTrack) {
				if (!y) {
					yMax++;
					yMin++;
					yTrack = 0;
					y = true;
				}
			} else if (yTrack % (yRat) == 0 && yTrack < lastYTrack) {
				if (!y) {
					yMax--;
					yMin--;
					yTrack = 0;
					y = true;

				}
			} else {
				y = false;
			}

			lastXTrack = xTrack;
			lastYTrack = yTrack;

			controller.changeMaxX(xMax);
			controller.changeMinX(xMin);
			controller.changeMaxY(yMax);
			controller.changeMinY(yMin);
			controller.updatePane();
			
			drawBounds();
		});
	}

	/**
	 * This method sets the values for the edge counters for the x and y values on
	 * the pane.
	 */
	public void drawBounds() {
		canvas.getChildren().clear();
		if (xMin < 0 && xMax > 0) {
			Line yAxis = new Line((xRat * (-1 * xMin)), 0, (xRat * (-1 * xMin)), CANHIG);
			canvas.getChildren().add(yAxis);
		}
		if (yMin < 0 && yMax > 0) {
			Line xAxis = new Line(0, (yRat) * yMax, CANWID, (yRat) * yMax);
			canvas.getChildren().add(xAxis);
		}

		Label xBoundMin = new Label(String.valueOf(xMin));
		xBoundMin.setFont(Font.font("Times New Roman", 12));
		xBoundMin.relocate((double) 2, (canvas.getHeight() / 2) - 15);
		Label xBoundMax = new Label(String.valueOf(xMax));
		xBoundMax.setFont(Font.font("Times New Roman", 12));
		xBoundMax.relocate(canvas.getWidth() - 25, (canvas.getHeight() / 2) - 15);
		Label yBoundMin = new Label(String.valueOf(yMax));
		yBoundMin.setFont(Font.font("Times New Roman", 12));
		yBoundMin.relocate(canvas.getWidth() / 2 + 5, 0.0);
		Label yBoundMax = new Label(String.valueOf(yMin));
		yBoundMax.setFont(Font.font("Times New Roman", 12));
		yBoundMax.relocate(canvas.getWidth() / 2 + 5, canvas.getHeight() - 15);

		canvas.getChildren().addAll(xBoundMin, xBoundMax, yBoundMin, yBoundMax);
	}

	/**
	 * This method makes a new window for scaling the graph in the graph pane using
	 * text fields to store and take in data.
	 */
	public void setWindow() {
		Stage windowStage = new Stage();
		GridPane grid = new GridPane();

		windowStage.setTitle("Set Graph Bounds");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		HBox xBox = new HBox(4);
		HBox yBox = new HBox(4);
		HBox stepBox = new HBox(2);
		HBox okBox = new HBox();

		Label minX = new Label("Min X:");
		TextField minXField = new TextField();
		minXField.setText(String.valueOf(xMin));

		Label maxX = new Label("Max X:");
		TextField maxXField = new TextField();
		maxXField.setText(String.valueOf(xMax));

		xBox.getChildren().addAll(minX, minXField, maxX, maxXField);

		Label minY = new Label("Min Y:");
		TextField minYField = new TextField();
		minYField.setText(String.valueOf(yMin));

		Label maxY = new Label("Max Y:");
		TextField maxYField = new TextField();
		maxYField.setText(String.valueOf(yMax));

		yBox.getChildren().addAll(minY, minYField, maxY, maxYField);

		Label stepLabel = new Label("Step Value:");
		TextField stepField = new TextField();
		stepField.setText(String.valueOf(step));
		stepBox.getChildren().addAll(stepLabel, stepField);

		Button Ok = new Button("Ok");
		Ok.setOnAction((runOk) -> {
			double[] prevValues = { xMin, xMax, yMin, yMax, step };
			try {
				if (Double.parseDouble(minXField.getText()) < Double.parseDouble(maxXField.getText())
						&& Double.parseDouble(minYField.getText()) < Double.parseDouble(maxYField.getText())) {
					xMin = Double.parseDouble(minXField.getText());
					xMax = Double.parseDouble(maxXField.getText());
					yMin = Double.parseDouble(minYField.getText());
					yMax = Double.parseDouble(maxYField.getText());
					step = Double.parseDouble(stepField.getText());

					controller.changeMinX(xMin);
					controller.changeMaxX(xMax);
					controller.changeMinY(yMin);
					controller.changeMaxY(yMax);
					controller.changeStep(step);
					controller.updatePane();
					
					drawBounds();

					if (!para) {
						start = Double.parseDouble(minXField.getText());
						end = Double.parseDouble(maxXField.getText());
					}
				}
			} catch (NumberFormatException e) {
				xMin = prevValues[0];
				xMax = prevValues[1];
				yMin = prevValues[2];
				yMax = prevValues[3];
				step = prevValues[4];
			}

			xRat = Math.round(CANWID / (xMax - xMin));
			yRat = Math.round(CANHIG / (yMax - yMin));
			windowStage.close();
		});

		Button Cancel = new Button("Cancel");
		Cancel.setOnAction((runCancel) -> {
			windowStage.close();
		});

		okBox.getChildren().addAll(Ok, Cancel);
		okBox.setAlignment(Pos.CENTER);

		grid.add(xBox, 0, 0);
		grid.add(yBox, 0, 1);
		grid.add(stepBox, 0, 2);
		grid.add(okBox, 0, 3);

		windowStage.setScene(new Scene(grid));
		windowStage.show();

	}

	/**
	 * Formats the start and end values to work with the pane based grid layout
	 * 
	 * @param bound: either the start or end bound
	 * @param funct: the function the bound is associated with
	 * @return the new value of the bound
	 */
	public Double prepBounds(double bound, Function funct) {
		if (bound < 0) {
			bound = (bound) - (xMin);
		} else if (bound >= 0) {
			bound = bound + (-1.0 * xMin);
		}
		return bound;
	}

	/**
	 * Updates the graph when functions or equations are input on the graph input
	 * window, this also allows the graph to move dynamically if the pane is
	 * shifted.
	 */
	@Override
	public void update(Observable o, Object arg) {
		CalculatorModel model = (CalculatorModel) o;
		ArrayList<Function> functs = (ArrayList) arg;
		int dotIndex = 0;
		for (Function funct : functs) {
			if (funct != null) {
				double start = this.start;
				double end = this.end;
				start = prepBounds(start, funct);
				end = prepBounds(end, funct);

				double x = start * xRat;
				double lastX = 0.0;
				Point lastP = null;
				double lastYPlace = 0.0;
				double yPlace = 0.0;
				boolean print = true;
				Path path = new Path();
				path.setStroke(Color.valueOf(funct.getColor()));
				for (double i = minBound; i <= maxBound; i += step) {
					i = Math.round((i * 10)) / 10.0;
					Point p = funct.calc(i);
					if (lastP != null) {
						if (p.getX() < lastP.getX()) {
							x -= (model.getStep() * xRat);
							x = Math.round((x * 10)) / 10.0;
						} else {
							x += (model.getStep() * xRat);
							x = Math.round((x * 10)) / 10.0;
						}
					}
					Double y = p.getY();
					yPlace = (yRat * yMax) - (yRat * y);
					if (yPlace <= 0) {
						y = yMax;
						yPlace = 0.0;
						if (yPlace == lastYPlace) {
							print = false;
						}

					} else if (yPlace >= CANHIG) {
						y = yMin;
						yPlace = CANHIG;
						if (yPlace == lastYPlace) {
							print = false;
						}

					} else {
						print = true;
					}

					if (!Double.isNaN(y)) {
						if (i != minBound && print) {
							path.getElements().add(new MoveTo((lastX), lastYPlace));
							path.getElements().add(new LineTo((x), yPlace));
						}
					} else {
						path.getElements().add(new MoveTo((x), yRat * y));
					}
					lastYPlace = yPlace;

					lastX = x;
					lastP = p;
				}

				canvas.getChildren().add(path);
				if (dotHolder.get(dotIndex) != null && !canvas.getChildren().contains(dotHolder.get(dotIndex))) {
					addDot(dotIndex, dotLocs.get(dotIndex), funct.calc(dotLocs.get(dotIndex)).getY(), funct.getColor());
					if (funct.calc(dotLocs.get(dotIndex)).getY() >= yMin
							&& funct.calc(dotLocs.get(dotIndex)).getY() <= yMax) {
						canvas.getChildren().add(dotHolder.get(dotIndex));
					}
				}
				dotIndex++;
			}
		}

	}
}