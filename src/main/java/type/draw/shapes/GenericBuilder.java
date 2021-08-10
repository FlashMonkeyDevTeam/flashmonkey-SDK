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

import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import type.CopyPasteInterface;
import type.FMMouseInterface;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;


import java.util.ArrayList;

/**
 * This is the Parent Builder Class to the FMShape Builder classes. It contains the common methods, and variables for the
 * FMShape Builder classes. As well as abstract methods that must be implemented in builder classes that inherit
 * this class.
 *
 * @author Lowell Stadelman
 */
public abstract class GenericBuilder<T extends GenericShape<T>, B extends GenericBuilder<T,B>> implements FMMouseInterface<T,B>, CopyPasteInterface
{
    SectionEditor editorRef;                         // package private
    ArrayList<GenericShape> gbcopyArrayOfFMShapes;   // package private

    // For drawing on canvas
    private Canvas canvas;
    // For drawing
    private GraphicsContext gc;
    // To hold the overlayPane
    private Pane overlayPane;
    // Pane for resize nodes
    private Pane nodesPane;

    // Flag for drawing a new shape
    private static boolean newShape = false;
    Button deleteBtn;                               // package private
    protected boolean paste;


    /**
     * No args constructor
     */
    public GenericBuilder() { /* No args constructor */ }

    /**
     * Constructor used when new shapes are created, Call this first
     * to create the overlayPane over the area selected.
     * @param c The Canvas
     * @param graphC The GraphicsContext
     * @param pane The overlayPane that the shape is contained in.
     * @param editor The sectionEditor for this set of shapes
     *
     */
    public GenericBuilder(Canvas c, GraphicsContext graphC, Pane pane, SectionEditor editor)
    {
        System.out.println("\n *** GenericBuilder full constructor called ***");

        // The gbcopyArrayOfFMShapes is a shallow copy of the SectionEditors arrayOfFMShapes
        this.gbcopyArrayOfFMShapes = editor.getArrayOfFMShapes();

        // initial rectangle for sizing the pane
        FMRectangle fmrect = new FMRectangle();
        fmrect.setWd(pane.getWidth());
        fmrect.setHt(pane.getHeight());
        if (! gbcopyArrayOfFMShapes.isEmpty()) {
            gbcopyArrayOfFMShapes.set(0, fmrect);
        } else {
            gbcopyArrayOfFMShapes.add(fmrect);
        }

        overlayPane = pane;
        canvas = c;
        gc = graphC;
        //dt = this.getDrawTools();
        paste = false;
        //this.editor = edit;
        this.editorRef = editor;
    }



    /*************************************************************************************************************

                                                        SETTERS

     *************************************************************************************************************/


    public static void setNewShape(Boolean bool)
    {
        newShape = bool;
    }

    public void setBuilderShapeAryIdx0(FMRectangle fmRectangle) {
        gbcopyArrayOfFMShapes.set(0, fmRectangle);
    }

    /**
     * Sets anchorX and anchorY to the mouse x and y location
     * in the scene.
     */
    abstract void setAnchors(MouseEvent mouse);


    /**
     * Is there an item being pasted? Boolean
     * @param bool
     */
    public void setPaste(boolean bool)
    {
        paste = bool;
    }





    /********************************************************************************************************** **

                                                        GETTERS

     ********************************************************************************************************* ***/




    public Canvas getCanvas()
    {
        return canvas;
    }

    public GraphicsContext getGc()
    {
        return gc;
    }
    
    public Pane getOverlayPane()
    {
        return overlayPane;
    }

    /**
     * Creates the handles for a shape. The circles the EncryptedUser.EncryptedUser
     * uses to changes a shapes height, and width with.
     * @param gs
     * @param thisShape
     * @return The arrayList of circles
     */
    protected abstract ArrayList<Circle> getHandles(GenericShape gs, Shape thisShape );


    /**
     * Returns the boolean paste
     * @return
     */
    public boolean getPaste() { return this.paste; }



    /********************************************************************************************************** **

                                                        OTHERS

     ********************************************************************************************************* ***/



    /**
     * The capability to change a shapes demensions.
     * @param shape
     * @param gs
     * @return
     */
    protected abstract Shape editableShapeAction(Shape shape, GenericShape gs);




    public static boolean isNewShape()
    {
        return newShape;
    }


    /**
     * Zeros the deltas of the class. Used to zero deltas
     * at the start of a mouse drag event.
     */
    abstract void zeroDeltas();

    /**
     * Removes a shape from the overlayPane, rightPane, and arrayOfFMShapes.
     * - Each FMShape contains it's own index. In order to preserve the order of
     * the shapes, and maintain order, after an FMShape is deleted the element
     * containing the FMShape then points to null. Rather than shifting elements
     * of the array to the left, the element is turned to null. Thus, each
     * FMShapes index remains valid.
     * The FMShapes are then reloaded using a loop that skips over null
     *
     */
    public void deleteButtonAction(Shape shape, GenericShape gs)
    {
        double scale = editorRef.getScale();
        // Clear the nodes pane if any
        DrawTools draw = DrawTools.getInstance();
        draw.clearNodes();

        // Remove this shape from the overlay pane
        overlayPane.getChildren().remove(shape);

        // renumber each shape, clear from rightPane,
        // and add array back to rightPane
        gs.delete(gs, editorRef, scale); // potential problem here
    }

    /**
     * When the mouse is pressed in the overlayArea. If (copy = true), create
     * a shape.
     * @param mouse
     */
    @Override
    public void mousePressed(MouseEvent mouse)
    {
        System.out.println("\n *** mousePressed in GenericShape **** ");
        DrawTools draw = DrawTools.getInstance();
        // Clear the resize nodes if they are present
        draw.clearNodes();

        if(paste) {
            System.out.println("\t - paste action called ***");
            // Paste the copy
            pasteAction(mouse);
            draw.getOverlayScene().setCursor(Cursor.DEFAULT);
            paste = false;
        } else if (draw.getShapeNotSelected()) { // Avoids conflict between new shape, shape dragged, and shape resized

            // Set the shape start point x and y
            setAnchors(mouse);
            // Set deltaX & deltaY to 0
            zeroDeltas();
        }
        mouse.consume();
    }


    /**
     * Creates the resize handles from the shape, not the fmShape
     * @param shape The shape in the overlayPane
     */
    //@Override
    public void shapeRightPress(MouseEvent mouse, GenericShape gs, Shape shape)
    {
        System.out.println("\n *** shapeRightPress in genericBuilder ***");

        // Clear previous shapes nodes if any

            DrawTools draw = DrawTools.getInstance();
            draw.clearNodes();

            if(shape.getCursor() != Cursor.DEFAULT) {
                shape.setCursor(Cursor.DEFAULT);
            }

            // Checks if command & c are pressed.
            copyActions(mouse, gs);

            deleteBtn = new Button("X");
            deleteBtn.setId("cltBtn");
            deleteBtn.setLayoutX(mouse.getSceneX() + 10);
            deleteBtn.setLayoutY(mouse.getSceneY() + 10);
            deleteBtn.setOnAction(e -> deleteButtonAction(shape, gs));
            // Set current shapes nodes for resizing
            nodesPane = draw.getNodesPane();
            nodesPane.getChildren().addAll(getHandles(gs, shape));
            nodesPane.getChildren().add(deleteBtn);
            overlayPane.getChildren().add(nodesPane);
     
        mouse.consume();
    }

    public void shapeHover(MouseEvent mouse, Shape shape)
    {
        overlayPane.setCursor(Cursor.DEFAULT);
        mouse.consume();
    }

    public void shapeExit(MouseEvent mouse, Shape shape)
    {
        overlayPane.setCursor(Cursor.CROSSHAIR);
        mouse.consume();
    }

    public void copyActions(MouseEvent mouse, GenericShape gs)
    {
        overlayPane.setOnKeyPressed((KeyEvent e) ->
        {
            // Copy
            if ( e.isMetaDown() && e.getCode() == KeyCode.C)
            {
                //dt.getOverlayScene().setCursor(Cursor.CROSSHAIR);
                System.out.println("\n ^~^~^~ COPY ACTION in GenericBuilder ^~^~^~");
                copyAction(mouse, gs);

                paste = true;
            }
        });
        mouse.consume();
    }


    /**
     * This method provides ability to Paste a shape, that is copied to the
     * buffer earlier in the copyAction.
     * @param mouse
     */
    @Override
    public void pasteAction(MouseEvent mouse)
    {
        Shape fxShape;
        DrawTools draw = DrawTools.getInstance();
        System.out.println( "\n ^~^~^~ PasteAction in GenericBuilder ^~^~^~");

        editorRef.getRightPane().getChildren().clear();
        editorRef.getRightPane().getChildren().add(editorRef.getImageView());

        System.out.println("\t - Should choose the appropriate class here");
        overlayPane.getChildren().clear();

        /**
        Problem: newShapeAction is not selecting the correct class.

        discussion: Is there an example when a method is selected from a child class
            Yes: When gbcopyArrayOfFMShapes is read, the getShape() method is used. It is implicitly selected.
            - The method getShape() is from
        */
        double scale = editorRef.getScale();// getFitHeight();

        for(int i = 1; i < gbcopyArrayOfFMShapes.size(); i++)
        {
            System.out.println("\t in loop, class is: " + gbcopyArrayOfFMShapes.get(i).getClass().getName() );
            // Get shapes builder, assign generic to type, and
            B b = (B) gbcopyArrayOfFMShapes.get(i).getBuilder(editorRef);

            fxShape = b.editableShapeAction(gbcopyArrayOfFMShapes.get(i).getShape(), gbcopyArrayOfFMShapes.get(i));

            overlayPane.getChildren().add(fxShape);
            editorRef.getRightPane().getChildren().add(gbcopyArrayOfFMShapes.get(i).getScaledShape(scale));
        }

        paste = false;
        mouse.consume();
    }


    @Override
    public void verticyReleased(GenericShape gs)
    {
        System.out.println("\n *** VERTICY_RELEASED pressed ***");
        //System.out.println("\t - this.fmRectangle paneIndex: " + ((FMRectangle)gs).getShapeAryIdx());

        // update shape x, y, width, and height in rightPane

        editorRef.getRightPane().getChildren().clear();
        if(editorRef.getImageView() != null) {
            editorRef.getRightPane().getChildren().add(editorRef.getImageView());
        }
        double scale = editorRef.getScale();// getFitHeight();

        for(int i = 1; i < gbcopyArrayOfFMShapes.size(); i++)
        {
            editorRef.getRightPane().getChildren().add(gbcopyArrayOfFMShapes.get(i).getScaledShape(scale));
        }

        DrawTools.setShapeNotSelected(true);
    }

    // for testing

    /**
     * Returns true if the number of nodes in the nodes pane is equivelant to the number
     * provided in the parameter.
     * @param num
     * @return
     */
    public boolean areNodesinPane(int num) {
        return (this.nodesPane.getChildren().size() == num);
    }
}
