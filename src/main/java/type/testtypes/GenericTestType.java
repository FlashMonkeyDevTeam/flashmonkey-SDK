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

//import flashmonkey.ReadFlash;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import flashmonkey.FlashCardMM;
//import javafx.scene.layout.VBox;
import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;

import java.util.ArrayList;
//import java.util.LinkedList;

/**
 * GenericTestType provides the interface for All test types.
 * To create a new testType.
 * 1. Add the testType to the bottom of the GenericTestType array in
 * testtypes.TestList
 * 2. add it's bitSet number to its bitset. Use the bottom tests
 * index of the setBit. The next test will be the left bitSet from
 * that one.
 * 3. Implement all of the neccessary methods from this interface.
 * You can use a previously finished TestType as a guide.
 *
 *
 *@author Lowell Stadelman
 */
public interface GenericTestType<A extends GenericTestType>
{
    // @todo finish GenericTestType
    
    
    /**
     * If this card/test type is disabled
     */
    public abstract boolean isDisabled();
    
    /**
     * Returns the Test creator Pane for each test type.
     * @param flashList
     * @param q
     * @param a
     * @return
     */
    public abstract Pane getTEditorPane(ArrayList<FlashCardMM> flashList, SectionEditor q, SectionEditor a);

    /**
     * Returns the readPane that contains the testType.
     * @param cc
     * @param genCard
     * @param pane The parent pane
     * @return
     */
    public abstract Pane getTReadPane(FlashCardMM cc, GenericCard genCard, Pane pane);

    /**
     * Each testTypes save action
     * - Save BitSet testType array
     * - Save CardLayout
     * - Section Layouts
     */
    public abstract int getTestType();


    /**
     * Provides the card layout in a char.
     * layout: 'S' = single card layout,
     * 'D' = double horizontal card, and
     * 'd' = double vertical card
     * @return
     */
    public abstract char getCardLayout();

    public abstract GenericTestType getTest();

    /**
     * Returns the array of ansButtons, An array
     * should include the prevAnsButton and nextAnsButton (if they are needed)
     * and always should include the answerButton. Used
     * in placement in the south/bottom pane
     * @return
     */
    public abstract Button[] getAnsButtons();

    /**
     * returns each tests answerButton as opposed
     * to the array of AnswerButtons.
     * @return
     */
    public abstract Button getAnsButton();

    public abstract String getName();

    public abstract void ansButtonAction();

    public abstract void prevAnsButtAction();

    public abstract void nextAnsButtAction();

    /**
     * Called when a new card is created. IE when
     * a flashCard is added to the flashList. Prepares
     * or clears the UI as well as any data structures
     * that should be cleared.
     */
    public abstract void reset();



    //public abstract void nextQButtonAction();  THESE ARE IN READFLASH not in tests

    //public abstract char getQLayout();

    //public abstract char getALayout();

}
