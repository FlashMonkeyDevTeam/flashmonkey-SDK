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


import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import uicontrols.UIColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.util.ArrayList;

/**
 * This class creates a Rectangle object. It will create a square using the Rectangle
 * class by using the alt key.
 *
 * @author Lowell Stadelman
 */
public class RectangleBuilder extends GenericBuilder<FMRectangle, RectangleBuilder>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RectangleBuilder.class);
    
    // local variables
    // the initial x location on mouse click
    private double anchorX = 0.0;
    // the initial y location on mouse click
    private double anchorY = 0.0;
    // width
    private double deltaX = 0.0;
    // height
    private double deltaY = 0.0;
    
    private String fillColor;
    private String strokeColor;

    public RectangleBuilder()
    {
        //no args constructor
    }
    
    /**
     * Constructor used when new shapes are created, Rectangle is
     * Also used to define the
     * popUp panes size. Called first
     * to create the overlayPane over the area selected.
     * @param c
     * @param graphC
     * @param overlayPane
     * @param editor
     * @param strokeClr
     * @param fillClr
     */
    public RectangleBuilder(Canvas c, GraphicsContext graphC, Pane overlayPane, SectionEditor editor, String strokeClr, String fillClr)
    {
        super(c, graphC, overlayPane, editor);
    
        LOGGER.info("RectangleBuilder full constructor called strokeColor: {}", strokeClr);
        
        this.strokeColor = strokeClr;
        this.fillColor = fillClr;
    }
    
    /**
     * Basic constructor without setting the stroke color or fill color
     * Called by FMRectangle getBuilder
     * @param fmRect
     * @param c
     * @param graphC
     * @param pane
     * @param editor
     */
    public RectangleBuilder(FMRectangle fmRect, Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor)
    {
        super(c, graphC, pane, editor);
        Rectangle fxRect = fmRect.getShape();
    
        LOGGER.info("RectangleBuilder basic constructor called ");
        
        // add mouse actions to the shape when constuctor is called
        // to edit existing shapes
        fxRect.setOnMousePressed( f -> shapePressed( f, fmRect, fxRect));
        fxRect.setOnMouseDragged( f -> shapeDragged( f, fmRect, fxRect));
        fxRect.setOnMouseReleased(f -> shapeReleased(f, fmRect, fxRect));

        getOverlayPane().getChildren().add( fxRect);

        setNewShape(true); // Not sure if it should be true or false ...
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
     * rectangle.
     * @param e
     */
    @Override
    public void mouseDragged( MouseEvent e) {

        Canvas can = getCanvas();
        GraphicsContext gC = getGc();

        gC.setStroke(Color.web(strokeColor));
        gC.setFill(Color.web(fillColor));
        
        DrawTools draw = DrawTools.getInstance();
        if(draw.getShapeNotSelected()) {
            setNewShape(true);
    
            getGc().clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());
    
            deltaX = e.getSceneX() - anchorX;
            deltaY = e.getSceneY() - anchorY;
    
            if ((deltaX + deltaY) < 0)
            {
                deltaX = deltaX * -1;
                deltaY = deltaY * -1;
                anchorX = e.getSceneX();
                anchorY = e.getSceneY();

                if (e.isAltDown()) // create rectangle
                {
            gC.strokeRect(anchorX, anchorY, deltaX, deltaX);

                } else { // create an rectangle

                    gC.strokeRect(anchorX, anchorY, deltaX, deltaY);
                }
            } else if (deltaX < 0) {
                deltaX = deltaX * -1;
                anchorX = e.getSceneX();
                if (e.isAltDown())
                {
                    gC.strokeRect(anchorX, anchorY, deltaX, deltaX);

                } else {

                    gC.strokeRect(anchorX, anchorY, deltaX, deltaY);
                }
            } else if (deltaY < 0) {
                deltaY = deltaY * -1;
                anchorY = e.getSceneY();

                if (e.isAltDown())
                {
                    gC.strokeRect(anchorX, anchorY, deltaX, deltaX);

                } else {

                    gC.strokeRect(anchorX, anchorY, deltaX, deltaY);
                }
            } else {

                if (e.isAltDown())
                {
                    gC.strokeRect(anchorX, anchorY, deltaX, deltaX);

                } else {
                    gC.strokeRect(anchorX, anchorY, deltaX, deltaY);
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
    public void mouseReleased( MouseEvent mouse)
    {
        DrawTools draw = DrawTools.getInstance();
        if(draw.getShapeNotSelected())
        {
            Canvas can = getCanvas();
            GraphicsContext gC = getGc();

            gC.clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            if (isNewShape() && (deltaX + deltaY) != 0)
            {
                FMRectangle fmRect;

                if (mouse.isAltDown()) // Creates a square
                {
                    fmRect = new FMRectangle(
                            anchorX,
                            anchorY,
                            deltaX,
                            deltaX,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size() - 1
                    );
                } else {
                    fmRect = new FMRectangle(
                            anchorX,
                            anchorY,
                            deltaX,
                            deltaY,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size() - 1
                    );
                }

                // add shape to gbcopyArrayOfFMShapes
                gbcopyArrayOfFMShapes.add(fmRect);

                // get the shape from the saved shape
                Rectangle fxRect = fmRect.getShape();

                // add mouse actions to the shape
                fxRect.setOnMousePressed( f -> shapePressed( f, fmRect, fxRect));
                fxRect.setOnMouseDragged( f -> shapeDragged( f, fmRect, fxRect));
                fxRect.setOnMouseReleased(f -> shapeReleased(f, fmRect, fxRect));

                // Show the shape in the overlayPane
                getOverlayPane().getChildren().add( fxRect);


                // show the shape in the overlayPane
                //getOverlayPane().getChildren().add( fxRect);
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
     *                                          CUT & PASTE ACTIONS
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/



    /**
     * Overrides the GenericBuilder
     * Adds mouse actions to ellipses in the overlaypane
     * adds it to the overlayPane and adds it to the
     * right pane.
     * Called by genericBuilder
     * @param fxRect The javaFX Rectangle object
     * @param gs GenericShape object
     */
    @Override
    public Shape editableShapeAction(Shape fxRect, GenericShape gs) {
        // add mouse actions to the shape
        fxRect.setOnMousePressed(f -> shapePressed( f, (FMRectangle) gs, fxRect));
        fxRect.setOnMouseDragged(f -> shapeDragged( f, (FMRectangle) gs, fxRect));
        fxRect.setOnMouseReleased(f -> shapeReleased(f, (FMRectangle) gs, fxRect));

        return fxRect;
    }

    /**
     * <p>CopyPaste interface copyAction:</p>
     * <p>creates a new FMRectangle using the mouse current X and Y, and the Shape's current
     * width and height, adds it to gbcopyArrayOfFMShapes, then clears
     * the nodes.</p>
     * @param mouse
     * @param gs
     */
    @Override
    public void copyAction(MouseEvent mouse, GenericShape gs)
    {
        FMRectangle fmRect;

        fmRect = new FMRectangle( mouse.getSceneX(), mouse.getSceneY(),
                ((FMRectangle) gs).getWd(), ((FMRectangle) gs).getHt(), gs.getStrokeWidth(),
                gs.getStrokeColor(), gs.getFillColor(), gbcopyArrayOfFMShapes.size() );

        gbcopyArrayOfFMShapes.add(fmRect);
        setPaste(true);
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();

        mouse.consume();
    }


    /** ************************************************************************************************************ **
    *                                                                                                                  *
    *                                                 PANNING the shape
    *                                               and other mouse actions
    *                                                                                                                  *
    ** ************************************************************************************************************ ***/


    /**
     * Gets the x and y locations of the mouse in the Shape and the pane.
     * or, if right mouse button is selected goes to shapeRightPress.
     * @param mouse The mouse MouseEvent
     */
    @Override
    public void shapePressed(MouseEvent mouse, GenericShape gs, Shape fxShape) {
        fxShape.setStrokeWidth(fxShape.getStrokeWidth() + 2);

        Rectangle fxRect = (Rectangle) fxShape;
        fxL = fxRect;
        fmSq = (FMRectangle) gs;
        //obsvPts =  fxCircle.getPoints();
        DrawTools.setShapeNotSelected(false);
        // Clear the resize nodes if they are present
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();
        // Clear all listeners unless
        if(!mouse.isShiftDown()) {
            draw.clearListeners();
        }

        draw.getFillProperty().addListener(this::fillChanged);
        draw.getStrokeProperty().addListener(this::strokeChanged);


        /* If shape right mouse click create resize nodes **/
        if(mouse.isSecondaryButtonDown()) {
            shapeRightPress(mouse, gs, fxShape);
        } else {
            //gs.setX( ((Rectangle) fxShape).getX());
           // gs.setY( ((Rectangle) fxShape).getY());
            // Used for shape drag
            // set the delta between the shapes x,y location for a point,
            // and the mouse's x,y location. Then use delta x & y later
            // for comparison.
            deltaX = gs.getX() - mouse.getSceneX();
            deltaY = gs.getY() - mouse.getSceneY();
        }
        mouse.consume();
    }



    // ***************** LISTENERS ********************

    private Rectangle fxL = new Rectangle();
    private FMRectangle fmSq = new FMRectangle();

    public void strokeChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
        fxL.setStroke(Color.web(newVal));
        fmSq.setStrokeColor(newVal);
        strokeColor = newVal;
    }

    public void fillChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
        fxL.setFill(Color.web(newVal));
        fmSq.setFillColor(newVal);
        fillColor = newVal;
    }



    @Override
    public void clearListeners() {
        DrawTools draw = DrawTools.getInstance();
        draw.getFillProperty().removeListener(this::fillChanged);
        draw.getStrokeProperty().removeListener(this::strokeChanged);
    }


    @Override
    public void shapeDragged(MouseEvent mouse, GenericShape gs, Shape shape)
    {
        if(mouse.isPrimaryButtonDown())
        {
            ((Rectangle) shape).setX(mouse.getSceneX() + deltaX);
            ((Rectangle) shape).setY(mouse.getSceneY() + deltaY);
        }
        mouse.consume();
    }


    /**
     * On mouse released, Sets the new location in
     * the gbcopyArrayOfFMShapes
     * @param gs
     * @param shape
     */
    @Override
    public void shapeReleased(MouseEvent mouse, GenericShape gs, Shape shape)
    {
        SectionEditor editor = editorRef;
        shape.setStrokeWidth(shape.getStrokeWidth() - 2);
        
        // Update this fmShape/fmRect
        gs.setX( ((Rectangle) shape).getX() );
        gs.setY( ((Rectangle) shape).getY() );

        double origWd = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getWd();
        double origHt = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getHt();
        editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, origWd, origHt);
    }

    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                    RESIZING the Rectangle ... Moving verticies                                  *
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    /**
     * Helper method. Places the points at the rectangles mid-points on each side.
     * - Returns an array of Point2D x and y coordinates for centered on each side
     * center, and the outer tips of radiusX and radiusY.
     * Even numbers are verticle verticies, odd are horizontal vertices.
     * 0 is an even number in this case
     * @return
     */
    //@Override
    private Point2D[] getPoints(Shape shape)
    {
        Rectangle r = (Rectangle) shape;
        Point2D[] points = new Point2D[4];

        double xY = r.getY() + (r.getHeight() / 2);
        double yX = r.getX() + (r.getWidth() / 2);

        points[0] = new Point2D( r.getX(), xY); // h verticy left side
        points[1] = new Point2D( yX, r.getY()); // v verticy top
        points[2] = new Point2D(r.getX() + r.getWidth(), xY); // h verticy right side
        points[3] = new Point2D( yX, r.getY() + r.getHeight()); // v verticy bottom

        return points;
    }


    /**
     * Gets the nodes/resize handles for a shape. Resize handles are left and right for Width "H" adjustment
     * and Top and Bottom height "V" adjustment
     * @param thisShape This shape
     * @return  Returns an arraylist of handles located left, right and top, bottom.
     */
    public ArrayList<Circle> getHandles(GenericShape gs,  Shape thisShape )
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

        points.get(0).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(0), points.get(2), points.get(1), points.get(3), thisShape)); // left node / H adjustment
        points.get(1).setOnMouseDragged(e -> verticyVDragged(e, gs, points.get(1), points.get(3), points.get(0), points.get(2), thisShape)); // top node / W adjustment
        points.get(2).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(2), points.get(0), points.get(1), points.get(3), thisShape)); // right node / H adjust
        points.get(3).setOnMouseDragged(e -> verticyVDragged(e, gs, points.get(3), points.get(1), points.get(0), points.get(2), thisShape)); // bottom node / W adjust

        return points;
    }

    @Override
    public void verticyPressed(int idx, MouseEvent mouse, Shape s)
    {
        DrawTools.setShapeNotSelected(false);
        mouse.consume();
    }

    @Override
    public void verticyHDragged(MouseEvent mouse, GenericShape gs, Shape vertH, Shape vertHOther, Shape vertV1, Shape vertV2, Shape rect)
    {
        if(! mouse.isSecondaryButtonDown() )
        {
            ((Circle) vertH).setCenterX(mouse.getSceneX());

            double vertD = ((Circle) vertH).getCenterX();
            double otherD = ((Circle) vertHOther).getCenterX();
            double wd = Math.abs(vertD - otherD);

            // Update the Rectangle in overlayPane,
            // minX = the lessor of vertH or other.
            double smallest = smallest(otherD, vertD);

            ((Rectangle) rect).setX(smallest);
            ((Rectangle) rect).setWidth(wd);

            // Update the FMRectangle to smallest & width
            gs.setX( smallest );
            ((FMRectangle) gs).setWd(wd);

            // update top and bottom verticies
            double vertWd = ((Rectangle) rect).getX() + (wd / 2);
            ((Circle) vertV1).setCenterX(vertWd);
            ((Circle) vertV2).setCenterX(vertWd);
        }
        mouse.consume();
    }

    /**
     * When top or bottom verticies are dragged
     * @param mouse
     * @param gs
     * @param vertV Top or Bottom
     * @param vertVOther Top or bottom
     * @param vertH1 left
     * @param vertH2 right
     * @param rect
     */
    @Override
    public void verticyVDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertVOther, Shape vertH1, Shape vertH2, Shape rect)
    {
        if(! mouse.isSecondaryButtonDown() )
        {
            // set verticy to vertical movement of the mouse
            ((Circle) vertV).setCenterY(mouse.getSceneY());

            double vertD = ((Circle) vertV).getCenterY();
            double otherD = ((Circle) vertVOther).getCenterY();
            double ht = Math.abs(vertD - otherD);
            // Update the fmRectangle, minY = the lessor of vert or other.
            double smallest = smallest(otherD, vertD);

            ((Rectangle) rect).setY(smallest);
            ((Rectangle) rect).setHeight(ht);

            gs.setY( smallest );
            ((FMRectangle) gs).setHt(ht);

            // update the location of the left and right verticies
            double vertHt = ((Rectangle) rect).getY() + (ht / 2);
            ((Circle) vertH1).setCenterY(vertHt);
            ((Circle) vertH2).setCenterY(vertHt);

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
        // not used
    }

    /**
     * Helper method for FMRectangle, verticy mouse actions. Maintains the upper/ or left, aka smallest
     * verticies despite which is selected by the EncryptedUser.EncryptedUser.
     * @param otherD
     * @param vertD
     * @return
     */
    private double smallest(double otherD, double vertD)
    {
        return ( vertD < otherD ? vertD : otherD );
    }


}
