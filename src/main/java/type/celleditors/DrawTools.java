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

package type.celleditors;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import type.draw.DrawObj;
import type.draw.shapes.*;
import uicontrols.UIColors;
//import fmannotations.FMAnnotations;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import flashmonkey.CreateFlash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.stream.Stream;


/**
 * <p>Singleton Class. Not synchronized, not thread-safe.</p>
 * This class contains the drawing tools to draw over an image, or create an SVG drawing on a canvas.
 *      Desirements:
 *          - Click on a shapeButton once, and add several shapes with each new click on overlayPane.
 *          - Delete shapes
 *          - Rotate a shape
 *          - Resize a shape
 *          - Color a shape
 *          - Select several shapes and manipulate them
 *              - Each SVGshape is a series of points. As a group of shapes is grabbed at an outside point. And
 *              the point is dragged, All points grow or shrink by x = deltaX / 2, and y = deltaY / 2. Arcs grow
 *              by ???
 *          - Copy a shape
 *          - Add a copied shape to another shape
 *
 * Algorithm:
 *      1) When User releases the Mouse button while taking a snapshot, toolPane pops up near the mouse.
 *      2) The User clicks a shape, moves the mouse to the canvas, clicks and holds the mouse button down,
 *      drags the shape to the desired size, and releases.
 *      3) //@TODO The shape has an axis and can be rotated on the axis
 *      4) The shape has handles so the User.User can resize the shape.
 *      5) Saving: After the User.User presses a shape button, then clicks on the overlayPane
 *          - newShape is true
 *          - The shape is created according to the mouse drag.
 *          - When the EncryptedUser.EncryptedUser releases the mouse button
 *              - If the shape Width or Height are greater than 0 and newShape is true, it is added to the arrayOfBuilderShapes.
 *      6) When the User exits the ToolWindow, the arrayOfBuilderShapes are saved to a file for this card.
 *
 *      @author Lowell Stadelman
 */
public class DrawTools<T extends GenericShape<T>> //extends SectionEditor //implements CopyPasteInterface
{

    private static DrawTools CLASS_INSTANCE;

    // THE LOGGER
    // REMOVE BEFORE
    private static final Logger LOGGER = LoggerFactory.getLogger(DrawTools.class);

    // A reference to the right pane in the ass!!!
    private static SectionEditor classEditorRef;
    // For the overlay window. The window that shapes
    // are drawn in.
    private Stage overlayWindow;
    private Scene overlayScene;
    private Pane  overlayPane;
    // Nodes for grabbing a shape
    private Pane  nodesPane;
    // Contains the shapes and the image
    private Canvas overlayCanvas;

    // For the ToolWindow popUp
    private static Stage toolWindow;
    private static TilePane toolPane;
    // contains tool popUp and action buttons
    private static BorderPane popUpPane;
    private static Scene toolScene;
    // contains non-shape buttons
    private static HBox zBtnHBox;
    private static GridPane colorBtnGrid;
    public static GraphicsContext toolGC;
    // The shape buttons
    private static Button circleBtn, rectBtn, triangleBtn,
            polyBtn, lineBtn, arrowBtn, penBtn, txtBtn;

    // Other buttons
    private static Button deleteBtn, saveBtn, clearBtn, quitBtn;
    // The size of the overLayPane to be drawn on.
    private static double overlayHt;
    private static double overlayWd;
    // For the ShapeBuilders... IE CircleBuilder
    private static boolean shapeNotSelected;
    
    // Color related
    private static StringProperty strokeProperty = new SimpleStringProperty(UIColors.BELIZE_BLUE);
    private static StringProperty fillProperty = new SimpleStringProperty("0x00000000");
    private static StringProperty strokeBTNProperty = new SimpleStringProperty(UIColors.BELIZE_BLUE);
    private static StringProperty fillBTNProperty = new SimpleStringProperty("0x00000000");

    /**
     * private No args constructor
     */
    private DrawTools() { /* empty */ }

    
    /**
     * Singleton class instantiation. There
     * should only be one instance of DrawTools
     * Synchronized
     * @return The class instance
     */
    public static synchronized DrawTools getInstance() {
        if(CLASS_INSTANCE == null) {
            CLASS_INSTANCE = new DrawTools();
        }
        return CLASS_INSTANCE;
    }
    
    
    /**
     * Called by DrawPadButton in SectionEditor, Shapes and no image
     * @param x
     * @param y
     * @param fullPathName
     * @param paramEditor
     */
    public void buildDrawTools(double x, double y, String fullPathName, SectionEditor paramEditor) {
        // height and width of drawpad
        int wd = 300;
        int ht = 300;
        
        LOGGER.info("line 145) buildDrawTools for drawPad, fullPathName: " + fullPathName );

        StageStyle style = StageStyle.UTILITY;
        //shapeNotSelected = false;
        overlayWindow = buildDrawTools(x, y, wd, ht, fullPathName, paramEditor, style, null);
        // if the user clicks outside of the pane
        // keep overlay on top ALWAYS!!!
        overlayWindow.setAlwaysOnTop(true);
        overlayWindow.show();
    }
    
    
    /**
     * Called by SectionEditor immediately after image is captured.
     * @param drawObj The drawObject containing minX, minY, width, height, qOrA, cID, and deckName
     * @param paramEditor
     */
    public void buildDrawTools(DrawObj drawObj, SectionEditor paramEditor) {

       //System.out.println("Called buildDrawTools called by snapShot");
    
        //System.err.println("\t are images null in editor: " + (sEditor.getMediaFileName() == null));
        
        // Clear the arrayOfFMShapes if it has
        // shapes left in it from the last time
        // it was used.
        //if(sEditor.getArrayOfFMShapes() != null) {
        //    sEditor.getArrayOfFMShapes().clear();
        //}

        StageStyle style = StageStyle.TRANSPARENT;
        //shapeNotSelected = true;
        overlayWindow = buildDrawTools(drawObj.getMinX(), drawObj.getMinY(), drawObj.getDeltaX(), drawObj.getDeltaY(),
                drawObj.getFullPathName(), paramEditor, style, null);
        overlayWindow.show();
    }
    
    /**
     * Called by editor when card exists, by popup in SectionEditor
     * @param //drawObj
     * @param paramEditor
     * @param iView
     */
    public void buildDrawTools( String fullPathName, SectionEditor paramEditor, ImageView iView, double x, double y, double wd, double ht) {
        StageStyle style = StageStyle.DECORATED;
        overlayWindow = buildDrawTools(x, y, wd, ht, fullPathName, paramEditor, style, iView);
        overlayWindow.show();
    }



    private Stage buildDrawTools(double prevX, double prevY, double prevWd, double prevHt,
                                String fullPathName, SectionEditor paramEditor, StageStyle style, ImageView iView)
    {
        
        LOGGER.info("fullPathName: {} ends with .date: {}", fullPathName, fullPathName.endsWith(".dat"));

        Stage window = new Stage(style);
        // Misc variables
        overlayHt = prevHt;
        overlayWd = prevWd;

        popUpPane = new BorderPane();
        toolScene = new Scene(popUpPane);

        // Class SectionEditor referance
        classEditorRef = paramEditor;

        // Object instantiation and pane assignment for the Drawing area
        overlayCanvas = new Canvas();
        overlayPane = new Pane();
        if(iView == null) {
            overlayPane.getChildren().add(overlayCanvas);
        } else {
            overlayPane.getChildren().addAll(iView, overlayCanvas);
        }
 
        // Windows 10 fix
        overlayScene = new Scene(overlayPane, prevWd, prevHt, new Color(0, 0, 0, 1d / 255d));
        
        // overlay Window settings
        window.setHeight(prevHt);
        window.setWidth(prevWd);
        window.setX(prevX);
        window.setY(prevY);
        window.setScene(overlayScene);
        overlayScene.getStylesheets().addAll("css/drawtools.css", "css/buttons.css");

        // Overlay Pane settings
        overlayPane.setMaxHeight(overlayPane.getBoundsInParent().getHeight());
        overlayPane.setMaxWidth(overlayPane.getBoundsInParent().getWidth());
        overlayPane.setLayoutX(overlayPane.getBoundsInParent().getMinX());
        overlayPane.setLayoutY(overlayPane.getBoundsInParent().getMinY());
        overlayPane.setStyle("-fx-border-color: rgba(48, 128, 185, .9); " +
                "-fx-background-color: transparent; -fx-border-width: 3;");
        
        // Overlay canvas settings
        overlayCanvas.setWidth( overlayPane.getBoundsInParent().getWidth() );
        overlayCanvas.setHeight( overlayPane.getBoundsInParent().getHeight() );
        overlayCanvas.setLayoutX( overlayPane.getBoundsInParent().getMinX() );
        overlayCanvas.setLayoutY( overlayPane.getBoundsInParent().getMinY() );

        toolGC = overlayCanvas.getGraphicsContext2D();

        toolScene.setOnKeyPressed((KeyEvent f) -> {
            if(f.getCode() == KeyCode.ESCAPE)
            {
                window.close();
                toolWindow.close();
                onClose(fullPathName, paramEditor);
            }
        });

        overlayPane.setOnKeyPressed( (KeyEvent f) -> {
            if(f.getCode() == KeyCode.ESCAPE)
            {
                window.close();
                toolWindow.close();
                onClose(fullPathName, paramEditor);
            }
        });

        // Update the size of the rectangle used for the shapespane size
        // in arrayOfFMShapes. Used when iView doesn't exist.
        overlayScene.setOnMouseClicked( (MouseEvent e) -> {
            FMRectangle rect = paneSizeRectangle();
            rect.setWd(overlayCanvas.getWidth());
            rect.setHt(overlayCanvas.getHeight());
            if(paramEditor.getArrayOfFMShapes().size() > 0) {
                paramEditor.getArrayOfFMShapes().set(0, rect);
            }
        });

        double[] linePts = {0,0,20,0};
        double[] arrowPts = {23.4970013410768, 25.295000853688194, 44.0, 13.0, 23.502999877840676, 0.6950015850386819, 23.501200316811513, 8.075001365633536, 3.0011997073527747, 8.080000146270098, 2.9988002926472253, 17.9199998537299, 23.498800902105963, 17.91500107309334, 23.4970013410768, 25.295000853688194};
        double[] polyPts = {20.0, 19.0, 32.0, 8.0, 9.0, 8.0, 9.0, 29.0, 31.0, 30.0};
        FMCircle fmCirc = new FMCircle(-10, 0, 10, 10, 3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(), 0);
        FMRectangle fmRect = new FMRectangle(0, 0, 20, 20, 3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(), 0);
        FMTriangle fmTri = new FMTriangle(10, 0, 20, 20, 3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(), 0);
        FMLetter fmLetter = new FMLetter(0, 0, 10, 10, 3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(), 0, "T");
        FMPolyLine fmLine = new FMPolyLine(linePts,3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(),0);
        FMPolyLine fmArrow = new FMPolyLine(arrowPts, 3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(), 0);
        FMPolygon fmPolygon = new FMPolygon(polyPts, 3, strokeBTNProperty.getValue(), fillBTNProperty.getValue(), 0);

        Ellipse btnCircle = fmCirc.getShape();
        Rectangle btnSqr = fmRect.getShape();
        Polygon btnTri = fmTri.getShape();
        Polyline btnLine = fmLine.getShape();
        Text btnText = fmLetter.getShape();
        btnText.setStyle("-fx-font-size: 24;");
        btnText.setFill(Color.web(strokeBTNProperty.getValue()));
        Polyline btnArrow = fmArrow.getShape();
        Polygon btnPoly = fmPolygon.getShape();

        btnCircle.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(fillBTNProperty.get()), fillBTNProperty));
        btnCircle.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));
        btnSqr.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(fillBTNProperty.get()), fillBTNProperty));
        btnSqr.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));
        btnTri.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(fillBTNProperty.get()), fillBTNProperty));
        btnTri.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));

        btnText.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));
        btnText.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));

        btnLine.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(fillBTNProperty.get()), fillBTNProperty));
        btnLine.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));

        btnArrow.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(fillBTNProperty.get()), fillBTNProperty));
        btnArrow.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));

        btnPoly.fillProperty().bind(Bindings.createObjectBinding(() -> Color.web(fillBTNProperty.get()), fillBTNProperty));
        btnPoly.strokeProperty().bind(Bindings.createObjectBinding(() -> Color.web(strokeBTNProperty.get()), strokeBTNProperty));
        
        // Tool buttons array
        Button[] toolBtns = {
                circleBtn = new Button("Circle", btnCircle),
                rectBtn = new Button("Rectangle", btnSqr),
                triangleBtn = new Button("Triangle", btnTri),
                polyBtn = new Button("Polygon", btnPoly),
                lineBtn = new Button("Line", btnLine),
                arrowBtn = new Button("Arrow", btnArrow),
                penBtn = new Button("Pen"),
                txtBtn = new Button("Text", btnText)
        };
        circleBtn.setAlignment(Pos.BASELINE_LEFT);
        rectBtn.setAlignment(Pos.BASELINE_LEFT);
        triangleBtn.setAlignment(Pos.BASELINE_LEFT);
        txtBtn.setAlignment(Pos.BASELINE_LEFT);
        arrowBtn.setAlignment(Pos.BASELINE_LEFT);
        lineBtn.setAlignment(Pos.BASELINE_LEFT);
        polyBtn.setAlignment((Pos.BASELINE_LEFT));
        penBtn.setAlignment(Pos.BASELINE_LEFT);
        
        //polyBtn.setDisable(true);
        penBtn.setDisable(true);
        
        
        // Color buttons for Fill Color and Stroke Color
        final int btnSize = 20;
        String[] colors = {UIColors.TRANSPARENT,
                UIColors.FM_WHITE, UIColors.FLASH_RED, UIColors.HIGHLIGHT_PINK,
                UIColors.HIGHLIGHT_ORANGE, UIColors.HIGHLIGHT_YELLOW, UIColors.HIGHLIGHT_GREEN,
                UIColors.BELIZE_BLUE_OPAQUE, UIColors.GRAPH_BGND, UIColors.FLASH_BLACK};
    
        Rectangle[] strokeColorBtns = createColorBtns(btnSize, false, colors);
        Rectangle[] fillColorBtns = createColorBtns(btnSize, true, colors);
        
        colorBtnGrid = new GridPane();
        colorBtnGrid.setVgap(2);
        colorBtnGrid.setHgap(2);
        colorBtnGrid.setPadding(new Insets(10,2,2,2));
        colorBtnGrid.setAlignment(Pos.CENTER);
        colorBtnGrid.addRow(0, strokeColorBtns);
        colorBtnGrid.addRow(1, fillColorBtns);


        //  Set shapes to move in right pane as shapes are moved
        //  in overlayPane
        
        // Shape button calls
        arrowBtn.setOnAction(a -> arrowBtnAction(paramEditor));
        rectBtn.setOnAction(a -> rectBtnAction(paramEditor));
        circleBtn.setOnAction(a -> circleBtnAction(paramEditor));
        triangleBtn.setOnAction(a -> triangleBtnAction(paramEditor));
        lineBtn.setOnAction(a -> lineBtnAction(paramEditor));
        polyBtn.setOnAction(a -> polyBtnAction(paramEditor));
        txtBtn.setOnAction(a -> txtBtnAction(paramEditor));

        // Service buttons
        deleteBtn = new Button("delete");
        deleteBtn.setTooltip(new Tooltip("Deletes the active shape"));

        saveBtn = new Button("save");
        saveBtn.setTooltip(new Tooltip("Save and exit"));
        saveBtn.setOnAction(a -> saveExitAction(fullPathName, paramEditor));
        
        clearBtn = new Button("clear");
        clearBtn.setTooltip(new Tooltip("Clear all shapes"));
        clearBtn.setOnAction(a -> clearShapesAction(paramEditor));
        
        quitBtn = new Button("quit");
        quitBtn.setTooltip(new Tooltip("clears the shapes and exits"));
        quitBtn.setOnAction(e -> quitBtnAction(paramEditor));

        zBtnHBox = new HBox(saveBtn, quitBtn, clearBtn);
        zBtnHBox.setAlignment(Pos.CENTER);
        zBtnHBox.setPadding(new Insets(2, 0, 4, 0));
        zBtnHBox.setId("buttonBox");
        // Pane containing the shape buttons
        toolPane = new TilePane();
        toolPane.setVgap(3);
        toolPane.setHgap(3);
        toolPane.setPrefColumns(2);
        toolPane.setAlignment(Pos.CENTER);
        toolPane.setPadding(new Insets(10, 4, 20, 4));
        // Pane containing nodes when a shape is
        // resized.
        nodesPane = new Pane();
        overlayScene.setOnMouseExited((MouseEvent e) -> releaseMouse());

        setButtonsSettings(toolPane, toolBtns);

        return window;

    } // *** END buildDrawTools() ***


    /**
     * The popup tool window
     */
    public void popUpTools()
    {
        toolWindow = new Stage();
        popUpPane.setTop(colorBtnGrid);
        popUpPane.setCenter(toolPane);
        popUpPane.setBottom(zBtnHBox);
        popUpPane.setStyle("-fx-background-color: " + UIColors.GRAPH_BGND);
//        toolScene.getStylesheets().add("css/newCascadeStyleSheet.css");

        toolWindow.setScene(toolScene);
        double x = overlayWindow.getX() + overlayWindow.getWidth() + 10;
        double y = overlayWindow.getY() + 10;
        toolWindow.setX(x);
        toolWindow.setY(y);
        toolWindow.setTitle("Draw tools");
        // toolWindow/stage should always be
        // on top until it is closed.
        toolWindow.setAlwaysOnTop(true);
        toolWindow.setOnHidden(e -> justClose());
        toolWindow.show();
    }


    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                        GETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    public StringProperty getStrokeProperty() {
        return strokeProperty;
    }

    public StringProperty getFillProperty() {
        return fillProperty;
    }

    public boolean getShapeNotSelected()
    {
        return shapeNotSelected;
    }

    public Canvas getCanvas()
    {
        return overlayCanvas;
    }

    public Pane getOverlayPane()
    {
        return overlayPane;
    }

    public GraphicsContext getGrapContext()
    {
        return toolGC;
    }

    public Scene getOverlayScene() { return overlayScene; }

    public Pane getNodesPane() { return nodesPane; }

    public double getOverlayHt()
    {
        return this.overlayHt;
    }

    public double getOverlayWd()
    {
        return this.overlayWd;
    }
    
  //  public DrawObj getDrawObj() {
  //      return this.drawObj;
  //  }




    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                        SETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/


    public static void setShapeNotSelected(boolean bool)
    {
        shapeNotSelected = bool;
    }

    public void setOverlayWindow(Stage window)
    {
        this.overlayWindow = window;
    }

    public void setOverlayScene(Scene scene)
    {
        this.overlayScene = scene;
    }

    public void setOverlayPane(Pane pane)
    {
        this.overlayPane = pane;
    }


    /**
     * Sets the graphics setting for these buttons
     * @param grid
     * @param buttons
     */
    private static void setButtonsSettings(Pane grid, Button[] buttons)
    {
        for(Button b : buttons)
        {
            b.setId("drawToolButton");
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            grid.getChildren().add(b);
        }
    }


    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                    OTHER METHODS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/
    
    /**
     * Creates an array of square rectangles
     * @param isFill
     * @param colors An array of UIColors
     * @return Returns an array of rectangle color buttons based on the UIColors provided in
     * the parameter.
     */
    private Rectangle[] createColorBtns(final int btnSize, boolean isFill, String ... colors) {

        Rectangle[] rects = new Rectangle[colors.length];
        int idx = 0;
        for(String s : colors) {
            // Fill color
            if (isFill) {
                rects[idx] = new Rectangle(btnSize, btnSize, Color.web(s));
                rects[idx].setOnMouseClicked(e -> {
                    fillBTNProperty.setValue(s);
                    fillProperty.setValue(s);
                    setShapesColors();
                });
                //rects[idx].setOnMouseReleased(e -> strokeProperty.setValue(UIColors.TRANSPARENT));
            // Stroke Color
            } else {
                rects[idx] = new Rectangle(btnSize - 2, btnSize);
                rects[idx].setStroke(Color.web(s));
                rects[idx].setStrokeWidth(2);
                rects[idx].setFill(Color.web(UIColors.TRANSPARENT));
                rects[idx].setOnMouseClicked(e -> {
                    strokeBTNProperty.setValue(s);
                    strokeProperty.setValue(s);
                    setShapesColors();
                });
            }
            idx++;
        }
        
        return rects;
    }

    private void setShapesColors() {
       if( ! classEditorRef.getArrayOfFMShapes().isEmpty()) {
           FMRectangle r = (FMRectangle) classEditorRef.getArrayOfFMShapes().get(0);
           double wd = r.getWd();
           double ht = r.getHt();
           classEditorRef.setShapesInRtPane(classEditorRef.getArrayOfFMShapes(), wd, ht);
       }
    }

    public void clearNodes() {
        if(overlayPane.getChildren().contains(nodesPane)) {
            nodesPane.getChildren().clear();
            overlayPane.getChildren().remove(nodesPane);
        }
    }

    /**
     * Creates new properties and clears all listeners.
     */
    public void clearListeners() {
        LOGGER.debug("clearListeners() called");
        fillProperty = new SimpleStringProperty(fillProperty.getValue());
        strokeProperty = new SimpleStringProperty(strokeProperty.getValue());
    }


    public void releaseMouse() {
        overlayScene.setCursor(Cursor.DEFAULT);
        overlayScene.setOnMousePressed(null);
        overlayScene.setOnMouseReleased(null);
        overlayScene.setOnMouseDragged(null);
        overlayPane.setOnMousePressed(null);
        overlayPane.setOnMouseDragged(null);
        overlayPane.setOnMouseEntered(null);
        overlayPane.setOnMouseReleased(null);
        overlayPane.setOnMouseExited(null);
    }

    
    
    /**  ****** BUTTON ACTIONS *******  **/
    
    private void clearShapesAction(SectionEditor paramEditor) {
        
        // @todo bug with overlayPane shapes. After clear, you do not see the shapes when the pane is redrawn on.
        
        paramEditor.clearShapes(paramEditor.getArrayOfFMShapes());
        overlayPane.getChildren().remove(1, overlayPane.getChildren().size());
    }


    private void saveExitAction(String fullPathName, SectionEditor paramEditor) {
        LOGGER.info("exitButtonAction called");
        // Exit overLayWindow, User does not want to create a drawing,
        popUpPane.getChildren().clear();
        overlayWindow.close();
        toolWindow.close();

        onClose(fullPathName, paramEditor);
    }
    
    private void quitBtnAction(SectionEditor paramEditor) {
        clearShapesAction(paramEditor);
        justClose();
    }


    /**
     * Adds a circle to the drawing
     * @param ed
     */
    private void circleBtnAction(SectionEditor ed)
    {
        clearListeners();
        // Clear the overlayPane of any resize nodes if they exist
        clearNodes();

        CircleBuilder cBuilder = new CircleBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());

        overlayScene.setOnMousePressed(cBuilder::mousePressed);
        overlayScene.setOnMouseDragged(cBuilder::mouseDragged);
        overlayScene.setOnMouseReleased(cBuilder::mouseReleased);

       //System.out.println("\n *** circeBtnAction called ***");
        toolGC = overlayCanvas.getGraphicsContext2D();
        
        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR));
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;
    }
    
    /*   ********** SHAPE BUTTON ACTIONS **********    */

    /**
     * Adds a rectangle to the drawing
     * @param ed
     */
    private void rectBtnAction(SectionEditor ed)
    {
        clearListeners();
        // Clear the overlayPane of any resize nodes if they exist
        clearNodes();

        RectangleBuilder rectBuilder = new RectangleBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());

        overlayScene.setOnMousePressed(rectBuilder::mousePressed);
        overlayScene.setOnMouseDragged(rectBuilder::mouseDragged);
        overlayScene.setOnMouseReleased(rectBuilder::mouseReleased);

       //System.out.println("\n *** rectBtnAction called ***");
        toolGC = overlayCanvas.getGraphicsContext2D();

        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR) );
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;
    }

    /**
     * Adds an isosocles triangle to the drawing
     * @param ed
     */
    private void triangleBtnAction(SectionEditor ed)
    {
        clearListeners();
        // Clear the overlayPane of any resize nodes if they exist
        clearNodes();

        TriangleBuilder triBuilder = new TriangleBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());

        overlayScene.setOnMousePressed(triBuilder::mousePressed);
        overlayScene.setOnMouseDragged(triBuilder::mouseDragged);
        overlayScene.setOnMouseReleased(triBuilder::mouseReleased);

        //System.out.println("\n *** triangleBtnAction called ***");
        toolGC = overlayCanvas.getGraphicsContext2D();

        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR) );
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;

    }

    /**
     * Creates a closed shape with multiple-points
     */
    private void polyBtnAction(SectionEditor ed) {
        clearListeners();
        // Clear the overlayPane of any resize nodes if they exist
        clearNodes();
        PolygonBuilder polygonBuilder = new PolygonBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());

        overlayScene.setOnMousePressed(polygonBuilder::mousePressed);
        overlayScene.setOnMouseDragged(polygonBuilder::mouseDragged);
        overlayScene.setOnMouseReleased(polygonBuilder::mouseReleased);
        toolGC = overlayCanvas.getGraphicsContext2D();

        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR) );
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;
    }

    /**
     * Creates a one or more straight lines with multiple-points
     */
    private void lineBtnAction(SectionEditor ed) {
        clearListeners();
        // Clear the overlayPane of any resize nodes if they exist
        clearNodes();
        PolylineBuilder polylineBuilder = new PolylineBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());
        overlayScene.setOnMousePressed(polylineBuilder::mousePressed);
        overlayScene.setOnMouseDragged(polylineBuilder::mouseDragged);
        overlayScene.setOnMouseReleased(polylineBuilder::mouseReleased);
        toolGC = overlayCanvas.getGraphicsContext2D();

        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR) );
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;
    }

    /**
     * Creates an arrow
     */
    private void arrowBtnAction(SectionEditor ed) {
        clearListeners();
        clearNodes();
        ArrowBuilder arrow = new ArrowBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());
        overlayScene.setOnMousePressed(arrow::mousePressed);
        overlayScene.setOnMouseDragged(arrow::mouseDragged);
        overlayScene.setOnMouseReleased(arrow::mouseReleased);
        toolGC = overlayCanvas.getGraphicsContext2D();

        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR) );
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;
    }

    /**
     * Creates a freeform line that curves
     */
    private void freeformBtnAction()
    {

    }

    /**
     * Creates a textbox and places it on the drawing
     */
    private void txtBtnAction(SectionEditor ed) {
       //System.out.println("\n *** txtBtnAction called ***");
        clearListeners();
        // Clear the overlayPane of any resize nodes if they exist
        clearNodes();
    
        LetterBuilder letterBuilder = new LetterBuilder(overlayCanvas, toolGC, overlayPane, ed, strokeProperty.getValue(), fillProperty.getValue());
    
        overlayScene.setOnMousePressed(letterBuilder::mousePressed);
        overlayScene.setOnMouseDragged(letterBuilder::mouseDragged);
        overlayScene.setOnMouseReleased(letterBuilder::mouseReleased);
    
        LOGGER.info("txtBtnAction line 719");
        
        toolGC = overlayCanvas.getGraphicsContext2D();
    
        overlayPane.setOnMouseEntered((MouseEvent e) -> overlayPane.setCursor(Cursor.CROSSHAIR) );
        overlayPane.setOnMouseExited((MouseEvent e) -> overlayPane.setCursor(Cursor.DEFAULT));
        shapeNotSelected = true;
        
        LOGGER.info("txtBtnAction completed");
    }
    
    
    /**
     * Action taken when the user closes this class
     * with the intention of saving files. Or in case
     * of a failure close, saves users work.
     * Saves the shapes to file. Sets shapesFileName
     * in SectionEditor to the fileName here.
     * @param fullPath
     * @param paramEditor
     */
    private void onClose(String fullPath, SectionEditor paramEditor) {
        clearListeners();
        LOGGER.info("onCLose called");
        
        CreateFlash cfp = CreateFlash.getInstance();
        cfp.enableButtons();
        
        int num = fullPath.lastIndexOf('/');
        String fileName = fullPath.substring(num + 1);
    
       //System.out.println("DrawTools onClose fileName: " + fileName);
        
        paramEditor.setShapeFile(fileName);
        toolWindow.close();
        overlayWindow.close();
        // cannot clear the array of shapes here.
        // It is needed in card editor
    }

    /**
     * When the User.User does something unexpected.
     * close everything
     */
    public void justClose() {
        CreateFlash cfp = CreateFlash.getInstance();
        cfp.enableButtons();

        if(toolWindow != null) {
            toolWindow.close();
            overlayWindow.close();
        }
    }



    /**
     * Provides the size of the pane from the first shape in shapesArray
     * @return
     */
    private static FMRectangle paneSizeRectangle() {
        return new FMRectangle(0,0,0,0,1.0,UIColors.TRANSPARENT, UIColors.TRANSPARENT,0);
    }

    //---------------------- Testing Puposed Methods ----------------------
/*
    @FMAnnotations.DoNotDeployMethod
    public Point2D getRectBtnXY() {
        Bounds bounds = rectBtn.getLayoutBounds();
        return rectBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getTriBtnXY() {
        Bounds bounds = triangleBtn.getLayoutBounds();
        return triangleBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getCircleBtnXY() {
        Bounds bounds = circleBtn.getLayoutBounds();
        return circleBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getClearListBtnXY() {
        Bounds bounds = clearBtn.getLayoutBounds();
        return clearBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getExitBtnXY() {
        Bounds bounds = saveBtn.getLayoutBounds();
        return saveBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getOverlayPaneXY() {
        Bounds bounds = overlayPane.getLayoutBounds();
        return overlayPane.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public boolean checkOverLayPaneRef(Pane pane) {
        return pane == this.overlayPane;
    }

    @FMAnnotations.DoNotDeployMethod
    public Canvas getOverlayTestCanvas() {
       //System.out.println("Canvas reference in getOverlayCanvas() " + overlayCanvas);
        return overlayCanvas;
    }

    @FMAnnotations.DoNotDeployMethod
    public String getFileString() {

       //System.out.println("in testMethod getFileString, fileStringName: " + classEditorRef.getShapesFileNameTestMethod());

        return classEditorRef.getShapesFileNameTestMethod();
    }

 */



}
