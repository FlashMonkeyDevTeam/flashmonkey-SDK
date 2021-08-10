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

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Shape;
import type.celleditors.SectionEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This is the abstract shape class. The parent class for each FM Shape.
 * It contains the varables and methods common to most of the FMShapes as
 * well as the abstract methods that must be implemented by all FMshapes
 * classes.
 *
 * @author Lowell Stadelman
 */
public abstract class GenericShape<T extends GenericShape> //extends DrawTools
{

    // THE LOGGER
    // REMOVE BEFORE DEPLOYING
    //private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericShape.class);


    public GenericShape() {
        /* Empty Constructor */
    }

    /**  SETTERS  **/

    /** Sets the origin x location or top left position **/
    public abstract void setX(double x1);// { this.x = x1; }

    /** Sets the origin y location or top left position **/
    public abstract void setY(double y1);// { this.y = y1; }
    
    /** sets the points using an observable list... ie for polynomials */
    public abstract void setPoints(ObservableList<Double> pts);

    /**
     * Each shape object knows it's index in the shape array
     * Sets this shapes index
     **/
    public abstract void setShapeAryIdx(int i);// { this.shapeAryIdx = i; }

    /** Line width **/
    public abstract void setStrokeWidth(double strokeWd);// {this.strokeWidth = strokeWd;}

    /** Line color **/
    public abstract void setStrokeColor(String strokeClr);//

    /** Fill color **/
    public abstract void setFillColor(String fillClr);

    //** Moved to the first shape in the shape array
    // ... Each object knows its original pane height **/
    //public abstract void setOrigPaneHt(double origPnHt);// { this.origPaneHt = origPnHt; }

    //** Moved to the first shape in the shape array
    // ... Each object knows its original pane height **/
    //public abstract void setOrigPaneWd(double origPnWd);// { this.origPaneWd = origPnWd; }


    /**  GETTERS  **/

    public abstract double[] getDblPts();

    /**
     * Returs the X position of this shape. Generally the top left
     * (aka minimum)  or the X position of the center of the
     * shape if it si an ellipse.
     * @return
     */
    public abstract double getX();// { return this.x; }

    /**
     * Returns the Y position of this shape Generally the top left
     * ( aka minimum ) or the Y position of the center of the
     * shape if it is an ellipse.
     * @return
     */
    public abstract double getY();// { return this.y; };

    /**
     * Returns the index that this object exists in within the
     * arrayOfShapes or shapesArray
     * @return the index that this object exists in
     */
    public abstract int getShapeAryIdx();// { return this.shapeAryIdx; }

    /**
     * Returns the line or stroke width of the shape
     * @return
     */
    public abstract double getStrokeWidth();// { return this.strokeWidth; }

    /**
     * Returns the shapes line color or stroke color
     * @return the shapes line color or stroke color
     */
    public abstract String getStrokeColor();// { return this.strokeColor; }

    /**
     * Returns the fill color for the object
     * @return
     */
    public abstract String getFillColor();// { return this.fillColor; }

    /**
     * Returns the java shape i.e. Rectangle and not FMRectangle
     * @return the java shape i.e. Rectangle and not FMRectangle
     */
    public abstract Shape getShape();
    

    /**
     * Resizes the this.shape based on
     * the panes height. Provide the scale from
     * the original ht to the rightPane ht in the
     * parameter.
     * @param scale
     * @return Returns the shape
     */
    public abstract Shape getScaledShape(double scale);

    /**
     * Returns a new builder created within the method. An FM shape
     * does not contain a field for the BuilderClass of a shape.
     * @param editor
     * @return
     */
    public abstract GenericBuilder getBuilder(SectionEditor editor);


    /**
     * <p> Used to create a shape in the overLay canvas. Sets
     * the shape from an existing shape. Call this method then
     * call <@code>getGCShape()</@code> to get the Graphics Context shape. </p>
     *
     * <p>Returns a new builder created within the method. An FM Shape
     * does not contain a field for a Builder Object of a shape.</p>
     * @param editor The current section editor
     * @param scaled Not currently used
     * @return
     */
    public abstract GenericBuilder getBuilder(SectionEditor editor, boolean scaled);


    /**  OTHERS  **/

    /**
     * Converts this shape to a canvas shape.
     * - Currently this field is not used.
     * - May be depricated in future versions. May leave as a stub.
     * @param canvas
     * @return
     */
    public abstract Canvas convertToCanvas(Canvas canvas);

    /**
     * Converts this shape from a Canvas shape to an FMShape.
     * - Currently this field is not used.
     * - May be depricated in future versions. May leave as a stub.
     * @param shape
     * @return
     */
    public abstract GenericShape convertFmCanvas(Canvas shape);


    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();
    
    @Override
    public abstract GenericShape clone();


    /**
     * Converts a Color String representation from hex ( 0x........ )
     * to rgba( xxx, xxx, xxx, .xx)
     *
     * //@param colorString A 10 digit color string beginning with 0x
     * @return Returns the rgba color.
     */
    /*
    public Color convertColor(String colorString)
    {
        if (colorString == null) {

            throw new NullPointerException("The color components name must be specified");
            //colorString = "rgba(0,0,0,0)";

        }
        if (colorString.isEmpty()) {

            throw new IllegalArgumentException("Invalid color specification");
           ///colorString = "rgba(0,0,0,0)";
        }

        int r;
        int g;
        int b;
        int a;

        if(colorString.startsWith("r"))
        {
            return Color.web(colorString);
        }

        if(colorString.startsWith("0x"))
        {
            colorString = colorString.substring(2);
            int len = colorString.length();

            try
            {
                if (len == 6)
                {
                    r = Integer.parseInt(colorString.substring(0, 2), 16);
                    g = Integer.parseInt(colorString.substring(2, 4), 16);
                    b = Integer.parseInt(colorString.substring(4, 6), 16);

                    return Color.rgb(r, g, b, 1);

                } else if (len == 8)
                {

                    r = Integer.parseInt(colorString.substring(0, 2), 16);
                    g = Integer.parseInt(colorString.substring(2, 4), 16);
                    b = Integer.parseInt(colorString.substring(4, 6), 16);
                    a = Integer.parseInt(colorString.substring(6, 8), 16);

                    return Color.rgb(r, g, b, a / 255.0);

                } else
                {

                    System.err.println("ERROR: ColorString is: " + colorString);

                }
            } catch (NumberFormatException nfe)
            {
                System.err.println("\nERROR:  NumberFormatException in GenericShape line 190\n");
            }

            throw new IllegalArgumentException("Invalid color specification: Digits not 6 or 8 ");
        }
        System.out.println("\n using Color.web(colorString");
        try
        {
            return Color.web(colorString);

        } catch (IllegalArgumentException e) {

            System.out.println("ERROR: ColorString invalid, IllegalArgumentException at convertColor in GenericSHape. " +
                    "\n colorString is: " + colorString);
            System.out.println(e.getStackTrace());
        }
        // Default
        return Color.ALICEBLUE;
    }
    
     */


    public boolean delete(GenericShape gs, SectionEditor editor, double scale)
    {
        //SectionEditor cEdt = new SectionEditor();

        /**
        problem here. Indexes change when a shape is deleted. The rest of the shapes after the shape will be off.
         Solution 1: Use hash tables - slow creation, T of ?
         Solution 2: Renumber the shapes, creation O of N (not great) , T of 10 worst case.
         Solution 3: Use the shapes reference. - Must create and hold the object, Memory -- Doesn' work consistently,
         not robust.
        */
        //boolean deleteBool = false;
        int aryIdx = gs.getShapeAryIdx();


        // When a shape is newly created, it's idx is correct. When it is edited, it's idx may be
        // off due to panes added prior to the shapes being added in the Pane/container. Thus we
        // search using idx as the starting point.

        System.out.println(" In delete, gs object == " + gs.getShape().getClass().toString() ) ;
        do {
            if (gs.equals(editor.getArrayOfFMShapes().get(aryIdx)))
            {
                System.out.println("Shapes match in GenericShape.delete");

                editor.getArrayOfFMShapes().remove(aryIdx);

                editor.getRightPane().getChildren().clear();
                if(editor.getImageView() != null) {
                    editor.getRightPane().getChildren().add(editor.getImageView());
                }

                for (int i = 1; i < editor.getArrayOfFMShapes().size(); i++) {
                    ((GenericShape) editor.getArrayOfFMShapes().get(i)).setShapeAryIdx(i);
                    editor.getRightPane().getChildren().add(((GenericShape) editor.getArrayOfFMShapes().get(i)).getScaledShape(scale));
                }

                return true;

            } else {

                LOGGER.info("Shape not deleted, searching");

                aryIdx++;
            }
        } while (aryIdx < editor.getArrayOfFMShapes().size());
        LOGGER.warn("ERROR: Shape < {} > not found. Delete failed", gs.getClass().getName());
        return false;

    }
}
