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
 */

package flashmonkey;

/*******************************************************************************
 * Version MultiMedia 2018-05-25
 * CLASS DESCRIPTION: An answer consists of a String "answer", and a string path. The
 * String path is a constant which is used later to identify the MultiMedia type.
 * 
 * To comply with the strategy that enables multiple answers to be correct
 * for this question. An ArrayList of other possible correct answers
 * is provided for comparison.
 * Sets added. 1/14/2016
 *
 * IMPLEMENTS Serializable, comparable
 *
 * @author Lowell Stadelman
 *
 ******************************************************************************/

import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public abstract class Answer implements Serializable, Comparable
{
   private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    /*** CONSTANTS ***/
    public static final ArrayList<Integer> DEF_LIST = new ArrayList<>(1);
    
    /*** VARIABLES ***/
    private String aText;
    // ANumber is the index number in flashList. It is used
    // as the anchor for a flashCard in flashList during a session.
    // It is set to it's index number during build tree.
    private int aNumber;
    
    /*** ARRAY ***/
    private ArrayList<Integer> answerSet; // a list of possible questions that 
                                          // this could be an answer for.
    /**
     * Default constructor
     */
    public Answer()
    {
        this.aText = "";
        this.aNumber = 0;
    }


    /**
     * Full constructor
     * @param txt The answer text
     * @param aNum The cards number in the stack, used as a reference
     * @param ansSet The set of questions this answer will also be correct for
     */
    public Answer(String txt, int aNum, ArrayList<Integer> ansSet)
    {
        //System.out.println("\nAnswer constructor (String, int, ArrayList) called. aText :" + txt);
        this.aText = txt;
        this.aNumber = aNum;
        // this.qID = id;       qID is the original card number when the card was initially created.
        this.answerSet = ansSet;
    }

    /**
     * Creates a Answer Object with an empty ArrayList
     * @param txt
     * @param aNum
     */
    public Answer(String txt, int aNum) {
        if(txt == null) {
            aText = "";
        }
        else {
            this.aText = txt;
        }
        this.aNumber = aNum;
        this.answerSet = new ArrayList<>();
    }

    
    // DESCRIPTION: Shallow Copy Constructor
    public Answer(Answer original) {
        if(original == null) {
            System.err.println("ERROR: Answer class Copy Constructor given NULL"
                    + "Exiting...");
            System.exit(0);
        }
        else {
            this.aText = original.aText;
            this.aNumber = original.aNumber;
            this.answerSet = new ArrayList<>(original.answerSet);
        }
    }


    //*** SETTERS ***
    
    public void setAText(String txt)
    {
        this.aText = txt;
    }

    public void setANumber(int aNum)
    {
        this.aNumber = aNum;
    }

    /**
     * The set of index's of all correct possible answers.
     * May include this answer.
     * @param ansSet
     */
    public void setAnswerSet(ArrayList<Integer> ansSet)
    {
        this.answerSet = new ArrayList<>(ansSet);
    }

    // *** GETTERS ***

    public String getAText() {
        return this.aText;
    }

    protected int getANumber() {
        return this.aNumber;
    }
    
    protected ArrayList<Integer> getAnswerSet() {
        return new ArrayList<>(answerSet);
    }


    // *** OTHERS ***

    /**
     * toString method
     * @return this answer
     */
    public String toString() {
        return this.aText;
    }

    /**
     * equals method
     * @param other
     * @return
     */
    public boolean equals(Answer other) {
        return this.aText.equalsIgnoreCase(other.aText);
    }
    
    
    /**
	 * Default compareTo() that calls Strings compareTo method.
	 * All other number comparisons should be done with a comparator.
	 * @param otherAnswer, the objected being compared to.
	 * @return -1 if the other answer is larger.
	 * 			0 if the other answer is equal.
	 * 			1 if the other answer is smaller.
	 */
    @Override
    public int compareTo(Object otherAnswer) {
        return this.aText.compareTo(((Answer)otherAnswer).aText);
    }
}
