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

package type.draw;

import javafx.geometry.Insets;
//import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.Serializable;

/**
 * Manages the values of the VBox that contains textAreas used by FlashMonkey.
 * It does not include editable textAreas. Refer to the child Class FMTextEditable if an
 * editable TextArea is needed.
 *
 * @author Lowell Stadelman
 */
public class FMText //implements Serializable
{
    //private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    /** Variables **/
    protected String promptTxt;

    /** OBJECTS FROM JAVA LIBRARIES **/
    protected StackPane stackPane;
    protected TextArea textArea;

    


    /**
     * Default constructor
     */
    protected FMText() {
        /* empty */
    }

    /**
     * Full constructor
     * @param prompt
     */
    protected FMText(String prompt, double x, double y, double wd, double ht) {
        this.promptTxt = prompt;
        stackPane = new StackPane();
        stackPane.setLayoutX(x);
        stackPane.setLayoutY(y);
        stackPane.setPrefSize(wd, ht);
        stackPane.setMaxSize(wd, ht);
        stackPane.setMinSize(wd, ht);
        textArea = new TextArea();
        stackPane.setPadding(new Insets(6, 4, 6, 4));
        stackPane.setStyle("-fx-background-color: white");
        stackPane.setAlignment(Pos.BOTTOM_LEFT);
        textArea.setWrapText(true);
        textArea.setPromptText(promptTxt);

        stackPane.getChildren().add(textArea);
    }

    /** SETTERS **/
    protected void setPromptText(String prompt) {
        this.promptTxt = prompt;
    }

    /** GETTERS **/
    protected String getPromptText() {
        return promptTxt;
    }

    /**
     * Returns the VBox object
     * @return returns the VBox that contains the TextArea of this class
     */
    protected StackPane getStackPane() {
        return stackPane;
    }
}
