/*
 * Copyright (c) 2019 - 2021. FlashMonkey Inc. (https://www.flashmonkey.xyz) All rights reserved.
 *
 * License: This is for internal use only by those who are current employees of FlashMonkey Inc, or have an official
 *  authorized relationship with FlashMonkey Inc..
 *
 * DISCLAIMER OF WARRANTY.
 *
 * COVERED CODE IS PROVIDED UNDER THIS LICENSE ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY
 *  KIND, EITHER EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES THAT THE COVERED
 *  CODE IS FREE OF DEFECTS, MERCHANTABLE, FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE
 *  ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY
 *  COVERED CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY OTHER
 *  CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR CORRECTION. THIS
 *  DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.  NO USE OF ANY COVERED
 *  CODE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 *
 */

package flashmonkey;



import ch.qos.logback.classic.Level;
import fmtree.FMWalker;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import org.slf4j.LoggerFactory;
import type.cardtypes.GenericCard;
import type.testtypes.*;
import type.tools.imagery.Fit;
import uicontrols.ButtoniKon;
import uicontrols.SceneCntl;

import static java.lang.Math.floor;

/*******************************************************************************
 * Provides the EncryptedUser.EncryptedUser interface to reading flash cards
 * <strong>Singleton Class. Synchronized.</strong>
 * 
 * NOTES: This class starts off asking the EncryptedUser.EncryptedUser what study mode to use. The class
 * uses the int mode variable to determine which scene to represent.
 *
 * Modification to Multi-Media & Multi - Test Sept 28th 2018
 *
 * Previously buttonActions were lambda's within the readflash constructor
 *  - For the original test mode, muliple choice, much of the code existed
 *  within the lamda block. With multiple test and multiple media types and
 *  moving forward to interactive capabilities, Tests will become thier own
 *  classes, Multiple Choice did have a class but much of the code will
 *  exist in it's own class with a simple call from the constructor. Use of
 *  static variables and static objects is minimized appropriatly to reduce
 *  the amount of memory needed to run the program. In the MVC model, A test
 *  will be control, ie button actions. View, or appearance of buttons and
 *  thier actions according to settings are TBD
 *
 *  Creating individual classes for each test is theoretically more efficient,
 *  providing access to the type classes, or layers/levels of panes where they
 *  are needed, rather than simply through a single factory that parses a call
 *  into the desired format. The test will simply access the card, section, cell
 *  at the level that makes the best fit. In effect making the cardFactory
 *  unneeded with exception to a few classes. This means programming each test
 *  will take greater effort, however provides greater control of the desired
 *  actions in the compiler.
 *
 * @author Lowell Stadelman
 ******************************************************************************/


public final class ReadFlash {

    private volatile static ReadFlash CLASS_INSTANCE;
    private static final ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ReadFlash.class);
    

    // *** CONSTANTS ***
    protected static final GenericCard GEN_CARD = new GenericCard();


    // **** PANES **** //
    // The main scene object & pane
    private Scene masterScene;
    private static BorderPane masterBPane;// = new BorderPane();
    // Titles and messages in rpNorth
    private static HBox rpNorth;
    // Q section and A section. May also contain the graph
    protected static VBox rpCenter;// = new VBox(2);  //
    // Contains gauges, exit button, Navigation buttons etc...
    //public static BorderPane rpSouth;
    // The menu for Q&A or Test
    private static GridPane modeSelectPane = new GridPane();
    // Button box contained in southpane
    GridPane exitBox;

    // **** VARIABLES **** //
    /** The name of the active deck **/
    private static String deckName;

    // **** INDEXES **** //
    /** the visible number displayed to the EncryptedUser.EncryptedUser for the card */
    //protected static int visIndex = 1;
    /** The last score total **/
    private static double score = 0.0;

    // *** BUTTONS *** //
    private static Button exitButton;
    /** The beginning of the tree **/
    protected static Button firstQButton;
    private static Button endQButton;
    private static Button nextQButton;
    private static Button prevQButton;
    //private static Button createButton;

    /** Back button .. returns to the QorA/Test menu **/
    protected static Button menuButton;// = MENU.get();
    

    /** question and answer mode buttons*/
    private Button qaButton;
    private Button testButton;
    private Button deckSelectButton;
    // private static boolean hint = false;
    /** MENU BUTTONS **/
    /** resets flashList and sends EncryptedUser.EncryptedUser to the firstScene **/
    // protected static Button fileSelectButton = new Button(" < | Study deck selection");

    // *** LABELS ***
    private static Label deckNameLabel;// = new Label(FC_OBJ.FO.getFileName());
    protected static Label message; // = new Label();
    private static Label scoreLabel; //?????

    // *** TEXT FIELDS ***
    private static TextField scoreTextF = new TextField();


    /**
     * Private Constructor
     */
    private ReadFlash() {
        deckName = "default";
    }

    /**
     * Returns an instance of the class. If getInstance hasn't been called before
     * a new instance of the class is created. otherwise it returns the existing
     * instance.
     * To intialize: ReadFlash readFlashObj = ReadFlash.getInstance();
     * @return ReadFlash
     */
    public static synchronized ReadFlash getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new ReadFlash();
        }
        return CLASS_INSTANCE;
    }
     
    /**
     * The createRead Scene class creates the flashcard scene.
     *  UI FLow: The user enters the scene from the MenuPane/modeSelectPane after
     * clicking on the study button.
     * 1) The first scene the EncryptedUser.EncryptedUser sees asks
     * them what study mode to use.
     *  a) There is Q and A session which is a
     * normal Flash card mode. The pane presents the 1st question and the
     * EncryptedUser.EncryptedUser clicks for the answer or the next question. The users can
     * traverse forward or backwards through the questions. There is no
     * scoring associated with QAndA mode.
     *  b) The Test session is the test mode. In test mode the
     * EncryptedUser.EncryptedUser is presented a question and one of the test cards.
     */
    public Scene readScene() {
        LOGGER.info("\n\n **** createReadScene() called ****");
        LOGGER.setLevel(Level.DEBUG);
        LOGGER.debug("Set ReadFlash LOGGER to debug");



     // NORTH & CENTER PANES read fields
        rpNorth = new HBox(2);
        // holds questionPane and answerPane
        rpCenter = new VBox(2);
        rpCenter.setId("rpCenter");
        rpCenter.setSpacing(2);
        masterBPane = new BorderPane();
        // nav buttons
        firstQButton = ButtoniKon.getQFirstButton();
        nextQButton = ButtoniKon.getQNextButton();
        prevQButton = ButtoniKon.getQPrevButton();
        endQButton = ButtoniKon.getQLastButton();
        
        // menu buttons shown in the menu pane
        deckSelectButton = ButtoniKon.getDeckSelectButton();

        preRead();
    
        exitButton = ButtoniKon.getExitButton();
        menuButton = ButtoniKon.getMenuButton();
        exitBox = new GridPane(); // HBox with spacing provided
        exitBox.setHgap(2);
        /* For the lower panel on modeSelectPane window */
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(50);
        exitBox.getColumnConstraints().add(col0);
        exitBox.setVgap(2);
        exitBox.setPadding(new Insets(15, 15, 15, 15));
        exitBox.addColumn(1, menuButton);
        exitBox.addColumn(2, exitButton);
        exitBox.setId("buttonBox");
        
        deckNameLabel = new Label(getDeckName());
        deckNameLabel.setId("label16");

        rpNorth.getChildren().add(deckNameLabel);

        // The study menu pane
        LOGGER.debug("setting window to modePane");
        masterBPane.setCenter(modeSelectPane);
        masterBPane.setBottom(exitBox);
        masterBPane.setId("bckgnd_image_study");
        masterScene = new Scene(masterBPane, SceneCntl.getWd(), SceneCntl.getHt());

        LOGGER.debug("readflash masterBPane width: " + masterBPane.widthProperty());
        
        // *** BUTTON ACTIONS ***
        menuButton.setOnAction(e -> FlashMonkeyMain.setWindowToNav());
        exitButton.setOnAction(e -> exitAction() );
        qaButton.setOnAction((ActionEvent e) -> qaButtonAction());
        testButton.setOnAction((ActionEvent e) -> testButtonAction());
        /*
        * Navigation buttons, handles actions for first, last, next, and previous
        * buttons.
         */
        nextQButton.setOnAction((ActionEvent e) -> nextQButtonAction());
        prevQButton.setOnAction((ActionEvent e) -> prevQButtonAction());
        endQButton.setOnAction(this::endQButtonAction);
        firstQButton.setOnAction(this::firstQButtonAction);
        /*
         * shortCut Key actions
         */
        masterBPane.setOnKeyPressed(this::shortcutKeyActions);
        masterScene.getStylesheets().addAll("css/mainStyle.css", "css/buttons.css");

        LOGGER.debug("end readFlash scene");
        return masterScene;

    }    // ******** END OF READ SCENE ********

    private void preRead() {
        testButton = ButtoniKon.getTestButton();
        qaButton = ButtoniKon.getQandAButton();

        FlashCardOps fcOps = FlashCardOps.getInstance();

        if(fcOps.getFlashList().size() < 4) {
            emptyListAction();
        } else {
            modeSelectPane = listHasCardsAction();
        }
    }



    /**
     * The shortCutKeys for ReadFlash actions.
     * Helper method to createReadScene()
     * @param e
     */
    private void shortcutKeyActions(KeyEvent e) {
        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();
        GenericTestType test = TestList.selectTest( currentCard.getTestType() );

        if (e.isAltDown() || e.isShiftDown()) {
            if(e.getCode() == KeyCode.ENTER)
            {
                LOGGER.debug("Enter pressed for testAnswer");
                test.ansButtonAction();
                masterBPane.requestFocus();
            }
            else if (e.getCode() == KeyCode.LEFT)
            {
                LOGGER.debug("Left pressed for leftAnswer");

                test.prevAnsButtAction();
                masterBPane.requestFocus();

            }
            else if (e.getCode() == KeyCode.RIGHT)
            {
                LOGGER.debug("Right pressed for rightAnswer");
                test.nextAnsButtAction();
                masterBPane.requestFocus();
            }
        }
        else if(e.getCode() == KeyCode.ENTER)
        {
            LOGGER.debug("Enter pressed for testAnswer w/o alt or shift");
            test.ansButtonAction();
            masterBPane.requestFocus();
        }
        else if (e.getCode() == KeyCode.LEFT)
        {
            prevQButtonAction();
            masterBPane.requestFocus();
        }
        else if (e.getCode() == KeyCode.RIGHT)
        {
            nextQButtonAction();
            masterBPane.requestFocus();
        }
    }


    /**
     * Helper Method to ReadScene. Action when list has cards.
     * Builds tree, and sets values in appropriate panes.
     * @return
     */
    private GridPane listHasCardsAction() {
        LOGGER.debug("listHasCardsAction() called");


        Label label = new Label("Study Mode");
        label.setId("label24White");
        GridPane gPane = new GridPane();
        VBox buttonBox = new VBox(2);
        VBox spacer = new VBox();
        spacer.setPrefHeight(300);
        //spacer.setPrefWidth(500);
        buttonBox.setId("studyModePane");
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.getChildren().addAll(label, qaButton, testButton);

        gPane.setId("rightPaneTransp");
        gPane.setAlignment(Pos.CENTER);
        gPane.addRow(0, spacer);
        gPane.addRow(1, buttonBox);

        return gPane;
    }


    /**
     * Sends the UI to the menu screen and displays the message
     * provided in the argument.
     * NOTE: Set the the String errorMsg in FlashMonkeyMain. and
     * provide FlashMonkeyMain.errorMsg in the argument.
     */
    public void emptyListAction() {
        Button createButton = ButtoniKon.getCreateButton();
        
        modeSelectPane.setAlignment(Pos.CENTER);

        int num = 4 - FlashCardOps.getInstance().getFlashList().size();

        String str = "Oooph! \nThere are not enough cards to work.\n\n Please create at least " + num + " more.\n\n";

        message = new Label(str);
        message.setId("message");
        message.setMaxWidth(SceneCntl.getButtonWidth());
        message.setTextAlignment(TextAlignment.CENTER);

        createButton.setOnAction(e -> FlashMonkeyMain.createButtonAction());
        createButton.setPrefWidth(SceneCntl.getButtonWidth());
        deckSelectButton.setOnAction(e -> deckSelectButtonAction());
        deckSelectButton.setPrefWidth(SceneCntl.getButtonWidth());
        
        modeSelectPane.getChildren().clear();
        //modeSelectPane.addRow(0, iView);
        modeSelectPane.addRow(1, message);
        modeSelectPane.addRow(2, createButton);
        modeSelectPane.addRow(3, deckSelectButton);
    }

    /**
     * The previous question button action
     */
    @SuppressWarnings("rawTypes")
    private void prevQButtonAction()
    {

        //Node currentNode = FMTWalker.getCurrentNode();
        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();

        rpCenter.getChildren().clear();
        // All of the work is accessed from selectTest()
        GenericTestType test = TestList.selectTest( currentCard.getTestType() );

        if(mode == 't') {
            rpCenter.getChildren().add(test.getTReadPane(currentCard, GEN_CARD, rpCenter));
        } else {
            rpCenter.getChildren().add(QandA.QandASession.getInstance().getTReadPane(currentCard, GEN_CARD, rpCenter));
        }
        masterBPane.setBottom(manageSouthPane(mode));

        if(test.getAnsButton() != null) {
            test.getAnsButton().setDisable(false);
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(test.getAnsButton(), 1, 750, false);
            FMTransition.nodeFadeIn.play();
            //test.getAnsButton().setText("");
        }

        // Set the status of the answer button. A question should only
        // be answered once
        ansQButtonSet(currentCard.getIsRight(), test);
        //setAnsButtonStatus(currentCard.getIsRight(), test);

        // The animation for this button
        FMTransition.getQLeft().play();
        if(FMTransition.getAWaitTop() != null) {
            FMTransition.getAWaitTop().play();
        }
    }


    /**
     * The next question button action
     * - Get the next card from the tree,
     *  - read the cards bitset and get the test
     *  -
     */
    private void nextQButtonAction() {
        // Get the next node in the treeWalker
        FMWalker.getInstance().getNext();

        // Node currentNode = FMTWalker.getCurrentNode();
        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();

        rpCenter.getChildren().clear();
        // All of the work is accessed from selectTest()
        GenericTestType test = TestList.selectTest( currentCard.getTestType() );
        
        if(mode == 't') {
            rpCenter.getChildren().add(test.getTReadPane(currentCard, GEN_CARD, rpCenter));
        } else {
            rpCenter.getChildren().add(QandA.QandASession.getInstance().getTReadPane(currentCard, GEN_CARD, rpCenter));
        }
        masterBPane.setBottom(manageSouthPane(mode));
    
        if(test.getAnsButton() != null) {
            test.getAnsButton().setDisable(false);
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(test.getAnsButton(), 1, 750, false);
            FMTransition.nodeFadeIn.play();
        }

        // Set the status of the answer button. A question should only
        // be answered once
        ansQButtonSet(currentCard.getIsRight(), test);

        // The animation for this button
        FMTransition.getQRight().play();
        if(FMTransition.getAWaitTop() != null) {
            FMTransition.getAWaitTop().play();
        }
    }


    //@todo change qNavButtonAction() to private in readflash.
    public void qNavButtonAction() {
        LOGGER.debug("\n *** qNavButtonAction ***");
        //buttonTreeDisplay(FMTWalker.getCurrentNode());
    }

    /**
     * Sends the treedisplay and the flashcard panes to the
     * first question in the tree.
     */
    private void firstQButtonAction(ActionEvent e)
    {
        //visIndex = 1;
        FMWalker.getInstance().setToFirst();

        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();

        rpCenter.getChildren().clear();
        GenericTestType test = TestList.selectTest( currentCard.getTestType() );
        if(mode == 't') {
            rpCenter.getChildren().add(test.getTReadPane(currentCard, GEN_CARD, rpCenter));
        } else {
            rpCenter.getChildren().add(QandA.QandASession.getInstance().getTReadPane(currentCard, GEN_CARD, rpCenter));
        }
        masterBPane.setBottom(manageSouthPane(mode));

        // answer button transitions
        if(test.getAnsButton() != null) {
            test.getAnsButton().setDisable(false);
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(test.getAnsButton(), 1, 750, false);
            FMTransition.nodeFadeIn.play();
            //test.getAnsButton().setText("");
        }

        // Set the status of the answer button. A question should only
        // be answered once
        // setAnsButtonStatus(currentCard.getIsRight(), test);
        ansQButtonSet(currentCard.getIsRight(), test);

        // Animations
        FMTransition.getQLeft().play();
        if(FMTransition.getAWaitTop() != null) {
            FMTransition.getAWaitTop().play();
        }
    }

    /**
     * sends the flashCard pane to the last flashCard that is displayed in the
     * tree. The firthest right card.
     */
    private void endQButtonAction(ActionEvent e) {

        //Node currentNode = TR.getCurrentNode();
        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();

        rpCenter.getChildren().clear();
        GenericTestType test = TestList.selectTest( currentCard.getTestType() );
        if(mode == 't') {
            rpCenter.getChildren().add(test.getTReadPane(currentCard, GEN_CARD, rpCenter));
        } else {
            rpCenter.getChildren().add(QandA.QandASession.getInstance().getTReadPane(currentCard, GEN_CARD, rpCenter));
        }
        masterBPane.setBottom(manageSouthPane(mode));

        if(test.getAnsButton() != null) {
            test.getAnsButton().setDisable(false);
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(test.getAnsButton(), 1, 750, false);
            FMTransition.nodeFadeIn.play();
        }

        // Set the status of the answer button. A question should only
        // be answered once
        //setAnsButtonStatus(currentCard.getIsRight(), test);
        ansQButtonSet(currentCard.getIsRight(), test);

        // Animations
        FMTransition.getQRight().play();
        if(FMTransition.getAWaitTop() != null) {
            FMTransition.getAWaitTop().play();
        }

    }

    /**
     * Sets the answerButton status to either enabled,
     * or disabled based on a cards isRight status. 0
     * is unanswered, anything else it has been answered.
     * @param isRight The status of this question in this session
     * @param test The TestType for this question.
     */
    private void setAnsButtonStatus(int isRight, GenericTestType test) {
        if(isRight == 0) {
            if (test.getAnsButton() != null) {
                test.getAnsButton().setDisable(false);
                FMTransition.nodeFadeIn = FMTransition.ansFadePlay(test.getAnsButton(), 1, 750, false);
                FMTransition.nodeFadeIn.play();
            }
        } else {
            test.getAnsButton().setDisable(true);
        }
    }


    /**
     * <p><b>Returning to the main pane resets and saves data from the current
     * session.</b></p>
     * File Select Button Action: Sends the user back to the first scene where
     * they can select another flashDeck/file. The flashList is saved, then
     * reset to zero elements. MetaData is saved and sent to the DB. The AGR File list
     * is recreated from new showing any new files.
     */
    protected void deckSelectButtonAction()
    {
        LOGGER.debug("call to fileSelectButtonAction()");
        FlashCardOps fcOps = FlashCardOps.getInstance();
        // clear the flashlist
        fcOps.clearFlashList();

    }

    /**
     * TEST BUTTON ACTION:  provides the actions for the test button scene
     * Void method. testButton exists in the second menu after the study button
     * has been pressed by the EncryptedUser.EncryptedUser.
     *
     * <p><b>Note: </b> The xpected Result... multi choice example:
     * the centerPane is set to two sections, the top section is for the
     * question, and the bottom section is for the answers. The first answer
     * drops down from the top and the successive answers come in from the right
     * or left depending on which answer button is clicked. The select button
     * selects the current answer that is in view and will compare it with the
     * correct answer for that question. It scores the answer and adds or 
     * subtracts depending on if it is right or wrong.</p>
     *
     * <p>- Uses genericTest.getTReadpane to call the appropriate test and card.
     * After the first card is set, each test is responisible for displaying
     * the correct test card for "that card" being the "currentCard". The
     * qNavigation buttons are responsible for displaying the next card in the
     * treeWalker navigation tree. :)</p>
     */
    protected void testButtonAction() {

        LOGGER.info(" testButtonAction called ");


        if( !rpCenter.getChildren().isEmpty()) {
            rpCenter.getChildren().clear();
        }


        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();
        
        System.err.println("\tCurrentCard data is null? " + (currentCard.getQText() == null));
        mode = 't';

        // All of the work is accessed from selectTest()
        GenericTestType test = TestList.selectTest( currentCard.getTestType() );
        rpCenter.getChildren().add(test.getTReadPane(currentCard, GEN_CARD, rpCenter));

        VBox topVBox = new VBox();
        Label deckNameLabel = new Label("Flash Deck:  " + deckName);
        deckNameLabel.setId("label16");
        topVBox.getChildren().add(deckNameLabel);
 //       topVBox.setStyle("-fx-background-color: " + UIColors.GRAPH_BGND);
        topVBox.setAlignment(Pos.CENTER);

        masterBPane.setTop(topVBox);
        masterBPane.setCenter(rpCenter);
        masterBPane.setBottom(manageSouthPane('t'));

        masterBPane.setId("readFlashPane");

    }


    /**
     * QAndA BUTTON ACTION
     * qaButton Action method: Question and Answer button action
     * Void method
     * This action sets the scene to the simple question and answer mode
     * Uses treeWalker
     */
    protected void qaButtonAction()
    {
        if(! rpCenter.getChildren().isEmpty()) {
            rpCenter.getChildren().clear();
        }
    

        FlashCardMM currentCard = (FlashCardMM) FMWalker.getCurrentNode().getData();
        rpCenter.getChildren().add( QandA.QandASession.getInstance().getTReadPane(currentCard, GEN_CARD, rpCenter));
        //studyButton.setText("Back");
        masterBPane.setCenter(rpCenter);
        mode = 'q';
        masterBPane.setBottom(manageSouthPane('q'));

    }


    public void exitAction() {
        //leaveAction();
        System.exit(0);
    }

    // *** SETTERS *** //


    /**
     * Mode controls the bottom pane and what buttons,
     * and gauges are located there.
     * @param m
     */
    public void setMode(char m) { mode = m; }
    
    
    // *** GETTERS *** //

    public Pane getMasterBPane() {
        return this.masterBPane;
    }
    public Pane getRPCenter() {
        return rpCenter;
    }

    /**
     * Returns the mode 't' or 'q'
     * @return
     */
    public char getMode() { return mode; }
    
    /**
     * returns the value of score
     * static method
     * @return double score
     */
    public double getScore()
    {
    	return score;
    }

    /**
     * Returns the current deckName
     * @return
     */
    public String getDeckName() {
        LOGGER.debug("\n *^*^* in ReadFlash.getDeckname() *^*^*\n\tdeckName: " + deckName + "\n");
        return deckName;
    }

 



    private static boolean showAnsNavBtns = false;
    private static char mode = 'q';
    /**
     * NOTE: Search is handled in 'Q' since it is only available
     * in the Question and Answer mode. The search bar is added to the
     * rpSouth section. The center section is replaced by the search results
     * with a hyperlink from the SearchPane class. When the EncryptedUser.EncryptedUser clicks on
     * the hyperlink, the linked card replaces the list of search results.
     * The majority of these actions are handled in SearchPane as the endpoint.
     *
     * - manageMode: Manages the rpSouth gridPane where most of the buttons reside
     * T is for Test pane. Q is for and qAndA pane, and E is for edit pane.
     * Buttons and search are available depending on the mode entered in
     * the parameter. If the answer nav buttons should be displayed,
     * set showAnsNavBtns to true. else false.
     * @param mode char
     * @return Returns a BorderPane
     */
    public VBox manageSouthPane(char mode)
    {
        HBox navBtnHbox = new HBox();
        navBtnHbox.setSpacing(3);
        HBox buttonBox = new HBox();
        // padding top, right, bottom, left
        buttonBox.setPadding(new Insets(0, 0, 10, 0));
        buttonBox.setSpacing(15);

        BorderPane contrlsBPane = new BorderPane();
        contrlsBPane.setId("studyBtnPane");

        switch(mode)
        {
            case 't': // Test mode
            {
                LOGGER.debug("\n ~^~^~^~ In ManageMode set mode to 'T' ~^~^~^~");

                scoreLabel = new Label("Points");
                scoreTextF.setMaxWidth(50);
                
                
                HBox boxR = new HBox();
                buttonBox.getChildren().clear();
                navBtnHbox.getChildren().addAll(firstQButton, prevQButton, nextQButton, endQButton);

                buttonBox.getChildren().addAll(navBtnHbox, boxR);
                HBox scoreBox = new HBox();
                scoreBox.setPadding(new Insets(0, 10, 10, 10));
                scoreBox.setSpacing(10);
                scoreTextF.setEditable(false);

                //scoreBox.getChildren().addAll(sGaugePane, pGaugePane);
    
                BorderPane bottomBPane = new BorderPane();
                bottomBPane.setPadding(new Insets(20, 10, 10, 10));
                bottomBPane.setLeft(buttonBox);
                bottomBPane.setRight(scoreBox);
                bottomBPane.setMaxHeight(SceneCntl.getSouthBPaneHt());
                bottomBPane.setMinHeight(SceneCntl.getSouthBPaneHt());
                VBox vBox = new VBox();
                vBox.setId("studyBtnPane");
                vBox.getChildren().addAll(bottomBPane, exitBox);
                return vBox;
            }
            default:
            case 'a': // Q&A mode
            {
                buttonBox.getChildren().clear();

                // Navigation buttons
                navBtnHbox.getChildren().clear();
                navBtnHbox.getChildren().addAll(firstQButton, prevQButton, nextQButton, endQButton);
                buttonBox.getChildren().add(navBtnHbox);
                contrlsBPane.setCenter(buttonBox);
                
                // The guage and button at the bottom of the screen
                BorderPane bottomBPane = new BorderPane();
                bottomBPane.setPadding(new Insets(20, 10, 10, 10));
                //bottomBPane.setTop(searchPane.getSearchBox(FMTWalker.getInstance(), FlashCardOps.getInstance().getFlashList() ));
                bottomBPane.setCenter(navBtnHbox);
                //bottomBPane.setRight(pGaugePane);
                VBox vBox = new VBox();
                vBox.setId("studyBtnPane");
                contrlsBPane.setBottom(bottomBPane);
                // Returning the bottom section BorderPane
                vBox.getChildren().addAll(bottomBPane, exitBox);
                return vBox;
            }
        }
    }
    
    // Other methods

    // *** BUTTON COLORS ***
    /**
     * Helper method for button.setOnActions()
     * Depending on the isRight setting, if question has been answered previously
     * or if its answer has been viewed previously, it cannot be answered in test
     * mode. This method disables the selectAnswerButton as well as sets its color
     * based on if the EncryptedUser.EncryptedUser answered the question correctly = green (isRight == 1),
     * incorrect = red (isRight == -1); or simply viewed in q&a mode = grey
     * (isRight == 3).
     * @param isRight this cards isRight for this session
     * @param test The testType
     */
    protected static void ansQButtonSet(int isRight, GenericTestType test)
    {
        try
        {
            if(isRight != 0) {
                test.getAnsButton().setDisable(true);

                switch(isRight)
                {
                    case -1: // button set to red
                    {
                        test.getAnsButton().setStyle("-fx-background-color: RGBA(255, 0, 0, .4); -fx-text-fill: RGBA(255, 255, 255, .7);");
                        //LOGGER.debug(" set button color orange faded \n");
                        break;
                    }
                    case 1: // button set to green
                    {
                        test.getAnsButton().setStyle("-fx-background-color: RGBA(0, 255, 53, .4); -fx-text-fill: RGBA(255, 255, 255, .7);");
                        break;
                    }
                    case 3: // button greyed out,
                    {
                        test.getAnsButton().setStyle("-fx-background-color: RGBA(202, 202, 202, .7); -fx-text-fill: RGBA(255, 255, 255, .7);");
                        break;
                    }
                    default:
                    {
                        // no changes
                        break;
                    }
                }
            }
            else {
                test.getAnsButton().setDisable(false);
                test.getAnsButton().setVisible(true);
                test.getAnsButton().setStyle("-fx-color: #FFFFFF;");
            }
        }
        catch (NullPointerException f) {
            // print an error message
            LOGGER.debug("\n *** Null pointer in Test *** "
                    + "\n\tselectAnswerButton may not work as"
                    + "\n\tintended");
        }

    }

    public void setShowAnsNavBtns(Boolean bool) {
        showAnsNavBtns = bool;
    }

}
