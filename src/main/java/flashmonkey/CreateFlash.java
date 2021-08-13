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
 * @author: Lowell Stadelman
 */

package flashmonkey;

// *** JAVAFX IMPORTS ***


import ch.qos.logback.classic.Level;


import fmtree.FMWalker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.controlsfx.control.PrefixSelectionComboBox;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;

import type.testtypes.AIMode;
import type.testtypes.GenericTestType;
import type.testtypes.TestList;
import uicontrols.ButtoniKon;

import uicontrols.FMAlerts;
import uicontrols.FxNotify;
import uicontrols.SceneCntl;


import java.util.ArrayList;
import java.util.ListIterator;


/*******************************************************************************
 * Synchronized Singleton class provides the user interface to create new flash-cards.
 * <p> Creates a new FlashCardMM set to defaults in the createFlashScene method. FlashCardMM
 * constructor creates a new cID. The currentCard is updated in save methods.
 * New Card Action methods use the default FlashCardMM constructor</p>
 *<P>
 * To Add a new TestType. - Add the class name to the ObservableList TESTS,
 *  The left is the name that appears in the choicebox and the right is the class
 *  name. Add the class, include the unique bitSet. The index of TESTS is
 *  indicated by the bitset's bit that is set from the littleEndian. Only one
 *  bit should be set. </p>
 * <pre>
 * Algorithm
 *      BEGIN
 *          User selects test type or default AI is used.
 *              - if AI ???? @todo create AI
 *              - Test type selects Card type
 *                  - Default is Double Horizontal
 *               - Section type selected by
 *                  - Default is single with TextCell
 *                  - if user drags a file into textArea
 *                  - or if authcrypt.user clicks snapShot
 *                  - then section = double section with TextCell on left
 *                  and media cell on right
 *
 *                  - if user clicks 'X' button on Text area then section is
 *                  multi-media only. Multi-Media includes image and drawings
 *                  - if user clicks 'x' button on media area. Section becomes
 *                  single section with TextCell only.
 *
 *              User clicks next FlashCard
 *                  - flashCard is saved to creatorList
 *                  - TextArea is cleared
 *                  - multimedia areas are cleared
 *                  - Users previous test type is used
 *                  - Sections return to default.
 *
 *             User clicks save
 *                  - creatorList saved to file
 *                  - TextArea is cleared
 *                  - Multimedia is cleared.
 *                  - Test type is default.
 *                  - User is sent to menu.
 *
 *             Navigation through existing cards
 *                  User clicks previousQButton or nextQButton
 *                      flashListChanged = true
 *                      if(changed) saveChanges
 *                      clear editors
 *                      populate editors
 *                      setCenter.clickedOn( changed = true )
 *                      changed = true
 *
 *                  User clicks revert
 *                      ensure we have the right card
 *                      revert creatorList(idx) to flashList(idx)
 *                  User clicks on delete
 *                      ensure we have the right card
 *                      iterator.remove()
 *                      remove the card in creatorList
 * </pre>
 *
 * <pre>
 *  When a section first appears, it appears as a single text pane.
 *    - When the user clicks on snapshot:
 *    - The section becomes a double pane with text left and image right.
 *    - if the user drags a file into the section,
 *     it becomes a double pane with that type of media on the right.
 *
 *     Create
 *         - Text only 't'
 *         - Image or image and shapes only 'c'
 *         - Shapes only 'c'
 *         - Audio only 'm'
 *         - Video only 'm'
 *         - Combination text and media 'C' or 'M'
 * </pre>
 *
 * <pre>
 *  Test types in order. ComboBox selection types are order important. It is tightly bound.
 *  EncryptedUser must select a mode. If not we send the user an error message.
 *
 *    0 = Default = multi-choice, multi-answer, T or F, and Write in.
 *    1 = Multiple choice
 *    2 = Multiple answer
 *    3 = True or false
 *    4 = Write it in
 *    5 = FITB fill in the blank
 *    6 = Turn in audio
 *    7 = Turn in video
 *    8 = Turn in Drawing
 *    9 = Draw over image
 *    10 = MathCard
 *    11 = GraphCard
 *    12 = NoteTaker
 *
 * </pre>
 *
 * <pre>
 * <b>Changing to editing mode</b>
 *     On leftQButton or rightQButton click, mode changes from creating a new
 *     card, to editing an existing card.
 * </pre>
 *
 ******************************************************************************/


public final class CreateFlash<C extends GenericCard> {

    private final static ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(CreateFlash.class);
    //private static final Logger LOGGER = LoggerFactory.getLogger(CreateFlash.class);
    // Only one instance of this class may exist within a JVM. Not
    // a 100% solution.
    private static CreateFlash CLASS_INSTANCE;
    
    //private FlashCardOps fcOps;
    // The generic reference
    private GenericCard gCard;
    
    // The message when the card is not enabled
    String cardNotEnabledMsg = null;

    // if cards in flashList have changed
    // ensures that if there is an exit, the users
    // modifications are saved.
    private boolean flashListChanged;

    // Editors
    private SectionEditor editorU = new SectionEditor();
    private SectionEditor editorL = new SectionEditor();

    // MarketPlace button
    private Button sellButton;
    // Button to add FlashCard question and answer
    private Button saveDeckButton, insertCardButton, newCardButton, abandButton;
    // prev and next buttons for editing existing cards
    private Button nextQButton, prevQButton, deleteQButton, undoQButton;
    // resets all q's performance metrics to zero
    private Button resetDeckButton;
    // The card buttons HBox
    private HBox cardBtnBox;
    private VBox masterPane;
    private VBox cfpCenter;
    private Stage metaWindow;// = new Stage();


    // currentCard = is always a card stored in
    // the creatorList. The last card on the list
    // is always empty after start. The last card
    // is removed before being set in the list in
    // FlashCardOps.setListInFile(...)
    private FlashCardMM currentCard;

    //*** VARIABLES ***
    //private static int cardNum; //question number, starts at 0 unless list is not new.
    //private String cID;
    private int startSize;
    private PrefixSelectionComboBox<TestMapper> entryComboBox;

    //*** ARRAY List ***
    private static volatile ArrayList<FlashCardMM> creatorList;//  = new ArrayList<>(20);

    // The iterator index. Starts at the last existing
    // index prior to the new card added.
    private int listIdx;// = creatorList.size();

    // Combobox contains placeholder classes that
    // currently do nothing.
    // To select the test types in the ComboBox.
    private final ObservableList<TestMapper> TESTS = FXCollections.observableArrayList( TestMapper.getTestList(TestList.TEST_TYPES));
    
    
    /*******  METHODS ********/

    /**
     * no args constructor
     */
    private CreateFlash() {
        flashListChanged = false;
    }

    /* ------------------------------------------------------- **/

    /**
     * Returns an instance of the class. If getInstance hasn't been called before
     * a new instance of the class is created. otherwise it returns the existing
     * instance.
     * To intialize: ReadFlash readFlashObj = ReadFlash.getInstance();
     * @return ReadFlash
     */
    public static synchronized CreateFlash getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new CreateFlash();
        }
        return CLASS_INSTANCE;
    }

    /* ------------------------------------------------------- **/

    /**
     * Scene CONSTRUCTOR
     */
    Scene createFlashScene() {  // package private
    
        LOGGER.setLevel(Level.DEBUG);
        
        LOGGER.debug("called createFlashScene()");

        // local
        FlashCardOps fcOps = FlashCardOps.getInstance();

        
        LOGGER.debug("flashList is empty: {}", fcOps.getFlashList().isEmpty());
        
        // If this is adding cards to an existing flashCard list, then
        // creates the list to be added to. Also sets the
        // startSize and the cardNum
        if ( ! fcOps.getFlashList().isEmpty()) {
            setCreatorList();
        } else {
            LOGGER.debug("flashList is empty.");
            creatorList = new ArrayList<>(10);
        }

        // The list's index starts at the end
        // for the purpose of editing. It
        // starts at the end of the list.
        if(creatorList != null) {
            listIdx = creatorList.size();
        }

        // NORTH & CENTER PANES entry fields
        HBox cfpNorth = new HBox(4);
        cfpNorth.setPadding(new Insets(2, 4, 2, 4 ));
        // Contains the CardType
        cfpCenter = new VBox(2);
        //cfpCenter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cfpCenter.setPadding(new Insets(0, 4, 0, 4 ));
        cfpCenter.setSpacing(2);

        // contains the buttons in a horizontal row
        BorderPane cfpSouth;
        cfpNorth.setId("cfpNorthPane");

        /*
         * ****  ComboBox selects the Test type. Default is no mode.  ****
         * 0 = Default = multi-choice, multi-answer, T or F, and Write in.
         * 1 = Multiple choice
         * 2 = Multiple answer
         * 3 = True or false
         * 4 = Write it in
         * 5 = FITB fill in the blank
         * 6 = Turn in audio
         * 7 = Turn in video
         * 8 = Turn in Drawing
         * 9 = Draw over image
         * 10 = MathCard
         * 11 = GraphCard
         * 12 = NoteTaker
         */
        entryComboBox = new PrefixSelectionComboBox<>();
        //entryComboBox = new FMAutoCompleteBox<>();
        
        // Set the array of TESTS in comboList
        // entryComboBox.editableProperty().setValue(true);
        entryComboBox.getItems().addAll(TESTS);
        // Set tooltip
        entryComboBox.setTooltip(new Tooltip("Select the test mode for this card." +
                //"\nAI: (Default) Intelligent selection mode, Intelligently selects top 4 modes." +
                //"\nAll others are as stated. " +
                "\nEffects the card layout. " +
                "\n** Type a card name in for rapid search :)" +
                "\n\n!!! NOTE: Not all card types work in this version. :("));
        // Set prefered width and height of combo box
        entryComboBox.setMaxWidth(Double.MAX_VALUE);
        entryComboBox.setPrefHeight(15);
        entryComboBox.setPromptText("Test type");
        // set number of rows visible
        entryComboBox.setVisibleRowCount(5);
        entryComboBox.show();
        // For quick search of combobox items
        // new SerialAutoCompleteBox(entryComboBox);

    // Set section editors for Upper area and lower area
        gCard = new GenericCard();
        // create blank card with new cID for the first card
        // and add it to creatorList. getCID from card in list.
        currentCard = new FlashCardMM();
        creatorList.add(currentCard);
        currentCard.setCNumber(999);
    
        LOGGER.info("CreatorList size: {}, listIdx: {}", creatorList.size(), listIdx);
        // section editors
        editorU = new SectionEditor(makeQPrompt(listIdx), 'U', this.currentCard.getCID());
        editorL = new SectionEditor("Enter the answer here", 'L', this.currentCard.getCID());


        // If this is the first session, set the default create to AIMode
        if (cfpCenter.getChildren().isEmpty()) {
            AIMode aiMode = new AIMode();
            cfpCenter.getChildren().addAll(aiMode.getTEditorPane(creatorList, editorU, editorL));
        }

        // Action call
        entryComboBox.setOnAction(e -> {
            cfpCenter.getChildren().clear();
            if(entryComboBox.getValue().getTestType().isDisabled()) {
                cardNotEnabledMsg = "Arrrff! This feature will be enabled in a future version. It is on our to do list " +
                        "and is not yet available. :(";
            } else {
                cardNotEnabledMsg = null;
            }
            editorU.styleToPrompt();
            editorU.setPrompt(makeQPrompt(listIdx));
            cfpCenter.getChildren().addAll(entryComboBox.getValue().getTestType().getTEditorPane(creatorList, editorU, editorL));
        });

        Label label = new Label(ReadFlash.getInstance().getDeckName());
        label.setId("label");
        // Add comboBox to buttonBox
        cfpNorth.getChildren().addAll(entryComboBox, label );
        
        //****         CFP SOUTH AND BUTTON ACTIONS         ***
        // CFP south contains buttons gauges and non content control/information

        getKeyboardShortcuts();
        cfpSouth = getCreatorButtons();
        cfpSouth.setPrefHeight(125);
        cfpSouth.setMinHeight(Region.USE_PREF_SIZE);
        cfpSouth.setMaxHeight(Region.USE_PREF_SIZE);
        cfpSouth.setPadding(new Insets(10, 4, 10, 4));
        // Create a master borderPane then add buttons to the scene
        masterPane = new VBox(2);
        masterPane.setId("cfMasterPane");
        masterPane.getStylesheets().addAll("css/buttons.css", "css/mainStyle.css");
        masterPane.getChildren().addAll(cfpNorth, cfpCenter, cfpSouth);
        Scene masterScene = new Scene(masterPane, SceneCntl.getEditorWd(), SceneCntl.getEditorHt());
        
        // the initial displayed element "last in list" is empty. Other
        // cards are handled by nav buttons

        if(FMWalker.getInstance().getCurrentNode() != null) {
            FMWalker.getInstance().setCurrentNode(currentCard);
        }

        return masterScene;

    }   /* END createFlashScene() ***/

    /* ------------------------------------------------------- **/
    

    /**
     * @return the selected testType in the comboBox.
     */
    public GenericTestType getTestType() {
        if(null != entryComboBox && null != entryComboBox.getValue()) {
            return entryComboBox.getValue().getTestType(); //getTestType();
        }
        return null;
    }


    /* ------------------------------------------------------- **/

    public VBox getCFPCenter() {
        return this.cfpCenter;
    }

    /**
     * Returns the upper SectionEditor reference
     * @return upper SectionEditor referenc
     */
    public SectionEditor getEditorU() { return this.editorU; }

    /* ------------------------------------------------------- **/
    /**
     * Returns the lower SectionEditor reference
     * @return lower SectionEditor reference
     */
    public SectionEditor getEditorL() { return this.editorL; }

    /* ------------------------------------------------------- **/

    /**
     * Helper method for previous and next buttons
     * Sets the CreateFlash UI
     * <pre>
     * - sets the upper and lower editors fields
     * - sets the ComboBox
     * </pre>
     * @param currentCard
     */
    protected void setUIFields(FlashCardMM currentCard) {
        this.currentCard = currentCard;

        // Get the test and set it in the ComboBox.
        // Uses the bitset from the TestType.
        GenericTestType ga = TestList.selectTest(currentCard.getTestType());
        entryComboBox.setValue(new TestMapper(ga.getName(), ga));

        if(editorU.hasTextCell(currentCard.getQType())) {
            editorU.setText(currentCard.getQText());
        }
        if(editorL.hasTextCell(currentCard.getAType())) {
            editorL.setText(currentCard.getAText());
        }

        if(currentCard.getQType() != 't') {
            editorU.setSectionMedia(currentCard.getQFiles(), currentCard.getQType(), 'U', currentCard.getCID());
            editorU.setSectionType(currentCard.getQType());
        }

        if(currentCard.getAType() != 't' ) {
            editorL.setSectionMedia(currentCard.getAFiles(), currentCard.getAType(), 'L', currentCard.getCID());
            editorL.setSectionType(currentCard.getAType());
        }
    }

    /* ------------------------------------------------------- **/

    /**
     * Used in SnapShot to set edit mode.
     * @param bool
     */
    public void setFlashListChanged(boolean bool) {
        this.flashListChanged = bool;
    }

    /* ------------------------------------------------------- **/

    public void setListIdx(int idx) {
        this.listIdx = idx;
    }


    /* ------------------------------------------------------- **/

    /**
     * Disables the buttons for SectionEditor.
     */
    public void disableButtons() {
        this.editorL.disableEditorBtns();
        this.editorU.disableEditorBtns();
        this.insertCardButton.setDisable(true);
        this.newCardButton.setDisable(true);
        this.prevQButton.setDisable(true);
        this.nextQButton.setDisable(true);
        this.resetDeckButton.setDisable(true);
        this.saveDeckButton.setDisable(true);
        this.deleteQButton.setDisable(true);
    }

    /* ------------------------------------------------------- **/

    /**
     * Enables the buttons for SectionEditor.
     */
    public void enableButtons() {
        this.editorL.enableEditorBtns();
        this.editorU.enableEditorBtns();
        this.insertCardButton.setDisable(false);
        if(this.listIdx > 0) {
            this.prevQButton.setDisable(false);
            this.undoQButton.setDisable(false);
        }
        if(this.listIdx < creatorList.size() - 1) {
            this.nextQButton.setDisable(false);
            this.undoQButton.setDisable(false);
        }
        this.resetDeckButton.setDisable(false);
        this.newCardButton.setDisable(false);
        this.saveDeckButton.setDisable(false);
        this.deleteQButton.setDisable(false);
    }

    /* ------------------------------------------------------- **/

    /**
     * Sets the buttons for CreatorList and returns a vBox continaining
     * the buttons
     * @return VBox contining the buttons for this pane
     */
    private BorderPane getCreatorButtons() {

        newCardButton = ButtoniKon.getNewCardButton();
        newCardButton.setOnAction(e ->
        {
            // Check content and save, or give user
            // an error message. Respond based on user
            // choice.
            CardSaver cs = new CardSaver();
            boolean ret = cs.saveCard(editorU, editorL);
            if(ret) {
                return;
            }
            editorU.tCell.getTextArea().requestFocus();
        });

        //  "Add" & "Save" Buttons in the south pane
        insertCardButton = ButtoniKon.getInsertCardButton();

        
        // Save button & Action.
        // Exits createFlash scene
        saveDeckButton = ButtoniKon.getSaveDeckButton();
        saveDeckButton.setOnAction(e ->
        {
            saveDeckAction();
            // May cause a NullPointerException if
            // there is not a popUp pane open from
            // both of these.
            if(editorL.getDrawTools() != null) {
                editorL.getDrawTools().justClose();
            } else if (editorU.getDrawTools() != null) {
                editorU.getDrawTools().justClose();
            }

        });
        
        
        // Abandon button & Action
        // Exits createFlash scene
        abandButton = ButtoniKon.getQuitChangesButton();
        abandButton.setOnAction(e ->
        {
            // May cause a NullPointerException if
            // there is not a popUp pane open from
            // both of these.
            if(editorL.getDrawTools() != null) {
                editorL.getDrawTools().justClose();
            } else if (editorU.getDrawTools() != null) {
                editorU.getDrawTools().justClose();
            }

            creatorList.clear();
            FlashMonkeyMain.setWindowToNav();
        });


        nextQButton = ButtoniKon.getCreateQNextButton();
        nextQButton.setOnAction(e ->
        {
            nextQButtonAction();
        });

        prevQButton = ButtoniKon.getCreateQPrevButton();
        prevQButton.setOnAction(e ->
        {
            prevQButtonAction();
        });

        undoQButton = ButtoniKon.getUndoChangesButton();

        undoQButton.setOnAction(e -> {
            undoQButtonAction(currentCard);
        });

        deleteQButton = ButtoniKon.getDeleteCardButton();
        deleteQButton.setOnAction(e -> {
            deleteQButtonAction(currentCard);
        });

        resetDeckButton = ButtoniKon.getResetOrderButton();
        sellButton = ButtoniKon.getSellButton();
        cardBtnBox = new HBox(2);
        //cardBtnBox.setAlignment(Pos.CENTER);
        cardBtnBox.getChildren().addAll(newCardButton, prevQButton, nextQButton, undoQButton, deleteQButton);
        HBox deckBtnBox = new HBox(2);
        //deckBtnBox.setAlignment(Pos.CENTER);
        deckBtnBox.getChildren().addAll(resetDeckButton, saveDeckButton, abandButton);
        BorderPane southBPane = new BorderPane();
        //vBox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(40);
        vbox.getChildren().addAll(cardBtnBox, deckBtnBox);
        southBPane.setRight(sellButton);
        southBPane.setLeft(vbox);

        //vBox.getChildren().addAll(cardBtnBox, deckBtnBox);
        return southBPane;
    }
    
    /* ------------------------------------------------------- **/


    /**
     * Sets the UI to the next card for
     * editing
     */
    private void nextQButtonAction() {
        LOGGER.info("called nextQButtonAction");
    
        // Check content and save, or give user
        // an error message. Respond based on user
        // choice.
        CardSaver cs = new CardSaver();
        boolean ret = cs.saveCard(editorU, editorL);
        if(ret) {
            return;
        }
        
        // increment the cardIdx and for
        // setting fileNames to each card
        listIdx++;
        // clear editor fields
        editorU.resetSection();
        editorL.resetSection();

        // if there is an exit, should data be saved.
        // flashListChanged = true;

        if (listIdx > -1 && listIdx < creatorList.size()) {

            // set the current cards data into the fields
            FlashCardMM currentCard = creatorList.get(listIdx);
            
            editorU.setPrompt(makeQPrompt(listIdx));
            editorL.setPrompt("Enter answer");
            setUIFields(currentCard);
            FMWalker.getInstance().setCurrentNode(currentCard);
        }

        // Enable buttons according to idx
        navButtonDisplay(listIdx);

        String insertBtnMsg = "Insert a new card at " + (listIdx + 1) ;
        insertCardButton.setTooltip(new Tooltip(insertBtnMsg));
    }


    /* ------------------------------------------------------- **/
    

    /**
     * Sets the UI to the previous existing card for
     * editing
     */
    private void prevQButtonAction() {
        LOGGER.info("prevQButtonAction called");
    
        String insertBtnMsg = "Insert a new card at " + listIdx;
        insertCardButton.setTooltip(new Tooltip(insertBtnMsg));

        // Check content and save, or give user
        // an error message. Respond based on user
        // choice.
        CardSaver cs = new CardSaver();
        boolean ret = cs.saveCard(editorU, editorL);
        if(ret) {
            return;
        }

        
        // clear editor fields
        editorU.resetSection();
        editorL.resetSection();

        // decrement the cardNumber for setting fileNames to each card
        listIdx--;

        LOGGER.info(Integer.toString(listIdx));

        // set the UI to the current card
        if(listIdx > -1 ) {
            if(listIdx >= creatorList.size()) {
            //    --cardNum;
                listIdx = listIdx - 1;
            }
            FlashCardMM currentCard = creatorList.get(listIdx);
            editorU.setPrompt(makeQPrompt(listIdx));
            setUIFields(currentCard);
            FMWalker.getInstance().setCurrentNode(currentCard);
        }
        // if there is an exit, should data be saved.
        // flashListChanged = true;
        // enable buttons according to idx
        LOGGER.info("listIdx: <{}>", listIdx);
        navButtonDisplay(listIdx);
        // close popup if exists
        
    }
    

    /* ------------------------------------------------------- **/


    private void navButtonDisplay(final int idx) {
        
        if(idx < 1) {
            prevQButton.setDisable(true);
            undoQButton.setDisable(true);
            //insertCardButton.setDisable(true);

        } else if (idx > (creatorList.size() -2)) {
            nextQButton.setDisable(true);
            prevQButton.setDisable(false);
            //insertCardButton.setDisable(false);
            deleteQButton.setDisable(true);
            undoQButton.setDisable(true);

            cardBtnBox.getChildren().clear();
            cardBtnBox.getChildren().addAll(newCardButton, prevQButton, nextQButton, undoQButton, deleteQButton);
        } else {
            sellButton.setDisable(false);
            prevQButton.setDisable(false);
            nextQButton.setDisable(false);
            deleteQButton.setDisable(false);
            insertCardButton.setDisable(false);
            undoQButton.setDisable(false);
            cardBtnBox.getChildren().clear();
            cardBtnBox.getChildren().addAll(insertCardButton, prevQButton, nextQButton, undoQButton, deleteQButton);
        }
    }


    /* ------------------------------------------------------- **/


    private void getKeyboardShortcuts() {
        // Keyboard shortcut, Moves between text fields
        editorU.tCell.getTextArea().setOnKeyPressed((KeyEvent e) -> {
            if (e.isAltDown() || e.isShiftDown()) {
                if (e.getCode() == KeyCode.ENTER) {
                    // Check content and save, or give user
                    // an error message. Respond based on user
                    // choice.
                    CardSaver cs = new CardSaver();
                    boolean ret = cs.saveCard(editorU, editorL);
                    if(ret) {
                        return;
                    }
                }
            }
        });
        // Keyboard shortcut, Moves between text fields
        editorL.tCell.getTextArea().setOnKeyPressed((KeyEvent e) -> {
            if (e.isAltDown() || e.isShiftDown()) {
                if (e.getCode() == KeyCode.ENTER) {
                    // Check content and save, or give user
                    // an error message. Respond based on user
                    // choice.
                    CardSaver cs = new CardSaver();
                    boolean ret = cs.saveCard(editorU, editorL);
                    if(ret) {
                        return;
                    }
                }
            }
        });
    }
    
    /* ------------------------------------------------------- **/

    /**
     * Returns this masterPane
     * @return
     */
    public VBox getMasterPane() { return this.masterPane; }

    
    /* ------------------------------------------------------- **/


    /**
     * Checks if the last card question has either
     * text or files.length > 0;
     * @return true if the last cards question has data.
     */
    private boolean lastCardQHasData() {
        FlashCardMM lastCard = creatorList.get(creatorList.size() -1);
        return !lastCard.getQText().isEmpty() || lastCard.getQFiles().length > 0;
    }
    
    /* ------------------------------------------------------- **/

    /**
     * If the comboBox has a testType selected it returns true
     * otherwiseit returns false and a pop-up error message
     * is displayed.
     * @return
     */
    private boolean checkComboBox()
    {
        if(entryComboBox.getValue() == null) {
            String emojiPath = "emojis/flashFaces_sunglasses_60.png";
            String message = "  Ooops! \n  Please select a test type  ";
            int w = (int) editorU.tCell.getTextCellVbox().getWidth();
            double x = editorU.sectionHBox.getScene().getWindow().getX() + (w / 8);
            double y = editorU.sectionHBox.getScene().getWindow().getY() + 50;
            FMPopUpMessage.setPopup(
                    x,
                    y,
                    w,
                    message,
                    emojiPath,
                    FlashMonkeyMain.getWindow(),
                    3000
            );
            return false;
        }
        return true;
    }
    

    /* ------------------------------------------------------- **/

    /**
     * Deleting and adding cards from the center of the list creates numbering issues.
     * To solve this issue, we begin with the listIdx which may not be accurate.
     * Then search using the binary search method and perform an equals on a cards CID
     * with the desired delete CID.
     * @param currentCard
     */
    private void deleteQButtonAction(FlashCardMM currentCard) {
        // The closest reliable number to this cards
        // location in the current deck.
        String delCID = currentCard.getCID();
        int fwdIdx = listIdx;
        int revIdx = listIdx - 1;

        LOGGER.info("called");

        while( fwdIdx < creatorList.size() && revIdx > -1 ) {

        //    FlashCard is not deleted when using treePane, Although the list length shows that
        //            the card has been removed. Apparently the card is still in the lsit that
        //            the tree is uing to build from. ??? IDK!!!

            if(fwdIdx < creatorList.size() && creatorList.get(fwdIdx).getCID().equals(delCID)) {

                ListIterator<FlashCardMM> iterator = creatorList.listIterator(fwdIdx);
                iterator.next();
                iterator.remove();
                listIdx = fwdIdx - 1;
                currentCard = creatorList.get(listIdx);
                setUIFields(currentCard);

                break;
            } else if (revIdx > -1 && creatorList.get(revIdx).getCID().equals(delCID)) {

                ListIterator iterator = creatorList.listIterator(revIdx);
                iterator.previous();
                iterator.remove();
                listIdx = revIdx - 1;
                currentCard = creatorList.get(listIdx);
                setUIFields(currentCard);

                break;
            }
        }
    }

    /* ------------------------------------------------------- **/

    /**
     * Reverts the current card shown in the users display
     * to it's original version.
     * <p><b>NOTES:</b> Due to adding and deleting cards, the index
     * that this cards number originated from may no longer
     * be correct. Thus, this method conducts a search starting at
     * the original index and working from there in both directions.</p>
     * Algorithm
     *  <pre>
     *      1) Get this card's aNumber
     *      2) find this card in creatorList starting at this aNumber
     *      3) change found card in creatorList to the flashlist(aNumber)
     *  </pre>
     * @param currentCard
     */
    private void undoQButtonAction(FlashCardMM currentCard) {

        LOGGER.debug("undoQButtonAction called");
        
        //FlashCardOps fcOps = FlashCardOps.getInstance();
        // The closest reliable number to this cards
        // location in the current deck.
        final ArrayList<FlashCardMM> copyFlashList = FlashCardOps.getInstance().getFlashList();
        int aNum = currentCard.getANumber();
        int fwdIdx = aNum;
        int revIdx = aNum - 1;
        
        // finding the card
        while( fwdIdx < copyFlashList.size() && revIdx > -1 ) {
            if(fwdIdx < copyFlashList.size() && copyFlashList.get(fwdIdx).getANumber() == aNum) {
                
                creatorList.set(fwdIdx, copyFlashList.get(aNum));
                fwdIdx++;
                break;
            } else if (revIdx > -1 && copyFlashList.get(revIdx).getANumber() == aNum) {
                
                creatorList.set(revIdx, copyFlashList.get(aNum));
                revIdx--;
                break;
            }
        }
    
        editorU.resetSection();
        editorL.resetSection();
        
        setUIFields(creatorList.get(listIdx));
    }


    // ********************* INNER CLASS ************************* //
    
    private class CardSaver {
    
    
        CardSaver() { /* empty no args constructor */}
    
        /**
         * Returns true if the user requests to return to the editor
         * else it continues and saves the card.
         * @param editorU
         * @param editorL
         * @return
         */
        boolean saveCard(SectionEditor editorU, SectionEditor editorL) {
    
            // Check content and save, or give user
            // an error message. Respond based on user
            // choice.
            int result = checkContent(editorU, editorL);
            LOGGER.debug("saveCard(...) param \"result\": <{}>", result);

            int ret = statusResponse(result);

            LOGGER.debug("status response returned: <{}>", result);
            if (ret == -1) {
                return true;
            }
            if (ret == 1) {
                addCardAction();
            }
            return false;
        }


        /**
         * DESCRIPTION: Adds flashCard (question and answer objects)
         * to the creatorList arrayList.
         */
        private void addCardAction() {
            LOGGER.info("\n *** addCardAction() *** \n");
            LOGGER.debug("\n\t saved as qID, The qID: {}", currentCard.getCID());
            LOGGER.debug("\t cNum {}", currentCard.getCNumber());
            LOGGER.debug("\t creatorList size: {}", creatorList.size() + 1);


            // Order is important for proper display of
            // card number.
            // update the listIdx
            listIdx++;
            // Save this card to the creator list
                saveNewCardToCreator();
            // set the prompt
            editorU.styleToPrompt();
            editorU.setPrompt(makeQPrompt(listIdx));
            editorU.tCell.getTextArea().requestFocus();

            insertCardButton.setText("New card");
            // display in the nav tree
            navButtonDisplay(listIdx);
        }
    
    
        /**
         * Adds a new card to the end of the creator list. Numbering is
         * updated when a deck is uploaded from a file.
         *
         * Assumes that the card is complete
         *
         * @return
         */
        private void saveNewCardToCreator() {
        
            LOGGER.info("\n *** IN SAVE NEW CARD TO CREATOR *** \n");
        
            //if(checkContent(editorU, editorL) == 1) {
            // Assign currentTest from combobox
            GenericTestType gT = entryComboBox.getValue().getTestType();
            // card at end of list, or ??
            int cardNum = listIdx;
            // Create a new FlashCardMM
            currentCard.setAll(
                    cardNum * 10,        // Not super important until later. cardNumbers end as a multiple of ten. Used to set a card in the Tree
                    gT.getTestType(),         // BitSet TESTS
                    gT.getCardLayout(),         // cardLayout
                    editorU.getText(),          // questionText
                    editorU.getMediaType(),     // question Section Layout 'M', Also the media type
                    editorU.getMediaFiles(),    // String[] of media files
                    editorL.getText(),          // answerText
                    cardNum,                    // Not super important until later. answerNumber is a cards index in FlashList
                    editorL.getMediaType(),     // answer section layout
                    editorL.getMediaFiles(),    // String[] of media files
                    new ArrayList<Integer>()    // Arraylist of other correct answers
            );

            creatorList.add(currentCard);
            // create new card for next round.
            currentCard = new FlashCardMM();
        
            // Reset
            editorU.resetSection();
            editorL.resetSection();
            gT.reset();
        
            LOGGER.info("added to creatorList, size is: " + creatorList.size());
            LOGGER.info("\n\ncurrentCard {}", currentCard.toString() );

        }
    
    
    
        /* ------------------------------------------------------- **/
    
        /**
         * <p>Provides an int and response based on the int returned from
         * checkContent().</p>
         * <pre>
         *
         *     - A '0'  = Do nothing and continue.
         *     - A '-1' = Send user back 'return' to previous method.
         *     - A '1'  = Save and continue.
         *     - If the param is LESS than 3, it retuns a '0' and provides no error message;
         *     - Otherwise: If the param is even, it posts a 'MISSING_TESTTYPE' and returns a '-1'
         *     or '1' based on the users response.
         *     - If the testType is a singleSection card, and the param is equal to 3,
         *     it returns a '0'.
         *     - If the param is equal to 7, it is a complete double section card and it
         *     returns a '0'.
         *     - if the param is anything else it posts a MISSING_SAVE error message to
         *     the user and returns true;
         * </pre>
         * @param result
         * return Returns an int
         */
        private int statusResponse(int result) {

            LOGGER.debug("statusResponse(...) param result: <{}>", result);
            
            // There is no content but test testType may be set.
            if(result < 3) {
                return 0;
            }
            // if testType is not set and
            // there is data in the card.
            if(result % 2 != 1) {
                FMAlerts alerts = new FMAlerts();
                boolean stop = alerts.saveAlertAction(alerts.MISSING_TESTTYPE, "emojis/flash_mustache_75.png");
                if(!stop) {
                    return -1;
                }
                deleteQButtonAction(currentCard);
                return 0;
            }
            // Is a single section card and card is complete.
            if(getTestType().toString().contains("NoteTaker") && result == 3) {
                return 1;
            }
            // Card is double section and complete
            if(result == 7) {
                return 1;
            }
            //Card is not-complete error. Notify user
            FMAlerts alerts = new FMAlerts();
            boolean stop = alerts.saveAlertAction(FMAlerts.MISSING_SAVE, "emojis/flash_mustache_75.png");
            if(stop) {
                return -1;
            }
            return 1;
        }
    
        /* ------------------------------------------------------- **/
    
        /**
         * Checks if the upper editor, and the lower editor if exists,
         * either contains data, has no data, or the card is
         * incomplete. There are no error messages generated here.
         * Uses an integer and sets it's bit based on the data that
         * is set.
         * @param editorU
         * @param editorL
         * @return Returns 0 if the card contains no data. returns an even number
         * if the testType comboBox is not set, returns a 2 or 3 if editorU has
         * data and editorL does not, and 7 if everything has data and it is not a single card. and returns
         * 4 or 5 if editorL has data but editorU does not.
         */
        private int checkContent(SectionEditor editorU, SectionEditor editorL) {
           // LOGGER.debug("checkContent: is text empty? editorU: {}, editorL {} \nis there an image?: {} ", editorU.getText().isEmpty(), editorL.getText().isEmpty(), (editorL.getMediaFiles()[0] != null));
            int stat = 0;
            // Is there a test type selected?
            if (getTestType() != null) {
                //stat set bit 1
                stat |= (1); // = 1
            }
            // Is there text in the upper editor?
            if (! editorU.getText().isEmpty()) {
                //stat set bit 2
                stat |= (2);
            }
            else if (editorU.getMediaFiles()[0] != null && editorU.getMediaFiles()[1] != null) {
                //stat set bit 2
                stat |= (2);
            }
            // Is there data in the lower editor?
            if ( ! editorL.getText().isEmpty()) {
                //stat set bit 3
                stat |= (4);
            }
            else if (editorL.getMediaFiles()[0] != null && editorL.getMediaFiles()[1] != null) {
                //stat set bit 3
                stat |= (4);
            }
        
            LOGGER.info("checkContent() Returning: <{}>", stat);
        
            return stat;
        }
    }

    /* ------------------------------------------------------- **/

    /**
     * If this is the first session and the blank display
     * is not a card.
     */
    boolean isntStarted() {
        return currentCard == null;
    }


    /* ------------------------------------------------------- **/

    /**
     *  DESCRIPTION: Sets the creatorList from file. Meaning that an ArrayList is
     *  created from the binary file.
     */

    public void setCreatorList() {
        FlashCardOps fcOps = FlashCardOps.getInstance();
        creatorList = fcOps.cloneList(fcOps.getFlashList());
        startSize = creatorList.size();
        LOGGER.info("creatorList size: ", startSize);
    }

    /* ------------------------------------------------------- **/

    /**
     * Exits to menu and Saves the flashcard arraylist to the
     * binary file named/selected by the user at program start.
     * Also saves media and file to cloud. Saves users edits in the
     * shapesPane if open.
     */
    protected void saveDeckAction() {
        LOGGER.info(" saveDeckAction ");

        // Check content and save, or give user
        // an error message. Respond based on user
        // choice.
        CardSaver cs = new CardSaver();
        boolean ret = cs.saveCard(editorU, editorL);
        // If requested, then
        // then return user to screen.
        if(ret) {
            return;
        }
        //If there are changes. Save them.
        if (flashListChanged || !ret) {

            // Save the current card or not
            FlashCardMM card = creatorList.get(creatorList.size() - 1);
            if (card.getQText().isEmpty() || card.getQFiles().length > 0) {
                LOGGER.debug("calling saveButtonActionHelper. last cardQtext is empty. minus: -");
                saveDeckActionHelper('-');
            } else {
                // Save deck metadata to DB.
                LOGGER.debug("calling saveButtonActionHelper. minus: +");
                saveDeckActionHelper('+');
            }
            FlashMonkeyMain.setWindowToNav();

        } else {
            // There is no content in the upper and lower editors
            // There is nothing to save, Give the authcrypt.user
            // a message
            String emojiPath = "image/Flash_hmm_75.png";
            String message   = "There are no changes to save." +
                    "\n If you're finished, click quit. ";
            FxNotify.notificationBlue("Ouch!", message, Pos.CENTER, 5,
                    emojiPath, FlashMonkeyMain.getWindow());
        }
    }


    /* ------------------------------------------------------- **/


    /**
     * helper method to saveDeckAction
     * Saves the creator list to file.
     */
    private void saveDeckActionHelper(char minus) {

        LOGGER.info("saveButtonActionHelper char minus: <{}>, num cards <{}>", minus, creatorList.size());
        FlashCardOps fo = FlashCardOps.getInstance();
        fo.clearFlashList();
        fo.setFlashList(creatorList);
        creatorList.clear();
    }

    /* ------------------------------------------------------- **/


    /**
     * Sets the prompt based upon the length of the creatorList
     * @param cardNum
     */
    private String makeQPrompt(int cardNum) {
        int creatorListLength = creatorList.size();
        if(cardNotEnabledMsg != null) {
            String msg = cardNotEnabledMsg;
            cardNotEnabledMsg = null;
            return msg;
        }
        if(creatorListLength < 5)
        {
            return "Please create " + (-1 * (creatorListLength - 5)) + " more flash Cards ";
        }
        
        LOGGER.info("makeQPrompt, listIdx: <{}>", listIdx);
        return "Flash Card " + (listIdx + 1);
    }


    /* ------------------------------------------------------- **/



    /*************************************************************
                                INNER CLASS
                        TestMapper for dropDown menu
     *************************************************************/

    /**
     * This class provides the object
     * with the Name of the test that
     * appears in the dropDown button and the related
     * TestType (aka AnswerType).
     */
    public static class TestMapper
    {
        private String name;
        private GenericTestType testType;

        TestMapper(String s, GenericTestType type)
        {
            this.name = s;
            this.testType = type;
        }

        // **** GETTERS *****

        public String getName()
        {
            return this.name;
        }

        public GenericTestType getTestType()
        {
            return this.testType;
        }

        @Override
        public String toString()
        {
            return name;
        }

        static ArrayList<TestMapper> getTestList(GenericTestType[] tList) {
            ArrayList<TestMapper> tm = new ArrayList<>(15);
            for(int i = 0; i < tList.length; i++) {
                tm.add(new TestMapper(tList[i].getName(), tList[i].getTest()));
            }
            return tm;
        }
    }

    // **************** METHODS USED FOR UI TESTING ******************
/*
    @FMAnnotations.DoNotDeployMethod
    public Point2D getPrevBtnXY() {
        Bounds bounds = prevQButton.getLayoutBounds();
        return prevQButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);

    //    xxxxxx images are not working... we redid the media naming to use the new  cID / cHash.

    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getNextBtnXY() {
        Bounds bounds = nextQButton.getLayoutBounds();
        return nextQButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getNewCardBtnXY() {
        Bounds bounds = newCardButton.getLayoutBounds();
        return newCardButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getInsertCardBtnXY() {
        Bounds bounds = insertCardButton.getLayoutBounds();
        return insertCardButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getSaveDeckButtonXY() {
        Bounds bounds = saveDeckButton.getLayoutBounds();
        return saveDeckButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getUpperTextXY() {
        Bounds bounds = editorU.tCell.getTextArea().getLayoutBounds();
        return editorU.tCell.getTextArea().localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getLowerTextXY() {
        Bounds bounds = editorL.tCell.getTextArea().getLayoutBounds();
        return editorL.tCell.getTextArea().localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getSellButtonXY() {
        Bounds bounds = sellButton.getLayoutBounds();
        return sellButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public ArrayList<FlashCardMM> getCreatorList() {
        return this.creatorList;
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getComboBoxXY() {
        Bounds bounds = entryComboBox.getLayoutBounds();
        return entryComboBox.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public DeckMetaModel getMetaModelObj() {
        return metaPane.getModel();
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getFormMinXY() {
        Double x = metaWindow.getX();
        Double y = metaWindow.getY();
        Point2D d2 = new Point2D(x, y);
        return d2;
    }
 */
}
