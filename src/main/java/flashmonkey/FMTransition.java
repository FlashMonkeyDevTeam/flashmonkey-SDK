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

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Provides the necessary methods and
 * variables for animated transitions
 *
 * @author Lowell Stadelman
 */

/*** IMPORTS ***/


public abstract class FMTransition
{
    
// *** Transition objects ***

    private static TranslateTransition qRight;
    public static TranslateTransition aRight;
    protected static TranslateTransition rpCenterLeft;
    protected static TranslateTransition ansTop;
    protected static TranslateTransition qLeft;
    protected static TranslateTransition aLeft;
    protected static TranslateTransition sBottom;
    
    protected static SequentialTransition ansWaitTop; // moves answer fm top
    protected static SequentialTransition goodNod;
    protected static SequentialTransition badNod;
    
    //protected static FadeTransition lbFadeIn;
    public static FadeTransition nodeFadeIn;
    protected static FadeTransition topFadeIn;
    protected static FadeTransition createFadeIn;
    //protected static FadeTransition ansButtonFadeIn;


    public static void setQRight(TranslateTransition rightQTransition)
    {
        qRight = rightQTransition;
    }

    public static TranslateTransition getQRight()
    {
        return qRight;
    }

    public static void setQLeft(TranslateTransition leftQTransition)
    {
        qLeft = leftQTransition;
    }

    public static TranslateTransition getQLeft()
    {
        return qLeft;
    }

    public static void setAWaitTop(SequentialTransition aTransition)
    {
        ansWaitTop = aTransition;
    }

    public static void setTransitionBtm(TranslateTransition bottomTransition) { sBottom = bottomTransition; }

    public static SequentialTransition getAWaitTop()
    {
        return ansWaitTop;
    }

    public static TranslateTransition getSBottom() {
        return sBottom;
    }
    
    
    /**
     * Question transition from right. 
     * @param node
     */
    public static TranslateTransition transitionFmRight(Node node)
    {
        // @test

        // Transition the textframe
    	TranslateTransition trans = new TranslateTransition(Duration.millis(500), node);
    	trans.setFromX(300f);
    	trans.setToX(0);
    	trans.setCycleCount(1);
    	trans.setAutoReverse(false);
    	
    	return trans;
    }
  
     
    /**
     * Transition from top. 
     * @param node
     */
    public static TranslateTransition transitionFmTop(Node node, int duration, int fmHt)
    {

    	TranslateTransition trans = new TranslateTransition(Duration.millis(duration), node);
    	trans.setFromY(-fmHt);
    	trans.setToY(0);
    	trans.setCycleCount(1);
    	trans.setAutoReverse(false);     
    	
    	return trans;    
    }
    
    /**
     * Transition from bottom. 
     * @param node
     */
    public static TranslateTransition transitionFmBottom(Node node)
    {
    	TranslateTransition trans = new TranslateTransition(Duration.millis(500), node);
    	trans.setFromY(-100f);
    	trans.setToY(0);
    	trans.setCycleCount(1);
    	trans.setAutoReverse(false);     
    	
    	return trans;    
    }
    
    /**
     * Transition from left
     * @param node
     */
    public static TranslateTransition transitionFmLeft(Node node)
    {
        // Transition the textframe
        TranslateTransition trans = new TranslateTransition(Duration.millis(500), node);
        trans.setFromX(-300f);
        trans.setToX(0);
        trans.setCycleCount(1);
        trans.setAutoReverse(false);
        
        return trans;
    }
    
    /**
     * Sequential transition from QPane from left,then AnsPane from top
     * @param node: The nodeObject that will transition
     * @return SequentialTransition
     */
 /*   public static SequentialTransition newQRightTop(Node node)
    {
        // @test
        System.out.println("newQRightTop called: FMTransition line 133");

    	TranslateTransition transLeft = new TranslateTransition();
    	transLeft = transitionFmLeft(node);
    	TranslateTransition transTop = new TranslateTransition();
    	transTop = transitionFmTop(node);
    	
    	return new SequentialTransition(node, transLeft, transTop);
    }
 */
    /**
     * Nod LR (left right) fade animation used by the select button
     * when the answer is incorrect. Object in argument fades slightly
     * then moves in a left right animation, then fades out.
     * @param node
     * @return SequentialTransition
     */
    public static SequentialTransition nodLRFade(Node node)
    {

    	FadeTransition aTrans = new FadeTransition(Duration.millis(100));
    	aTrans.setFromValue(1.0);
    	aTrans.setToValue(.6);
    	aTrans.setCycleCount(1);
    	aTrans.setAutoReverse(true);
    	
    	TranslateTransition trans = new TranslateTransition(Duration.millis(400));
        trans.setFromX(0f);
        trans.setToX(-20f);
        trans.setCycleCount(4);
        trans.setAutoReverse(true);
        
        FadeTransition fTrans = new FadeTransition(Duration.millis(400));
    	fTrans.setFromValue(.7);
    	fTrans.setToValue(.2);
    	fTrans.setCycleCount(1);
    	fTrans.setAutoReverse(true);
        
        
        return new SequentialTransition(node, aTrans, trans, fTrans);
    }
    
    /*public static SequentialTransition nodUpDownFade(Node button)
    {
    	//FillTransition ft = colorTrans(button, 200.0);
    	return nodUpDownFade(button);
    }*/
    /**
     * Node up down used by the select button when the 
     * answer is correct. Object in argument fades slightly,
     * then moves in an up down animation, then fades out
     * @param button
     * @return SequentialTransition
     */
    public static SequentialTransition nodUpDownFade(Node button, int millis)
    {
    	//int millis = 300;
    	TranslateTransition btrans = new TranslateTransition(Duration.millis(millis));
        btrans.setFromY(0f);
        btrans.setToY(-8f);
        btrans.setCycleCount(1);
        btrans.setAutoReverse(false);
        
        TranslateTransition ctrans = new TranslateTransition(Duration.millis(millis));
        ctrans.setFromY(-8f);
        ctrans.setToY(8f);
        ctrans.setCycleCount(1);
        ctrans.setAutoReverse(false);
        
        TranslateTransition dtrans = new TranslateTransition(Duration.millis(millis));
        dtrans.setFromY(8f);
        dtrans.setToY(0f);
        dtrans.setCycleCount(1);
        dtrans.setAutoReverse(false);
	
	    FadeTransition fade = new FadeTransition(Duration.millis(75));
	    fade.setFromValue(1.0);
	    fade.setToValue(0.3);
	    fade.setCycleCount(1);
	    fade.setAutoReverse(false);
        
        return new SequentialTransition(button, btrans, ctrans, dtrans, fade);
    }
    
    /**
     * lbFadeIn answer Button fade in. Button fades from 1% to
     * 100%. Used on transition from left. 
     * @param node, the node object
     * @param cycle count
     * @param autoReverse true or not
     */
    public static FadeTransition ansFadePlay(Node node, int cycle, double duration,
                                             boolean autoReverse)
    {
    	FadeTransition fTrans = new FadeTransition(Duration.millis(duration), node);
    	fTrans.setFromValue(.1);
    	fTrans.setToValue(1.0);
    	fTrans.setCycleCount(cycle);
    	fTrans.setAutoReverse(autoReverse);
    	
    	return fTrans;    	
    }
    
    /**
     * lbFadeIn Left Button fade in. Button fades from 1% to
     * 100%. Used on transition from left. 
     * @param node, the node object
     * @param cycle count
     * @param autoReverse true or not
     */
    public static FadeTransition fadeTrans(Node node, Double from, Double to, int cycle, double duration,
    		boolean autoReverse)  
    {
    	FadeTransition fTrans = new FadeTransition(Duration.millis(duration), node);
    	fTrans.setFromValue(from);
    	fTrans.setToValue(to);
    	fTrans.setCycleCount(cycle);
    	fTrans.setAutoReverse(autoReverse);
    	
    	return fTrans;    	
    }    
    
    /**
     * Wait Transition from top. 
     * @param node
     */
    public static SequentialTransition waitTransFmTop(Node node, int fmHt, int duration, int pause)
    {
    	FadeTransition fadeTrans;// = new FadeTransition();
    	fadeTrans = fadeTrans(node, 0.0, 1.0, 1, pause, false);
    	TranslateTransition trans;// = new TranslateTransition();
        trans = transitionFmTop(node, duration, fmHt);
        SequentialTransition seqTrans = new SequentialTransition(
        		new PauseTransition(Duration.millis(pause)),
        		fadeTrans,
        		trans
        ); 
        return seqTrans;        
    }

    
    public static FillTransition colorTrans(Node node, int millis)
    {
    	FillTransition ft = new FillTransition();
    //	//ft.
    	ft.setDuration(Duration.millis(millis));
    	Color green = Color.rgb(0, 255, 53);
    	Color white = Color.rgb(255, 255, 255);
    	ft.setFromValue(white);
    	ft.setToValue(green);
        ft.setAutoReverse(true);
    	return ft;
    }

}

