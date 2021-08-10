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
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;
import uicontrols.UIColors;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * For polygons
 */
public class PolygonBuilder extends GenericBuilder<FMPolygon, PolygonBuilder> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PolygonBuilder.class);



	// The mouse start point
	private double anchorX = 0.0;
	private double anchorY = 0.0;

	private double prevMouseX = 0.0;
	private double prevMouseY = 0.0;

	private String fillColor;
    private String strokeColor;

	private StringProperty fillProperty = new SimpleStringProperty();
	private StringProperty strokeProperty = new SimpleStringProperty();

	private Polygon fxPolygon = new Polygon();
	// used to show the polygon as being drawn
	private Polyline fxLine;
	private FMPolygon fmPolygon;

    protected int verticyIdx;


	/**
	 * No args constructor
	 */
	public PolygonBuilder() { /* no args constructor */}


	/**
	 * Full Constructor: Includes stroke color and fill color,
	 *      * Sets this shape in the parent GenericBuilder
	 * @param c
	 * @param graphC
	 * @param overlayPane
	 * @param editor
	 * @param strokeClr
	 * @param fillClr
	 */
	public PolygonBuilder(Canvas c, GraphicsContext graphC, Pane overlayPane, SectionEditor editor, String strokeClr, String fillClr) {
		super(c, graphC, overlayPane, editor);
		fxPolygon = new Polygon();
		this.strokeColor = strokeClr;
		this.fillColor = fillClr;
		strokeProperty.setValue(strokeClr);
		fillProperty.setValue(fillClr);
	}
	
	/**
	 * /**
	 *      * Basic constructor: Sets this shape in the parent GenericBuilder,
	 *      * creates a builder object using an already existing FMPolygon.
	 *      * Called by getBuilder(...) in FMTriangle.
	 * @param fmPoly
	 * @param c
	 * @param graphC
	 * @param pane
	 * @param editor
	 */
	public PolygonBuilder(FMPolygon fmPoly, Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor) {
		super(c, graphC, pane, editor);
        fxPolygon = fmPoly.getShape();

		this.anchorX = fmPoly.getX();
		this.anchorY = fmPoly.getY();
		

		// add mouse actions to the shape when constuctor is called
		// to edit existing shapes
		fxPolygon.setOnMousePressed( f -> shapePressed(f, fmPoly, fxPolygon));
		fxPolygon.setOnMouseDragged( f -> shapeDragged(f, fmPoly, fxPolygon));
		fxPolygon.setOnMouseReleased( f -> shapeReleased(f, fmPoly, fxPolygon));

		strokeProperty.setValue(fmPoly.getStrokeColor());
		fillProperty.setValue(fmPoly.getFillColor());

		getOverlayPane().getChildren().add( fxPolygon);
		
		//setNewShape(true); // Not sure if it should be true or false ...
	}


	@Override
	public void zeroDeltas() {

	}

	@Override
    public void setAnchors(MouseEvent mouse) {
		anchorX = mouse.getSceneX();
		anchorY = mouse.getSceneY();
	}



	/**
	 * When the mouse is pressed in the overlayArea. If (copy=true), create a shape, else
	 * create a new Polygon.
	 * If alt is NOT down, start a new shape. Clear zero's
	 * else, set zero's to last point.
	 * @param mouse
	 */
	public void mousePressed(MouseEvent mouse) {
		DrawTools draw = DrawTools.getInstance();
		GraphicsContext gC = getGc();
		gC.setStroke(Color.web(strokeColor));
		gC.setFill(Color.web(fillColor));

		if (paste) {
			System.out.println("\t - paste action called ***");
			// Paste the copy
			pasteAction(mouse);
			draw.getOverlayScene().setCursor(Cursor.DEFAULT);
			paste = false;
        } else if (draw.getShapeNotSelected()) { // Avoids conflict between new shape, shape dragged, and shape resized
			// Clear the resize nodes if they are present
			System.out.println("is dt null: " + (this == null));

			draw.clearNodes();
			setNewShape(true);

            // Set the shape start point x and y
			if( anchorX+anchorY == 0.0) {
				fxLine = new Polyline();
				//fxLine.strokeProperty().bind(draw.getStrokeProperty());
				fxPolygon = new Polygon();
				fxPolygon.setFill(Color.web(fillProperty.getValue()));
				fxPolygon.setStroke(Color.web(strokeProperty.getValue()));
				fxPolygon.setStrokeWidth(3);
				setAnchors(mouse);
				fxLine.getPoints().addAll(anchorX, anchorY);
				System.out.println("alt is not down. setting anchors to mouse");
			}
			setAnchors(mouse);

			LOGGER.debug("mousePressed, numPOitns: {}", fxLine.getPoints().size());
			mouse.consume();
		}
	}


	
	/**
	 * Create new polygon segment
	 * As the mouse is dragged, create the shape of the
	 * line.
	 * DeltaX and DeltaY for a shape, are different than for a line or polygon
	 * @param e
	 */
	@Override
	public void mouseDragged(MouseEvent e) {

		Canvas can = getCanvas();
		GraphicsContext gC = getGc();

		DrawTools draw = DrawTools.getInstance();
		if (draw.getShapeNotSelected() ) {
				//setNewShape(true);

				getGc().clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
						can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

				double[] xList = {anchorX, e.getSceneX()};
				double[] yList = {anchorY, e.getSceneY()};
				gC.strokePolyline(xList, yList, 2);
		}
		e.consume();
	}
	
    /**
     * When the mouse is released, set the data in FMShape, store the fmShape in the gbcopyArrayOfFMShapes, display
     * the shape in the right pane.
     * @param mouse
     */
	@Override
	public void mouseReleased(MouseEvent mouse) {
		DrawTools draw = DrawTools.getInstance();
		LOGGER.debug("mouseReleased called in PolygonBuilder");
		if(draw.getShapeNotSelected() ) {
			Canvas can = getCanvas();
			GraphicsContext gC = getGc();

			gC.clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
					can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

			if(isNewShape()) {
				// if alt is down we create a line
				// while the user is still creating the polygon
				fxLine.getPoints().addAll(mouse.getX(), mouse.getY());

				if ( ! mouse.isAltDown()) {

					double[] pts = Stream.of(fxLine.getPoints().toArray()).mapToDouble(o -> Double.parseDouble(o.toString())).toArray();

					System.out.println("FMPolygon pts: ");
					for(double d : pts) {
						System.out.print(d + ", ");
					}

					if (fxLine.getPoints().size() > 4) {
						//anchorX = 0.0;
						//anchorY = 0.0;
						setNewShape(false);
						fxPolygon.getPoints().addAll(fxLine.getPoints());
						fxPolygon.setStrokeWidth(3);
						fmPolygon = new FMPolygon(
								pts,
								3,
								this.strokeProperty.getValue(),
								this.fillProperty.getValue(),
								gbcopyArrayOfFMShapes.size());
						gbcopyArrayOfFMShapes.add(fmPolygon);

						Polygon fxPolygon = fmPolygon.getShape();

						fxPolygon.setOnMousePressed( f -> shapePressed( f, fmPolygon, fxPolygon));
						fxPolygon.setOnMouseDragged( f -> shapeDragged( f, fmPolygon, fxPolygon));
						fxPolygon.setOnMouseReleased(f -> shapeReleased(f, fmPolygon, fxPolygon));
						getOverlayPane().getChildren().remove(fxLine);
						getOverlayPane().getChildren().add(fxPolygon);

						// Add the scaled version of the shape in the right pane
						int wd = (int) getOverlayPane().getWidth();
						int ht = (int) getOverlayPane().getHeight();

						editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, wd, ht );
						setNewShape(false);
						DrawTools.setShapeNotSelected(true);
						anchorX = 0.0;
						anchorY = 0.0;

						LOGGER.debug("num points in fxLine: {}", fxLine.getPoints().size());
						LOGGER.debug("num points in fxPolygon: {}", fxPolygon.getPoints().size());
						LOGGER.debug("num points in fmPolygon: {}", fmPolygon.points.length);
					}
				}
				else if( ! getOverlayPane().getChildren().contains(fxLine)) {
					getOverlayPane().getChildren().add(fxLine);
				}
			}
		}
		LOGGER.debug("mouseReleased numPOints: {}", fxPolygon.getPoints().size());
		mouse.consume();
	}

    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                          CUT & PASTE ACTIONS
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/

	/**
	 * The capability to change a shapes demensions.
	 *
	 * @param shape
	 * @param gs
	 * @return
	 */
	@Override
	protected Shape editableShapeAction(Shape shape, GenericShape gs) {
		fxPolygon.setOnMousePressed(f -> shapePressed( f, gs, fxPolygon));
		fxPolygon.setOnMouseDragged(f -> shapeDragged( f, gs, fxPolygon));
		fxPolygon.setOnMouseReleased(f -> shapeReleased(f, gs, fxPolygon));

		return fxPolygon;
	}

	/**
	 * Creates the handles for a shape. The circles the EncryptedUser.EncryptedUser
	 * uses to changes a shapes height, and width with.
	 *
	 * @param gs
	 * @return The arrayList of circles
	 */
	@Override
    public void copyAction(MouseEvent mouse, GenericShape gs) {
        FMPolygon fmPolygon;

		fmPolygon = new FMPolygon(
                gs.getDblPts(), gs.getStrokeWidth(),
                gs.getStrokeColor(), gs.getFillColor(), gbcopyArrayOfFMShapes.size() );

        gbcopyArrayOfFMShapes.add(fmPolygon);
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


	ObservableList obsvPts;
    /**
     * Gets the x and y locations of the mouse in the Shape and the pane.
     * or, if right mouse button is selected goes to shapeRightPress.
     * @param mouse The mouse MouseEvent
     */
	@Override
	public void shapePressed(MouseEvent mouse, GenericShape gs, Shape fxShape) {

		LOGGER.debug("shapePressed called");
		// if not adding to the existing line
		if(!mouse.isAltDown()) {
			fxShape.setStrokeWidth(fxShape.getStrokeWidth() + 2);
			Polygon fxPolygon1 = (Polygon) fxShape;
			fxP = fxPolygon1;
			obsvPts = fxPolygon1.getPoints();

			DrawTools.setShapeNotSelected(false);
			// Clear the resize nodes if they are present
			DrawTools draw = DrawTools.getInstance();
			draw.clearNodes();
			// Clear all listeners unless
			if( ! mouse.isShiftDown()) {
				draw.clearListeners();
			}

			draw.getFillProperty().addListener(this::fillChanged);
			draw.getStrokeProperty().addListener(this::strokeChanged);

			/* If fxShape right mouse click create resize nodes **/
			if (mouse.isSecondaryButtonDown()) {
				shapeRightPress(mouse, gs, fxShape);
				System.out.println("after shapeRightPress");
			} else {

				prevMouseX = mouse.getSceneX();
				prevMouseY = mouse.getSceneY();
			}

			mouse.consume();
		}
	}

	// ***************** LISTENERS ********************

	private static Polygon fxP = new Polygon();
	public  void strokeChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
		gbcopyArrayOfFMShapes.get(fmPolygon.getShapeAryIdx()).setStrokeColor(newVal);
		fxP.setStroke(Color.web(newVal));
	}

	public void fillChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
		gbcopyArrayOfFMShapes.get(fmPolygon.getShapeAryIdx()).setFillColor(newVal);
		fxP.setFill(Color.web(newVal));
	}

	@Override
	public void clearListeners() {
		DrawTools draw = DrawTools.getInstance();
		draw.getFillProperty().removeListener(this::strokeChanged);
		draw.getStrokeProperty().removeListener(this::fillChanged);
	}

	@Override
	public void shapeDragged(MouseEvent mouse, GenericShape gs, Shape fxShape) {
		if(!mouse.isAltDown()) {
			if (mouse.isPrimaryButtonDown()) ;
			{
				// uses the observable list/reference
				// to move shape. :)
				Polygon fxPolygon = (Polygon) fxShape;
				obsvPts = fxPolygon.getPoints();
				for (int i = 0; i < obsvPts.size() / 2; i++) {
					double x = (double) obsvPts.get(i * 2);
					double y = (double) obsvPts.get(i * 2 + 1);
					obsvPts.set(i * 2, x + (mouse.getSceneX() - prevMouseX));
					obsvPts.set(i * 2 + 1, y + (mouse.getSceneY() - prevMouseY));
				}
				prevMouseX = mouse.getSceneX();
				prevMouseY = mouse.getSceneY();
			}
			mouse.consume();
		}
	}
	
    /**
     * On mouse released, Sets the new location in
     * the gbcopyArrayOfFMShapes
     * @param gs
     * @param shape
     */
	@Override
	public void shapeReleased(MouseEvent mouse, GenericShape gs, Shape shape) {
		// if not adding to the existing line
		if(!mouse.isAltDown()) {
			LOGGER.debug("ShapeReleased called in Polygon");
			SectionEditor editor = editorRef;
			shape.setStrokeWidth(shape.getStrokeWidth() - 2);

			editor.getRightPane().getChildren().clear();
			if (editor.getImageView() != null) {
				editor.getRightPane().getChildren().add(editor.getImageView());
			}
			// Update this fmShape/fmLine
			gs.setPoints(obsvPts);
			// The first shape in the gbcopyArrayOfFMShapes is an FMRectangle
			// with the demensions of the original SnapShot rectangle.
			double origWd = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getWd();
			double origHt = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getHt();
			editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, origWd, origHt);
			//obsvPts.clear();
		}
	}
	
    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                    RESIZING the Line ... Moving verticies                                       *
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    /**
     * Helper method. Places the points at the lines end-points.
     * - Returns an array of Point2D x and y coordinates.
     * @return
     */
    //@Override
    private Point2D[] get2DPoints(Shape shape) {
        ObservableList<Double> rawPoints = ((Polygon) shape).getPoints();
        LOGGER.debug("get2DPoints, numPoints: {}", rawPoints.size() );
        int size = rawPoints.size();
        double[] points = new double[size];
        Point2D[] result = new Point2D[size/2];

        for(int i = 0; i < size; i++) {
            points[i] = rawPoints.get(i);
        }
        for(int n = 0; n < size/2; n++) {
            result[n] = new Point2D(points[n * 2], points[n * 2 + 1]);
        }

        return result;
    }

    private Point2D[] getTranslatedPoints(Shape s, Point2D[] points) {
        Point2D[] result = new Point2D[points.length];
        for(int i = 0; i < points.length; i++) {
            result[i] = s.localToParent(points[i]);
        }
        return result;
    }


    /**
     * Gets the nodes/resize handles for a shape. Resize handles start point and end point
     * @param thisShape This shape
     * @return  Returns an arraylist of handles located left, right and top, bottom.
     */
    public ArrayList<Circle> getHandles(GenericShape gs, Shape thisShape ) {
        Point2D[] pts2D = get2DPoints(thisShape);
		LOGGER.debug("getHandles called, num pts: {}", pts2D.length);
        ArrayList<Circle> verticies = new ArrayList<>(pts2D.length);
        Circle c;

        for(int i = 0; i < pts2D.length; i++ ) {
            c = new Circle(pts2D[i].getX(), pts2D[i].getY(), 4, Color.web(UIColors.HIGHLIGHT_ORANGE));
            int finalI = i;
            c.setOnMousePressed(e -> verticyPressed(finalI, e, thisShape));
            c.setOnMouseReleased(e -> verticyReleased(gs));
            c.setOnMouseDragged(e -> verticyXYDragged(e, gs, verticies, thisShape));
            verticies.add(c);
        }
        System.out.println("after for loop");

        System.out.println("return");
        return verticies;
    }

	@Override
	public void verticyPressed(int idx, MouseEvent mouse, Shape s) {
		this.verticyIdx = idx;
		DrawTools.setShapeNotSelected(false);
		mouse.consume();
	}



	/**
     * Used for the moving verticy. The verticy that is moving is index 0. The static verticy
     * is index 1.
	 * @param mouse
	 * @param gs
	 * @param vertArry An array containing verticies
	 * @param shape    The Shape or Polygon being moved.
	 */
	@Override
	public void verticyXYDragged(MouseEvent mouse, GenericShape gs, ArrayList<Circle> vertArry, Shape shape) {
		if(! mouse.isSecondaryButtonDown() && ! mouse.isAltDown()) {
			Circle c = vertArry.get(verticyIdx);
			c.setCenterX(mouse.getSceneX());
			c.setCenterY(mouse.getSceneY());
			// set line endings to new position
			((Polygon) shape).getPoints().set(verticyIdx*2, mouse.getSceneX());
			((Polygon) shape).getPoints().set(verticyIdx*2+1, mouse.getSceneY());
		}
		mouse.consume();
	}

	@Override
	public void verticyReleased(GenericShape gs)
	{
		SectionEditor editor = editorRef;
		//shape.setStrokeWidth(shape.getStrokeWidth() - 2);

		editor.getRightPane().getChildren().clear();
		if (editor.getImageView() != null) {
			editor.getRightPane().getChildren().add(editor.getImageView());
		}
		// Update this fmShape/fmLine
		gs.setPoints(obsvPts);
		// The first shape in the gbcopyArrayOfFMShapes is an FMRectangle
		// with the demensions of the original SnapShot rectangle.
		double origWd = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getWd();
		double origHt = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getHt();
		editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, origWd, origHt);
		//obsvPts.clear();
	}



    /**
     * Not used
     */
	@Override
    public void verticyVDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertVOther, Shape vertH1, Shape vertH2, Shape line) {
        // not used

	}

    /**
     * Not used
     */
	@Override
    public void verticyHDragged(MouseEvent mouse, GenericShape gs, Shape vertH, Shape vertHOther, Shape vertH1, Shape vertH2, Shape line) {
        // not used
    }

}
