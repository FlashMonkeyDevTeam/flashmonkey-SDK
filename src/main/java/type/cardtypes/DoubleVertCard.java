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

//@todo implement the and answer actions

import flashmonkey.FMTransition;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import type.sectiontype.GenericSection;

/**
 * @author Lowell Stadelman
 */

public class DoubleVertCard extends GenericCard
{
    public HBox qBox = new HBox();
    public HBox ansBox = new HBox();
    HBox hBox = new HBox();

    //@Override
    public HBox retrieveCard(String qTxt, char upperType, String aTxt, char lowerType, String[] qFiles, String[] aFiles)
    {
        //System.out.println(" ~+~+~ in DoubleVertCard, retrieveCard()~+~+~");
        GenericSection gs = GenericSection.getInstance();


        qBox = gs.sectionFactory(qTxt, upperType, 1, true, 0, qFiles);
        ansBox = gs.sectionFactory(aTxt, lowerType, 1, true, 0, aFiles);
        hBox.getChildren().addAll(qBox, ansBox);

        FMTransition.setQRight( FMTransition.transitionFmRight(qBox));
        FMTransition.setQLeft( FMTransition.transitionFmLeft(qBox));
        FMTransition.setAWaitTop( FMTransition.waitTransFmTop(ansBox, 0, 300, 350));

        return hBox;
    }
}
