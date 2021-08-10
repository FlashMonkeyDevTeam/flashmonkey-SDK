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

package type.sectiontype;

/**
 * The abstract class to for section views.
 *
 * @author Lowell Stadelman
 */

import javafx.scene.layout.HBox;

public class GenericSection
{

    private static GenericSection CLASS_INSTANCE;

    private GenericSection() { /* default constructor */}

    public static GenericSection getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new GenericSection();
        }
        return CLASS_INSTANCE;
    }
    /**
     * Abstract method, Must provide a left cell type, the string that is either text for a text area,
     *  or filePath/fileName for a multi-media file. Will return a singleCell or section view if the
     *  rightType is set to 'N' and data in rtTxt is ignored.
     * @param txt       Text in the TextArea
     * @param mmType    The multi-media.
     * @param path      The filePath/fileName if multi-media.
     * @return Return a node
     */
    //protected abstract Node sectionView(String txt, char mmType, String path);

    /**
     * Creates a section, either single or double, with cells
     * populated with data. If rtType == 'N' then it is a single
     * cell section.

     * @param txt      The text in a textArea
     * @param cType    The type of cell. if mmType == 't' then the single section is text only,
     *                  any other type that is in a single section is represented by the lower case
     *                  equivelant of what they are.
     * @param path      The filePath w/ fileName for a file
     *
     * @return  Returns E as a VBox.
     */

    public HBox sectionFactory(String txt, char cType, int numHSections, boolean isEqual, double otherHt, String ... path)
    {
        switch(cType)
        {
            case 'C':   // image
            case 'D':   // drawing
            case 'M':   // media audio or video
            {
                DoubleCellSection s = new DoubleCellSection();
                return s.sectionView( txt, cType, numHSections, isEqual, otherHt, path);
            }
            // Single section
            case 'c': // image
            case 'd': // drawing
            case 'm': // media
            case 't': // text
            default:
            {
                SingleCellSection s = new SingleCellSection();
                return s.sectionView( txt, cType, numHSections, isEqual, otherHt, path);
            }
        }
    }
}
