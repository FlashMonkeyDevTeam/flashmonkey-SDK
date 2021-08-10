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
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;
import java.io.Serializable;

import static uicontrols.UIColors.*;

/**
 * A serializable line. Holds the values pts, and line,
 * In addition to the Java API Line, also holds the index in the shapeArray
 * Constructor builds a Line.
 */
public class FMPolyLine extends GenericShape<FMPolyLine> implements Serializable
{
    private static final long serialVersionUID = FlashMonkeyMain.VERSION;

    // This shapes x and y positions
    //private ObservableList<Double> pts;
    private double[] pts;
    private Polyline line;

    // Stroke and fill

    // A shape knows its index in the
    // shape array
    private int shapeAryIdx;


    /**
     * No args constructor
     */
    public FMPolyLine() { /* empty */ }

    /**
     * Called by shape builders that extend this class.
     * @param pts
     * @param strokeW
     * @param stroke
     * @param fill
     */
    public FMPolyLine(double[] pts, double strokeW, String stroke, String fill)
    {
        line = new Polyline(pts);
        line.setFill(Color.web(fill));
        line.setStrokeWidth(strokeW);
        line.setStroke(Color.web(stroke));

        System.out.println(" \n *** FMLine 1st full constructor called ***");
    }


    /**
     * Full constructor
     * @param pts the ObservableList of points
     * @param stroke stroke color
     * @param fill fill color
     * @param index this objects index in the arrayOfShapes/arrayOfFMShapes
     */
    public FMPolyLine(double[] pts, double strokeW, String stroke, String fill, int index)
    {
        line = new Polyline(pts);
        line.setFill(Color.web(fill));
        line.setStrokeWidth(strokeW);
        line.setStroke(Color.web(stroke));
        this.shapeAryIdx = index;

        System.out.println(" \n *** FMLine 1st full constructor called ***");
    }

    /**
     * <p> Creates an FMLine using a shallow copy of the parameters values.
     * if the parameter does not have a stroke or fill color, sets stroke to <@code>UIColors.
     * HIGHLIGHT_PINK</@code> and fill to <@code>UIColors.TRANSPARENT</@code></p>
     *
     * @param line
     */
    public FMPolyLine(Polyline line)
    {
        this.line = line;

        System.out.println(" \n *** 3rd constructor from FMLine called ***");
    }

    /**
     * FMLine Copy constructor
     * @param other
     */
    public FMPolyLine(FMPolyLine other)
    {
        line = new Polyline(other.getDblPts());
        line.setFill(Color.web(other.getFillColor()));
        line.setStrokeWidth(other.getStrokeWidth());
        line.setStroke(Color.web(other.getStrokeColor()));
        this.shapeAryIdx = other.shapeAryIdx;

        System.out.println("Copy constructor called");
    }


    /**
     * @return Returns a deep copy of this FMLine
     */
    @Override
    public FMPolyLine clone() {
        return new FMPolyLine(
                this.pts,
                this.getStrokeWidth(),
                this.getStrokeColor(),
                this.getFillColor(),
                this.shapeAryIdx
        );
    }



    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     GETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/

    public ObservableList<Double> getPts() {return this.getPts();}

    public double[] getDblPts() {return this.pts;}

    public int getShapeAryIdx() { return this.shapeAryIdx; }

    public double getStrokeWidth() { return line.getStrokeWidth(); }

    public String getStrokeColor() { return line.getStroke().toString(); }

    public String getFillColor() { return line.getFill().toString(); }

    private Polyline getLine() {
        return this.line;
    }

    /**
     * @return Returns a JavaFX PolyLine from this object
     */
    @Override
    public Polyline getShape() {
        return this.line;
    }

    /**
     * Returns a scaled Line using the scale
     * provided in the parameter.
     * @param scaleY
     * @return
     */
    @Override
    public Polyline getScaledShape(double scaleY)
    {
        System.out.println("\n *** FMPolyLine getScaledShape() called ***");
        setPoints(this.line.getPoints());

        int size = pts.length;
        double[] scaledPts = new double[size];
        // Using JavaFX Line
        for(int i =0; i < size; i++) {
            scaledPts[i] = pts[i] * scaleY;
        }
        Polyline l = new Polyline(scaledPts);
        l.setStrokeWidth(this.getStrokeWidth() * scaleY);
        l.setStroke(Color.web(this.getStrokeColor()));
        l.setFill(Color.web(this.getFillColor()));

        return l;
    }


    @Override
    public GenericBuilder<FMPolyLine, PolylineBuilder> getBuilder(SectionEditor editor, boolean scaled) {

        DrawTools dt = DrawTools.getInstance();
        //scaled to be used later = scaled
        return new PolylineBuilder(
                this,
                dt.getCanvas(),
                dt.getGrapContext(),
                dt.getOverlayPane(),
                editor
        );
    }

    @Override
    public GenericBuilder<FMPolyLine, PolylineBuilder> getBuilder(SectionEditor editor) {

        DrawTools draw = DrawTools.getInstance();
        return new PolylineBuilder(
                draw.getCanvas(),
                draw.getGrapContext(),
                draw.getOverlayPane(),
                editor,
                this.getStrokeColor(),
                this.getFillColor()
        );
    }




    /** ************************************************************************************************************ ***
     *                                                                                                                 *
     SETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/



    public void setShapeAryIdx(int i) { this.shapeAryIdx = i; }

    public void setStrokeWidth(double strokeWd) { line.setStrokeWidth(strokeWd); }

    public void setStrokeColor(String strokeClr) { line.setStroke(Color.web(strokeClr)); }

    public void setFillColor(String fillClr) { line.setFill(Color.web(fillClr)); }


    /**
     * not used
     * @param notUsed
     **/
    @Override
    public void setX(double notUsed) {
        // not used
    }

    /**
     * not used
     * @param notUsed
     **/
    @Override
    public void setY(double notUsed) {
        // not used
    }

    /**
     * Sets the points for this PolyLine
     * @param points
     */
    public void setPoints(ObservableList points) {
        double[] p = new double[points.size()];
        for(int i = 0; i < points.size(); i++) {
            p[i] = (double) points.get(i);
        }

        this.pts = p;
    }


    /**
     * Returs the X position of this shape. Generally the top left
     * (aka minimum)  or the X position of the center of the
     * shape if it si an ellipse.
     *
     * @return
     */
    @Override
    public double getX() {
        return line.getPoints().get(0);
    }

    /**
     * Returns the Y position of this shape Generally the top left
     * ( aka minimum ) or the Y position of the center of the
     * shape if it is an ellipse.
     *
     * @return
     */
    @Override
    public double getY() {
        //return line.getPoints().get(line.getPoints().size()/2 + 1);
        return line.getPoints().get(line.getPoints().size()/2 );
    }



    /** ************************************************************************************************************ ***
     *                                                                                                                 *
                                                    OTHER METHODS
     *                                                                                                                 *
     ** ************************************************************************************************************ **/


    /**
     * We are only concerned with xy location, width, and height
     * @param other FMLine
     * @return boolean if the FMLine in the parameter matches
     * this FMLine.
     */
    @Override
    public boolean equals(Object other ) {

        if(other.getClass() != FMPolyLine.class) {
            return false;
        }

        FMPolyLine otherLine = (FMPolyLine) other;
        return this.pts.equals(otherLine.pts);
    }


    /**
     * Returns a hashcode for this FMLine based on width, height, x, y
     * and the hashcode from the classname.
     * @return A hashcode for this FMCircle
     */
    @Override
    public int hashCode() {
        return  this.getClass().getName().hashCode()
                + this.pts.hashCode();
    }


    private double[] getXPts() {
        double[] p = new double[pts.length];
        for(int i = 0; i < pts.length / 2; i++) {
            p[i] = pts[i];
        }
        return p;
    }


    private double[] getYPts() {
        double[] p = new double[pts.length];
        for(int i = pts.length/2 + 1; i < pts.length/2; i++) {
            p[i] = pts[i];
        }
        return p;
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
        System.out.println(" \n *** FMLine convertToCanvas() called ***");

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.strokePolyline(getXPts(), getYPts(), (pts.length/2));
        gc.setStroke(convertColor(getStrokeColor()));
        gc.setFill(convertColor(getFillColor()));
        gc.setLineWidth(this.getStrokeWidth());

        return canvas;
    }

    /**
     * not used
     */
    @Override
    public FMPolyLine convertFmCanvas(Canvas l)
    {
        return new FMPolyLine();
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(" rightPaneIndex = " + getShapeAryIdx() + "\n points: ");
        for(Double o : pts) {
            sb.append(o + ",");
        }
        sb.append("\n lineWd = " + getStrokeWidth()
                + "\n lineColor = " + getStrokeColor()
                + "\n fillColor = " + getFillColor());

        return sb.toString();
    }

}
