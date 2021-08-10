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


import fileops.FileOpsShapes;
import flashmonkey.FlashMonkeyMain;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.draw.shapes.FMRectangle;
import type.draw.shapes.GenericShape;
import type.tools.imagery.Fit;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Contains images and shapes/drawings
 *
 * @author Lowell Stadelman
 */
public class CanvasCell<T extends GenericShape> extends GenericCell implements Serializable {
    private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    // THE LOGGER
    // Logging reporting level is set in src/main/resources/logback.xml
    private static final Logger LOGGER = LoggerFactory.getLogger(CanvasCell.class);

    /**
     * Builds the canvas cell.
     * Note: If there is an image, the size of the pop-up comes from the
     * original image size, and the scale in Fit is set by the original
     * image size. If using a drawing created by DrawTools, the size of
     * the popup is set using the first element in the ArrayOfShapes
     * files. The first element is a rectangle that is the size of
     * the original pane.
     */
    String shapesPath = "";

    public Pane buildCell(Pane canvasPane, double imgWd, double imgHt, boolean rescalable, String... canvasPaths) {

        LOGGER.info("called, img and canvasPath: {}", canvasPaths);
        canvasPane.setId("rightPaneWhite");

        // clear the fields
        canvasPane.getChildren().clear();

        // Process shapes if present.
        // Get the shapes file from the second element
        // in canvasPaths.
        if (canvasPaths.length > 1) {
            shapesPath = canvasPaths[1];
            canvasPane = InnerShapesClass.getShapesPane(shapesPath, true, true);
            canvasPane.setOnMouseClicked(e -> {
                LOGGER.info("mouse clicked on Media popup with image. shapesPath: " + shapesPath);
                 MediaPopUp.getInstance().popUpScene(InnerShapesClass.getShapesPane(shapesPath, false, false));
            });
        }

        //** SHAPES  and DrawPad**
        //   - 1st shape is an FMRectangle and
        // its ht and wd sets the drawpad size.
        // If no shapes are present do not show the pad.
        if (shapesPath.equals("")) { // && shapesPaths.length() < 1) {

        }

        return canvasPane;
    } // --- end buildCell ---


    /**
     *  Helper method to buildCell(). Processes and scales an image to the
     *  size of it's container. Checks if media files are synchronized.
     *
     * @param imgPath The path to the image
     * @param imgWd
     * @param imgHt
     * @param rescaleable If this ImageView should resize/scale if the
     *                 container it is in grows or shrinks.
     * @return Returns an ImageView containing a scaled
     * image that was provided in the parameter. Resizes if scalable is true
     * else it  does not rescale if it's container is resized after its initial
     * size.
     */



    // *** GETTERS ***

    /**
     * Resizes an SVG shape. Not finished
     *
     * @param svgShape
     * @param h
     * @param w
     */
    public void resizeSVG(Shape svgShape, int h, int w) {
        double originalWidth = h;
        double originalHeight = w;

        double scaleX = w / originalWidth;
        double scaleY = h / originalHeight;

        svgShape.setTranslateY((h - 100) / 2);
        svgShape.setTranslateX((w - 100) / 2);
        svgShape.setScaleX(scaleX);
        svgShape.setScaleY(scaleY);
    }

    /*************************************************************
                            INNER CLASS
                            For shapes
     *************************************************************/

    public static class InnerShapesClass {
        private static ArrayList<GenericShape> fmShapesAry;

        /**
         * Helper method to buildCell(). Parses shapes from the
         * drawing filePath provided, and outputs them in a
         * scaled size or in thier original size depending on the
         * boolean parameter scaled. If true they are scaled.
         * @param scaled Is the result a smaller size than original?
         * @param drawingPath Path to the shapes, includes shapes file name.
         * @return
         */
        public static Pane getShapesPane(String drawingPath, boolean scaled, boolean drawingOnly) {

            Pane shapesPane = new Pane();
            fmShapesAry = new ArrayList<>();
            if(drawingOnly) {
                shapesPane.setId("rightPaneWhite");
            } else {
                shapesPane.setId("rightPaneTransp");
            }

            // If the shapesPath is not null
            File check = new File(drawingPath);

            LOGGER.info("file check = " + check.exists() + " drawingPath: " + drawingPath);

            if (check.exists()) {
                FileOpsShapes fo = new FileOpsShapes();
                fmShapesAry = fo.getListFromFile(drawingPath);
                LOGGER.info("getShapesPane() getListFromFile request using drawingPath: {}", drawingPath);

                if (fmShapesAry == null || fmShapesAry.size() == 0) {
                    LOGGER.warn("shapes array is null or empty");
                } else {

                    // The first shape, or shapesPaths[0], is the FMRectangle containing the
                    // resizable drawpad/pane size. Use FMRectagnel's getWd and getHt from
                    // the first shape.
                    double origWd = ((FMRectangle) fmShapesAry.get(0)).getWd();
                    double origHt = ((FMRectangle) fmShapesAry.get(0)).getHt();

                    if (scaled) {
                        double scale;
                        scale = Fit.calcScale(origWd, origHt, 100, 100);

                        for (int i = 1; i < fmShapesAry.size(); i++) {
                            shapesPane.getChildren().add(fmShapesAry.get(i).getScaledShape(scale));
                        }

                    } else {
                        for (int i = 1; i < fmShapesAry.size(); i++) {
                            shapesPane.getChildren().add(fmShapesAry.get(i).getShape());
                            shapesPane.setPrefWidth(origWd);
                            shapesPane.setPrefHeight(origHt);
                        }
                    }
                }
            }
            return shapesPane;
        }

    }
    /* Test Only Methods */
/*
    @FMAnnotations.DoNotDeployMethod
    public CanvasCell getCanvasCell() {
        return this;
    }
 */

}
