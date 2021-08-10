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

package type.celltypes;


import javafx.scene.layout.Pane;

import javax.print.DocFlavor;

/**
 * Assists with building and selecting different cell types in
 * the UI.
 * @author Lowell Stadelman
 */
public class GenericCell//  extends GenericSection
{

    public CanvasCell cCell;
    public TextCell tCell;


    /**
     * Returns the cell containing a media that is sized according to the scale givein in the
     * parameters.
     * @param cellType Media (video or audio) = 'M'/'m' or Canvas / Image Drawing = 'C' 'c' 'D' 'd'
     * @param pane
     * @param paneWd
     * @param paneHt
     * @param scaleable If the image or media should rescale if the container it is in
     *                  is resized after it's initial size
     * @param fileNames Files as an array. Expects the image or media to be at idx [0]
     * @return Returns the cell containing a media
     */
    public Pane cellFactory(char cellType, Pane pane, int paneWd, int paneHt, boolean scaleable, String ... fileNames)
    {
        final String CANVAS = "../";
        final String MEDIA = "../";

        switch (cellType)
        {

            case 'c': // image or both. for singleCell section
            case 'd': // drawing only. singleCell
            case 'C': // image, or both. for doubleCell section
            case 'D': // drawing only. doubleCell
            default:
            {
                String canvasPaths[] = new String[2];
                int i = 0;
                cCell = new CanvasCell();

                for(String s : fileNames)
                {
                    canvasPaths[i++] = CANVAS + s;
                }
                // the image cell on the right.
                return cCell.buildCell(pane, paneWd, paneHt, scaleable, canvasPaths);
            }
        }
    }

    /**
     * Builds the left TextCell
     * @param text
     * @return
     */
    public Pane cellFactory(String text, int paneWd, int paneHt, int numSections, boolean isEqual, double otherHt) {
        tCell = new TextCell();
        return tCell.buildCell(text, "", paneWd, paneHt, numSections, isEqual, otherHt);
    }

}
