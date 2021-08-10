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

package type.draw.shapes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.transform.Rotate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.awt.geom.AffineTransform;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class ArrowBuilder extends PolylineBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrowBuilder.class);

    double[] arrowPts = {1.1, 1.1};

    private double baseX = 50;
    private double baseY = 50;

    private int arrowVerts1 = 16;
    private int arrowVerts2 = 6;
    private int rectW = 24;
    // arrowTop, tip, bottom, lowerRectBase, leftLowerRect, leftUpperRect, upperRectBase, arrowTop
    private double[] arrowTop = new double[2];
    private double[] tip = new double[2];
    private double[] arrowBtm = new double[2];
    private double[] lowerRectBase = new double[2];
    private double[] upperRectBase = new double[2];
    private double[] leftLowerRect = new double[2];
    private double[] leftUpperRect = new double[2];

    public ArrowBuilder() {
        super();
    }

    double deltaX = 0.0;
    double deltaY = 0.0;

    /**
     * Constructor used when new shapes are initially created, Rectangle is
     * used to define the
     * popUp panes size. Called first
     * to create the overlayPane over the area selected.
     *
     * @param c
     * @param graphC
     * @param overlayPane
     * @param editor
     * @param stroke
     * @param fill
     */
    public ArrowBuilder(Canvas c, GraphicsContext graphC, Pane overlayPane, SectionEditor editor, String stroke, String fill) {
        super(c, graphC, overlayPane, editor, stroke, fill);
//        super.fmLine = new FMPolyLine();
    }

    /**
     * Basic constructor without setting the stroke color or fill color
     * Called by FMLine getBuilder
     *
     * @param fmPolyLine
     * @param c
     * @param graphC
     * @param pane
     * @param editor
     */
    public ArrowBuilder(FMPolyLine fmPolyLine, Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor) {
        super(fmPolyLine, c, graphC, pane, editor);
    }

    @Override
    public void zeroDeltas() {
        this.deltaX = 0.0;
        this.deltaY = 0.0;
    }

    /**
     * When the mouse is pressed in the overlayArea. If (copy = true), create
     * a shape.
     * @param mouse
     */
    @Override
    public void mousePressed(MouseEvent mouse) {
        //LOGGER.debug("mousePressed() in PolylineBuilder()");

        DrawTools draw = DrawTools.getInstance();
        GraphicsContext gC = getGc();
        gC.setStroke(Color.web(strokeColor));
        gC.setFill(Color.web(fillColor));

        if(paste) {
            LOGGER.debug("\t - paste action called ***");
            // Paste the copy
            pasteAction(mouse);
            draw.getOverlayScene().setCursor(Cursor.DEFAULT);
            paste = false;
        } else if (draw.getShapeNotSelected()) { // Avoids conflict between new shape, shape dragged, and shape resized
            // Clear the resize nodes if they are present

            draw.clearNodes();
            setNewShape(true);

            // Set the shape start point x and y
            if(anchorX+anchorY == 0.0) {
                fxLine = new Polyline();
                setAnchors(mouse);
                double[] pts = {anchorX, anchorY};
                fmLine = new FMPolyLine(pts,3, strokeColor, fillColor, gbcopyArrayOfFMShapes.size());
                fxLine = fmLine.getShape();

                //gbcopyArrayOfFMShapes.add(fmLine);
                LOGGER.debug("alt is not down. setting anchors to mouse");


            }

        }
        mouse.consume();
    }

    /**
     * As the mouse is dragged, create the shape of the
     * line.
     * ** DeltaX and DeltaY for a shape, are different than for a line or poly
     * @param e
     */
    @Override
    public void mouseDragged( MouseEvent e) {
        System.out.println("ArrowBuilder.mouseDragged");
        Canvas can = getCanvas();
        GraphicsContext gC = getGc();
        Utility ut = new Utility();

        gC.setStroke(Color.web(strokeColor));
        gC.setFill(Color.web(fillColor));

        DrawTools draw = DrawTools.getInstance();
        if(draw.getShapeNotSelected()) {

            //AffineTransform transform = new AffineTransform();

            setNewShape(true);
            getGc().clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            deltaX = e.getSceneX() - anchorX;
            deltaY = e.getSceneY() - anchorY;
            Point2D mousePt = new Point2D(e.getSceneX(), e.getSceneY());
            Point2D anchorPt = new Point2D(anchorX, anchorY);
            double length = anchorPt.distance(mousePt.getX(), mousePt.getY());


            ArrayList<Point2D> rectBase = ut.calcRectBase(anchorPt, mousePt, length);

            tip[0] = anchorX;
            tip[1] = anchorY;
            // REF: When arrow points right
            arrowTop[0] = rectBase.get(0).getX();//x0 + headLength;
            arrowTop[1] = rectBase.get(0).getY();//yMouse - headWidth;
            arrowBtm[0] = rectBase.get(1).getX();//x0 + headLength;
            arrowBtm[1] = rectBase.get(1).getY();//yMouse + headWidth;
            System.out.println("arrowTopY: " + arrowTop[1] + " arrowBottomY: " + arrowBtm[1]);
            upperRectBase[0] = rectBase.get(2).getX();
            upperRectBase[1] = rectBase.get(2).getY();
            lowerRectBase[0] = rectBase.get(3).getX();
            lowerRectBase[1] = rectBase.get(3).getY();

            leftLowerRect[0] = rectBase.get(5).getX();
            leftLowerRect[1] = rectBase.get(5).getY();
            leftUpperRect[0] = rectBase.get(4).getX();
            leftUpperRect[1] = rectBase.get(4).getY();

            double[] arrowPts = {
                    arrowTop[0], arrowTop[1],
                    tip[0], tip[1],
                    arrowBtm[0], arrowBtm[1],
                    lowerRectBase[0], lowerRectBase[1],
                    leftLowerRect[0], leftLowerRect[1],
                    leftUpperRect[0], leftUpperRect[1],
                    upperRectBase[0], upperRectBase[1],
                    arrowTop[0], arrowTop[1]};

            System.out.println("arrowPts");
            for(double d : arrowPts) {
                System.out.print(Double.toString(d) + ", ");
            }


            setXYarrs(arrowPts);
            gC.strokePolyline(xAry, yAry, arrowPts.length / 2);
            //gC.getTransform().appendRotation(20, anchorX, anchorY);

            this.arrowPts = arrowPts;
        }
        e.consume();
    }

    /**
     * When the mouse is released, set the data in FMShape, store the fmShape in the gbcopyArrayOfFMShapes, display
     * the shape in the right pane.
     * @param mouse
     */
    @Override
    public void mouseReleased( MouseEvent mouse)
    {

        DrawTools draw = DrawTools.getInstance();
        if(draw.getShapeNotSelected())
        {
            Canvas can = getCanvas();
            GraphicsContext gC = getGc();
            for(double d : arrowPts) {
                fmLine.getShape().getPoints().add(d);
            }

            gC.clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            if (isNewShape())
            {

                double[] pts = Stream.of(fmLine.getShape().getPoints().toArray()).mapToDouble(o -> Double.parseDouble(o.toString())).toArray();

                if (mouse.isAltDown()) {// Creates a square
                    fmLine = new FMPolyLine(
                            pts,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size()
                    );
                } else {
                    fmLine = new FMPolyLine(
                            pts,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size()
                    );
                }

                // add shape to gbcopyArrayOfFMShapes
                gbcopyArrayOfFMShapes.add(fmLine);

                // get the shape from the saved shape
                Polyline fxxLine = fmLine.getShape();

                // add mouse actions to the shape
                fxxLine.setOnMousePressed( f -> shapePressed( f, fmLine, fxxLine));
                fxxLine.setOnMouseDragged( f -> shapeDragged( f, fmLine, fxxLine));
                fxxLine.setOnMouseReleased(f -> shapeReleased(f, fmLine, fxxLine));

                // add shape to gbcopyArrayOfFMShapes
                gbcopyArrayOfFMShapes.add(fmLine);

            //    Rotate rotate = new Rotate(30, anchorX, anchorY);
            //    fxxLine.getTransforms().addAll(rotate);

                // show the shape in the overlayPane
                getOverlayPane().getChildren().add( fxxLine);
                // Add the scaled version of the shape in the right pane
                int wd = (int) getOverlayPane().getWidth();
                int ht = (int) getOverlayPane().getHeight();

                editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, wd, ht );

                setNewShape(false);
                anchorX = 0.0;
                anchorY = 0.0;
            }
        }


        DrawTools.setShapeNotSelected(true);
        mouse.consume();
    }


    double[] xAry = new double[8];
    double[] yAry = new double[8];
    private void setXYarrs(double[] ary) {

        for(int i = 0, j = 0; i < (ary.length / 2) ; i++) {
            xAry[i] = ary[i*2];
            yAry[i] = ary[i*2+1];
        }
    }

    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                    RESIZING the shape ... Moving verticies                                     *
     *                                                                                                                 *
     ** ************************************************************************************************************ **/








}
