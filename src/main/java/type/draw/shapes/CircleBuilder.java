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
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import uicontrols.UIColors;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.util.ArrayList;

//import type.celleditors.SectionEditor;

/**
 * This class creates an ellipse. It will create a circle using the alt key
 * or an ellipse without.
 * @author Lowell Stadelman
 */

public class CircleBuilder extends GenericBuilder<FMCircle, CircleBuilder> {
    
    // local variables
    private double deltaX = 0.0;
    private double deltaY = 0.0;
    private double anchorX = 0.0;
    private double anchorY = 0.0;
    
    private String fillColor;
    private String strokeColor;

    public CircleBuilder()
    {
        //no args constructor
    }

    /**
     * Constructor used when new shapes are created, Call this first
     * to create the overlayPane over the area selected.
     * @param c The Canvas
     * //@param graphC The GraphicsContext
     //* @param shapeAry The gbcopyArrayOfFMShapes from DrawTools
     */
    public CircleBuilder(Canvas c, GraphicsContext graphC, Pane overlayPane, SectionEditor editor, String strokeClr, String fillClr)
    {
        super(c, graphC, overlayPane, editor);
        this.strokeColor = strokeClr;
        this.fillColor = fillClr;
    }

    public CircleBuilder(FMCircle fmCircle, Canvas c, GraphicsContext graphC, Pane overlayPane, SectionEditor editor) {
        super(c, graphC, overlayPane, editor);

        anchorX = fmCircle.getX();
        anchorY = fmCircle.getY();
        // width / 2
        deltaX = fmCircle.getRadiusX();
        // height / 2
        deltaY = fmCircle.getRadiusY();

        Ellipse fxCircle = fmCircle.getShape();

        // add mouse actions to the shape
        fxCircle.setOnMousePressed(e -> shapePressed( e, fmCircle, fxCircle ));
        fxCircle.setOnMouseDragged(e -> shapeDragged( e, fmCircle, fxCircle ));
        fxCircle.setOnMouseReleased(e -> shapeReleased(e, fmCircle, fxCircle ));

        // Show the shape in the overlayPane
        getOverlayPane().getChildren().add(fxCircle);
    }


    @Override
    public void zeroDeltas()
    {
        deltaX = 0.0;
        deltaY = 0.0;
    }

    @Override
    public void setAnchors(MouseEvent mouse)
    {
        anchorX = mouse.getSceneX();
        anchorY = mouse.getSceneY();
    }


    /**
     * As the mouse is dragged, create the shape of the
     * ellipse.
     * @param e
     */
    @Override
    public void mouseDragged( MouseEvent e)
    {
        Canvas can = getCanvas();
        GraphicsContext gC = getGc();

        gC.setStroke(Color.web(strokeColor));
        gC.setFill(Color.web(fillColor));

        //System.out.println(" - mouseDragged in circleBuilder");
        DrawTools draw = DrawTools.getInstance();
        if(draw.getShapeNotSelected())
        {
            // Creating a new shape. Set to true
            setNewShape(true);

            // Clear the rectangle of any previous shapes
            getGc().clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            // Set the deltas to the value between current mouse position and anchors
            deltaX = e.getSceneX() - anchorX;
            deltaY = e.getSceneY() - anchorY;

            // Set the size of the circle
            if ((deltaX + deltaY) < 0)
            {
                deltaX = deltaX * -1;
                deltaY = deltaY * -1;
                anchorX = e.getSceneX();
                anchorY = e.getSceneY();

                if (e.isAltDown()) // create circle
                {
                    gC.strokeOval(anchorX, anchorY, deltaX, deltaX);

                } else { // create an ellipse

                    gC.strokeOval(anchorX, anchorY, deltaX, deltaY);
                }
            } else if (deltaX < 0) {
                deltaX = deltaX * -1;
                anchorX = e.getSceneX();
                if (e.isAltDown())
                {
                    gC.strokeOval(anchorX, anchorY, deltaX, deltaX);

                } else {

                    gC.strokeOval(anchorX, anchorY, deltaX, deltaY);
                }
            } else if (deltaY < 0) {
                deltaY = deltaY * -1;
                anchorY = e.getSceneY();

                if (e.isAltDown())
                {
                    gC.strokeOval(anchorX, anchorY, deltaX, deltaX);

                } else {

                    gC.strokeOval(anchorX, anchorY, deltaX, deltaY);
                }
            } else {

                if (e.isAltDown())
                {
                    gC.strokeOval(anchorX, anchorY, deltaX, deltaX);

                } else {

                    gC.strokeOval(anchorX, anchorY, deltaX, deltaY);
                }
            }
        }
        e.consume();
    }

    /**
     * When the mouse is released, set the data in FMShape, store the fmShape in the gbcopyArrayOfFMShapes, display
     * the shape in the right pane.
     * @param mouse
     */
    @Override
    public void mouseReleased(MouseEvent mouse)
    {
        DrawTools draw = DrawTools.getInstance();
        if(draw.getShapeNotSelected())
        {
            Canvas can = getCanvas();
            GraphicsContext gC = getGc();

            gC.clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            if ((isNewShape()) && (deltaX + deltaY) != 0) {
                FMCircle fmCirc;

                if (mouse.isAltDown()) { // Create a Circle {
                    fmCirc = new FMCircle(
                            anchorX + (deltaX / 2),
                            anchorY + (deltaY / 2),
                            deltaX / 2,
                            deltaY / 2,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size() - 1
                    );

                } else {
                    fmCirc = new FMCircle(
                            anchorX + (deltaX / 2),
                            anchorY + (deltaY / 2),
                            deltaX / 2,
                            deltaY / 2,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size() - 1
                    );
                }

                // add shape to gbcopyArrayOfFMShapes
                gbcopyArrayOfFMShapes.add(fmCirc);


                // get the shape from the saved shape
                Ellipse fxEl = fmCirc.getShape();

                // add mouse actions to the shape
                fxEl.setOnMousePressed(e -> shapePressed( e, fmCirc, fxEl ));
                fxEl.setOnMouseDragged(e -> shapeDragged( e, fmCirc, fxEl ));
                fxEl.setOnMouseReleased(e -> shapeReleased(e, fmCirc, fxEl ));

                // Show the shape in the overlayPane
                getOverlayPane().getChildren().add( fxEl);

                // Add the scaled version of the shape in the right pane


                int wd = (int) getOverlayPane().getWidth();
                int ht = (int) getOverlayPane().getHeight();

                //getRightPane().getChildren().add( fmCirc.getScaledShape());
                editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, wd, ht );

                setNewShape(false);
            }
        }
        DrawTools.setShapeNotSelected(true);
        mouse.consume();
    }


    /** ************************************************************************************************************ **
     *                                                                                                                 *
                                                    CUT & PASTE ACTIONS
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/


    /**
     * Overrides the GenericBuilder
     * Adds mouse actions to ellipses in the overlaypane
     * adds it to the overlayPane and adds it to the
     * right pane.
     * @param gs
     */
    @Override
    public Shape editableShapeAction(Shape fxEl, GenericShape gs)
    {
        System.out.println("\n*** editableShapeAction in CircleBuilder ***" +
                "\t - THIS FMCIRCLE: " + ((FMCircle) gs).toString());

        // add mouse actions to the shape
        fxEl.setOnMousePressed(e -> shapePressed( e, (FMCircle) gs, fxEl ));
        fxEl.setOnMouseDragged(e -> shapeDragged( e, (FMCircle) gs, fxEl ));
        fxEl.setOnMouseReleased(e -> shapeReleased(e, (FMCircle) gs, fxEl ));
        return fxEl;
    }

    /**
     * CopyPaste interface copyAction
     * creates a new FMCircle using the mouse current X and Y, and the Shape's current
     * radiusX and radiusY, adds it to arrayOfFMShapes, then clears
     * the nodes.
     * @param mouse
     * @param gs
     */
    @Override
    public void copyAction(MouseEvent mouse, GenericShape gs)
    {
        System.out.println("\t copyAction called in CircleBuilder");

        FMCircle fmCirc;

        fmCirc = new FMCircle( mouse.getSceneX(),
                mouse.getSceneY(), ((FMCircle) gs).getRadiusX(), ((FMCircle) gs).getRadiusY(), gs.getStrokeWidth(),
                gs.getStrokeColor(), gs.getFillColor(), gbcopyArrayOfFMShapes.size() );

        gbcopyArrayOfFMShapes.add(fmCirc);
        setPaste(true);
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();

        mouse.consume();
    }


    /** ************************************************************************************************************ **
    *                                                                                                                 *
                                                     PANNING the shape
                                                   and other mouse actions
    *                                                                                                                 *
    ** ************************************************************************************************************ ***/

    // unique to JavaFX Polyline and Polygon classes are lists of observable points.
    // provides great convenience for manipulation and rotation.
    //ObservableList obsvPts;
    /**
     * Gets the x and y locations of the mouse in the Shape and the pane.
     * or, if right mouse button is selected goes to shapeRightPress.
     * @param mouse The mouse MouseEvent
     */
    @Override
    public void shapePressed(MouseEvent mouse, GenericShape gs, Shape fxshape)
    {
        fxshape.setStrokeWidth(fxshape.getStrokeWidth() + 2);

        Ellipse fxCircle = (Ellipse) fxshape;
        fxL = fxCircle;
        fmC = (FMCircle) gs;

        DrawTools.setShapeNotSelected(false);
        // Clear the resize nodes if they are present
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();
        // Clear others from builderList
        if(!mouse.isShiftDown()) {
            draw.clearListeners();
        }

        draw.getFillProperty().addListener(this::fillChanged);
        draw.getStrokeProperty().addListener(this::strokeChanged);
        gs.setStrokeColor(draw.getStrokeProperty().getValue());
        gs.setFillColor(draw.getFillProperty().getValue());


        /* If shape right mouse click create resize nodes **/
        if(mouse.isSecondaryButtonDown()) {
            shapeRightPress(mouse, gs, fxshape);
        } else {
            // used in shapeDragged
            deltaX = gs.getX() - mouse.getSceneX();
            deltaY = gs.getY() - mouse.getSceneY();
        }
        mouse.consume();
    }

    // ***************** LISTENERS ********************

    private Ellipse fxL = new Ellipse();
    private FMCircle fmC = new FMCircle();
    public void strokeChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
        fxL.setStroke(Color.web(newVal));
        fmC.setStrokeColor(newVal);
        strokeColor = newVal;
    }

    public void fillChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
        fxL.setFill(Color.web(newVal));
        fmC.setFillColor(newVal);
        fillColor = newVal;
    }

    @Override
    public void clearListeners() {
        DrawTools draw = DrawTools.getInstance();
        draw.getFillProperty().removeListener(this::strokeChanged);
        draw.getStrokeProperty().removeListener(this::fillChanged);
    }


    @Override
    public void shapeDragged(MouseEvent mouse, GenericShape fmCirc, Shape shape)
    {
        if(mouse.isPrimaryButtonDown())
        {
            ((Ellipse) shape).setCenterX(mouse.getSceneX() + deltaX);
            ((Ellipse) shape).setCenterY(mouse.getSceneY() + deltaY);
        }
        mouse.consume();
    }

    @Override
    public void shapeReleased(MouseEvent mouse, GenericShape gs, Shape shape) {
        shape.setStrokeWidth(shape.getStrokeWidth() - 2);

        // update this fmCircle
        gs.setX( ((Ellipse) shape).getCenterX() );
        gs.setY( ((Ellipse) shape).getCenterY() );


        double origWd = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getWd();
        double origHt = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getHt();
        editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, origWd, origHt);
    }

    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                    RESIZING the circle ... Moving verticies                                     *
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    /**
     * Helper method.
     * Returns an gbcopyArrayOfFMShapes of Point2D x and y coordinates for the
     * center, and the outer tips of radiusX and radiusY.
     * Even numbers are horizontal verticies, odd are even.
     * 0 is an even number in this case
     * @return
     */
    //@Override
    private Point2D[] getPoints(Shape shape)
    {
        Ellipse c = (Ellipse) shape;

        //System.err.println("\n ### GET_POINTS centerY of ellipse/circle: " + c.getCenterY() + " ###");

        Point2D[] points = new Point2D[4];
        points[0] = new Point2D(c.getCenterX(), c.getCenterY() + c.getRadiusY()); // h verticy
        points[1] = new Point2D(c.getCenterX() - c.getRadiusX(), c.getCenterY()); // v verticy
        points[2] = new Point2D(c.getCenterX(), c.getCenterY() - c.getRadiusY()); // h verticy
        points[3] = new Point2D(c.getCenterX() + c.getRadiusX(), c.getCenterY()); // v verticy

        return points;
    }


    /**
     * Gets the nodes/resize handles for a shape. Resize handles are left and right for Width "H" adjustment
     * and Top and Bottom height "V" adjustment
     * @param thisShape This shape
     * @return  Returns an arraylist of handles located left, right and top, bottom.
     */
    protected ArrayList<Circle> getHandles(GenericShape gs,  Shape thisShape )
    {
        ArrayList<Circle> points = new ArrayList<>(4);
        Point2D[] pt = getPoints(thisShape);
        Circle c;

        for(int i = 0; i < 4; i++ )
        {
            c = new Circle(pt[i].getX(), pt[i].getY(), 4, Color.web(UIColors.HIGHLIGHT_ORANGE));
            int finalI = i;
            c.setOnMousePressed(e -> verticyPressed(finalI, e, thisShape));
            c.setOnMouseReleased(e -> verticyReleased(gs));
            points.add(c);
        }

        points.get(0).setOnMouseDragged(e -> verticyVDragged(e, gs, points.get(0), points.get(2), points.get(1), points.get(3), thisShape)); // left node / H adjustment
        points.get(1).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(1), points.get(3), points.get(0), points.get(2), thisShape)); // top node / W adjustment
        points.get(2).setOnMouseDragged(e -> verticyVDragged(e, gs, points.get(2), points.get(0), points.get(1), points.get(3), thisShape)); // right node / H adjust
        points.get(3).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(3), points.get(1), points.get(0), points.get(2), thisShape)); // bottom node / W adjust


        System.out.println(" Checking point[1] " + points.get(1).toString());


        return points;
    }


    @Override
    public void verticyPressed(int idx, MouseEvent mouse, Shape s)
    {
        DrawTools.setShapeNotSelected(false);
        mouse.consume();
    }

    @Override
    public void verticyHDragged(MouseEvent mouse, GenericShape gs, Shape vertH, Shape vertHOther, Shape vertH1, Shape vertH2, Shape el)
    {
        if(! mouse.isSecondaryButtonDown() )
        {
            System.out.println(" *** verticyH dragged *** ");

            ((Circle) vertH).setCenterX(mouse.getSceneX());

            double vert = ((Circle) vertH).getCenterX();
            double other = ((Circle) vertHOther).getCenterX();
            double centX = (vert + other) / 2;
            double radX = Math.abs(vert - centX);

            // set centerY of h1 and h2 to centerY of ellipse
            ((Circle) vertH1).setCenterX(centX);
            ((Circle) vertH2).setCenterX(centX);

            // set centerY of ellipse to (vertH + vertVOther) / 2
            ((Ellipse) el).setCenterX(centX);
            // set radiusY of ellipse
            ((Ellipse) el).setRadiusX(radX);

            // Update the fmCircle
            ((FMCircle)gs).setX( centX );
            ((FMCircle)gs).setRadiusX( radX );
        }
        mouse.consume();
    }

    @Override
    public void verticyVDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertOther, Shape vertV1, Shape vertV2, Shape el)
    {
        if(! mouse.isSecondaryButtonDown() )
        {
            // set verticy to vertical movement of the mouse
            ((Circle) vertV).setCenterY(mouse.getSceneY());

            double vert = ( (Circle) vertV ).getCenterY();
            double other = ( (Circle) vertOther ).getCenterY();
            double centY = ( vert + other ) / 2;
            double radY = Math.abs( vert - centY );

            // set centerY of h1 and h2 to centerY of ellipse
            ((Circle) vertV1).setCenterY( centY );
            ((Circle) vertV2).setCenterY( centY );

            // set centerY of ellipse to (vertV + vertVOther) / 2
            ((Ellipse) el).setCenterY( centY );
            // set radiusY of ellipse
            ((Ellipse) el).setRadiusY( radY );

            // Update the fmCircle
            ((FMCircle)gs).setY( centY );
            ((FMCircle)gs).setRadiusY( radY );
        }
        mouse.consume();
    }

    /**
     * Use for shapes that are polygons or for lines
     *
     * @param mouse
     * @param gs
     * @param vertArry An array containing verticies
     * @param shape    The Shape or Polygon being moved.
     */
    @Override
    public void verticyXYDragged(MouseEvent mouse, GenericShape gs, ArrayList<Circle> vertArry, Shape shape) {
        // Not used
    }
}
