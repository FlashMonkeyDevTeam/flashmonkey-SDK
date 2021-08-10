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
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.io.Serializable;

import static uicontrols.UIColors.convertColor;

/**
 * A serializable triangle.
 * In addition to the Java API Polygon, also holds the index in the shapeArray
 * Constructor builds a polygon. To get an fmtriagnle use the full constructor, the default constructor,
 * or build one from a polygon.
 * @author Benjamin Boyle
 */
public class FMTriangle extends GenericShape<FMTriangle> implements Serializable
{
    private static final long serialVersionUID = FlashMonkeyMain.VERSION;
    
    protected double x1;
    protected double y1;
    protected double x2;
    protected double y2;
    protected double x3;
    protected double y3;
    
    private double strokeWidth;
    private String strokeColor;
    private String fillColor;
    
    // A shape knows its index in the
    // shape array
    private int shapeAryIdx;

    /**
     * No args constructor,
     */
    public FMTriangle() {
        /* empty */
    }


    /**
     * Constructor with one point
     * @param x1
     * @param deltaX
     * @param y1
     * @param deltaY
     */
    public FMTriangle(double x1, double y1, double deltaX, double deltaY,
                      double strokeW, String stroke, String fill, int index)
    {
        this.x1 = x1;
        this.y1 = y1 + deltaY;
        this.x2 = x1 + deltaX;
        this.y2 = y1 + deltaY;
        this.x3 = x1 + (deltaX / 2);
        this.y3 = y1;
        this.strokeWidth = strokeW;
        this.strokeColor = stroke;
        this.fillColor = fill;
        this.shapeAryIdx = index;
    }

    /**
     * Constructor with 3 points
     * @param x1
     * @param x2
     * @param x3
     * @param y1
     * @param y2
     * @param y3
     * testing
     */
    public FMTriangle(double x1, double y1, double x2, double y2, double x3,
                      double y3, double strokeW, String stroke, String fill, int index)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.strokeWidth = strokeW;
        this.strokeColor = stroke;
        this.fillColor = fill;
        this.shapeAryIdx = index;
    }


    /**
     * Copy constructor
     * @param other
     */
    public FMTriangle(FMTriangle other) {
        this.x1 = other.x1;
        this.y1 = other.y1;
        this.x2 = other.x2;
        this.y2 = other.y2;
        this.x3 = other.x3;
        this.y3 = other.y3;
        this.strokeWidth = other.strokeWidth;
        this.strokeColor = other.strokeColor;
        this.fillColor = other.fillColor;
        this.shapeAryIdx = other.shapeAryIdx;
        this.strokeColor = other.strokeColor;
    }
    
    
    /**
     * @return Returns a deep copy of this FMTriangle
     */
    @Override
    public FMTriangle clone() {
        return new FMTriangle(
                this.x1,
                this.y1,
                this.x2,
                this.y2,
                this.x3,
                this.y3,
                this.strokeWidth,
                this.strokeColor,
                this.fillColor,
                this.shapeAryIdx
        );
    }



    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     *                                              GETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/




    /**
     * Returns a polygon in the form of a triangle
     * @return Returns the polygon (triangle) from this object
     */


    @Override
    public Polygon getShape() {
        Polygon t = new Polygon(x1, y1, x2, y2, x3, y3);

        t.setStrokeWidth(this.getStrokeWidth());
        t.setStroke(convertColor(this.getStrokeColor()));
        t.setFill(convertColor(this.getFillColor()));
        return t;
    }

    /**
     * Returns a scaled shape set to the expected 100 x 100 starting size of the
     * right pane
     * @return
     */
    @Override
    public Polygon getScaledShape(double scaleY) {
        
        System.out.println("*** FMTriangle getScaledShape *** \nTriangle:\n\t x1 and y1: (" + x1 + ", " + y1 + ")" +
                "\n\tx2 and y2: (" + x2 + ", " + y2 + ")\n" );
        
        Polygon t = new Polygon();
        if(scaleY > 0) {
            t = new Polygon(
                    this.x1 * scaleY - 1,
                    this.y1 * scaleY - 1,
                    this.x2 * scaleY - 1,
                    this.y2 * scaleY - 1,
                    this.x3 * scaleY - 1,
                    this.y3 * scaleY - 1
            );
        }

        t.setStrokeWidth(this.getStrokeWidth() * scaleY);
        t.setStroke(convertColor(this.getStrokeColor()));
        t.setFill(convertColor(this.getFillColor()));
        return t;
    }


    public double getX() { return this.x1; }

    public double getY() { return this.y1; }

    public double getX2() { return this.x2; }

    public double getY2() { return this.y2; }

    public double getX3() { return this.x3; }

    public double getY3() { return this.y3; }

    public double getDeltaX()
    {
        return (this.x2 - this.x1);
    }

    public double getDeltaY()
    {
        return this.y2 - this.y1;
    }

    public int getShapeAryIdx() { return this.shapeAryIdx; }

    public double getStrokeWidth() { return this.strokeWidth; }

    public String getStrokeColor() { return this.strokeColor; }

    public String getFillColor() { return this.fillColor; }

    //public double getOrigPaneHt() { return this.origPaneHt; }

    //public double getOrigPaneWd() { return this.origPaneWd; }

    @Override
    public GenericBuilder<FMTriangle, TriangleBuilder> getBuilder(SectionEditor editor, boolean scaled) {

        //scaled to be used later = scaled
        DrawTools dt = DrawTools.getInstance();
        return new TriangleBuilder(
                this,
                dt.getCanvas(),
                dt.getGrapContext(),
                dt.getOverlayPane(),
                editor
        );
    }


    @Override
    public GenericBuilder<FMTriangle, TriangleBuilder> getBuilder(SectionEditor editor) {

        DrawTools draw = DrawTools.getInstance();
        return new TriangleBuilder(
            draw.getCanvas(),
            draw.getGrapContext(),
            draw.getOverlayPane(),
            editor,
            this.strokeColor,
            this.fillColor
        );
    }


    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     SETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/


    public void setPoints(ObservableList<Double> pts) {
        if(pts.size() == 6) {
            x1 = pts.get(0);
            y1 = pts.get(1);
            x2 = pts.get(2);
            y2 = pts.get(3);
            x3 = pts.get(4);
            y3 = pts.get(5);
        }
    }

    public void setX(double x1) { this.x1 = x1; }

    public void setY(double y1) { this.y1 = y1; }

    public void setX2(double x2) { this.x2 = x2; }

    public void setY2(double y2) { this.y2 = y2; }

    public void setX3(double x3) { this.x3 = x3; }

    public void setY3(double y3) { this.y3 = y3; }

    public void setShapeAryIdx(int i) { this.shapeAryIdx = i; }

    public void setStrokeWidth(double strokeWd) { this.strokeWidth = strokeWd; }

    public void setStrokeColor(String strokeClr) { this.strokeColor = strokeClr; }

    public void setFillColor(String fillClr) { this.fillColor = fillClr; }

    /**
     * GETTERS
     **/
    @Override
    public double[] getDblPts() {
        double[] p = {x1, x2, x3, y1, y2, y3};
        return p;
    }


    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     OTHER METHODS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/


    /**
     * We are only concerned with xy location, width, and height
     * @param other FMCircle
     * @return boolean if the FMCircle in the parameter matches
     * this FMCircle.
     */
    @Override
    public boolean equals(Object other ) {

        if(other.getClass() != FMTriangle.class) {
            return false;
        }
        FMTriangle otherFMTri = (FMTriangle) other;
        if(this.x1 == otherFMTri.x1 &&
                this.x2 == otherFMTri.x2 &&
                this.x3 == otherFMTri.x3 &&
                this.y1 == otherFMTri.y1 &&
                this.y2 == otherFMTri.y2 &&
                this.y3 == otherFMTri.y3
        ) {
            return true;
        }
        return false;
    }


    /**
     * Returns a hashcode for this FMTriangle based on points
     * and the hashcode from the classname.
     * @return A hashcode for this FMTriangle
     */
    @Override
    public int hashCode() {
        return  this.getClass().getName().hashCode()
                + (int) this.x1
                + (int) this.x2
                + (int) this.x3
                + (int) this.y1
                + (int) this.y2
                + (int) this.y3;
    }


    /**
     * Converts this object to a Grahics Context for use with Canvas
     * //@param gc The GraphicsContext that this object will draw on.
     * @return Returns this object as GraphicsContext
     * strokecircle of the canvas given in the parameter.
     */
    @Override
    public Canvas convertToCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double[] xPoints = {x1, x2, x3};
        double[] yPoints = {y1, y2, y3};
        int numPoints = 3;

        gc.strokePolygon( xPoints, yPoints, numPoints);
        gc.setStroke(convertColor(this.getStrokeColor()));
        gc.setFill(convertColor(this.getFillColor()));
        gc.setLineWidth(this.getStrokeWidth());

        return canvas;
    }


    /**
     * Converts a Canvas Triangle to an FMTriangle
     * @param tri The Canvas Triangle
     * @return Returns a Serializable FMTriangle
     * ** Does not copy rounded corners
     */
    @Override
    public FMTriangle convertFmCanvas(Canvas tri) {

        //@todo Finish ConvertFmCanvas in FMTriangle

        System.out.println("\n *** convertFmCanvas called ***");

        return new FMTriangle(
                tri.getLayoutX() ,
                tri.getLayoutY() ,
                tri.getWidth(),
                tri.getHeight(),
                tri.getGraphicsContext2D().getLineWidth(),
                tri.getGraphicsContext2D().getStroke().toString(),
                tri.getGraphicsContext2D().getFill().toString(),
                0);
    }

    @Override
    public String toString()
    {
        return "this is a FMTriangle!";
    }


}
