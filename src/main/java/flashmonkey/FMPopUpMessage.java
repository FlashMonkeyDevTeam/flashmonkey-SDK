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

package flashmonkey;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;
import uicontrols.UIColors;

import java.io.FileInputStream;


/**
 * Singleton popup message class. Only one should run at a time.
 *
 * Example usage
 * FMPopUpMessage.setPopup(editorU.sectionHBox.getScene().getWindow().getX(),
 *                             editorU.sectionHBox.getScene().getWindow().getY(),
 *                             (int) editorU.tCell.getTextCellVbox().getWidth(),
 *                             (int) editorU.tCell.getTextCellVbox().getHeight(),
 *                             "  Ooops! \n  Please select a test type  ",
 *                             "FlashMonkeyMMedia/src/resources/emojis/flashFaces_sunglasses_60.png",
 *                                     FlashMonkeyMain.getWindow()
 *                         );
 * @author Lowell Stadelman
 */
public class FMPopUpMessage {
    
    private FMPopUpMessage classInstance;
    
    private FMPopUpMessage() { /* empty private constructor */}
    
    public synchronized FMPopUpMessage getInstance() {
        if(classInstance == null) {
            this.classInstance = new FMPopUpMessage();
        }
        return classInstance;
    }

    int millis = 2750;
    /**
     * Private constructor
     * Note that the MediaPopUp class uses the Screen X and Y locations requiring to get the Windows X and Y.
     * Placement of the Message MediaPopUp is in relation to the Parent Window. Not the Parent Scene or Pane.
     * @param x The screen X location of the message top left corner, Note not scene or pane but screen.
     * @param y The screen Y location of the message top left corner
     * @param w Width of the Pane or Scene message will appear in
     * @param message The message to be displayed
     * @param pathString The relitive image/emoji filePath string. Usual height of emoji or image is 60px
     * @param parentWindow The parent or owner Window. Usually FlashMonkeyMain.getWindow();
     */
    private FMPopUpMessage(double x, double y, int w, String message, String pathString, Window parentWindow,int millis)
    {
        this.millis = millis;
        final Label label;
        final Popup popup;
        //try {
            FileInputStream input;

            CornerRadii corners = new CornerRadii(5);
            Background background = new Background(new BackgroundFill(Color.web(UIColors.MESSAGE_BGND, 0.8), corners, Insets.EMPTY));

            popup = new Popup();
            popup.setAutoHide(true);
            popup.setHideOnEscape(true);
            popup.setX(x);
            popup.setY(y);

            Image image = new Image(pathString);
            ImageView imageView = new ImageView(image);

            label = new Label(message, imageView);
            //label = new Label(message);
            label.setStyle("-fx-text-fill: " + UIColors.FM_WHITE);
            label.setMinWidth(220);
            label.setMinHeight(20);

            label.setBackground(background);

            popup.getContent().add(label);
            popup.show(parentWindow);
            /*
            new Thread(() -> {
                try {
                    for(int i = 0; i < 1; i++) {
                        if (!popup.isShowing()) {
                            Platform.runLater(() -> popup.show(parentWindow));
                        } else {
                            Platform.runLater(() -> popup.hide());
                        }
                        Thread.sleep(this.millis);
                    }

                } catch (InterruptedException e) {
                    /* do nothing */
             /*   }

            }).start();
            */

    }

    /**
     * Setter for Singleton Class
     * Note that the MediaPopUp class uses the Screen X and Y locations requiring to get the Windows X and Y.
     * Placement of the Message MediaPopUp is in relation to the Parent Window. Not the Parent Scene or Pane.
     * @param x The screen X location of the message top left corner, Note not scene or pane but screen.
     * @param y The screen Y location of the message top left corner
     * @param w Width of the Pane or Scene message will appear in
     * @param message The message to be displayed
     * @param pathString The relitive image/emoji filePath string. Usual height of emoji or image is 60px
     * @param parentWindow The parent or owner Window. Usually FlashMonkeyMain.getWindow();
     */
    public static void setPopup(double x, double y, int w, String message, String pathString, Window parentWindow, int millis) {

        new FMPopUpMessage(x, y, w, message, pathString, parentWindow, millis);
    }

}
