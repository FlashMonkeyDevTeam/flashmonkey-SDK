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

package multimedia;

import java.io.Serializable;


/* *** IMPORTS *** */
import flashmonkey.FlashMonkeyMain;
import flashmonkey.Question;


/**
 * @author Lowell Stadelman
 */
public class QuestionMM extends Question implements Serializable, Comparable {

    private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    /** VARIABLES **/
    private char qType;
    // The files for this question
    // [0] is either an image, or video/audio
    // [1] is the listOfShapes file
    private String[] qFiles;

    /** DEFAULT CONSTRUCTOR **/
    public QuestionMM() {
        super();
        this.qType = 't';
    }


    /** FULL CONSTRUCTOR **/
    public QuestionMM(String q, char type, String[] fileAry) {
        super(q);
        this.qType = type;
        this.qFiles = fileAry;
    }

    /**
     * DESCRIPTION: SHALLOW COPY CONSTRUCTOR
     * @param original the original question
     */
    public QuestionMM(QuestionMM original) {
        if(original != null) {
            this.setQText(original.getQText());
            this.qType = original.qType;
            this.qFiles = new String[] {original.qFiles[0], original.qFiles[1]};
        }
        else {
            System.exit(0);
        }
    }

    /** SETTERS **/

    public void setQType(char type) {
        this.qType = type;
    }

    public void setQFiles(String[] files) {
        this.qFiles = files;
    }


    /* GETTERS */

    /**
     * Returns the files associated with this
     * question.
     * @return
     */
    public String[] getQFiles()
    {
        return this.qFiles;
    }

    /**
     * Returns the media Type. The media type chars
     * I = images, D = drawings, A = audio, or V = video
     * @return returns the media type
     */
    public char getQType()
    {
        return this.qType;
    }

    /**
     * toStsring method
     * @return Returns the question
     */
    public String toString()
    {
        return "Question: " + this.getQText();
    }


}