package type.testtypes;

import flashmonkey.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;
import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;
import type.tools.calculator.DijkstraParser;
import type.tools.calculator.ExpNode;
import type.tools.calculator.Graph;
import type.tools.calculator.OperatorInterface;
import uicontrols.SceneCntl;
import uicontrols.UIColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.EmptyStackException;

//import javafx.scene.shape.Line;
//import javafx.scene.text.Text;
//import javafx.scene.control.Tooltip;
//import javax.swing.ToolTipManager;
//import java.lang.reflect.Field;
//import java.rmi.server.ExportException;

// scilab jlatex math
//import org.scilab.forge.jlatexmath.TeXConstants;
//import org.scilab.forge.jlatexmath.TeXFormula;
//import org.scilab.forge.jlatexmath.TeXIcon;


/**
 * - To add additional operators to this class's capabilities,
 * add them in the appropriate class, or the Operator class.
 *  Add them as an enum. Include it's type in the getOperator() switch.
 *
 * //@todo Replace methods that are not parallelable and use ParallelStream for 4x effiency
 *
 * example:
 * String[] myArray = { "this", "is", "a", "sentence" };
 * String result = Arrays.ParallelStream(myArray)
 *                 .reduce("", (a,b) lamda a + b);
 * see https://www.sitepoint.com/java-8-streams-filter-map-reduce/ and  rice university parallel processing
 *
 *  Mutable reduction @ https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#Reduction
 *
 *   * Mutable reduction
 *   * A mutable reduction operation accumulates input elements into a mutable result container,
 *   * such as a Collection or StringBuilder, as it processes the elements in the stream.
 *   *
 *   * If we wanted to take a stream of strings and concatenate them into a single long string, we could achieve this with ordinary reduction:
 *   *
 *   * String concatenated = strings.reduce("", String::concat)
 *
 *
 * @author Lowell Stadelman
 */
public class GraphCard extends MathCard {

    // Contains the graph
    private Pane graphPane;
    private int graphXLow;
    private int graphXHigh;
    private int graphYLow;
    private int graphYHigh;
    //private int countX;
    //private int countY;
    private Graph cardGraph;
    private DijkstraParser parser;
    private SectionEditor upperEditor;
    private static FMToolTip fmQTip;

    //private final String FTCLatex = "\\int_{a}^{b} f'(x)dx = f(b) - f(a)";
    //private final String fafbLatexBase = "f(b) - f(a) = ";
    //private final String integralLatexBase = "\\int_{a}^{b} f'(x)dx = ";

    //private static DecimalFormat percentFormat = new DecimalFormat("0.#");
    //private static DecimalFormat coordinateFormat = new DecimalFormat("0.####");



    public GraphCard()
    {
        super();
    }
    
    @Override
    public boolean isDisabled() {
        return false;
    }

    private void init() {
        //djParser = new DijkstraParser();
        graphPane = new Pane();
        graphPane.setMaxHeight(200);
        graphPane.setMaxWidth(300);
        graphPane.setId("graphPane");

        graphXHigh = 6;
        graphXLow = -8;
        graphYHigh = 300;
        graphYLow = -20;

        int centerX = 150;
        int centerY = 100;

        parser = new DijkstraParser();
        cardGraph = new Graph(centerX, centerY);
        upperEditor = new SectionEditor();
    }

    /***  SETTERS ***/

    public void setGraphXHigh(int high) {
        this.graphXHigh = high;
    }

    public void setGraphXLow(int low) {
        this.graphXLow = low;
    }

    /*** GETTERS ***/

    /**
     * Sets 12th bit, or equal to  all other bits are 0
     * @return bitSet. Used when parsing
     * cards from flashList
     */
    @Override
    public int getTestType()
    {
        // 4096
        return 0b0001000000000000;
    }

    @Override
    public GenericTestType getTest() {

        return new GraphCard();
    }

    @Override
    public String getName() {
        return "Graph Card";
    }

    /**
     * Returns the Graph X High
     * @return
     */
    public int getGraphXHigh() {
        return this.graphXHigh;
    }

    /**
     * Returns the Graph Low X
     * @return
     */
    public int getGraphXLow() {
        return this.graphXLow;
    }


    /*** OTHERS ***/

    @Override
    public void reset() {

        graphPane.getChildren().clear();
        graphPane.getChildren().add(cardGraph.createXYAxis(Color.BLACK, SceneCntl.getCenterWd(), 200));
        graphPane.getChildren().add(cardGraph.createGrid(Color.web(UIColors.GRAPH_BGND), SceneCntl.getCenterWd(), 200 ));
        cardGraph.axisNums(graphPane, SceneCntl.getCenterWd(), 200);
    }


    @Override
    public VBox getTEditorPane(ArrayList<FlashCardMM> flashList, SectionEditor p, SectionEditor r)
    {
        VBox vBox = super.getTEditorPane(flashList, p, r);
        vBox.setSpacing(2);

        init();

        upperEditor = p;
        p.setPrompt("Enter Math Formula, quiz entries go into textFields, Graph will be displayed " +
                "in testing. ");
        FlowPane flow = new FlowPane();
        //graphPane.setMaxHeight(200);
        graphPane.getChildren().add(cardGraph.createXYAxis(Color.BLACK, SceneCntl.getCenterWd(), 200));
        graphPane.getChildren().add( cardGraph.createGrid(Color.web(UIColors.GRID_GREY), SceneCntl.getCenterWd(), 200));
        cardGraph.axisNums(graphPane, SceneCntl.getCenterWd(), 200);

        //centerX = SceneCntl.getWd() / 2;
        //centerY = 100;

        // Clickable graphPane for large graph pane
        graphPane.setOnMouseClicked((e) -> graphClickAction());

        flow.getChildren().addAll(graphPane, userSettingsBox());

        flow.setMaxHeight(226);
        flow.setVgap(4);
        flow.setAlignment(Pos.CENTER);

        vBox.getChildren().add(0, flow);
        Pane pane = new Pane();
        pane.setMinWidth(100);
        pane.setMinHeight(100);
        // LatexPane
//        vBox.getChildren().add(getLaTexPane());

        return vBox;
    }

    /**
     * The main card pane during testing
     * @param cc
     * @param genCard
     * @return
     */
    @Override
    public Pane getTReadPane(FlashCardMM cc, GenericCard genCard, Pane parentPane)
    {

        GridPane gridPane = (GridPane) super.getTReadPane(cc, genCard, parentPane);
        FlowPane flow = new FlowPane();

        init();

        //graphPane.setPrefHeight(200);
        graphPane.getChildren().add(cardGraph.createXYAxis(Color.BLACK, SceneCntl.getCenterWd(), 200));
        graphPane.getChildren().add( cardGraph.createGrid(Color.web(UIColors.GRID_GREY), SceneCntl.getCenterWd(), 200));
        cardGraph.axisNums(graphPane, SceneCntl.getCenterWd(), 200);

        super.expression = cc.getQText();

        // reset the screen
        reset();
        // split multiple expressions
        String[] exps = splitExps(expression);
        // display expressions on the graph
        for(String e : exps) {

            graphPane = graphExpression( e, graphXLow, graphXHigh, graphYLow, graphYHigh, graphPane, 300, 200);
        }

        // Determine Y upperRange, and Y lowerRange
        // Use defaults and provide EncryptedUser.EncryptedUser a way to change xlow
        // and xHigh range
        graphPane.setOnMouseClicked((e) -> graphClickAction());

        flow.getChildren().addAll(graphPane, userSettingsBox());

        flow.setMaxHeight(226);
        flow.setVgap(4);
        flow.setAlignment(Pos.CENTER);

        gridPane.getChildren().add(0, flow);

        return gridPane;
    }

    // Latex Math example -- REMOVE --
    private Pane getLaTexPane() {

        Pane latexPane = new Pane();

        String latex = "x = \\frac{ -b\\pm \\sqrt{-b^2 -4ac}}{2a}";
/*
        String latex = "\\begin{array}{l}";
        latex += "\\forall\\varepsilon\\in\\mathbb{R}_+^*\\ \\exists\\eta>0\\ |x-x_0|\\leq\\eta\\Longrightarrow|f(x)-f(x_0)|\\leq\\varepsilon\\\\";
        latex += "\\det\\begin{bmatrix}a_{11}&a_{12}&\\cdots&a_{1n}\\\\a_{21}&\\ddots&&\\vdots\\\\\\vdots&&\\ddots&\\vdots\\\\a_{n1}&\\cdots&\\cdots&a_{nn}\\end{bmatrix}\\overset{\\mathrm{def}}{=}\\sum_{\\sigma\\in\\mathfrak{S}_n}\\varepsilon(\\sigma)\\prod_{k=1}^n a_{k\\sigma(k)}\\\\";
        latex += "\\sideset{_\\alpha^\\beta}{_\\gamma^\\delta}{\\begin{pmatrix}a&b\\\\c&d\\end{pmatrix}}\\\\";
        latex += "\\int_0^\\infty{x^{2n} e^{-a x^2}\\,dx} = \\frac{2n-1}{2a} \\int_0^\\infty{x^{2(n-1)} e^{-a x^2}\\,dx} = \\frac{(2n-1)!!}{2^{n+1}} \\sqrt{\\frac{\\pi}{a^{2n+1}}}\\\\";
        latex += "\\int_a^b{f(x)\\,dx} = (b - a) \\sum\\limits_{n = 1}^\\infty  {\\sum\\limits_{m = 1}^{2^n  - 1} {\\left( { - 1} \\right)^{m + 1} } } 2^{ - n} f(a + m\\left( {b - a} \\right)2^{-n} )\\\\";
        latex += "\\int_{-\\pi}^{\\pi} \\sin(\\alpha x) \\sin^n(\\beta x) dx = \\textstyle{\\left \\{ \\begin{array}{cc} (-1)^{(n+1)/2} (-1)^m \\frac{2 \\pi}{2^n} \\binom{n}{m} & n \\mbox{ odd},\\ \\alpha = \\beta (2m-n) \\\\ 0 & \\mbox{otherwise} \\\\ \\end{array} \\right .}\\\\";
        latex += "L = \\int_a^b \\sqrt{ \\left|\\sum_{i,j=1}^ng_{ij}(\\gamma(t))\\left(\\frac{d}{dt}x^i\\circ\\gamma(t)\\right)\\left(\\frac{d}{dt}x^j\\circ\\gamma(t)\\right)\\right|}\\,dt\\\\";
        latex += "\\begin{array}{rl} s &= \\int_a^b\\left\\|\\frac{d}{dt}\\vec{r}\\,(u(t),v(t))\\right\\|\\,dt \\\\ &= \\int_a^b \\sqrt{u'(t)^2\\,\\vec{r}_u\\cdot\\vec{r}_u + 2u'(t)v'(t)\\, \\vec{r}_u\\cdot\\vec{r}_v+ v'(t)^2\\,\\vec{r}_v\\cdot\\vec{r}_v}\\,\\,\\, dt. \\end{array}\\\\";
        latex += "\\end{array}";
*/

        //String latexStr = fafbLatexBase + coordinateFormat.format(3)+" - "+coordinateFormat.format(2)+" = "+coordinateFormat.format(1);
        //String integralLatex = integralLatexBase+coordinateFormat.format(integral);
        String latexStr = "x = \\frac{ -b\\pm \\sqrt{-b^2 -4ac}}{2a}";

 //       latexPane.getChildren().add(buildLatexImage(latexStr));
        latexPane.setMinHeight(100);
        latexPane.setMinWidth(300);
        latexPane.setStyle("-fx-background-color: WHITE");
        return latexPane;
    }

 /*   private static ImageView buildLatexImage(String latex) {
        TeXFormula formula = new TeXFormula(latex);
        //TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();

        //BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);


        //icon. paintIcon(null, , 0, 0);
        java.awt.Image awtImage = formula.createBufferedImage(TeXConstants.STYLE_TEXT, 24, java.awt.Color.BLACK, null);
        Image fxImage = SwingFXUtils.toFXImage((BufferedImage) awtImage, null);
        ImageView imageView = new ImageView(fxImage);

        return imageView;
    }

  */


    /**
     * The box with fields to set the graph view
     * @return
     */
    private HBox userSettingsBox() {

        HBox box = new HBox(4);
        TextField xLow = new TextField();
        xLow.setPrefColumnCount(4);

        xLow.requestFocus();
        TextField xHigh = new TextField();
        xHigh.setPrefColumnCount(4);
        TextField yLow = new TextField();
        yLow.setPrefColumnCount(4);
        TextField yHigh = new TextField();
        yHigh.setPrefColumnCount(4);
        xLow.setId("graph_settings_border");
        xLow.setPromptText("xLow");
        xHigh.setId("graph_settings_border");
        xHigh.setPromptText("xHigh");
        yLow.setId("graph_settings_border");
        yLow.setPromptText("yLow");
        yHigh.setId("graph_settings_border");
        yHigh.setPromptText("yHigh");
        box.getChildren().clear();
        box.getChildren().addAll(xLow, xHigh, yLow, yHigh);
        return box;
    }



    /**
     * Splits a text area with multiple expressions into seperate expressions.
     * Searches for the new line or "\n".
     * @param exps
     * @return
     */
    private String[] splitExps(String exps) {

        //reset();
        return exps.split("\n");
    }


    /*******************************************************************************************************************
     *                                                                                                                 *
     *                                            EXPRESSION CALC                                                      *
     *                                                                                                                 *
     ******************************************************************************************************************/



    /**
     * Recieves the expression and inserts it as a line into an existing graphView.
     * @param exp       The expression
     * @param xLowLim   The low X limit
     * @param xHighLim  The high X limit
     * @param graphP    The pane the graph exists in
     * @return
     */
    public Pane graphExpression(String exp, double xLowLim, double xHighLim, double yLowLim, double yHighLim,
                                Pane graphP, int width, int height)
    {

        SVGPath path = new SVGPath();

        Canvas graphCanvas = new Canvas();
        graphCanvas.setHeight(height);
        graphCanvas.setWidth(width);
        // clip the graph edges to prevent any bleed over
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(graphCanvas.getLayoutX());
        clip.setLayoutY(graphCanvas.getLayoutY());

        fmQTip = new FMToolTip(graphP);

        path.setOnMouseEntered(e -> {
            fmQTip.getFMTooltip(e, exp);
            fmQTip.show();
        });

        path.setOnMouseExited(e -> {
            fmQTip.remove();
        });

        graphP.setOnMouseMoved(e -> {
            fmQTip.update(e, path);
        });

        path.setFill(Color.TRANSPARENT);
        path.setStroke(Color.BLUEVIOLET);
        path.setContent(graphFunction(exp, (int) xLowLim, (int) xHighLim, (int) yLowLim, (int) yHighLim, width / 2, height / 2));

        graphP.getChildren().add(path);

        graphP.setClip(clip);
        return graphP;
    }



    /**
     * Creates an svgPath string from a "y =" function.
     *  Expects that y = is not included in the expression.
     *  Executes y = x as per expression. X begins at xStart and
     *  ends at xEnd. Calculations beyond yLowLim or yHighLim are
     *  excluded. YlowLim and yHighLim are the high and low bounderies.
     *  Low being the top or 0  and high being the bottom or 300 by default.
     *  Bounderies are the height and width in pixels divided by 10 or the height
     *  in pixels between each tickmark on the x and y axis.
     *
     *  convert to a parallel method using memoization and Futures. See Parallel programming 2.1 and 2.2 in particular
     *  Blase Pascal's traingle. Memoization is particularly used in Functional Programming where F(X) is a function. The
     *  problem being that in a sequential program, sequential operation are not an issue, however in parallel programming,
     *  where a mathmatical problem is broken into batches the problem is not so simple. Using FUTURES, the problem then
     *  is stored, assuming there is a data structure available, in the data structure.
     *  The problem is not executed until it is called on by FUTURES.GET. Then the tasks are executed recursively in parallel.
     * @param expression
     * @param xStart
     * @param xEnd
     * @param yLowLim upper pane boundry, 0 by default
     * @param yHighLim Lower pane boundry, 300 by default
     * @return
     */
    private String graphFunction(String expression, int xStart, int xEnd, int yLowLim, int yHighLim, int centerX, int centerY) {

        System.out.println(" *!*!* IN graphFunction() !*!*!");

        int length = xEnd - xStart;
        length *= 5;
        System.out.println( " length: " + length);

        double y;
        double x = xStart;

        // the converted coordinates
        double xCoord;
        double yCoord;

        // Builds the string used to build the SVG.
        StringBuilder sb = new StringBuilder();

        sb.append("M");

        for(int i = xStart; i < length; i++) {

            int index = 0;
            x += .2;
            // Evaluate the expression and multiply
            // the result by -1. (display is opposite)
            y = evaluate(expression, x) * -1;

            yCoord = (y * 20) + centerY;
            xCoord = (x * 20) + centerX;

            // If the line goes out of bounds. Stop graphing it
            // and start over with a new line when it re-enters
            if (yCoord < yHighLim && yCoord > yLowLim) {

                sb.append( xCoord + " " + yCoord);

                if (i < length - 1) {
                    sb.append(", L");
                }
            } else if(sb.toString().charAt(sb.length() - 1) == 'L') {
                index = sb.lastIndexOf("L");

                sb.replace(index , index + 1,"M");
            }
        }
        // For the hanging 'M' added but without a point.
        // Prevents errors
        if(sb.toString().charAt(sb.length() - 1) == 'M') {
            int index = sb.lastIndexOf("M");
            sb.replace(index, index + 1, "");
        }

        //System.out.println("the svgString: " + sb);
        return sb.toString();
    }

    /**
     * Helper to graphFunction, evaluates an expression and replaces
     * "x" in the expression with the parameter x
     * for this iteration.
     * @param expression
     * @param x
     * @return
     */
    private double evaluate(String expression, double x) {

        //expression = expression.trim();
        ArrayList subExpList;

        try {
            subExpList = parse(expression, x);
            parser.parseIntoRPN(subExpList);
        } catch (Exception e) {
            // @ todo Handle operator bad input exception in GraphCard line 401
        }

        if(parser.isInvalidInput()) {

            System.out.println("TestButton checking for inValidInput set to true: " + parser.isInvalidInput());
            // Insert a temporary error message into the
            // text area
            StringBuilder sb = new StringBuilder();
            sb.append(expression);
            sb.append("\n\n " + parser.getErrorMessage());
            upperEditor.setText(sb.toString());
            upperEditor.setStyleError();
            // After a delay, return the textArea back
            // to the original expression.
            EventHandler<ActionEvent> eventHandler = e -> {
                upperEditor.setText(expression);
                upperEditor.setStyleNormal();
            };

            Timeline animation = new Timeline(new KeyFrame(Duration.millis(5000), eventHandler));
            animation.play();

            return 0;

        } else {

            return parser.execute(DijkstraParser.getOutQueue());
        }
    }

    /**
     * A Graph Specific method. Replaces DijkstraParser.parseIntoRPN()
     * This method parses the expression into sub-expressions and replaces the value for x
     * with the value in arguement "x".
     * @param express The string expression
     * @param x the value for "x"
     * @return returns an ArrayList of elements
     */
    private ArrayList parse(String express, double x) throws Exception {

        // Remove "y =" from the expression
        String expMinus = express.substring(3);
        expMinus = expMinus.trim(); // clean whitespace from start
        String element;
        String[] elements = expMinus.split(" ");
        ArrayList subExpList = new ArrayList(10);
        String strX = "";
        String strY = "";

        // Evaluate each sub-element and determine if it's an "x", an
        // operator, or a double.
        for(int i = 0; i < elements.length; i++) {
            element = elements[i];
            // Expression length,
            // used to determine if number or op
            int length = element.length();
            char c;

            // classify the element as a number or operator
            // and add it to the subExpList.

            // Check 2nd char in case it's a negative number
            if (length > 1) {
                c = element.charAt(length - 1);
            } else {
                c = element.charAt(0);
            }

            // Classify, seperate, and add to subExpList
            if (Character.isDigit(c)) {
                double num = Double.parseDouble(element);
                subExpList.add(num);

            } else if (c == 'x') {

                if (element.charAt(0) == '-') {
                    subExpList.add(-x);

                } else {

                    subExpList.add(x);
                }

            } else { // it's an operator

                OperatorInterface operator = DijkstraParser.getOperator(element);

                if (i < 0) {
                    System.err.println("ERROR: There are not enough numbers for the " + operator.getSymbol() +
                            " operation. \nCheck the format of the expression.");
                    if (operator.getPriority() != 0) {
                        throw new EmptyStackException();
                    }
                }

                if(operator.isUnaryOp()) { // sqrt, abs,

                    // for printing values prior to execution
                    strX = elements[i + 1];
                }
                else if(i > 1 && i < elements.length - 1) {
                    // for printing values prior to execution
                    strX = elements[i - 1];
                    strY = elements[i + 1];
                } else {
                    if (operator.getPriority() != 0) {
//                        throw new EmptyStackException();
                    }
                }

                // for display when answered incorrectly
                String strOrigSubExp = parser.getStrExpr(strX, strY, operator);
                ExpNode exp = new ExpNode(operator, strOrigSubExp, i);

                // add the expression to the sub-expession list
                subExpList.add(exp);
            }
        }
        return subExpList;
    }



    /*******************************************************************************************************************
     *                                                                                                                 *
     *                                                      TABLE                                                      *
     *                                                                                                                 *
     ******************************************************************************************************************/

/*
    private Stage tableWindow;
    private Scene tableScene;
    public void createTable() {

        Stage tableWindow = new Stage();
        Scene tableScene;// = new Scene();


        int tableMinX = (int) FlashMonkeyMain.getWindow().getX();
        int tableMinY = (int) FlashMonkeyMain.getWindow().getY();

        // Create new window for table

        // create lines for table with labels

        // Calculate X values at division of ... 1 unit == 20? pixels

        // Calculate y values at interval of ... 1 unit == 20? pixels


        // show the window
        tableScene = new Scene(SomePanes go here);
        tableWindow.setScene(tableScene);
        tableWindow.setX(tableMinX);
        tableWindow.setY(tableMinY);
        tableWindow.setTitle("Draw tools");
        tableWindow.show();

    }

    public static void onClose() {

    }
*/

    /*******************************************************************************************************************
     *                                                                                                                 *
     *                                                  BUTTON ACTIONS                                                 *
     *                                                                                                                 *
     ******************************************************************************************************************/


    /**
     * Create the table of x and y =
     * @param expression
     */
    //@Override
    public void calcButtonAction(String expression, SectionEditor p) {

        reset();
        // split multiple expressions
        String[] exps = splitExps(expression);
        // display expressions on the graph
        for(int i = 0; i < exps.length; i++) {
            graphPane = graphExpression( exps[i], graphXLow, graphXHigh, graphYLow, graphYHigh, graphPane, 300, 200);
        }

    }

    /**
     * Creates a large graph 3/4ths the screen size
     */
    private void graphClickAction() {

        LargeGraphPane lgPane = new LargeGraphPane();
        Stage graphStage = new Stage();
        // determin screen size and build graph to it's size minus the width of the main app.
        // make the pane panable and zoomable.

        System.out.printf("*** graphClickAction called ***");

        lgPane.start(graphStage);

    }


    /**------------------------------------------------------------**
     *                        SUPPORT CLASSES
     **------------------------------------------------------------**/


    /**
     * Associates the Expression string with the line for display in pop-up
     */
    /*
    class LineObject {

        private SVGPath path;
        private String exp;
        private Tooltip tip;// = new Tooltip(lo.getExpression());

        LineObject( String expression) {
            this.path = new SVGPath();
            this.exp = expression;
            this.tip = new Tooltip(this.exp);

            //tooltipStartTiming(tip, 50);
            //tooltipHideTiming(tip, 6000);
            tip.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: RGBA(255, 255, 255, .7);");

            path.setOnMouseEntered(e -> {

                Tooltip.install(this.path, this.tip);
                System.out.println("TOOLTIP: " + exp + " should show");
            });


        lo.getSVGPath().setOnMouseExited(e -> {
            tooltipHideTiming(tip, 4000);
            tooltipStartTiming(tip, 1000);
        });

        }

        SVGPath getSVGPath() {
            return this.path;
        }

        String getExpression() {
            return this.exp;
        }
    }
    */

    /** ------------------------------------------------- **/

    /**
     * INNER CLASS
     * Creates a large garph area 3/4 the size of the screen
     */
    public class LargeGraphPane extends Application {

        @Override
        public void start(Stage graphStage) {

            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            double height = screensize.getHeight();
            height -= 40;
            double ratio = height / 300;
            int centerX = (int) height / 2;
            int centerY = centerX;
            Graph largeGraph = new Graph(centerX, centerY);

            System.out.println("\n\n *** ratio: " + ratio + " ***" +
                    "\n height " + height);

            // set deminsions of graphStage
            graphStage.setWidth(height);
            graphStage.setHeight(height);

            // set pane
            Pane pane = new Pane();

            //pane = createXYAxis(Color.web(UIColors.FM_WHITE), height, height);
            pane.getChildren().add( largeGraph.createXYAxis(Color.BLACK, height, height));
            pane.getChildren().add( largeGraph.createGrid(Color.web(UIColors.GRID_GREY), height, height ));
            //countX -= 1;
            //countY -= 1;
            largeGraph.axisNums( pane, height, height );

            // split multiple expressions
            String[] exps = splitExps(expression);
            // display expressions on the graph

            for(String e : exps) {
                pane = graphExpression(
                        e,
                        graphXLow * ratio,
                        graphXHigh * ratio,
                        graphYLow * ratio,
                        graphYHigh* ratio,
                        pane,
                        (int) height,
                        (int) height);
            }


            Scene graphScene = new Scene(pane, height, height, Color.web(UIColors.FM_WHITE));
            graphScene.getStylesheets().add("css/mainStyle.css");
            graphStage.setScene(graphScene);
            graphStage.show();
        }
    }


    /**------------------------------------------------------------**
     *                        ToolTip HACK
     **------------------------------------------------------------**/


    /**
     * Uses Reflection to set the startTiming in ToolTip
     * @param tooltip
     * @param millis
     */
    /*
    public static void tooltipStartTiming(Tooltip tooltip, int millis) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(millis)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses Reflection to set the hidTiming in ToolTip
     * @param tooltip
     * @param millis
     */
    /*
    public static void tooltipHideTiming(Tooltip tooltip, int millis) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("hideTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(millis)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

}
