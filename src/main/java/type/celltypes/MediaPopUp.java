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

import uicontrols.SceneCntl;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The media popup class. A Singleton Class. Synchronized
 */
public class MediaPopUp {

    private static MediaPopUp CLASS_INSTANCE;
    private Stage window = new Stage();
    private int screenWt = SceneCntl.getScreenWd();
    private int screenHt = SceneCntl.getScreenHt();

    private MediaPopUp() {
        //window = new Stage();
    }

    /**
     * Returns an instance of this singleton class.
     * Synchronized, not thread safe. Expect only
     * one to exist within a JVM.
     * @return
     */
    public static synchronized MediaPopUp getInstance() {

        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new MediaPopUp();
        }
        return CLASS_INSTANCE;
    }

    /**
     * Popup with Image and Shapes:
     * <p>Creates a popUp Scene with the image and shapes
     * provided in the parameter</p>
     *
     * @param imgPath   The image path.
     * @param shapePane The unscaled image pane.
     */
    public void popUpScene(String imgPath, Pane shapePane) {

        Image img = new Image("File:" + imgPath);
        ImageView view = new ImageView(img);
       //System.out.println("called popUp using (ImageView view, Pane shapePane)");
        double wd = img.getWidth() + 4;
        wd = wd < screenWt ? wd : screenWt;
        double ht = img.getHeight() + 4;
        ht = ht < screenHt ? ht : screenHt;

        StackPane pane = new StackPane();
        pane.setMaxWidth(screenWt - 50);
        pane.setMaxHeight(screenHt - 100);
        pane.setPrefSize(wd, ht);
        pane.getChildren().add(view);

        shapePane.setStyle("-fx-background-color: TRANSPARENT");
        pane.getChildren().add(shapePane);

        Scene scene = new Scene(pane, wd, ht);
        getInstance();
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }


    /**
     * Popup with shapes only:
     * <p>Creates a popUp Scene with the shapes provided in the parameter</p>
     *
     * @param shapePane
     */
    public void popUpScene(Pane shapePane) {

       //System.out.println("Called popUp using (Pane shapePane)");

        shapePane.setMaxWidth(screenWt);
        shapePane.setMaxHeight(screenHt);
        Scene scene = new Scene(shapePane);

        getInstance();
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }

    /**
     * Popup with image only:
     * <p>Creates a popUp Scene with an image from the
     * path provided in the parameter</p>
     *
     * @param imgPath The image to be displayed
     */
    public void popUpScene(String imgPath) {

       //System.out.println("Called popUp using (String imgPath)");

        Image img = new Image("File:" + imgPath);
        ImageView view = new ImageView(img);
       //System.out.println("called popUp using (ImageView view, Pane shapePane)");
        double wd = img.getWidth() + 4;
        wd = wd < screenWt ? wd : screenWt;
        double ht = img.getHeight() + 4;
        ht = ht < screenHt ? ht : screenHt;

        StackPane stack = new StackPane(view);
        //stack.setMaxWidth(screenWt);
        //stack.setMaxHeight(screenHt);
        Scene scene = new Scene(stack, wd, ht);
        getInstance();
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }





}
