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


import flashmonkey.FMTransition;
import flashmonkey.FlashCardMM;
import flashmonkey.ReadFlash;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import type.cardtypes.GenericCard;
import type.celleditors.SectionEditor;
import type.sectiontype.GenericSection;
import uicontrols.ButtoniKon;
import java.util.ArrayList;


/**
 * This is the general or default, non-scoring/non-test layout. It is a generic FlashCard with a question and answer.
 *
 * Might be more appropriate to be independent of tests. However, gives flexibility
 * as a method to study by leaving it in as a test. This class does not include search. The child class
 * QandAMain includes search and is selected in Menu.
 *
 * @author Lowell Stadelman
 */
public abstract class QandA implements GenericTestType<QandA> {
    // The lower HBox used in TReadPane lambda
    private HBox lowerHBox;
    private HBox upperHBox;

    /** Answer Button **/
    private static Button answerButton;
    //private static char mode = 't';

    private QandA() {/* default constructor */}
    
    @Override
    public boolean isDisabled() {
        return false;
    }


    /**
     * Returns the testpane
     *
     * @param flashList the flashlist
     * @param q The questinos secion editor
     * @param a the answers section editor
     * @return Returns the testPane
     */
    @Override
    public VBox getTEditorPane(ArrayList<FlashCardMM> flashList, SectionEditor q, SectionEditor a) {
        // Instantiate vBox and "set spacing" !important!!!
        VBox vBox = new VBox(2);
        vBox.getChildren().addAll(q.sectionHBox, a.sectionHBox);
        return vBox;
    }

    @Override
    public GridPane getTReadPane(FlashCardMM cc, GenericCard genCard, Pane parentPane)
    {
       //System.out.println("\n*** in getTReadPane for QandA ***");
        
        ReadFlash rf = ReadFlash.getInstance();

        // Local variables
        GenericSection genSection = GenericSection.getInstance();
        GridPane gPane = new GridPane();
        gPane.setVgap(2);

        //AnchorPane anchorP = new AnchorPane();
        StackPane stackP = new StackPane();
        if(answerButton == null) {
            answerButton = ButtoniKon.getAnsSelect();
        } else {
            answerButton = ButtoniKon.getJustAns(answerButton, "Show Answer");
        }
        

        upperHBox = genSection.sectionFactory(cc.getQText(), cc.getQType(), 2, true, 0, cc.getQFiles());
        // Using 'T' in the cType(Cell type) to start off with a defualt empty answer string
        // in the answer section.
        lowerHBox = genSection.sectionFactory("", 't', 2, true, 0, cc.getAFiles());

        rf.setShowAnsNavBtns(false);
        // The answer btn action
        // Keeping action local. Makes using lamda easier.
        answerButton.setOnAction((ActionEvent e) ->
        {
            lowerHBox.getChildren().clear();
            FMTransition.nodeFadeIn = FMTransition.ansFadePlay(lowerHBox, 1, 750, false);

            lowerHBox.getChildren().add(genSection.sectionFactory(cc.getAText(), cc.getAType(), 2, true, 0, cc.getAFiles()));
            if (cc.getIsRight() == 0) {
    //            rf.getProgGauge().moveNeedle(500, rf.incProg());
            }
    //        answerButton.setVisible(false);
            cc.setIsRight(1);
            FMTransition.nodeFadeIn.play();
        });

        VBox btnVBox = new VBox();
        btnVBox.setPadding(new Insets(10));
        btnVBox.getChildren().add(answerButton);
        btnVBox.setAlignment(Pos.BOTTOM_CENTER);
        //anchorP.getChildren().add(lowerHBox);
        //AnchorPane.setBottomAnchor(btnVBox, 2.0);
        stackP.setAlignment(Pos.BOTTOM_CENTER);

        stackP.getChildren().add(lowerHBox);
        stackP.getChildren().add(answerButton);

        gPane.addRow(2, stackP);
        gPane.addRow(1, upperHBox);

        // Transition for Question, Right & end button click
        FMTransition.setQRight(FMTransition.transitionFmRight(upperHBox));
        // Transition for Question, left & start button click
        FMTransition.setQLeft(FMTransition.transitionFmLeft(upperHBox));
        // Transition for Answer for all navigation
        FMTransition.setAWaitTop(FMTransition.waitTransFmTop(lowerHBox, 0, 300, 350));

        return gPane;
    }

    /**
     * Sets 4th (= 8) bit, all others set to 0
     *
     * @return bitSet
     */
    @Override
    public int getTestType() {
        // lowest bit = 8, actual = 32776
        // high bit confirms this card
        // works with multi-choice & AI
        return 0b1000000000001000;
    }

    /**
     * This card is a double horizontal layout
     *
     * @return returns 'D' for a double horizontal card
     */
    @Override
    public char getCardLayout() {
        return 'D'; // double horizontal
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
    public void ansButtonAction() {
        // stub
    }

    @Override
    public void nextAnsButtAction() {
        // stub
    }

    @Override
    public void prevAnsButtAction() {
        // stub
    }

    @Override
    public String getName() {
        return "Q and A";
    }



    @Override
    public void reset() {
        lowerHBox = null;
        answerButton = null;
    }


    /**
     * Used in test session. Does not display a search pane and uses
     * 'T' for mode.
     */
    public static class QandATest extends QandA {
            private static QandATest CLASS_INSTANCE;

            private QandATest() {
                //ReadFlash.setMode('T');
                //answerButton.setVisible(true);
     //           ReadFlash.setShowAnsNavBtns(false);
            }


            public static synchronized QandATest getInstance() {
                if (CLASS_INSTANCE == null) {
                    CLASS_INSTANCE = new QandATest();
                }
                return CLASS_INSTANCE;
            }

            @Override
            public GenericTestType getTest() {
                return QandATest.getInstance();
            }
    }

    /**
     * Used in the Question and Answer "mode" in MenuPane of ReadFlash. Displays
     * a search box and uses 'A' for mode
     */
    public static class QandASession extends QandA
    {

            /**
             * The class instance for the singleton class
             */
            private static QandASession CLASS_INSTANCE;

            /**
             * Default constructor. Creates super/parent class
             */
            private QandASession() {
                ReadFlash.getInstance().setMode('a');
            }

            /**
             * Provides only one instance of this class.
             * @return Returns an instance of the class
             */
            public static synchronized QandASession getInstance() {
                if (CLASS_INSTANCE == null) {
                    CLASS_INSTANCE = new QandASession();
                }
                return CLASS_INSTANCE;
            }

            /** ------ GETTERS ----- **/

            @Override
            public GenericTestType getTest() {
                return new QandASession();
            }
    }


}
