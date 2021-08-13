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

package type.testtypes;


import flashmonkey.*;
import fmtree.FMWalker;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import multimedia.AnswerMM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;
import type.sectiontype.GenericSection;
import uicontrols.ButtoniKon;

import java.util.*;



/**
 * Multi-Choice test class
 *  - Singleton design pattern, Not Synchronized, Not thread safe
 *
 *  @author Lowell Stadelman
 */
public class MultiChoice implements GenericTestType<MultiChoice>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiChoice.class);
    // The singleton instance of this class
    private static MultiChoice CLASS_INSTANCE;

    private Test test;
    // The hash constant index used to
    // calculate a varying hashcode
    // depending pre test iteration.
    private int hashConstIDX;
    private GenericSection genSection;
    // contains the lower Section
    private HBox lowerHBox;
    // Buttons
    private Button nextAnsButton;
    private Button prevAnsButton;
    private Button selectAnsButton;
    private HBox   ansButtonBox;

    // testAnswer index, The array of 4 answers for this question.
    protected int taIndex = 0;



    /**
     * Default No-Args Constructor
     */
    private MultiChoice() {/* empty constructor */}

    /**
     * Returns an instance of the class. Use this method to
     * call the class. IE
     * MultiChoice multiChoiceObje = MultiChoice.getInstance();
     * @return
     */
    public static synchronized MultiChoice getInstance() {
        LOGGER.info(" **** MULTICHOICE getInstance() called ****");

        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new MultiChoice();
        }
        return CLASS_INSTANCE;
    }
    
    
    @Override
    public boolean isDisabled() {
        return false;
    }


    /**
     * Returns the editor panes
     * @param flashList
     * @param q
     * @param a
     * @return Returns a VBox containing the upper and lower editor panes
     */
    @Override
    public VBox getTEditorPane(ArrayList<FlashCardMM> flashList, SectionEditor q, SectionEditor a)
    {
        LOGGER.debug("\ncalled getTEditorPane in MultipleChoice :) ");

        // Instantiate vBox and "set spacing" !important!!!
        VBox vBox = new VBox(2);
        vBox.getChildren().addAll(q.sectionHBox, a.sectionHBox);
        return vBox;
    }

    /**
     * GetTReadPane:  Provides the pane for the Multi-Choice test
     * post-condition Sets the centerPane to two sections, the top section is for the
     * question, and the bottom section is for the answers. The first answer
     * drops down from the top and the successive answers come in from the right
     * or left depending on which answer button is clicked. The select button
     * selects the current answer that is in view and will compare it with the
     * correct answer for that question. It scores the answer and adds or
     * subtracts depending on if it is right or wrong.
     */
    @Override
    public GridPane getTReadPane(FlashCardMM currentCard, GenericCard genCard, Pane parentPane)
    {
        LOGGER.debug("\n\n*** GET-T-READ-PANE in multi-choice testcard ****");

        // Each section is it's own object. Not using GenericCard
        genSection = GenericSection.getInstance();

        GridPane gPane = new GridPane();
        gPane.setVgap(2);

        HBox upperHBox;
        StackPane stackP = new StackPane();

        // The test answer index
        taIndex = 0;
        // InnerClass object "Test"
        test = new Test(currentCard, FlashCardOps.getInstance().getFlashList(), currentCard.getANumber());

        // Buttons//
        if(nextAnsButton == null) {
            nextAnsButton = ButtoniKon.getAnsNext();
        }
        nextAnsButton.setDisable(false);
        nextAnsButton.setOnAction(e -> nextAnsButtAction());
        
        if(prevAnsButton == null) {
            prevAnsButton = ButtoniKon.getAnsPrev();
        }
        prevAnsButton.setOnAction(e -> prevAnsButtAction());
        prevAnsButton.setDisable(true);
        
        if(selectAnsButton == null) {
            selectAnsButton = ButtoniKon.getAnsSelect();
        } else {
            selectAnsButton = ButtoniKon.getJustAns(selectAnsButton, "Select");
        }
        selectAnsButton.setOnAction(e -> ansButtonAction());

        // Set keyActions for the answerButtons. MasterPaneFrom ReadFlash
        ReadFlash.getInstance().setShowAnsNavBtns(true);

        // add the question to vBox
        upperHBox = genSection.sectionFactory(currentCard.getQText(), currentCard.getQType(), 2, true, 0, currentCard.getQFiles());

        // add the answer to the a StackPane, add the answer button, and add the stackPane to the vBox.
        AnswerMM firstAns = test.ansAry[0];
        lowerHBox = genSection.sectionFactory(firstAns.getAText(), firstAns.getAType(), 2, true, 0, firstAns.getAFiles());
        
        // Position the selectAnsButton in the bottom center of
        // the lowerHBox
        stackP.setAlignment(Pos.BOTTOM_CENTER);
    
        ansButtonBox = new HBox();
        ansButtonBox.setPadding(new Insets(2, 0, 4, 0));
        ansButtonBox.getChildren().addAll(prevAnsButton, selectAnsButton, nextAnsButton);
        
        ansButtonBox.setMaxHeight(32);
        ansButtonBox.setId("navButtonBox");

        ansButtonBox.setAlignment(Pos.BOTTOM_CENTER);
        stackP.getChildren().add(lowerHBox);
        stackP.getChildren().add(ansButtonBox);
        
        gPane.addRow(2, stackP);
        gPane.addRow(1, upperHBox);

        // Transition for Question, Right & end button click
        FMTransition.setQRight(FMTransition.transitionFmRight(upperHBox));
        // Transition for Question, left & start button click
        FMTransition.setQLeft(FMTransition.transitionFmLeft(upperHBox));
        // Transition for Answer for all navigation
        FMTransition.setAWaitTop(FMTransition.waitTransFmTop(lowerHBox, 0, 300, 350));

        return gPane;
    }

    @Override
    public void reset() {
        // not used
    }

    /**
     * Sets bit 1 for Multi-Choice to true
     * All other bits set to 0
     * @return bitSet
     */
    @Override
    public int getTestType()
    {
        LOGGER.debug("\ncalled getTestType in MultiChoice");
        // could just use 2 but this is more visual
        // 32770, 16th bit used to mark if test type qualifeid
        // for this multi-choice testing
        return 0b1000000000000010;
    }

    /**
     * Refers to the card layout if horizontal, vertical
     * and single or double cell.
     * @return
     */
    @Override
    public char getCardLayout()
    {
        return 'D'; // double horizontal
    }

    @Override
    public GenericTestType getTest() {
        return new MultiChoice();
    }

    @Override
    public Button[] getAnsButtons() {
        return new Button[] { this.prevAnsButton, this.nextAnsButton };
    }

    @Override
    public Button getAnsButton() {
        return this.selectAnsButton;
    }

    @Override
    public String getName() {
        return "Multi-Choice";
    }

    @Override
    public void ansButtonAction()
    {
        LOGGER.debug("\n~*~*~ Answer ButtAction called ~*~*~");

        final FlashCardMM currentCard = (FlashCardMM) FMWalker.getInstance().getCurrentNode().getData();
        final FlashCardOps fo = FlashCardOps.getInstance();
        final ReadFlash rf = ReadFlash.getInstance();

        // card from the arrayList - Used to update data in the ArrayList Card
        FlashCardMM listCard = fo.getFlashList().get(currentCard.getANumber());



        /** if the referance to the currentCard.answer == the
         * users choice, question was answered correctly */
        if (test.getTheseAns()[taIndex] == currentCard.getAnswerMM()) {
//            rf.new RightAns(currentCard, listCard, this);
        } else {
//            rf.new WrongAns(currentCard, listCard, this);
        }
    }

    /**
     * The next Answer Button Action
     * mode.
     */
    @Override
    public void nextAnsButtAction()
    {
        LOGGER.debug("\n~*~*~ nextAnsButtAction called in MultiChoice ~*~*~");

        taIndex++;
        LOGGER.debug("taIndex: " + taIndex);

        if(taIndex > 2)
        {
            nextAnsButton.setDisable(true);
            // keep index at 3 dispite repetitive
            // nextButton clicks
            taIndex = 3;
        }
        else
        {
            if(prevAnsButton.isDisabled())
            {
                prevAnsButton.setDisable(false);
            }
        }

        LOGGER.debug("\ttaIndex = " + taIndex);

        // Get the next answer, taIndex is already incremented
        String aTxt = test.ansAry[taIndex].getAText();
        char cellType = test.ansAry[taIndex].getAType();// getMediaType();
        String[] files = test.ansAry[taIndex].getAFiles();

        // Set the next answer in the lowerHBox
        lowerHBox.getChildren().clear();
        HBox answerHBox = genSection.sectionFactory(aTxt, cellType, 2, true, 0, files);
        answerHBox.setPadding(new Insets(0));
        lowerHBox.getChildren().add( answerHBox);

        TranslateTransition aRight = FMTransition.transitionFmRight(lowerHBox);
        aRight.play();

        if( ! selectAnsButton.isDisabled()) {
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(selectAnsButton, 1, 750, false);
            FMTransition.nodeFadeIn.play();
        }
        prevAnsButton.setDisable(false);
    }

    /**
     * Previous Answer button action
     */
    @Override
    public void prevAnsButtAction()
    {

        LOGGER.debug("\n~*~*~ prevAnsButtAction called ~*~*~");

        //selectAnsButton.setVisible(true);
        taIndex--;
        if(taIndex < 1)
        {
            prevAnsButton.setDisable(true);
            taIndex = 0;
        }
        else
        {
            if(nextAnsButton.isDisabled())
            {
                nextAnsButton.setDisable(false);
            }
        }

        String aTxt = test.ansAry[taIndex].getAText();
        char cellType = test.ansAry[taIndex].getAType();// getMediaType();
        String[] files = test.ansAry[taIndex].getAFiles();

        lowerHBox.getChildren().clear();
        HBox answerHBox = genSection.sectionFactory(aTxt, cellType, 2, true, 0, files);
        answerHBox.setPadding(new Insets(0));
        lowerHBox.getChildren().add( answerHBox);

        // Transitions
        TranslateTransition aLeft = FMTransition.transitionFmLeft(lowerHBox);
        aLeft.play();


        if( ! selectAnsButton.isDisabled())
        {
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(selectAnsButton, 1,750, false);
            FMTransition.nodeFadeIn.play();
        }
    }

    /**
     * INNER CLASS:
     * CLASS DESCRIPTION: This is the Test class and inherits FlashCard. It provides
     * the logic for Multi-Choice Test.
     *
     * In the EncryptedUser.EncryptedUser strategy given in FlashMonkeyMain.

     * When the EncryptedUser.EncryptedUser builds their own cards with most systems in order to create a
     * test, the EncryptedUser.EncryptedUser would have to create three wrong answers which would multiply
     * the work of creating answers by 3. THe FlashMonkey Strategy is to use answers
     * that the EncryptedUser.EncryptedUser has already created from other answers in that section or test.
     * Additionally the EncryptedUser.EncryptedUser, when in study mode may review the answer they provided
     * and since that answer is a correct answer to another question, it may be beneficial
     * for the EncryptedUser.EncryptedUser to see the information that the answer is related to.
     * The Test() class provides/builds a random array of answers as well as inserting
     * the correct answer into the array for selection. Selection, and display, and
     * wrong or right answer actions is done in the upstream class or EncryptedUser.EncryptedUser class.
     *
     * Requirments/Algorithm:
     * Problem: Qualified questions are Q and A related and not all test types are Q and A types. A search for
     *      Q and A types must be conducted first. Then, as answers are selected for use in multi - choice, they
     *      cannot be repeated in other answers. Additionally, they cannot overwrite the correct answer.
     *
     *  1. Ensure that answers are not repeated
     *      Use a Set
     *
     *      a. build a HashSet of qualified answers
     *      b. Build a HashSet of answers from the qualified answers:
     *          -- First add the correct answer
     *          -- Ensure the HashSet has 4 answers total from the qualified list
     *              If the correct answer is added to the 4 answers, Set will not accept it
     *              and another will be needed until the Set reaches 4.
     *
     *      c. Make the referance to the correct answer available.
     *
     */
    class Test //extends FlashCardOps
    {
        private static final long serialVersionUID = FlashMonkeyMain.VERSION;

        //*** VARIABLES ***
        protected Timer time;
        protected AnswerMM[] ansAry;
        /** The integer for this objects hash value computation **/
        private int num;

        /**
         * Default constructor
         */
        public Test() { /* empty */ }


        /**
         * Full constructor.
         * pre-condition Tests if the flashList has more than 3
         * elements.
         * @param cc currentCard
         * @param flashList
         * @param num The number 0 through 9 used to compute this objects hashcode ie the n'th iteration
         *            of the loop. Assists with providing more random wrong answers
         *            for each test.
         */
        public Test(FlashCardMM cc, ArrayList<FlashCardMM> flashList, int num)
        {
            LOGGER.debug("\n\n !!! ------ NEW TEST ----- !!! \n\n");
            this.num = num;
            // if flashList is greater than 4
            if(flashList.size() > 3) {
                //time = Timer.getClassInstance();
                // build the arrayOfAnswers
                ansAry = buildAnswerAry(cc.getAnswerMM(), getQualifiedSet(flashList));
            } else {
                ReadFlash.getInstance().emptyListAction();
            }
            if( (hashConstIDX += 1) > 4) { hashConstIDX = 0; }
        }

        /**
         * Description: Holds the question index's that contain another
         * correct answers to this question. I.E. if more than one answer is
         * "True". Then the other questions that contain the same answer
         * would also be a correct answers.
         * @return Returns an int array. The indexs of correct answers to this
         * question
         */
        public AnswerMM[] getTheseAns()
        {
            return this.ansAry;
        }


        /**
         * Returns an arraylist of 4 answerMM's that are qualified for multi-choice tests.
         * checking against the referances which are "supposedly" stored in the stack
         * post-condition Expects that the 0th bit in a Test Types int making it odd. If not
         * then the Test Type does not qualify for this list.
         * @param flashList
         * @return
         */
        private HashSet<AnswerMM> getQualifiedSet(ArrayList<FlashCardMM> flashList) {

            //if( (hashConstIDX += 1) > 4) { hashConstIDX = 0; }
            // create hashset
            HashSet<AnswerMM> qualified = new HashSet<>(20);
            int odd;

            // loop through the flashList and if the int (testType) is larger
            // than 32768 then it is qualified for the list.
            for(FlashCardMM card : flashList) {


                // if larger than 2^16, 16th bit is set
                // and it is qualified.
                if(getTestType() > 32768 ) {
                    qualified.add(card.getAnswerMM());
                    LOGGER.debug("Qualified added " + card.getAnswerMM());
                }
            }

            if(qualified.size() < 4) {
                ReadFlash.getInstance().emptyListAction();
            }

            LOGGER.debug("qualified size: " + qualified.size());

            // remove the correct answer from the set
            // qualified.remove(rightAns);
            return qualified;
        }


        /**
         * Description:  Creates a set of random answers
         * including the correctAnswer. Randomly
         * selects other answers from flashList.
         * post-condition Builds an array of random ANumbers from flashList,
         * and the currentCards ANumber.
         * @param correctAnswer
         * @param qualifiedSet Expects a randomly selected Set of answers that are
         *                      compatable with Multi-Choice tests. IE FlashCards and not
         *                      true or false questions. And possibly created using HashList.
         * @return Returns an arraylist of AnswerMM of any qualified answers for Multi-Choice
         * test and selected randomly.
         */
        private AnswerMM[] buildAnswerAry(final AnswerMM correctAnswer, Set<AnswerMM> qualifiedSet) {

            LOGGER.debug("\n\n *** in buildAnswerAry *** \n");

            // Create a set of all qualified answers and give them a
            // unique hash-number to this iteration/Object of Test
            HashSet<HashObj> ansHashSet = new HashSet<>(1);
            HashObj ho = new HashObj(correctAnswer);
            ansHashSet.add(ho);

            LOGGER.debug("correctAnswer [ " + ho.answerMM + " ] and hashCode [ " + ho.hashCode() + " ]");

            Iterator it = qualifiedSet.iterator();

            int i = 0;
            // Build the answer set by adding qualified
            // 3 more answers or until set reaches 4
            try {
                while (ansHashSet.size() < 4 && i < 4) {
                    AnswerMM answerMM = (AnswerMM) it.next();
                    LOGGER.debug("\tPrinting in while loop " + answerMM);
                    ansHashSet.add(new HashObj(answerMM));
                    i++;
                }
            } catch (NoSuchElementException e) {
                    if(ansHashSet.size() < 4) {
                        // display a popup
                        Platform.runLater(() -> {
                            String message = "  Oweeee!!!  I didn't expect that." +
                                    "\n  There are not enough Multi-Choice questions for me to work.  " +
                                    "\n  Please create more Multi-Choice questions.";
                            String emojiPath = "emojis/flashFaces_sunglasses_60.png";
                            int w = (int) FlashMonkeyMain.getWindow().getWidth();
                            int h = (int) FlashMonkeyMain.getWindow().getHeight();
                            double x = FlashMonkeyMain.getWindow().getX() + (w / 8);
                            double y = FlashMonkeyMain.getWindow().getY() + (h / 2);
                            FMPopUpMessage.setPopup(
                                    x,
                                    y,
                                    w,
                                    message,
                                    emojiPath,
                                    FlashMonkeyMain.getWindow(),
                                    3000
                            );
                        });

                        // Set the answer box to the correct answer
                        AnswerMM ansMM = new AnswerMM();
                        ansMM.setAText(correctAnswer.getAText());
                        AnswerMM[] ansAry = new AnswerMM[1];
                        ansAry[0] = ansMM;
                        // return the answer array and end this method.
                        return ansAry;
                    }
                }
            // In case there are not enough multi-choice cards and the error
            // was not caught previously.

            LOGGER.debug("\nfinished while loop\n\nprinting ansHSet");
            //** PRINT FOR TEST **/
            for(HashObj a : ansHashSet) {
                LOGGER.debug("text: " + a.answerMM + "   hashCode: " + a.hashCode());
            }


            LOGGER.debug("\n\t ** now adding to resultList **\n");
            AnswerMM[] resultList = new AnswerMM[4];
            int j = 0;
            for(HashObj ansHObj : ansHashSet) {
                //HashSet may have null elements, remove them
                if(ansHObj != null && j < 4) {
                    resultList[j++] = ansHObj.answerMM;

                }
            }
            TestForRepeats repeatTester = new TestForRepeats();
            if( repeatTester.testAnsForRepeats(resultList) ) {
                System.err.println(" REPEATED TEXT FOUND ");
 //@TODO remove system.exit if repeats occur in multichoice test.
                //System.exit(0);
            }
            return resultList;
        }

        /**
         *InnerClass to create a new HashCode per Test iteration.
         */
        private class HashObj extends HashSet{

            private AnswerMM answerMM;
            private int hash; // Default to 0

            public HashObj(AnswerMM a) {
                this.answerMM = a;
            }

            // a consistent means to hash.
            @Override
            public int hashCode() {
                hash = FMHashCode.getHashCode(answerMM, hashConstIDX);
                return hash;
            }
        }

        /*** INNER INNER CLASS for testing REMOVE before deployement ***/
        // @todo Remove TestForRepeats inner class from MultiChoice Test.

        public class TestForRepeats
        {
            private static final long serialVersionUID = FlashMonkeyMain.VERSION;

            boolean fcRepeatTrue;
            AnswerMM[] aObjects;

            public TestForRepeats () {
                fcRepeatTrue = true;
                aObjects = new AnswerMM[4];
            }

            /*
             * Tests that the parameter does not have a duplicate within it's array
             * @param aObjs The array of AnswerObj's
             * @return True if a duplicate is found. False otherwise.
             */

            protected boolean testAnsForRepeats(AnswerMM[] aObjs) {

                for (int j = 0; j < 4; j++) {

                    String thisAnsIdxAns = aObjs[j].getAText();

                    if(j != 0 && testEquals(thisAnsIdxAns, aObjs[0].getAText()) ) {
                        return true;
                    }
                    if(j != 1 && testEquals(thisAnsIdxAns, aObjs[1].getAText()) ) {
                        return true;
                    }
                    if(j != 2 && testEquals(thisAnsIdxAns, aObjs[2].getAText()) ) {
                        return true;
                    }
                    if(j != 3 && testEquals(thisAnsIdxAns, aObjs[3].getAText()) ) {
                        return true;
                    }
                }
                return false;
            }

            protected boolean testIdxForRepeats(int[] idxAry) {

                LOGGER.debug(" --###-- in testIdxForRepeats --###--");
                //LOGGER.debug( "\n\nthisAnsIdx ansSet: " + idxAry[0] + ", " + idxAry[1] + ", " + idxAry[2] + ", " + idxAry[3] + "\n\n");

                for (int i = 0; i < 4; i++) {

                    int thisAnsIdx = idxAry[i];

                    if (i != 0 && testEquals(thisAnsIdx, idxAry[0]) ) {
                        return true;
                    }
                    if (i != 1 && testEquals(thisAnsIdx, idxAry[1]) ) {
                        return true;
                    }
                    if (i != 2 && testEquals(thisAnsIdx, idxAry[2]) ) {
                        return true;
                    }
                    if (i != 3 && testEquals(thisAnsIdx, idxAry[3]) ) {
                        return true;
                    }
                }
                return false;
            }

            private boolean testEquals(Object t, Object o) {
                return t.equals(o);
            }

        }
    }

    public void onClose() {
        this.test = null;
        this.lowerHBox = null;
        this.nextAnsButton = null;
        this.prevAnsButton = null;
        this.selectAnsButton = null;
        this.taIndex = 0;
    }
}
