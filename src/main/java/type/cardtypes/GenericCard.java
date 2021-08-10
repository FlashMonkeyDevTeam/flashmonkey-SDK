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

package type.cardtypes;


import javafx.scene.layout.Pane;


/**
 *
 *
 *     @author Lowell Stadelman
 */
public class GenericCard
{

    public DoubleHorzCard dhc;// = new DoubleHorzCard();
    public DoubleVertCard dvc;


    /**
     * Creates a        flashCard in the layout specified.
     *  layout =        'S' = single card layout, 'D' = double horizontal card, and d = double vertical card
     * @param qTxt      The question or upper text
     * @param upType    The upper type 't' = text, ...
     * @param aTxt      The answer text or upper text
     * @param lowerType The lower type 't' = text, ...
     * @param layout    The card layout type.
     * @param qFiles    The file paths for multi-media.
     * @param aFiles    The file paths for multi-media.
     * @return Returns a Pane.
     */
    public Pane cardFactory(String qTxt, char upType, String aTxt, char lowerType, char layout, String[] qFiles, String[] aFiles)
    {
        switch (layout)
        {
            case 'S': // SingleCellCard
            {
                SingleCard s = new SingleCard();
                return s.retrieveCard(qTxt, upType, qFiles);
            }
            case 'D': // two sections stacked vertically
            default:
            {
                dhc = new DoubleHorzCard();
                return dhc.retrieveCard(qTxt, upType, aTxt, lowerType, qFiles, aFiles);
            }
            case 'd': // two sections side by side
            {
                dvc = new DoubleVertCard(); // up = left & lower = right
                return dvc.retrieveCard(qTxt, upType, aTxt, lowerType, qFiles, aFiles);
            }
        }
    }
}
