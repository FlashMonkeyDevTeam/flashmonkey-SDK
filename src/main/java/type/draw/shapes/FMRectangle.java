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

import flashmonkey.FlashMonkeyMain;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;
import uicontrols.UIColors;

import java.io.Serializable;

import static uicontrols.UIColors.*;

/**
 * A serializable rectangle w/ or wo/ rounded corners. Holds the values minX, minY, and width and height,
 * arcW and arcH. Fill color, stroke color, stroke width.
 * In addition to the Java API Rectangle, also holds the index in the shapeArray
 * Constructor builds a Rectangle. To get a square use the full constructor, the default constructor,
 * or build one from a Rectangle.
 * @author Lowell Stadelman
 */
public class FMRectangle extends GenericShape<FMRectangle> implements Serializable
{
    private static final long serialVersionUID = FlashMonkeyMain.VERSION;
    
    // This shapes x and y positions
    private double x; // minX
    private double y; // minY
    private double wd;
    private double ht;

    // Stroke and fill
    private double strokeWidth;
    private StringProperty fillProperty;
    private StringProperty strokeProperty;
    
    // A shape knows its index in the
    // shape array
    private int shapeAryIdx;
    
    // For rounded corners
    private double arcWidth = 0.0;
    private double arcHeight = 0.0;
    // This objects index in the shapeArray


    /**
     * No args constructor
     */
    public FMRectangle() { /* empty */}

    /**
     * Full constructor without an arc for the corners
     * @param x1 the minX
     * @param y1 the minY
     * @param wd width of rectangle
     * @param ht height of rectangle
     * @param stroke stroke color
     * @param fill fill color
     * @param index this objects index in the arrayOfShapes/arrayOfFMShapes
     */
    public FMRectangle(double x1, double y1, double wd, double ht,
                       double strokeW, String stroke, String fill, int index)
    {
        this.x = x1;
        this.y = y1;
        this.strokeWidth = strokeW;
        this.strokeProperty = new SimpleStringProperty(stroke);
        this.fillProperty = new SimpleStringProperty(fill);
        this.shapeAryIdx = index;
        this.wd = wd;
        this.ht = ht;

        System.out.println(" \n *** FMRectangle 1st full constructor called ***");
    }

    /**
     * Full constructor adds arcHeight and arcWidth.
     * @param x1
     * @param y1
     * @param wd
     * @param ht
     * @param strokeW
     * @param stroke
     * @param fill
     * @param arcH
     * @param arcW
     * @param index
     */
    public FMRectangle(double x1, double y1, double wd, double ht,
                       double strokeW, String stroke, String fill, double arcH, double arcW, int index)
    {
        //super(x1, y1, strokeW, stroke, fill, origWd, origHt, index);
        this.x = x1;
        this.y = y1;
        this.strokeWidth = strokeW;
        this.strokeProperty = new SimpleStringProperty(stroke);
        this.fillProperty = new SimpleStringProperty(fill);
        this.shapeAryIdx = index;

        this.wd = wd;
        this.ht = ht;
        this.arcWidth = arcW;
        this.arcHeight = arcH;

        System.out.println(" \n *** FMRectangle 2nd full constructor called ***");
    }

    /**
     * <p> Creates an FMRectangle using a shallow copy of the parameters values.
     * if the parameter does not have a stroke or fill color, sets stroke to <@code>UIColors.
     * HIGHLIGHT_PINK</@code> and fill to <@code>UIColors.TRANSPARENT</@code></p>
     *
     * @param rect
     */
    public FMRectangle(Rectangle rect)
    {
        //super(rect.getX(), rect.getY(), rect.getStrokeWidth(), rect.getStroke().toString(), rect.getFill().toString());
        this.x = rect.getX();
        this.y = rect.getY();
        this.strokeWidth = rect.getStrokeWidth();
        this.strokeProperty = new SimpleStringProperty(rect.getStroke() == null ?  UIColors.HIGHLIGHT_PINK : rect.getStroke().toString());
        this.fillProperty   = new SimpleStringProperty(rect.getFill() == null ? UIColors.TRANSPARENT : rect.getFill().toString());
        this.wd = rect.getWidth();
        this.ht = rect.getHeight();
        this.arcWidth = rect.getArcWidth();
        this.arcHeight = rect.getArcHeight();

        System.out.println(" \n *** 3rd constructor from FMRectangle called ***");
    }

    /**
     * FMRectangle Copy constructor
     * @param other
     */
    public FMRectangle(FMRectangle other)
    {
        this.x = other.x;
        this.y = other.y;
        this.strokeWidth = other.strokeWidth;
        this.strokeProperty = other.strokeProperty;
        this.fillProperty = other.fillProperty;
        this.shapeAryIdx = other.shapeAryIdx;

        this.wd = other.wd;
        this.ht = other.ht;
        this.arcWidth = other.arcWidth;
        this.arcHeight = other.arcHeight;

        System.out.println("Copy constructor called");
    }
    
    
    /**
     * @return Returns a deep copy of this FMRectangle
     */
    @Override
    public FMRectangle clone() {
        return new FMRectangle(
            this.x,
            this.y,
            this.wd,
            this.ht,
            this.strokeWidth,
            this.strokeProperty.getValue(),
            this.fillProperty.getValue(),
            this.arcHeight,
            this.arcWidth,
            this.shapeAryIdx
        );
    }



    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                        GETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    public double getX() { return this.x; }

    public double getY() { return this.y; };

    public int getShapeAryIdx() { return this.shapeAryIdx; }

    public double getStrokeWidth() { return this.strokeWidth; }

    public String getStrokeColor() { return this.strokeProperty.getValue(); }

    public String getFillColor() { return this.fillProperty.getValue(); }

    public StringProperty getStrokeProperty() { return this.strokeProperty; }

    public StringProperty getFillProperty() { return this.fillProperty; }

    public double getWd() { return this.wd; }

    public double getHt() { return this.ht; }

    public double getArcWidth() { return this.arcWidth; }

    public double getArcHeight() { return this.arcHeight; }

    /**
     * Returns a Java Rectangle from this object
     * @return Returns a Rectangle from this object
     */
    @Override
    public Rectangle getShape()
    {
        Rectangle r = new Rectangle(this.getX() , this.getY() , this.wd, this.ht);
        
        r.setStrokeWidth(this.getStrokeWidth());
        r.setStroke(convertColor(this.getStrokeColor()));
        r.setFill(convertColor(this.getFillColor()));
        r.setArcHeight(this.arcHeight);
        r.setArcWidth(this.arcWidth);
        return r;
    }
    
    /**
     * Returns a scaled Rectangle using the scale
     * provided in the parameter.
     * @param scaleY
     * @return
     */
    @Override
    public Rectangle getScaledShape(double scaleY)
    {
        System.out.println("\n *** FMRectangle getScaledShape() called ***");
        
        // Using JavaFX Rectangle
        Rectangle r = new Rectangle(
                this.getX() * scaleY - 1,
                this.getY() * scaleY - 1 ,
                this.wd  * scaleY,
                this.ht  * scaleY);
        
        r.setStrokeWidth(this.getStrokeWidth() * scaleY);
        r.setStroke(convertColor(this.getStrokeColor()));
        r.setFill(convertColor(this.getFillColor()));
        r.setArcHeight(this.arcHeight);
        r.setArcWidth(this.arcWidth);
        
        return r;
    }
    
    
    



    @Override
    public GenericBuilder<FMRectangle, RectangleBuilder> getBuilder(SectionEditor editor, boolean scaled) {

        DrawTools dt = DrawTools.getInstance();
        //scaled to be used later = scaled
        return new RectangleBuilder(
                this,
                dt.getCanvas(),
                dt.getGrapContext(),
                dt.getOverlayPane(),
                editor
        );
    }

    @Override
    public GenericBuilder<FMRectangle, RectangleBuilder> getBuilder(SectionEditor editor) {
        
        DrawTools draw = DrawTools.getInstance();
        return new RectangleBuilder(
            draw.getCanvas(),
            draw.getGrapContext(),
            draw.getOverlayPane(),
            editor,
                this.strokeProperty.getValue(),
                this.fillProperty.getValue()
        );
    }



    


    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                        SETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    public void setX(double x1) { this.x = x1; }

    public void setY(double y1) { this.y = y1; }

    public void setShapeAryIdx(int i) { this.shapeAryIdx = i; }

    public void setStrokeWidth(double strokeWd) { this.strokeWidth = strokeWd; }

    public void setStrokeColor(String strokeClr) { this.strokeProperty.set(strokeClr); }

    public void setFillColor(String fillClr) { this.fillProperty.set(fillClr); }

    /**
     * GETTERS
     **/
    @Override
    public double[] getDblPts() {
        return new double[0];
    }

    public void setWd(double wide) { this.wd = wide; }

    public void setHt(double height) { this.ht = height; }

    public void setArcWidth( double arcW) { this.arcWidth = arcW; }

    public void setArcHeight(double arcH) { this.arcHeight = arcH; }
    
    /**
     * Stub
     * @param pts
     */
    public void setPoints(ObservableList<Double> pts) { /* stub */}




    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                OTHER METHODS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/


    /**
     * We are only concerned with xy location, width, and height
     * @param other FMRectangle
     * @return boolean if the FMRectangle in the parameter matches
     * this FMRectangle.
     */
    @Override
    public boolean equals(Object other ) {

        if(other.getClass() != FMRectangle.class) {
            return false;
        }

        FMRectangle otherRect = (FMRectangle) other;
        if(this.wd == otherRect.getWd() &&
                this.ht == otherRect.getHt() &&
                this.x == otherRect.getX()
                && this.y == otherRect.getY()) {
            return true;
        }
        return false;
    }


    /**
     * Returns a hashcode for this FMRectangle based on width, height, x, y
     * and the hashcode from the classname.
     * @return A hashcode for this FMCircle
     */
    @Override
    public int hashCode() {
        return  this.getClass().getName().hashCode()
                + (int) this.wd
                + (int) this.ht
                + (int) this.x
                + (int) this.y;
    }


    /**
     * Converts this object to a Grahics Context for use with Canvas
     * //@param gc The GraphicsContext that this object will draw on.
     * @return Returns this object as GraphicsContext
     * strokecircle of the canvas given in the parameter.
     */
    @Override
    public Canvas convertToCanvas(Canvas canvas)
    {
        System.out.println(" \n *** FMRectangle convertToCanvas() called ***");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.strokeRoundRect(getX(), getY(), wd, ht, arcWidth, arcHeight);
        gc.setStroke(convertColor(this.getStrokeColor()));
        gc.setFill(convertColor(this.getFillColor()));
        gc.setLineWidth(this.getStrokeWidth());

        return canvas;
    }

    /**
     * Converts a Canvas Rectangle to an FMRectangle
     * @param rect The Canvas Rectangle
     * @return Returns a Serializable FMRectangle
     * ** Does not copy rounded corners
     */
    @Override
    public FMRectangle convertFmCanvas(Canvas rect)
    {

        //@todo rounded corners in convertFmCanvas in FMRectangle

        System.out.println("\n *** convertFmCanvas called ***");

        return new FMRectangle(
                rect.getLayoutX(),
                rect.getLayoutY(),
                rect.getWidth(),
                rect.getHeight(),
                rect.getGraphicsContext2D().getLineWidth(),
                rect.getGraphicsContext2D().getStroke().toString(),
                rect.getGraphicsContext2D().getFill().toString(),
                0);
    }


    @Override
    public String toString()
    {
        return " rightPaneIndex = " + getShapeAryIdx()
                + "\n x = " + getX()
                + "\n y = " + getY()
                + "\n wd = " + wd
                + "\n ht = " + ht
                + "\n arcHt: " + arcHeight
                + "\n arcWd: " + arcWidth
                + "\n lineWd = " + getStrokeWidth()
                + "\n lineColor = " + getStrokeColor()
                + "\n fillColor = " + getFillColor();
    }

}
