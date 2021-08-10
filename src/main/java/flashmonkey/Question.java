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

/*
 * AUTHOR: Lowell Stadelman
 */

package flashmonkey;

import java.io.*;

/**
 * Base Question class. Contains only text. See multimedia/QuestionMM for
 * multi-media methods and fields.
 *
 * @author Lowell Stadelman
 */

@SuppressWarnings("unchecked")
public abstract class Question implements Serializable, Comparable
{
   private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    //*** CONSTANTS ***
    public static final String DEFAULT_QUESTION = "";

    private String qText;

    // DESCRIPTION: DEFAULT CONSTRUCTOR
    public Question()
    {
        this.qText = DEFAULT_QUESTION;
    }

    // DESCRIPTION: FULL CONSTRUCTOR
    public Question(String q)
    {
        this.qText = q;
    }

    // DESCRIPTION: SHALLOW COPY CONSTRUCTOR
    public Question(Question original)
    {
        if(original != null)
        {
            this.qText = original.qText;
        }
        else
        {
           //System.out.println("Error: Cannot copy a null object");
            System.exit(0);                 
        }
    }

    /*** setters ***/
    public void setQText(String txt)
    {
        this.qText = txt;
    }

    /*** getters ***/
    public String getQText()
    {
        return this.qText;
    }

    public String toString()
    {
        return "Question: " + this.qText;
    }

    // DESCRIPTION: Equals method. Tests the text only.
    public boolean equals(Object other)
    {
        Question otherQuestion;
        
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
            otherQuestion = (Question) other;
            return this.qText.equalsIgnoreCase(otherQuestion.qText);
        }
    }
    
    /**
	 * Default compareTo() that calls Strings compareTo method.
	 * All other number comparisons should be done with a comparator.
	 * @param otherQuestion, the objected being compared to.
	 * @return -1 if the other string is larger.
	 * 			0 if the other string is equal.
	 * 			1 if the other string is smaller.
	 */
    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(Object otherQuestion) 
    {
        return this.qText.compareTo(((Question)otherQuestion).qText);
    }

}
