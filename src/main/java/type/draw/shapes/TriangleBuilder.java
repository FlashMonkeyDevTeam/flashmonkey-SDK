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
import uicontrols.UIColors;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.util.ArrayList;
import java.lang.Math;

/**
 * This class creates a Triangle object.
 *
 * @author Benjamin Boyle
 */
public class TriangleBuilder extends GenericBuilder<FMTriangle, TriangleBuilder> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TriangleBuilder.class);
    //private Polygon fxPolygon = new Polygon();
    
    // The mouse start point
    private double anchorX = 0.0;
    private double anchorY = 0.0;
    
    // Difference from mouse to a point
    private double deltaX = 0.0;
    private double deltaY = 0.0;
    
    private double prevMouseX = 0.0;
    private double prevMouseY = 0.0;
    
    private String fillColor;
    private String strokeColor;
    

	private StringProperty fillProperty = new SimpleStringProperty();
	private StringProperty strokeProperty = new SimpleStringProperty();

	/**
	 * No args constructor
	 */
    public TriangleBuilder() {
        // no args constructor
    }
    
    /**
     * Full Constructor: Includes stroke color and fill color,
     * Sets this shape in the parent GenericBuilder
     * @param c
     * @param graphC
     * @param pane
     * @param editor
	 * @param strokeClr
	 * @param fillClr
     */
    public TriangleBuilder(Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor, String strokeClr, String fillClr)
    {
        super(c, graphC, pane, editor);
        //fxPolygon = new Polygon();
        this.strokeColor = strokeClr;
        this.fillColor = fillClr;
		strokeProperty.setValue(strokeClr);
		fillProperty.setValue(fillClr);
    }
    
    
    /**
     * Basic constructor: Sets this shape in the parent GenericBuilder,
     * creates a builder object using an already existing FMTriangle.
     * Called by getBuilder(...) in FMTriangle.
     * @param fmTri
     * @param c
     * @param graphC
     * @param pane
     * @param editor
     */
    public TriangleBuilder(FMTriangle fmTri, Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor)
    {
        super(c, graphC, pane, editor);
        this.anchorX = fmTri.getX();
        this.anchorY = fmTri.getY();
        
        Polygon fxPoly = fmTri.getShape();
    
        // add mouse actions to the shape when constuctor is called
        // to edit existing shapes
        fxPoly.setOnMousePressed( f -> shapePressed( f, fmTri, fxPoly));
        fxPoly.setOnMouseDragged( f -> shapeDragged( f, fmTri, fxPoly));
        fxPoly.setOnMouseReleased(f -> shapeReleased(f, fmTri, fxPoly));

        strokeProperty.setValue(fmTri.getStrokeColor());
        fillProperty.setValue(fmTri.getFillColor());
        getOverlayPane().getChildren().add( fxPoly);
    
        setNewShape(true); // Not sure if it should be true or false ...
        
        LOGGER.info("Basic constructor called");
    }

    @Override
    public void zeroDeltas() {
        deltaX = 0.0;
        deltaY = 0.0;
    }

    @Override
    public void setAnchors(MouseEvent mouse) {
        anchorX = mouse.getSceneX();
        anchorY = mouse.getSceneY();
    }


    /**
     * As the mouse is dragged, create the shape of the
     * Traingle.
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        Canvas can = getCanvas();
        GraphicsContext gC = getGc();

        gC.setStroke(Color.web(strokeColor));
        gC.setFill(Color.web(fillColor));
        
        DrawTools draw = DrawTools.getInstance();

        // ALT KEY PRESSED
        if (draw.getShapeNotSelected() && e.isAltDown()) {
            setNewShape(true);

            getGc().clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            deltaX = e.getSceneX() - anchorX;
            deltaY = e.getSceneY() - anchorY;

            double[] xList = new double[]{anchorX, anchorX + deltaX, anchorX + (deltaX / 2)};
            double[] yList = new double[]{anchorY + deltaY, anchorY + deltaY, anchorY};
            gC.strokePolygon(xList, yList, 3);
            e.consume();
        }
        // MOUSE PRESSED W/O ALT KEY; Rotates around center of base.
        else if (draw.getShapeNotSelected()) {
            setNewShape(true);

            getGc().clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
                    can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

            deltaX = e.getSceneX() - anchorX;
            deltaY = e.getSceneY() - anchorY;

            // constant delta for base
            double diff = Math.sqrt(3);
            //                              TIP             BASE LEFT?                  BASE RIGHT?
            double[] xList = new double[]{anchorX , e.getSceneX() - (deltaY / diff), e.getSceneX() + (deltaY / diff)};
            double[] yList = new double[]{anchorY , e.getSceneY() + (deltaX / diff), e.getSceneY() - (deltaX / diff)};
            gC.strokePolygon(xList, yList, 3);
            e.consume();

        }
    }

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

            System.out.println("\n *** mouseReleased in FMTriangleBuilder called *** ");

            if (isNewShape() && (deltaX + deltaY) != 0.0) {
                FMTriangle fmTri;

                if (mouse.isAltDown()) {
                    fmTri = new FMTriangle(
                            anchorX,
                            anchorY,
                            deltaX,
                            deltaY,
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size() - 1
                    );
                } else {
                    double diff = Math.sqrt(3);
                    fmTri = new FMTriangle(
                            anchorX,
                            anchorY,
                            mouse.getSceneX() - (deltaY / diff),
                            mouse.getSceneY() + (deltaX / diff),
                            mouse.getSceneX() + (deltaY / diff),
                            mouse.getSceneY() - (deltaX / diff),
                            3,
                            this.strokeColor,
                            this.fillColor,
                            gbcopyArrayOfFMShapes.size() - 1
                    );
                }


                //LOGGER.debug("TriangleBuilder.mouseReleased x1, y1 (" +fmTri.x1 + ", " + fmTri.y1 + ") x2, y2 (" + fmTri.x2 + ") x3, y3 (" + fmTri.y2 + ", " + fmTri.x3 + " " + fmTri.y3 + ")");
                //LOGGER.debug("TriangleBuilder.mouseReleased anchorX, anchorY ( " + anchorX + ", " + anchorY + ")");
                // add shape to gbcopyArrayOfFMShapes
                gbcopyArrayOfFMShapes.add(fmTri);

                // get the shape from the saved shape
                Polygon fxTri = fmTri.getShape();

                // add mouse actions to the shape
                fxTri.setOnMousePressed( e -> shapePressed( e, fmTri, fxTri));
                fxTri.setOnMouseDragged( e -> shapeDragged( e, fmTri, fxTri));
                fxTri.setOnMouseReleased(e -> shapeReleased( e, fmTri, fxTri));

                // show the shape in the overlayPane
                getOverlayPane().getChildren().add( fxTri);
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
    public Shape editableShapeAction(Shape fxTriangle, GenericShape gs) {
        //LOGGER.debug("\n*** editableShapeAction in RectBuilder ***" + "\t - THIS FMRectangle: " + ((FMRectangle) gs).toString());

        // add mouse actions to the shape
        fxTriangle.setOnMousePressed(f -> shapePressed( f, gs, fxTriangle));
        fxTriangle.setOnMouseDragged(f -> shapeDragged( f, gs, fxTriangle));
        fxTriangle.setOnMouseReleased(f -> shapeReleased(f, gs, fxTriangle));

        return fxTriangle;
    }



    /**
     * CopyPaste interface copyAction
     * creates a new FMCircle using the mouse current X and Y, and the Shape's current
     * radiusX and radiusY, adds it to gbcopyArrayOfFMShapes, then clears
     * the nodes.
     * @param mouse
     * @param gs
     */
    @Override
    public void copyAction(MouseEvent mouse, GenericShape gs)
    {
        //LOGGER.debug("\t copyAction called in TriangleBuilder");

        FMTriangle fmTri;

        fmTri = new FMTriangle(mouse.getSceneX(), mouse.getSceneY(),
                ((FMTriangle) gs).getDeltaX(), ((FMTriangle) gs).getDeltaY(), gs.getStrokeWidth(),
                gs.getStrokeColor(), gs.getFillColor(), gbcopyArrayOfFMShapes.size() );

        gbcopyArrayOfFMShapes.add(fmTri);
        setPaste(true);
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();

        mouse.consume();
    }


    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     PANNING the shape
     and other mouse actions
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/



    private ObservableList obsvPts;
    /**
     * Gets the x and y locations of the mouse in the Shape and the pane.
     * or, if right mouse button is selected goes to shapeRightPress.
     * @param mouse The mouse MouseEvent
     */
    @Override
    public void shapePressed(MouseEvent mouse, GenericShape gs, Shape shape)
    {
        DrawTools.setShapeNotSelected(false);
        Polygon tri1 = (Polygon) shape;
        obsvPts = tri1.getPoints();

        shape.setStrokeWidth(shape.getStrokeWidth() + 2);
        fxP = tri1;
        fmT = (FMTriangle) gs;
        // Clear the resize nodes if they are present
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();
        // Clear others from builderList
        if(mouse.isShiftDown()) {
            draw.clearListeners();
        }

        StringProperty fillProp = new SimpleStringProperty(fillColor);
        StringProperty strokeProp = new SimpleStringProperty(strokeColor);
        strokeProp.bind(draw.getStrokeProperty());
        fillProp.bind(draw.getFillProperty());
        draw.getFillProperty().addListener(this::fillChanged);
        draw.getStrokeProperty().addListener(this::strokeChanged);



        /** If shape right mouse click create resize nodes **/
        if(mouse.isSecondaryButtonDown()) {
            shapeRightPress(mouse, gs, shape);
        } else {
            LOGGER.info("\n\n *** mousePrimary button  in TriangleBuilder  ***");
    
            // Used for shape drag
            
            deltaX = gs.getX() - mouse.getSceneX();
            deltaY = gs.getY() - mouse.getSceneY();

            prevMouseX = mouse.getSceneX();
            prevMouseY = mouse.getSceneY();
            //LOGGER.debug("deltaX: " + deltaX + " shapeX: " + gs.getX());
            //LOGGER.debug("deltaY: " + deltaY + " shapeY: " + gs.getY());
        }
        mouse.consume();
    }

    // ***************** LISTENERS ********************

    private Polygon fxP = new Polygon();
    private FMTriangle fmT = new FMTriangle();
    public void strokeChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
        fxP.setStroke(Color.web(newVal));
        fmT.setStrokeColor(newVal);
        strokeColor = newVal;
    }

    public void fillChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
        fxP.setFill(Color.web(newVal));
        fmT.setFillColor(newVal);
        fillColor = newVal;

    }

    @Override
    public void clearListeners() {
        DrawTools draw = DrawTools.getInstance();
        draw.getFillProperty().removeListener(this::strokeChanged);
        draw.getStrokeProperty().removeListener(this::fillChanged);
    }


    @Override
    public void shapeDragged(MouseEvent mouse, GenericShape gs, Shape shape)
    {
        if(mouse.isPrimaryButtonDown());
        {
            // uses the observable list/reference
            // to move shape. :)
            for(int i = 0; i < obsvPts.size()/2; i++) {
                double x = (double) obsvPts.get(i*2);
                double y = (double) obsvPts.get(i*2+1);
                obsvPts.set(i*2, x + (mouse.getSceneX() - prevMouseX));
                obsvPts.set(i*2+1, y + (mouse.getSceneY() - prevMouseY));
            }
            prevMouseX = mouse.getSceneX();
            prevMouseY = mouse.getSceneY();
        }
        mouse.consume();
    }

    /**
     * On mouse released, Updates the new location in
     * the gbcopyArrayOfFMShapes
     * @param gs
     * @param shape
     */
    @Override
    public void shapeReleased(MouseEvent mouse, GenericShape gs, Shape shape)
    {
        SectionEditor editor = editorRef;
        editor.getRightPane().getChildren().clear();
        if(editor.getImageView() != null) {
            editor.getRightPane().getChildren().add(editor.getImageView());
        }
        shape.setStrokeWidth(shape.getStrokeWidth() - 2);
        // Set the points in the traingle
        gs.setPoints(obsvPts);

        // The first shape in the gbcopyArrayOfFMShapes is an FMRectangle
        // with the demensions of the original SnapShot rectangle.
        double origWd = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getWd();
        double origHt = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getHt();
        editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, origWd, origHt);
    }

    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                    RESIZING the shape ... Moving verticies                                     *
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    /**
     * Helper method, gives average of two values
     */
    private Point2D ptsAvg(Point2D... points) {
        double x = 0;
        double y = 0;
        int size = 0;
        for (Point2D point: points) {
            x += point.getX();
            y += point.getY();
            size++;
        }
        return new Point2D(Math.abs(x)/size, Math.abs(y)/size);
    }

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
        ObservableList<Double> rawPoints = ((Polygon) shape).getPoints();

        double[] points = new double[6];
        Point2D[] result = new Point2D[3];
        for(int i = 0; i < rawPoints.size(); i++) {
            points[i] = rawPoints.get(i);
        }
        for(int n = 0; n < points.length/2; n++) {
            result[n] = new Point2D(points[n * 2], points[n * 2 + 1]);
        }

        return result;
    }

    protected ArrayList<Circle> getHandles(GenericShape gs, Shape thisShape )
    {
        ArrayList<Circle> points = new ArrayList<>(3);
        Point2D[] pt = getPoints(thisShape);
        pt = getTranslatedPoints(thisShape, pt);
        Circle c;

        for(int i = 0; i < 3; i++ )
        {
            c = new Circle(pt[i].getX(), pt[i].getY(), 4, Color.web(UIColors.HIGHLIGHT_ORANGE));
            int finalI = i;
            c.setOnMousePressed(e -> verticyPressed(finalI, e, thisShape));
            c.setOnMouseReleased(e -> verticyReleased(gs));
            points.add(c);
        }

        Circle unused = new Circle(0,0, 0);
        points.get(0).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(0), points.get(1), points.get(2), unused, thisShape)); // left node / H adjustment
        points.get(1).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(1), points.get(2), points.get(0), unused, thisShape)); // top node / W adjustment
        points.get(2).setOnMouseDragged(e -> verticyHDragged(e, gs, points.get(2), points.get(0), points.get(1), unused, thisShape)); // right node / H adjust

        //LOGGER.debug(" In FMRectangleBuilder() Checking point[1] " + points.get(1).toString());

        return points;
    }

    @Override
    public void verticyPressed(int idx, MouseEvent mouse, Shape s)
    {
        DrawTools.setShapeNotSelected(false);
        mouse.consume();
    }

    @Override
    public void verticyXYDragged(MouseEvent mouse, GenericShape gs, ArrayList<Circle> vertArray, Shape shape)
    {
        if(! mouse.isSecondaryButtonDown() && ! mouse.isAltDown())
        {
            //System.out.println(" *** verticyH dragged *** ");

            double initialX = vertArray.get(0).getCenterX();
            double initialY = vertArray.get(0).getCenterY();

            vertArray.get(0).setCenterX(mouse.getSceneX());
            vertArray.get(0).setCenterY(mouse.getSceneY());

            Point2D[] points = getPoints(shape);
            points = getTranslatedPoints(shape, points);

            // Finds point in shape's points array and changes those positions to new vertO point values
            for (int i = 0; i < points.length; i++)
            {
                if (points[i].getX() == initialX && points[i].getY() == initialY) {
                    ((Polygon) shape).getPoints().set(i*2, mouse.getSceneX());
                    ((Polygon) shape).getPoints().set(i*2+1, mouse.getSceneY());
                }
            }
        }
        // TODO: Get rotation working properly

        else if (! mouse.isSecondaryButtonDown() && mouse.isAltDown()) // Rotate around selected point
        {
            //LOGGER.debug(" *** Shape rotated *** ");

            Point2D[] points = getPoints(shape);
            points = getTranslatedPoints(shape, points);
            Point2D centerPoint = center(points);

            Point2D mousePosition = new Point2D(mouse.getSceneX(), mouse.getSceneY());

            double pivotX = vertArray.get(0).getCenterX();
            double pivotY = vertArray.get(0).getCenterY();

            double result = Math.atan2(mousePosition.getY() - pivotY, mousePosition.getX() - pivotX) -
                    Math.atan2(centerPoint.getY() - pivotY, centerPoint.getX() - pivotX);

            result = result * (180 / Math.PI);

            System.out.println(result);

            int anchorIndex = 0;
            for(int i = 0; i < points.length; i++) {
                if(points[i].getX() == pivotX && points[i].getY() == pivotY) {
                    anchorIndex = i;
                }
            }

            shape.getTransforms().add(new Rotate(result, pivotX, pivotY));
            double[] endPoints = pointToDouble(getPoints(shape));

            shape.getTransforms().removeAll();

            for(int i = 0; i < endPoints.length; i++) {
                ((Polygon) shape).getPoints().set(i, endPoints[i]);
            }

            vertArray.get(1).setCenterX(points[(anchorIndex+1)%3].getX());
            vertArray.get(1).setCenterY(points[(anchorIndex+1)%3].getY());
            vertArray.get(2).setCenterX(points[(anchorIndex+2)%3].getX());
            vertArray.get(2).setCenterY(points[(anchorIndex+2)%3].getY());
        }

        mouse.consume();
    }

    @Override
    // Unused
    public void verticyVDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertVOther, Shape vertH1, Shape vertH2, Shape rect) {
        // not used
    }

    @Override
    public void verticyHDragged(MouseEvent mouse, GenericShape gs, Shape vertH, Shape vertHOther, Shape vertH1, Shape vertH2, Shape shape) {
        // not used
    }

    private Point2D center(Point2D... points) {
        double finalX = 0, finalY = 0, currX = 0, currY = 0;
        for (int i = 0; i < points.length; i++) {
            currX += points[i].getX();
            currY += points[i].getY();
        }
        finalX = currX / points.length;
        finalY = currY / points.length;

        return new Point2D(finalX, finalY);
    }

    private Point2D[] getTranslatedPoints(Shape s, Point2D[] points) {
        Point2D[] result = new Point2D[points.length];
        for(int i = 0; i < points.length; i++) {
            result[i] = s.localToParent(points[i]);
        }
        return result;
    }

    private double[] pointToDouble(Point2D... points) {
        double[] total = new double[points.length * 2];
        for(int i = 0; i < points.length; i++){
            total[i*2] = points[i].getX();
            total[i*2+1] = points[i].getY();
        }
        return total;
    }
}

