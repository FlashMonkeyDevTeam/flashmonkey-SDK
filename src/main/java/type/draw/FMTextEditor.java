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

import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


/**
 * Adds buttons to the editable text area
 * and sets the text area to editable.
 *
 * @author Lowell Stadelman
 */
public class FMTextEditor extends FMText //implements Serializable
{
    /**
     * VARIABLES
     **/
    private Button clearButton;
    private HBox buttonBox;
    

    /**
     * Full constructor
     * @param prompt The prompt that will exist before the EncryptedUser.EncryptedUser begins typing
     */
    public FMTextEditor(String prompt, Double x, double y, double wd, double ht)
    {
        super(prompt, x, y, wd, ht);

        textArea.setEditable(true);
        textArea.requestFocus();
        buttonBox = new HBox(2);
        //buttonBox.setPadding(new Insets(2, 2, 2, 2));
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        
        stackPane.getChildren().add(buttonBox);
    }

    
    public StackPane getTextEditor() {
        return this.stackPane;
    }
    
    /**
     * Returns the input text from the text area. Uses
     * getText from TextArea class.
     *
     * @return returns the String input from the text area
     */
    public String getText()
    {
        return textArea.getText();
    }
    
    
    public void setText(String text) {
        textArea.setText(text);
    }
    
    /**
     * Adds a button at index 0 to the buttonBox
     * in the bottom right corner.
     */
    public void addButtons(Button ... buttons) {
        this.buttonBox.getChildren().addAll(buttons);
    }

    /**
     * clearTextArea() method clears the Question Pane
     * Void method.
     */
    protected void clearTextArea()
    {
        textArea.setText("");
        textArea.requestFocus();
    }
    
    public void requestFocus() {
        textArea.requestFocus();
    }

}
