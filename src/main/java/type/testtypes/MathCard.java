package type.testtypes;

import flashmonkey.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import type.tools.calculator.*;
//import type.tools.calculator.Expression;
import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;
import type.sectiontype.GenericSection;
import uicontrols.FxNotify;
import uicontrols.SceneCntl;
import uicontrols.UIColors;

import java.util.ArrayList;
import java.util.BitSet;


/**
 * MathCard is a unique test type. It is interactive in nature.
 *
 * - INTENT:
 *  - Allows EncryptedUser.EncryptedUser to input formula
 *  - Ensures compliance with PERMDAS, division, multiplication, signs
 *      etc.. during card use.
 *      Provides advice upon an incorrect answer and uses
 *      logic by calculating incorrect answers when attempting
 *      to understand the users mistakes. Then provides the EncryptedUser.EncryptedUser
 *      advice on thier error. And or what to check.
 *      -Pops up advice about the possible errors. IE
 *      PERMDAS error. and provides correction. As a possiblity
 *      may provide additional problems to assist with correcting
 *      the error.
 *      - Card may input random number values into fields and test the EncryptedUser.EncryptedUser
 *      on the card.
 *  - Ensures compliance with PERMDAS, Signs, Multiplication, Division, Addition,
 *      subtraction, etc...
 *
 *  - Most likely use is that the tool/cards are developed by students, TA's or the
 *  professor for the class. Cards are likely to be an asset in s$
 *
 *  - FITB provides blanks for response.
 *
 *  * Note that the Flow for the cards are that... The problem apears to the EncryptedUser.EncryptedUser
 *  with the formula, Random generated numbers for the problem, and ... the
 *  EncryptedUser.EncryptedUser fills in the answer blanks in the problem TextAreas. The response field
 *  appears after the EncryptedUser.EncryptedUser has provided an answer.
 *
 *  - To add additional operators add them in the appropriate class or the Operator class.
 *  Add them as an enum. Include it's type in the getOperator() switch in DijkstraParser. Provide
 *  it's priority and ensure that it is correct.
 *
 *  @author Lowell Stadelman
 */
public class MathCard implements GenericTestType<MathCard> {

    //private static MathCard CLASS_INSTANCE;

    protected DijkstraParser parser;// = new DijkstraParser();

    /** Panes **/
    // Contains the entire card
    protected GridPane gPane;// vBox;
    // Contains the upper Section
    private HBox upperHBox;
    // contains the lower Section
    private VBox lowerVBox;
    // Contains the interactive indicator over the question
    private Pane pane;


    // Contains the answer field
    private TextField ansField;

    // The GenericSection object
    private GenericSection genSection;

    // Answer Button
    private Button answerButton;

    // The Mathmatical expression from/for the question
    protected String expression;

    // Problem as executed by DijkstraParser
    private static String[] ansComponents;

    // Buttons
    Button calcButton = new Button("Calc");
    Button addGraph = new Button("graph");

    public MathCard() { /* empty constructor */}

    /*public static MathCard getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new MathCard();
        }
        return CLASS_INSTANCE;
    }*/
    
    @Override
    public boolean isDisabled() {
        return false;
    }


    /**
     * Builds the Test Editor Pane for this card. This is primarily used for the visual
     * layout, buttons, etc. Not generally used for computational/business work.
     * @param flashList
     * @param p The formula for the math/Algebra/Trig/Calc problem
     * @param r The response when the EncryptedUser.EncryptedUser gets the answer wrong or correct.
     * @return Returns the Vbox with problem card and response card.
     */
    @Override
    public VBox getTEditorPane(ArrayList<FlashCardMM> flashList, SectionEditor p, SectionEditor r)
    {
        // Instantiate vBox and "set spacing" !important!!!

        VBox vBox = new VBox(2);
        StackPane sPane = new StackPane();
        p.sectionHBox.setPrefHeight(SceneCntl.getHt());
        // Set prompt in Question/upperBox
        p.setPrompt("Enter Math Formula, student entries go into textFields");
        r.setPrompt("Enter the response if answered incorrectly");
        sPane.getChildren().add(p.sectionHBox);
        vBox.getChildren().addAll(sPane, r.sectionHBox);
        //boolean bool = p.verifyIsValid();

        vBox.getChildren().add(calcButton);

        calcButton.setOnAction(e -> {
            expression = p.getText();
            calcButtonAction(expression, p, r);
        });

        return vBox;
    }

    /** ------------------------------------------------- **/

    private String isNumber(String inStr) {

        try
        {
            Double.parseDouble(inStr);
        }
        catch(NumberFormatException e)
        {
            return "Please enter a valid number";
        }

        return inStr;
    }


    /**------------------------------------------------------------**
     *                          GETTERS
     **------------------------------------------------------------**/

    /**
     * Sets 11th bit (2048), all other bits are 0
     * @return bitSet. Used when parsing
     * cards from flashList
     */
    @Override
    public int getTestType() {
        // 2048
        return 0b0000000000010000;
        //return 0b0000100000000000;
    }

    @Override
    public String getName() {
        return "Math Card";
    }

    /** ------------------------------------------------- **/

    /**
     * This card is a double horizontal layout
     * // Maybe it should be a single ??? And cards flow from
     * Quiz to response. ???
     * @return
     */
    @Override
    public char getCardLayout()
    {
        return 'D'; // double horizontal
    }

    /** ------------------------------------------------- **/

    @Override
    public GenericTestType getTest() {

        return new MathCard();
    }

    @Override
    public Button[] getAnsButtons() {
        return new Button[] {answerButton};
    }

    @Override
    public Button getAnsButton() {
        return answerButton;
    }


    /**------------------------------------------------------------**
     *                        BUTTON ACTIONS
     **------------------------------------------------------------**/


    @Override
    public void ansButtonAction() {

        // The users response
        String response = ansField.getText();
        // Solves the expression
        parser = new DijkstraParser(expression);


        if( response.isEmpty() ) {

            ansField.setPromptText("Enter a valid response." );

        } else if(response.contains("/") && expression.contains("/")) { // It's a fraction

            System.out.println("Attempting a fraction response ");
            ansField.setEditable(false);
            answerButton.setDisable(true);

            ExpNode exp = Operator.getAnsComponents().getLast();
            String[] parts = response.split("/");
            String answer = exp.getExpSolved();
            String[] correctParts = answer.split(" ");

            // Testing
            System.out.println("correctParts");
            for(int i = 0; i < correctParts.length; i++) {
                System.out.println(i + ") " + correctParts[i]);
            }
            // Testing
            System.out.println("\nparts");
            for(int i = 0; i < parts.length; i++) {
                System.out.println(i + ") " + parts[i]);
            }

            if (Double.parseDouble(parts[0]) == Double.parseDouble(correctParts[1])
                    && Double.parseDouble(parts[1]) == Double.parseDouble(correctParts[3])) {

                ansField.setId("right_border");

            } else {
                responseWrong();
            }

        } else {

            ansField.setEditable(false);
            answerButton.setDisable(true);

            Double correctAnsDbl = 0.0;
            // Get the correct answer from the expression
            correctAnsDbl = parser.getResult();

            System.out.println("\tcorrectNum == " + correctAnsDbl);
            System.out.println("\tresponse == " + response);
            // for invalid inputs use try catch
            try {
                double responseDbl = Double.parseDouble(response);
                if (responseDbl == correctAnsDbl) {
                    ansField.setId("right_border");
                    //ansField.setStyle( "-fx-border-color: " + UIColors.HIGHLIGHT_GREEN + "; -fx-border-width: 5;");

                } else { // Answer is wrong
                    responseWrong();
                }
            } catch (NumberFormatException e) {
                responseWrong();
            }
        }
    }

    /** ------------------------------------------------- **/


    // Calc button, EncryptedUser may test input for answer, Tests input for incorrect
    // answer and outputs error message.
    private void calcButtonAction(String expression, SectionEditor uEditor, SectionEditor lEditor) {
        // Parses the expresssion
        parser = new DijkstraParser(expression);

        if (DijkstraParser.isInvalidInput()) {
            // Insert a temporary error message into the
            // text area

            StringBuilder sb = new StringBuilder();
            sb.append(expression);
            sb.append("\n\n " + parser.getErrorMessage());
//            FxNotify.notificationError("", " Hmmmm! " + sb, Pos.CENTER, 15,
//                    "emojis/Flash_headexplosion_60.png", FlashMonkeyMain.getPrimaryWindow());

        } else {
            StringBuilder sb = new StringBuilder(lEditor.getText());
            sb.append("\n\nRESPONSE = ");
            sb.append(parser.getResult());
            sb.append("\n");
            lEditor.setText(sb.toString());
        }
    }

    @Override
    public void nextAnsButtAction()
    {
        // stub
    }

    @Override
    public void prevAnsButtAction()
    {
        // stub
    }

    @Override
    public void reset() {
        if(parser != null) {
            parser.clearStructures();
        }
    }

    /**
     * Processing on the SectionEditors. E.g. on the text area such as what is used on the FillnTheBlank card.
     *
     * @param flashList
     * @param q         Upper SectionEditor
     * @param a         Lower SectionEditor
     */
    @Override
    public void doOnSave(ArrayList<FlashCardMM> flashList, SectionEditor q, SectionEditor a) {

    }

    /** ------------------------------------------------- **/

    /**
     * Actions when the EncryptedUser.EncryptedUser provides the incorrect answer. Clears the
     * upperBox and replaces with the interActiveQuestion. Each segment of
     * the expression is then displayed -contained in TextFields. In the lowerBox,
     * An ordered list of the operations as they were executed is displayed.
     * The executed operations (in the lowerBox) are contained in TextAreas and are clickable.
     * Each ordered (executed) operation contains a reference it's operator in the interActiveQuestion.
     * The operator in the interActiveQuestion is highlighted with a circle that is displayed
     * in a StackPane above the interActiveQuestion. Thus relating to the EncryptedUser.EncryptedUser which operation
     * produced which results in a PERMDAS order of operations.
     */
    private void responseWrong() {

        upperHBox.getChildren().clear();
        upperHBox.getChildren().add(interActiveQuestion(parser.getWriterList()));
        ansField.setId("wrong_border");
        //ansField.setStyle("-fx-border-color: " + UIColors.FM_RED_WRONG_OPAQUE + "; -fx-border-width: 3;");
        lowerVBox.getChildren().add(new TextField("Solve L -> R using PERMDAS"));
        lowerVBox.getChildren().add(getResponseBox(parser.getWriterList()));
    }


    /** ------------------------------------------------- **/

    /**
     * The main card pane during EncryptedUser.EncryptedUser "test" operations.
     * @param cc
     * @param genCard
     * @return
     */
    @Override
    public Pane getTReadPane(FlashCardMM cc, GenericCard genCard, Pane parentPane)
    {
        System.out.println("\n*** in getTReadPane for MathCard ***");

        // Clear the Operator answer components arrayList
        if(Operator.getAnsComponents() != null) {
            Operator.getAnsComponents().clear();
        }

        genSection = GenericSection.getInstance();
        gPane = new GridPane();
        gPane.setVgap(2);

        //gPane.setPrefSize(parentPane.getWidth(), parentPane.getHeight() / 2);

        // The Interactive Question pane.
        // Contains circle pointer.
        pane = new Pane();
        pane.setMinHeight(SceneCntl.getCellHt());
        pane.setMaxHeight(SceneCntl.getCellHt());

        answerButton = new Button("answer");
        answerButton.setOnAction(e -> ansButtonAction());

        // The String expression displayed to the EncryptedUser.EncryptedUser
        // Stored in the currentCard.
        expression = cc.getQText();

        double lowerHt = parentPane.getHeight() - 60;

        upperHBox = genSection.sectionFactory(expression, cc.getQType(), 2, false, lowerHt, cc.getQFiles());

        pane.getChildren().add(upperHBox);

        /** ----------------------- **/

        //ReadFlash.setMode('T');  //  IS THIS NEEDED??
        //ReadFlash.masterBPane.setBottom(ReadFlash.manageMode('T', false));
        ReadFlash.getInstance().setShowAnsNavBtns(false);

        /**
         *
         *  1) Create area for answer... Determine if it's images, video's etc for the question
         *      - Completed - Create section for question... IE TextArea.
         *      - Completed - Finish area for users response
         *      - Completed - Create visual cartisian coordinate graph for visualizing graphed equations/solutions.
         *      - In the answer area... Develop responses based on the users answer.. IE by attempting to show
         *      their errors. Or... Perhaps this is a third section that transitions from the bottom?
         **/

        // add the answer to the a StackPane, add the answer button, and add the stackPane to the vBox.
        lowerVBox = getAnswerBox();
        gPane.addRow(3, lowerVBox );
        gPane.addRow(2, pane ); // upperHBox
        //gPane.addRow(1, graphPane); // Graph


        // Transition for Question, Right & end button click
        FMTransition.setQRight( FMTransition.transitionFmRight(pane));
        // Transition for Question, left & start button click
        FMTransition.setQLeft( FMTransition.transitionFmLeft(pane));
        // Transition for Answer
        FMTransition.setAWaitTop( FMTransition.waitTransFmTop(lowerVBox, 30, 300, 300));
        FMTransition.getQRight().play();
        FMTransition.getAWaitTop().play();

        return gPane;
    } // END Test READ PANE

    /** ------------------------------------------------- **/

    /**
     * The users response/answer box
     * @return
     */
    private VBox getAnswerBox() {

        HBox box = new HBox(2);
        ansField = new TextField();
        ansField.setEditable(true);
        ansField.setPrefColumnCount(10);
        box.getChildren().addAll(ansField, answerButton);
        VBox vB = new VBox(2);
        vB.getChildren().add(box);

        return vB;
    }

    /** ------------------------------------------------- **/

    /**
     * Used by lambda in getResponseBox
     */
    HBox expHBox = new HBox();
    int prevIdx = 0;
    Circle circle = new Circle(9);
    //Rectangle rect = new Rectangle(18 , 22);
    ArrayList<TextField> tfs;
    /**
     * Provides the step by step evaluation of the math
     * problem
     * @return returns an HBox containing the evaluations
     * in ordered form.
     */
    private ScrollPane getResponseBox(ArrayList expList)
    {
        VBox vBox = new VBox(2);

        ScrollPane scrollPane = new ScrollPane();

        while( ! Operator.getAnsComponents().isEmpty())
        {
            ExpNode exp = Operator.getAnsComponents().poll();

            TextField tf = new TextField(exp.getExpSolved());
            //double ht = tf.getHeight();
            //double wd = tf.getWidth();
            tf.setOnMouseEntered(e -> { //  setOnMouseClicked(e -> {

                tf.setStyle("-fx-border-color:" + UIColors.HIGHLIGHT_PINK);
                //tf.setMaxHeight(ht - 8);
                //tf.setMaxWidth(wd - 8);
                setIActiveStyle( prevIdx, exp.getIndex());
                prevIdx = exp.getIndex();

            });
            tf.setOnMouseExited(e -> {
                mouseExitedExpBox();
                tf.setStyle("-fx-border-color: DEFAULT");
            });
            vBox.getChildren().add(tf);
        }

        scrollPane.setContent(vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /** ------------------------------------------------- **/


    /**
     * Sets the Interactive question when the users response is answered incorrectly. It
     * adds a circle/rectangle over the operator when the EncryptedUser.EncryptedUser selects an answer part in
     * the response box.
     * @param prevIdx
     * @param expIdx
     */
    private void setIActiveStyle(int prevIdx, int expIdx) {

        pane.getChildren().remove(circle);
        //pane.getChildren().remove(rect);

        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.web(UIColors.HIGHLIGHT_PINK));
        circle.setStrokeWidth(1.6);
        //rect.setFill(Color.TRANSPARENT);

        TextField tef = tfs.get(expIdx);
        // circle centerX and centerY
        double x = tef.getLayoutX() + (tef.getWidth() / 2);
        double y = tef.getLayoutY() + (tef.getHeight() / 2);
        circle.setCenterX(x);
        circle.setCenterY(y);

        System.out.println("Circle center is x & y " + circle.getCenterX() + " " + circle.getCenterY() );

        pane.getChildren().add(circle);
        // make the interactive question with transparent boxes.
        tef.setStyle("-fx-stroke-width: 0; -fx-stroke: TRANSPARENT; -fx-background-color: TRANSPARENT;");
    }

    /** ------------------------------------------------- **/

    private void mouseExitedExpBox() {

        System.out.println("*** Called mouseExitedExpBox() ***");

        pane.getChildren().remove(circle);
    }

    /** ------------------------------------------------- **/

    private HBox interActiveQuestion(ArrayList expList) {

        expHBox = new HBox();
        tfs = new ArrayList<>(10);
        int length;
        for(int i = 0; i < expList.size(); i++) {

            length = expList.get(i).toString().length();

            //String str = new DecimalFormat(".###").format(expList.get(i).toString());
            //str.
            TextField tf = new TextField(expList.get(i).toString());
            //tf.setPrefColumnCount(length);
            //double width = tf.getPrefColumnCount();
            tf.setMaxWidth(length * 9);
            tf.setMinWidth(length * 9);

            tf.setStyle("-fx-stroke-width: 0; -fx-stroke: TRANSPARENT; -fx-background-color: TRANSPARENT;");
            //tf.setMaxWidth(20);
            tf.setPadding(Insets.EMPTY);

            tf.alignmentProperty().setValue(Pos.CENTER);
            //tf.setId("roundCorners");
            tfs.add(tf);
            expHBox.getChildren().add(tf);
        }

        return expHBox;
    }
}
