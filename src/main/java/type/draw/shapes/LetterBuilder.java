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
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;
import type.draw.FMTextEditor;


import java.util.ArrayList;

public class LetterBuilder extends GenericBuilder<FMLetter, LetterBuilder>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LetterBuilder.class);
	
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
	
	// No Args constructor
	public LetterBuilder() { /* empty */}
	
	
	
	/**
	 * Constructor used when new shapes are created,
	 * @param c
	 * @param graphC
	 * @param overlayPane
	 * @param editor
	 * @param strokeClr
	 * @param fillClr
	 */
	public LetterBuilder(Canvas c, GraphicsContext graphC, Pane overlayPane, SectionEditor editor, String strokeClr, String fillClr)
	{
		super(c, graphC, overlayPane, editor);
		
		LOGGER.info("TxtBuilder full constructor called strokeColor: {}", strokeClr);
		
		this.strokeColor = strokeClr;
		this.fillColor = fillClr;
	}
	
	
	/**
	 * Basic constructor without setting the stroke color or fill color
	 * Called by FMLetter getBuilder
	 * @param fmLetters
	 * @param c
	 * @param graphC
	 * @param pane
	 * @param editor
	 */
	public LetterBuilder(FMLetter fmLetters, Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor)
	{
		super(c, graphC, pane, editor);
		Text fxText = fmLetters.getShape();
		//this.anchorX = fmLetters.getX();
		//this.anchorY = fmLetters.getY();
		
		LOGGER.info("TxtBuilder basic constructor called ");
		
		// add mouse actions to the shape when constructor is called
		// to edit existing shapes
		fxText.setOnMousePressed( f -> shapePressed( f, fmLetters, fxText));
		fxText.setOnMouseDragged( f -> shapeDragged( f, fmLetters, fxText));
		fxText.setOnMouseReleased(f -> shapeReleased(f, fmLetters, fxText));
		fxText.setOnMouseClicked( f -> shapeClicked( f, fmLetters, fxText));
		
		getOverlayPane().getChildren().add( fxText);
		
		setNewShape(true); // Not sure if it should be true or false ...
	}
	
	
	
	@Override
	void zeroDeltas() {
		deltaX = 0.0;
		deltaY = 0.0;
	}
	
	
	@Override
	void setAnchors(MouseEvent mouse) {
		anchorX = mouse.getSceneX();
		anchorY = mouse.getSceneY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
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
				
				if (e.isAltDown()) // create text
				{
					gC.strokeRect(anchorX, anchorY, deltaX, deltaX);
					
				} else { // create an text
					
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
	public void mouseReleased(MouseEvent mouse)
	{
		DrawTools draw = DrawTools.getInstance();
		if (draw.getShapeNotSelected())
		{
			Canvas can = getCanvas();
			GraphicsContext gC = getGc();

			gC.clearRect(can.getBoundsInLocal().getMinX(), can.getBoundsInLocal().getMinY(),
					can.getBoundsInLocal().getWidth(), can.getBoundsInLocal().getHeight());

			if (isNewShape() && (deltaX + deltaY) != 0.0) {
				FMLetter fmLetters;

				if (mouse.isAltDown()) { // Creates a square
					fmLetters = new FMLetter(
							anchorX,
							anchorY,
							deltaX,
							deltaX,  // perfect square
							3,
							this.strokeColor,
							this.fillColor,
							gbcopyArrayOfFMShapes.size(), // different
							""
					);
				} else {

					fmLetters = new FMLetter(
							anchorX,
							anchorY,
							deltaX,
							deltaY,
							3,
							this.strokeColor,
							this.fillColor,
							gbcopyArrayOfFMShapes.size(), // different
							""
					);
				}

				// get the FMTextEditor for editing
				FMTextEditor textEditor = fmLetters.getTextEditor();
				
				Button exitButton;
				exitButton = new Button("quit");
				exitButton.setFocusTraversable(true);
				exitButton.setTooltip(new Tooltip("exit, edits not saved"));
				exitButton.setOnAction(e -> getOverlayPane().getChildren().remove(textEditor.getTextEditor()));
				
				Button saveButton;
				saveButton = new Button("save");
				saveButton.setFocusTraversable(true);
				saveButton.setTooltip(new Tooltip("Save and exit"));
				
				saveButton.setOnAction(e -> {
					fmLetters.setText(textEditor.getText());
					getOverlayPane().getChildren().remove(textEditor.getTextEditor());
					setFxTextShape(fmLetters);
				});
				
				Button clearButton;
				clearButton = new Button("x");
				clearButton.setFocusTraversable(false);
				clearButton.setTooltip(new Tooltip("Clear the text area"));
				
				clearButton.setOnAction((ActionEvent e) ->
				{
					textEditor.setText("");
					textEditor.requestFocus();
				});
				
				textEditor.addButtons(saveButton, exitButton, clearButton);
				
				// add it to the overlayPane
				getOverlayPane().getChildren().add( textEditor.getTextEditor());
				
				// allow the user to press x or escape to exit editor
				textEditor.getTextEditor().setOnKeyPressed((KeyEvent e) -> {
					if (e.isControlDown()) {
						if(e.getCode() == KeyCode.ENTER) {
							//LOGGER.info("space and control pressed, removing TextArea and calling setFxTextShape(...)");
							fmLetters.setText(textEditor.getText());
							getOverlayPane().getChildren().remove(textEditor.getTextEditor());
							setFxTextShape(fmLetters);
						}
					}
				});
			}
		}
		mouse.consume();
	}
	
	/**
	 * Called when saving an edited FMLetter.
	 * <p><b>!!!Caution!!!</b> relies on using
	 * the Shape originally added to the overlayPane</p>
	 * @param gs
	 * @param fmLetters
	 */
	private void resetFxTextShape(GenericShape gs, FMLetter fmLetters, Shape shape) {
		// remove old Text shape from overlayPane
		getOverlayPane().getChildren().remove(shape);
		
		// create new Text object and add it to overlayPane
		Text fxText = fmLetters.getShape();
		getOverlayPane().getChildren().add(fxText);
		
		addMouseActions(fmLetters, fxText);
		addToRightPane();
		
		setNewShape(false);
		DrawTools.setShapeNotSelected(true);
		
	}

	private void setFxTextShape(FMLetter fmLetters) {
		
		// Get the FxText from FMLetter
		Text fxText = fmLetters.getShape();
		// Add the arrayOfFMShapes to the overlayPane
		getOverlayPane().getChildren().add(fxText);
		
		addMouseActions(fmLetters, fxText);

		// add shape to gbcopyArrayOfFMShapes
		gbcopyArrayOfFMShapes.add(fmLetters);
		// Add the scaled version of the shape in the right pane
		addToRightPane();

		setNewShape(false);
		DrawTools.setShapeNotSelected(true);
		
		LOGGER.debug("LetterBuilder mouseReleased() completed");
	}
	
	/**
	 * Helper method from setFxTextShape and restFxTextShape
	 * @param fmLetters
	 * @param fxText
	 */
	private void addMouseActions(FMLetter fmLetters, Text fxText) {
		// add mouse actions to the shape so it can be edited and moved
		fxText.setOnMouseClicked( f -> shapeClicked( f, fmLetters, fxText));
		fxText.setOnMousePressed( f -> shapePressed( f, fmLetters, fxText));
		fxText.setOnMouseDragged( f -> shapeDragged( f, fmLetters, fxText));
		fxText.setOnMouseReleased(f -> shapeReleased(f, fmLetters, fxText));
	}
	
	/**
	 * Helper method from setFxTextShape and restFxTextShape
	 */
	private void addToRightPane() {
		int wd = (int) getOverlayPane().getWidth();
		int ht = (int) getOverlayPane().getHeight();
		
		editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, wd, ht );
	}
	
	
	/** ************************************************************************************************************ **
	 *                                                                                                                 *
	 *                                               CUT & PASTE ACTIONS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ ***/



	/**
	 * <p>Overrides the GenericBuilder
	 * Adds mouse actions to ellipses in the overlaypane
	 * adds it to the overlayPane and adds it to the
	 * right pane.</p>
	 * <p>Called by GenericBuilder</p>
	 * @param fxText The JavaFX Text object
	 * @param gs GenericShape object
	 */
	@Override
	protected Shape editableShapeAction(Shape fxText, GenericShape gs) {
		// add mouse actions to the shape
		fxText.setOnMouseClicked( f -> shapeClicked( f, (FMLetter) gs, fxText));
		fxText.setOnMousePressed(f -> shapePressed(f, (FMLetter) gs, fxText));
		fxText.setOnMouseDragged(f -> shapePressed(f, (FMLetter) gs, fxText));
		fxText.setOnMouseReleased(f -> shapeReleased(f, (FMLetter) gs, fxText));

		return fxText;
	}


	
	
	
	/** ************************************************************************************************************ **
	 *                                                                                                                  *
	 *                                                 PANNING the shape
	 *                                               and other mouse actions
	 *                                                                                                                  *
	 ** ************************************************************************************************************ ***/
	
	
	
	/**
	 * On click, Creates the editor for this
	 * Text object.
	 * @param mouse
	 * @param gs The generic shape
	 * @param shape fx text shape
	 */
	public void shapeClicked(MouseEvent mouse, GenericShape gs, Shape shape) {
		DrawTools.setShapeNotSelected(false);

		// @TODO finish adding shapePressed/shapeClicked in LetterBuilder
		// @TODO finish adding shapePressed/shapeClicked in LetterBuilder
		// @TODO finish adding shapePressed/shapeClicked in LetterBuilder
		
		LOGGER.info("shapeCLicked()");
		
		if(mouse.isSecondaryButtonDown()) {
			shapeRightPress(mouse, gs, shape);
		} else {
			FMLetter fmLetters = (FMLetter) gs;
			FMTextEditor textEditor = fmLetters.getTextEditor(shape);
			
			// Used for shape drag
			// set the delta between the shapes x,y location for a point,
			// and the mouse's x,y location. Then use delta x & y later
			// for comparison.
			deltaX = gs.getX() - mouse.getSceneX();
			deltaY = gs.getY() - mouse.getSceneY();
			
			Button exitButton;
			exitButton = new Button("quit");
			exitButton.setFocusTraversable(true);
			exitButton.setTooltip(new Tooltip("exit, edits not saved"));
			// Action
			exitButton.setOnAction(e -> getOverlayPane().getChildren().remove(textEditor.getTextEditor()));
			
			Button saveButton;
			saveButton = new Button("save");
			saveButton.setFocusTraversable(true);
			saveButton.setTooltip(new Tooltip("Save and exit"));
			// Action
			saveButton.setOnAction(e -> {
				fmLetters.setText(textEditor.getText());
				getOverlayPane().getChildren().remove(textEditor.getTextEditor());
				//getOverlayPane().getChildren().add(fmLetters.getShape());
				
				resetFxTextShape(gs, fmLetters, shape);
			});
			
			Button clearButton;
			clearButton = new Button("x");
			clearButton.setFocusTraversable(false);
			clearButton.setTooltip(new Tooltip("Clear the text area"));
			// Action
			clearButton.setOnAction((ActionEvent e) ->
			{
				textEditor.setText("");
				textEditor.requestFocus();
			});
			
			textEditor.addButtons(saveButton, exitButton, clearButton);
			
			// add it to the overlayPane
			getOverlayPane().getChildren().add( textEditor.getTextEditor());
			// allow the user to press x or escape to exit editor
			textEditor.getTextEditor().setOnKeyPressed((KeyEvent e) -> {
				if (e.isControlDown()) {
					if(e.getCode() == KeyCode.ENTER) {
						//LOGGER.info("space and control pressed, removing TextArea and calling setFxTextShape(...)");
						fmLetters.setText(textEditor.getText());
						getOverlayPane().getChildren().remove(textEditor.getTextEditor());
						// sets the mouseActions for this shape.
						resetFxTextShape(gs, fmLetters, shape);
					}
				}
			});
		}
	}

	// ***************** LISTENERS ********************

	private static Text fxL = new Text();
	public static void strokeChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
		fxL.setStroke(Color.web(newVal));
	}

	public static void fillChanged(ObservableValue<? extends String> prop, String oldVal, String newVal) {
		fxL.setFill(Color.web(newVal));
	}

	@Override
	public void clearListeners() {
		DrawTools draw = DrawTools.getInstance();
		draw.getFillProperty().removeListener(LetterBuilder::strokeChanged);
		draw.getStrokeProperty().removeListener(LetterBuilder::fillChanged);
	}
	
	
	
	/** ************************************************************************************************************ **
	 *                                                                                                                  *
	 *                                                 PANNING the shape
	 *                                               and other mouse actions
	 *                                                                                                                  *
	 ** ************************************************************************************************************ ***/


	/**
	 * This is different from normal shapes. Most editing functions is provided by shapeClick().
	 * @param mouse
	 * @param gs
	 * @param shape
	 */
	@Override
	public void shapePressed(MouseEvent mouse, GenericShape gs, Shape shape) {
		DrawTools.setShapeNotSelected(false);

		shape.setStrokeWidth(shape.getStrokeWidth() + 2);

		// Clear the resize nodes if they are present
		DrawTools draw = DrawTools.getInstance();
		draw.clearNodes();
		if(mouse.isSecondaryButtonDown()) {
			shapeRightPress(mouse, gs, shape);
		} else {
			gs.setX( ((Text) shape).getX());
			gs.setY( ((Text) shape).getY());
			// Used for shape drag
			// set the delta between the shapes x,y location for a point,
			// and the mouse's x,y location. Then use delta x & y later
			// for comparison.
			deltaX = gs.getX() - mouse.getSceneX();
			deltaY = gs.getY() - mouse.getSceneY();
		}
        mouse.consume();
	}
	
	@Override
	public void shapeDragged(MouseEvent mouse, GenericShape gs, Shape shape) {
		if(mouse.isPrimaryButtonDown())
		{
			((Text) shape).setX(mouse.getSceneX() + deltaX);
			((Text) shape).setY(mouse.getSceneY() + deltaY);
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
	public void shapeReleased(MouseEvent mouse, GenericShape gs, Shape shape) {
		SectionEditor editor = editorRef;
		shape.setStrokeWidth(shape.getStrokeWidth() - 2);
		editor.getRightPane().getChildren().clear();
		if(editor.getImageView() != null) {
			editor.getRightPane().getChildren().add(editor.getImageView());
		}

		// Update this fmShape/fmRect
		gs.setX( ((Text) shape).getX() );
		gs.setY( ((Text) shape).getY() );
		double origWd = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getWd();
		double origHt = ((FMRectangle) gbcopyArrayOfFMShapes.get(0)).getHt();
		editorRef.setShapesInRtPane(gbcopyArrayOfFMShapes, origWd, origHt);

	}
	
	
	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 *                                    RESIZING the Text ... Moving verticies                                  *
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	
	
	/**
	 * Helper method. Places the points at the Text mid-points on each side.
	 * - Returns an array of Point2D x and y coordinates for centered on each side
	 * center, and the outer tips of radiusX and radiusY.
	 * Even numbers are verticle verticies, odd are horizontal vertices.
	 * 0 is an even number in this case
	 * @return
	 */
	private Point2D[] getPoints(Shape shape)
	{
		return null;
	}
	
	
	
	@Override
	protected ArrayList<Circle> getHandles(GenericShape gs, Shape thisShape) {
		return null;
	}
	
	@Override
	public void verticyPressed(int idx, MouseEvent mouse, Shape s) {
	
	}
	
	@Override
	public void verticyHDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertVOther, Shape vertH1, Shape vertH2, Shape shape) {
	
	}
	
	@Override
	public void verticyVDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertVOther, Shape vertH1, Shape vertH2, Shape shape) {
	
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

	}

	/**
	 * <p>CopyPaste interface copyAction:</p>
	 * <p>creates a new FMText using the mouse current X and Y, and the Shape's current
	 * width and height, adds it to gbcopyArrayOfFMShapes, then clears
	 * the nodes.</p>
	 * @param mouse
	 * @param gs
	 */
	@Override
	public void copyAction(MouseEvent mouse, GenericShape gs) {
		FMLetter fmLetter;

		fmLetter = new FMLetter( mouse.getSceneX(), mouse.getSceneY(),
				((FMLetter) gs).getWd(), ((FMLetter) gs).getHt(), gs.getStrokeWidth(),
				gs.getStrokeColor(), gs.getFillColor(), gbcopyArrayOfFMShapes.size(), ((FMLetter) gs).getText());

		gbcopyArrayOfFMShapes.add(fmLetter);
		setPaste(true);
		DrawTools draw = DrawTools.getInstance();
		draw.clearNodes();

		mouse.consume();

	}
}
