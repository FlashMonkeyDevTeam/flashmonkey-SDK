package type.testtypes;

import flashmonkey.FMTransition;
import flashmonkey.ReadFlash;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;
import flashmonkey.FlashCardMM;
import type.sectiontype.GenericSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;


/**
 * Note on this class
 * 1. How to indicate to the EncryptedUser.EncryptedUser, and parser, that "this" is a blank without making it
 * difficult for the EncryptedUser, and the parser. AND without restricting the EncryptedUser's
 * format. For a text entry such as a computer science subject: what is the symbol? and where is
 * the symbol placed to indicate to the user and the parser that the following word or words are
 * used for a blank? And What is the symbol that is unique that a user would not enter!?!
 * For example, the EncryptedUser
 * enters a [____] as part of a format in computer science, then when the text entry
 * is parsed, we would mistake it as a fill in the blank.
 * This should not cause an error if the parser looked for it as a symbol to indicate
 * that this is a question for the fill in the blank answer.
 */
public class FillnTheBlank implements GenericTestType<FillnTheBlank>
{
    // The singleton instance of this class
    private static FillnTheBlank CLASS_INSTANCE;

    private Button answerButton;

    /**
     * Default No-Args Constructor
     */
    /***************************amirStart**********************/
    private ArrayList<String> textParts;
    private BitSet isAnswer;
    private String questionText;
    private String answerText;
    private int currentAnswerIndex = -1;

    // This method will be called whenever the FillInTheBlank object is updated with a new question.
    // We will assume that it is called in the getTEditorPane method after processing the user input.
    private void updateTexts() {
        StringBuilder questionBuilder = new StringBuilder();
        StringBuilder answerBuilder = new StringBuilder();

        for (int i = 0; i < textParts.size(); i++) {
            if (isAnswer.get(i)) {
                questionBuilder.append("[___]");
                answerBuilder.append(textParts.get(i));
            } else {
                questionBuilder.append(textParts.get(i));
            }

            // To add spaces between words, except at the end
            if (i < textParts.size() - 1) {
                questionBuilder.append(" ");
                answerBuilder.append(" ");
            }
        }

        questionText = questionBuilder.toString();
        answerText = answerBuilder.toString();
    }

    /************************amirEnd*****************************/
    public FillnTheBlank() {/* empty constructor */}

    @Override
    public boolean isDisabled() {
        return false;
    }

    /**
     * Returns an instance of the class. Use this method to
     * call the class. IE
     * MultiChoice multiChoiceObje = MultiChoice.getInstance();
     * @return
     */
    public static synchronized FillnTheBlank getInstance() {
        System.out.println("\n\n **** FillInTheBlank getInstance() called ****\n\n");
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new FillnTheBlank();
        }
        return CLASS_INSTANCE;
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
        // Instantiate vBox and "set spacing" !important!!!
        VBox vBox = new VBox(2);

        //gCard = new GenericCard();
        //Pane pane = gCard.cardFactory("", 'T', "", 'T', 'D',files);

        /**    TEST CARD CREATION, SHAPES, IMAGES, VIDEO, AUDIO   Uncomment cfpCenter.* below to test and comment out above (2x) cfpCenter.*  **/

        //editorU = new SectionEditor("Enter a question here", flashList.size(), 'U', gCard.getTextCell().getTextArea());
        //editorL = new SectionEditor("Enter the answer here", flashList.size(), 'L', gCard.getTextCell().getTextArea());
        /***************************amirStart*******************************************/
//        String userInput = q.getText();  // Assuming this method exists
//        String[] parts = userInput.split("\\[|\\]");
//
//        textParts = new ArrayList<>(Arrays.asList(parts));
//        isAnswer = new BitSet(textParts.size());
//
//        for (int i = 0; i < textParts.size(); i++) {
//            if (i % 2 != 0) {  // If i is odd, this part is an answer
//                isAnswer.set(i);
//            }
//        }
//
//        updateTexts();
        /****************************amirEnd**************************************/

        vBox.getChildren().addAll(q.sectionHBox);

        return vBox;
    }

    /**
     * Returns the read pane for the FillInTheBlank class.
     * @param cc
     * @param genCard
     * @return
     */
    @Override
    public Pane getTReadPane(FlashCardMM cc, GenericCard genCard, Pane parentPane)
    {
        // Local Variables
        GenericSection genSection = GenericSection.getInstance();
        HBox upperHBox;

        answerButton = new Button("Answer");


        upperHBox = genSection.sectionFactory(cc.getQText(), cc.getQType(), 1, true, 0, cc.getQFiles());
        ReadFlash.getInstance().setShowAnsNavBtns(false);

        // The answer btn action
        // Keeping action local. Makes using lamda easier.
        /*
        answerButton.setOnAction((ActionEvent e) ->
        {

        }
        */

        // Transition for Question, Right & end button click
        FMTransition.setQRight(FMTransition.transitionFmRight(upperHBox));

        return new Pane(upperHBox);

    }

    /**
     * Sets Bit 5 (=32) for (FITB) to true
     * All other bits set to 0
     * @return bitSet
     */
    @Override
    public int getTestType()
    {
        // 32
        return 0b0000000000100000;
    }

    @Override
    public char getCardLayout()
    {
        return 'S'; // Single pane card
    }

    @Override
    public GenericTestType getTest() {

        return new FillnTheBlank();
    }

    @Override
    public Button[] getAnsButtons() {
        return null;
    }

    @Override
    public Button getAnsButton() {
        return null;
    }

    @Override
    public String getName() {
        return "FITB";
    }

    @Override
    public void ansButtonAction() {
        /**************************amirStart*******************************/
        // Go through each part of the text
        for (int i = 0; i < textParts.size(); i++) {
            // If this part is an answer, print it
            if (isAnswer.get(i)) {
                System.out.println(textParts.get(i));  // Display the answer
            }
        }

        /**************************amirEnd*********************************/
        // stub
    }

    @Override
    public void nextAnsButtAction()
    {
        /**************************amirStart*********************************/
        // Find the index of the next answer in the list
        int nextAnswerIndex = isAnswer.nextSetBit(currentAnswerIndex + 1);

        // If there is a next answer, update currentAnswerIndex and show the answer
        if (nextAnswerIndex >= 0) {
            currentAnswerIndex = nextAnswerIndex;
            System.out.println(textParts.get(currentAnswerIndex));  // Display the answer
        } else {
            System.out.println("No more answers.");  // Or handle the end of answers in some other way
        }
        /****************************amirEnd***************************************/
        // stub
    }

    @Override
    public void prevAnsButtAction()
    {
        //stub
    }

    @Override
    public void reset() {
        // stub
    }
}
