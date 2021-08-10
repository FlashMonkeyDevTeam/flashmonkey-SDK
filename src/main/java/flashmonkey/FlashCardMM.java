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


import common.FMFields;
import multimedia.AnswerMM;
import multimedia.QuestionMM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * @copyright Lowell Stadelman, all rights reserved, 2018/08/04
 * Creation date: 2018/08/04
 * @author lowell Stadelman
 */

public class FlashCardMM<T extends Comparable<T>> implements Serializable, Comparable<T>, Cloneable
{
    private static final long serialVersionUID = FlashMonkeyMain.VERSION;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FlashCardMM.class);

    //*** CONSTANTS / DEFAULTS***
    public static final String C_ID         = "0"; // Original card number. Never changes
    public static final int C_NUMBER        = 007; // Card number this session
    public static final char CARD_LAYOUT    = 'F'; // FlashCard, Top and Bottom
    public static final int IS_RIGHT        = 0; // was it answered correctly this session
    public static final int REMEMBER        = 0; // The prioirty of this card is set based on this number.
    public static final int SECONDS         = 0; // seconds to answer this session.
    public static final int NUM_RIGHT       = 0; // total number times right in it's existance
    public static final ZonedDateTime RT_DATE = ZonedDateTime.parse("2017-09-25T10:00:00-07:00[America/Los_Angeles]");
    public static final int NUM_SEEN        = 0; // total number of times it's been seen in it's exitstance
    // Use a BitSet as a boolean array to indicate the tests
    // that are possible for this question.
    // Default is multiple choice, multiple answer
    // write it in, and true or false.
    public static final int DEF_TEST_TYPE = 0b0000000000011111;


    // *** CARD VARIABLES ***
    /**
     *  A cards original card ID number, does not change during the life of a card. Used in associating
     * fileNames with a card. Formerly the qID or questionID.
     * !!! cID is set to the hash of the userName_deckName_&_time
     */
    private String cID;
    /** The cardNumber used as a priority as the flashcard is set in the tree, Formerly the qNum or question number */
    private int cNumber;
    /** The card layout type **/
    private char cardLayout;
    /** the question */
    private QuestionMM questionMM;  // contains its section layout type
    /** the answer */
    private AnswerMM answerMM; // contains its section layout type
    /** did the EncryptedUser.EncryptedUser get the question correct on this iteration -1 if wrong, 0 if unanswered, 1 if correct*/
    private int isRight;
    /** Priority/remember, Used to set this card towards the front, back, or hide from the tree. */
    private int remember;
    /** tracks how long it takes to answer this question */
    private int seconds;
    /** The number of times this question has been answered correctly */
    private int numRight;
    /** Date last answered correctly */
    private ZonedDateTime rtDate;
    /** The total number of times a question has been seen. */
    private int numSeen;
    /** The arrayOfFiles that contains multi-media and shapes for both question and answer */
    //private String[][] aryOfFiles;
    /** The history data structure that tracks EncryptedUser.EncryptedUser performance of this flashcard with different test types */
    // private TBD
    /** BitArray used for testTypes */
    private int testType;

    // *** CONSTRUCTORS *** /

    /**
     * Default constructor
     * Uses the default cHash, which may or may not be a new cID
     */
    public FlashCardMM()
    {
        this.answerMM = new AnswerMM();
        this.testType   = DEF_TEST_TYPE;  // Default Bit Array
        this.questionMM = new QuestionMM();
        //aryOfFiles = new String[2][];
        this.cardLayout = CARD_LAYOUT;
        // Card id. Never changes after it's been created
        this.cID        = createCardHash(FMFields.USER_NAME.get() + FMFields.DECK_NAME_ONLY) + System.currentTimeMillis();
        this.cNumber    = C_NUMBER;     // Card number former qNumber, it's priority in the tree
        this.remember   = REMEMBER;     // priority
        this.isRight    = IS_RIGHT;
        this.numRight   = NUM_RIGHT;    // number of times answered correctly
        this.seconds    = SECONDS;      // time it took to answer this quesiton
        this.rtDate     = RT_DATE;      // last time answered correctly
        this.numSeen    = NUM_SEEN;     // number of times seen
    
        LOGGER.info("\n\n*** Noargs constructor, cID: {} ***\n\n", cID);
    }

    /**
     * Create card constructor, creates a new FlashCardMM. Called only once in the life of a card.
     * only called by VanillaTextQA card-automator.
     *   for a new card, only need to provide the qNum, aNum, questionTxt, questionType, mmQfileName,
     *   answerTxt, answerType, answerMMFileName, Array of like answers, and card layout. All
     *   other varaibles are the defaults.
     * @param deckName Used to create a new cID/cHash. The current deck's name.
     * @param cNum  A changing number used to compare the card, sets its order in the tree
     * @param testType An int used as a boolean array indicating the tests for this session. Default is
     *        multiple choice, multiple answer, write it in, and true or false.
     * @param aNum  The answer number, used to retrieve an answer from flashList. This is the index of the flashList
     *              that is set during FlashCardOps.buildTree().
     * @param qTxt  The question text
     * @param qType   The type section the question will use
     * @param qFileNames The files associated with the question. IE for an image, sound or video, and shapes
     * @param aTxt  The answer text
     * @param aType  The type section the answer will use
     * @param aFileNames The files associated with the answer. IE for an image, sound, or video, and shapes
     * @param ansSt The set of answers that would also be correct for this question
     * @param layout    The layout for this card. IE full or half sections
     */
    public FlashCardMM(final String deckName, int cNum, int testType, char layout, String qTxt, char qType, String[] qFileNames, String aTxt, int aNum,
                        char aType, String[] aFileNames, ArrayList<Integer> ansSt)
    {
        this.answerMM = new AnswerMM(aTxt, aNum, aType, ansSt, aFileNames); // AnswerMM
        this.questionMM = new QuestionMM(qTxt, qType, qFileNames);
        this.testType   = testType;
        this.cardLayout = layout;
        this.cID        = createCardHash(FMFields.DECK_NAME_ONLY.get()); // may be created previously in the default constructor
        this.cNumber    = cNum;
        this.remember   = REMEMBER;
        this.isRight    = IS_RIGHT;
        this.numRight   = NUM_RIGHT;
        this.seconds    = SECONDS;
        this.rtDate     = RT_DATE;
        this.numSeen    = NUM_SEEN;
    
        LOGGER.info("\n\n ~~~ FlashCard full constructor ~~~\n \tcID: {}", cID);
    }


    /**
     * Full constructor, Used when editing a flashCard.
     * @param cID should not change from previous card. Used to associate files with this card. IE Images
     * @param cNum  A changing number used to compare the question, sets its order in the tree
     * @param testType  An int used as a boolean array indicating the tests for this session. Default is
     *        multiple choice, multiple answer, write it in, and true or false.
     * @param layout    The card layout (Should not change, copied from previous card.)
     * @param qTxt  The question text
     * @param qType   The type section the question will use
     * @param qFileNames The files associated with the question. IE for an image, sound or video
     * @param aTxt  The answer text
     * @param aNum  The answer number, used to retrieve an answer from flashList
     * @param aType  The type section the answer will use
     * @param aFileNames The files associated with the answer. IE for an image, sound, or video
     * @param ansSt The set of answers that would also be correct for this question
     * @param remember  Used when a question is answered, determins a cards priority (Should not change)
     * @param isRight   If this card was answered correctly
     * @param numRight  The number of times this card has been answerd correctly
     * @param sec   The amount of time it takes to answer the question
     * @param date  The date this question was last answered correctly
     * @param numSeen   The number of times this card has been seen.
     */
    public FlashCardMM(final String cID, int cNum, int testType, char layout, String qTxt, char qType, String[] qFileNames, String aTxt, int aNum,
                       char aType, String[] aFileNames, ArrayList<Integer> ansSt, int remember, int isRight, int numRight,
                       int sec, ZonedDateTime date, int numSeen)
    {
        this.answerMM = new AnswerMM(aTxt, aNum, aType, ansSt, aFileNames); // AnswerMM
        this.questionMM = new QuestionMM(qTxt, qType, qFileNames);
        this.testType   = testType;
        this.cardLayout = layout;
        this.cID        = cID;
        this.cNumber    = cNum;
        this.remember   = remember;
        this.isRight    = isRight;
        this.numRight   = numRight;
        this.seconds    = sec;
        this.rtDate     = date;
        this.numSeen = numSeen;

        LOGGER.info("\n\n ~~~ FlashCard full constructor for editing card~~~\n \tcID: {}", cID);
    }



    /**
     * FlashCard shallow copy constructor
     */
    public FlashCardMM(FlashCardMM original) {
        if(original == null) {//can't copy "nothing"! avoids crashes
            System.err.println("ERROR: FlashCard Class Copy Constructor given NULL. "
                    + "Exiting.");
            System.exit(0);
        }
        else {
            this.cID = original.cID;
            this.cardLayout = original.cardLayout;
            this.answerMM   = original.answerMM; // may cause an error
            this.testType   = original.testType;
            this.questionMM = original.questionMM;
            this.cNumber    = original.cNumber;
            this.remember   = original.remember;
            this.numRight   = original.numRight;
            this.numSeen    = original.numSeen;
            this.rtDate     = original.rtDate;
            this.isRight    = original.isRight;
            this.seconds    = original.seconds;
        }
    }

    /**
     * Clone constructor .. Creates a new FlashCardMM
     * from the original in the parameter.
     * Creates a deep copy of the FlashCardMM object.
     */
    @Override
    public FlashCardMM clone() {
        LOGGER.debug("called clone()");

        if(this.answerMM == null && this.questionMM == null) {//can't copy "nothing"! avoids crashes
            LOGGER.error("ERROR: FlashCard Class Copy Constructor given NULL. "
                    + "Exiting.");
            //System.exit(0);
            return new FlashCardMM(); // keep the compiler happy :)
        }
        else {
            return new FlashCardMM(
                    this.cID,
                    this.cNumber,
                    this.testType,
                    this.cardLayout,
                    this.questionMM.getQText(),
                    this.questionMM.getQType(),
                    this.getQFiles(),
                    this.answerMM.getAText(),
                    this.answerMM.getANumber(),
                    this.answerMM.getAType(),
                    this.getAFiles(),
                    this.answerMM.getAnswerSet(),
                    this.remember,
                    this.isRight,
                    this.numRight,
                    this.seconds,
                    this.rtDate,
                    this.numSeen);
        }
    }


    // ************************************** ******* **************************************
    // ************************************** SETTERS **************************************
    // ************************************** ******* **************************************
    
    /**
     * Sets the following values, the remainder retain the values of this card.
     * @param cNum
     * @param testType
     * @param layout
     * @param qTxt
     * @param qType
     * @param qFileNames
     * @param aTxt
     * @param aNum
     * @param aType
     * @param aFileNames
     * @param ansSt
     */
    public void setAll(int cNum, int testType, char layout, String qTxt,
                       char qType, String[] qFileNames, String aTxt, int aNum,
                       char aType, String[] aFileNames, ArrayList<Integer> ansSt)
    {
        this.answerMM = new AnswerMM(aTxt, aNum, aType, ansSt, aFileNames); // AnswerMM
        this.questionMM = new QuestionMM(qTxt, qType, qFileNames);
        this.testType   = testType;
        this.cardLayout = layout;
        this.cNumber    = cNum;
        
       //System.out.println("\n\n in setAll and cHash: " + this.cID + "\n\n");
    }


    protected void setQText(String q) {
        this.questionMM.setQText(q);
    }

    protected void setAText(String ans) {
        this.answerMM.setAText(ans);
    }

    protected void setQType(char qType) {
        this.questionMM.setQType(qType);
    }

    protected void setAType(char aType) {
        this.answerMM.setAType(aType);
    }

    protected void setQFiles(String[] files) {
        this.questionMM.setQFiles(files);
    }

    protected void setAFiles(String[] files) {
        this.answerMM.setAFiles(files);
    }

    protected void setCardLayout(char layout) {
        this.cardLayout = layout;
    }

    /**
     * Sets the answer object to the answer in the parameter
     * @param ansO
     */
    protected void setAnswerMM(AnswerMM ansO) { this.answerMM = ansO; }

    /**
     * Sets the BitSet or array of bits to the BitSet in the parameter
     * The TestBSet sets the possible tests that the EncryptedUser.EncryptedUser may see. If left
     * to default, AI(future) will select the tests set to true based on
     * the history-matrix. Currently only tests 1 - 4 may be used with AI.
     * In order starting from least significant bit as 0
     * 0 = reserved for later but used by ai (may not be neccessary)
     * 1 = Multiple choice
     * 2 = Multiple answer
     * 3 = q & a
     * 4 = True or false
     * 5 = FITB fill in the blank
     * 6 = Turn in video
     * 7 = Turn in audio
     * 8 = write it in
     * 9 = Draw
     * 10 = Draw over image
     * 11 = Math card
     * 12 = Graph card
     * 13 = Note Taker
     * ai = bits little endian 0,1,2,3,4
     */
    protected void setTestType(int testType) { this.testType = testType; }

    /**
     * Sets the 2d array of the file names for multi media in the question, and
     * the answer. Order is:
     *          Row 1) Question: Multi-media file name, fileOfShapes name
     *          Row 2) Answer:   Multi-media file name, fileOfShapes name
     * All rows are optional. Use null if not used. The first column must have
     * an item. Cannot be blank. If using shapes only then fileOfShapes goes in place
     * of the multi-media file name.
     * @return Returns a 2d array of files for the question and answer
     */
    /*
    public void setAryOfFiles(String[] qFiles, String[] aFiles)
    {
        aryOfFiles = new String[][]{
                {qFiles[0], qFiles[1]}, // row 1
                {aFiles[0], aFiles[1]}  // row 2
        };
    }
    */



    /**
     * Sets the number of times this flashCard has been seen.
     * @param num
     */
    public void setNumSeen(int num)
    {
        this.numSeen = num;
    }


    /**
     * setIsRight sets the int value for the users
     * response to this session. Upstream method
     *  / EncryptedUser.EncryptedUser method should be set to 0 unanswered, -1 wrong, 1
     *  correct.
     * @param correct
     */
    public void setIsRight(int correct)
    {
        this.isRight = correct;
    }

    /**
     * Sets the number of times this question has been answered correctly
     * @param num
     */
    public void setNumRight(int num)
    {
        this.numRight = num;
    }

    /**
     * Sets the question object to the question in the parameter
     * @param qO
     */
    protected void setQuestion(QuestionMM qO) { this.questionMM = qO; }

    /**
     * Sets the qNumber to the parameter
     * @param qNum
     */
    protected void setCNumber(int qNum) { this.cNumber = qNum; }

    public void setANumber(int aNum) {
        if(this.answerMM == null) {System.exit(1);}
        this.answerMM.setANumber(aNum); }

    /**
     * setRemember()  sets the priority that a flashcard is seen.
     * Setting is done either by the rdo button in editFlashPane
     * or when a EncryptedUser.EncryptedUser misses a question in the test.
     * Convention/Rule:
     * forget (hides this question by setting the priority to a value
     * higher than a threshold. Normal will not change the listing of the
     * flashCard from the original index/qNumber, and shortList will set
     * the flashCard to a negitive number if done by the EncryptedUser.EncryptedUser. If done
     * by FlashMonkey ReadFlash it will bump the priority down by a percentage
     * until it is negitive.
     * Less than threshold for forget, 0 for normal, greater than 0 for shortList seen first
     */
    public void setRemember(int rem)
    {
        //System.out.println("setRemember called. Remember: " + rem);
        this.remember = rem;
    }

    /**
     * Sets the date to the date the question was answered correctly. Or when
     * <p><i>Note</i> This should be set after the EncryptedUser.EncryptedUser is done with the question.
     * The Threshold() relies on this method/value to set a FlashCards metrics.
     * uses </p>
     * @param date
     */
    public void setRtDate(ZonedDateTime date)
    {
        this.rtDate = date;
    }

    /**
     * overide setSeconds with parameter sets seconds to the argument.
     * @param seconds
     */
    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }

    // ************************************** ******* **************************************
    // ************************************** GETTERS **************************************
    // ************************************** ******* **************************************

    /**
     * For convienience, AnswerMM is inherited, QuesitonMM is not,
     * thus, getAText() is available. this method is simply to reduce
     * the "confucion" of implementation for the authors.
     * @return
     */
    public String getQText()
    {
        return this.questionMM.getQText();
    }
    public String getAText() { return this.answerMM.getAText(); }

    public String[] getQFiles()
    {
        return this.questionMM.getQFiles(); // .getQFiles()[0], this.getQuestionMM().getQFiles()[1]};
    }

    public String[] getAFiles()
    {
        return this.answerMM.getAFiles();
    }

    public char getQType()
    {
        return this.questionMM.getQType();
    }

    public char getAType()
    {
        return this.answerMM.getAType();
    }

    /**
     * Returns the int contianing the test types in a boolean array/ int.
     * See setter for information on what test is set by what bit.
     * @return
     */
    public int getTestType()
    {
        return this.testType;
    }

    /**
     * getAnswer()
     * @return returns this Answer object
     */
    //public AnswerMM getAnswerMM() { return this.answerMM; }

    /**
     * Returns a 2d array of the file names for multi media in the question, and
     * the answer. Order is Row 1) Question: Multi-media file name, fileOfShapes name
     *                      row 2) Answer:   Multi-media file name, fileOfShapes name
     * All rows are optional. Use null if not used. The first column must have
     * an item. Cannot be blank. If using shapes only then fileOfShapes goes in place
     * of the multi-media file name.
     * @return Returns a 2d array of files for the question and answer
     */
    /*public String[][] getAryOfFiles()
    {
        return this.aryOfFiles;
    }
    */

    /**
     * getQuestion()
     * @return returns this Question object
     */
    public Question getQuestionMM()
    {
        return this.questionMM;
    }

    public AnswerMM getAnswerMM() { return this.answerMM; }

    /**
     * getIsRight() returns the value in current card
     * isRight variable.
     * @return
     */
    public int getIsRight()
    {
        return this.isRight;
    }

    /**
     * Returns the number of times this question has been answered correctly.
     * @return The number of times this question has been answered correctly.
     */
    public int getNumRight()
    {
        return this.numRight;
    }

    /**
     * returns the number of times this flashCard has been seen.
     * @return Returns the number of times this flashCard has been seen.
     */
    public int getNumSeen()
    {
        return this.numSeen;
    }

    /**
     * Returns the date that this question was last answered correctly.
     * @return the date that this question was last answered.
     */
    public ZonedDateTime getRtDate()
    {
        if(this.rtDate != null) {
            return this.rtDate;
        } else {
            // Set the rtDate to default
            this.setRtDate(RT_DATE);
            return this.rtDate;
        }
    }

    /**
     * getRemember() remembers which list the question is on. I.e.
     * forget (forgets this question, normal (keep showing this
     * question, EncryptedUser added repeat this question, and FlashMonkey
     * added repeat this question(when the EncryptedUser gets it wrong).
     * @return Convention/Rule = returns -1 for forget,
     * 0 for normal, 1 for self added longList/repeat, 2 for FM added
     * longList/repeat.
     * Note that this depends on what is set in the setRemember
     * provided by the upstream/EncryptedUser.EncryptedUser method.
     */
    public int getRemember()
    {
        return this.remember;
    }

    /**
     * getSeconds
     * @return int seconds that it took for the
     * EncryptedUser to answer a question.
     */
    public int getSeconds()
    {
        return this.seconds;
    }

    /**
     * Returns the cNumber (Card number) of this object
     * @return the cNumber (Card number) for this object.
     */
    public int getCNumber()
    {
        return this.cNumber;
    }

    public int getANumber() { return this.answerMM.getANumber(); }

    /**
     * Returns this cards cID number. A cID number never changes.
     * @return Returns this cards cID number. Do not change!
     */
    public final String getCID() {
        return new String(this.cID);
    }
    
    /**
     * Helper method to saveNewCardToCreator. Returns a new cHash, internal method only
     * @param deckName
     * @return
     */
    private static String createCardHash(String deckName) {
        String cHash = getCardHash(deckName, "SHA");
        return cHash;
    }
    
    
    // ************************************** ******* **************************************
    // **************************************  OTHER  **************************************
    // ************************************** ******* **************************************

    /**
     * compares the question and answer for equality
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if(other == null)
        {
            return false;
        }
        else if(this.getClass() != other.getClass())
        {
            return false;
        }
        else
        {
            FlashCardMM otherFC;
            otherFC = (FlashCardMM) other;

            boolean bool1 = this.questionMM.getQText().equalsIgnoreCase(otherFC.questionMM.getQText());
            //boolean bool2 = this.questionMM.getMediaPath().equals(otherFC.questionMM.getMediaPath());
            boolean bool3 = this.answerMM.getAText().equalsIgnoreCase(otherFC.answerMM.getAText());
            //boolean bool4 = this.answerMM.getMediaPath().equals(otherFC.answerMM.getMediaPath());

            //return (bool1 && bool2) && (bool3 && bool4);
            return (bool1 && bool3);
        }
    }

    /**
     * Default compareTo compares only the value of cNumber
     * cNumber is the flashList index number that is set when the card is
     * read into the tree from the flashList.
     * As cards are answered , remember is changed
     * and changes the cNumber (Done elsewhere). This compare method is also used in navigation.
     * Thus, if the value changes, it could cause errors in navigation.
     * All other comparisons should be done with a comparator.
     * // @param otherAnswer, the objected being compared to.
     * @return -1 if the other flashCard priority is larger.
     * 			0 if the other flashCard priority is equal.
     * 			1 if the other flashCard priority is smaller.
     */
    //@SuppressWarnings("unchecked")
    @Override
    public int compareTo(T other)
    {
        Integer thisValue = 0;
        Integer otherValue = 0;

        if(other == null) {
            //System.out.println("CompareTo is attempting to compare with a null item");
            //System.out.println("this.cNumber = " + this.cNumber);
            //System.out.println("Doing nothing about it");

        //    System.out.println(" other.cNumber " + ((FlashCardMM) other).getCNumber());
        }
        try {
            //Integer thisValue = this.remember + this.getCNumber();
            //Integer otherValue = ((FlashCardMM)other).remember + ((FlashCardMM)other).getCNumber();
            thisValue = this.cNumber;
            otherValue = ((FlashCardMM) other).cNumber;

        } catch (NullPointerException e) {
            // do nothing
        }
        return thisValue.compareTo(otherValue);
    }

    public int compare(Object other)
    {
        FlashCardMM fc = (FlashCardMM) other;
        int num = fc.cNumber;
        if (num < this.cNumber)
        {
            return -1;
        }
        else if (num > this.cNumber)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * toString()  printed in the treePane
     */
    @Override
    public String toString()
    {
        //System.out.println("\n *** FlashCard.toString() ***\n");
        //return  "Question ID: " + this.getQID() + ") "+ this.question
        //      + this.getAnswer(); // + " "
        // + " Matching answers " + this.getAnswerSet();
        return "Q " + this.cNumber + ", "
         //       + "\n\t A          = " + answerMM.getANumber()
         //       + ",  remember " + this.getRemember()
        //        + "\n\t getQText   = " + this.getQuestionMM().getQText()
        //        + "\n\t getAText   = " + this.getAnswerMM().getAText()
                + "\n\t getCID     = " + this.getCID()
                + "\n\t upperMedia Name: " + this.getQFiles();
         //       + "\n\t upperMedia Shapes: " + this.getQFiles()[1];
         //       + "\n\t numRight   = " + this.numRight
        //        + "\t numSeen    = " + this.numSeen
        //        + "\n\t getRemember = " + this.remember
        //        + "\t getSeconds = " + this.rtDate ;
        //        + "\n\nMultiMedia";
        // + "\n\t question Type = " + this.intType;

        //    return this.getANumber() + "";
        //    int qNum, int aNum, String qTxt, int intType, String qFileName, String aTxt, char charType,
        //    String aFileName, ArrayList<Integer> ansSt,
        //    int rem, int isRt, int numRt, int sec
    }

    /**
     * Returns a hash from the params provided.
     * @param name, presumedly the deckName. Creates a
     *              unique name for this cHash or cID.
     * @return
     */
    public static String getCardHash(String name, String algorith) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorith);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorith + " is not available");
        }
        String temp = name + System.currentTimeMillis();
        byte[] bHash = md.digest(temp.getBytes(StandardCharsets.UTF_8));

        return bytesToHex(bHash);
    }


    // private final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    /**
     * Helper method for getHash
     */
    private static String bytesToHex(byte[] bytes) {
        char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
        char[] chars = new char[bytes.length * 2];
        for(int i = 0; i < bytes.length; i++) {
            int x = 0x0F & bytes[i];
            chars[i * 2] = HEX_CHARS[x >>> 4];
            chars[1 + i * 2] = HEX_CHARS[0x0F & x];
        }
        return new String(chars);
    }
}
