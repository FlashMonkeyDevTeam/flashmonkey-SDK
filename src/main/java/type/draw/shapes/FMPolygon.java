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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.io.Serializable;
import java.util.ArrayList;

import static uicontrols.UIColors.convertColor;

public class FMPolygon extends GenericShape<FMPolygon> implements Serializable
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FMPolygon.class);
	
	double[] points;
	
	protected double x0;
	protected double y0;
	
	private double strokeWidth;
	private String strokeColor;
	private String fillColor;

	// For rounded corners
	private double arcWidth = 0.0;
	private double arcHeight = 0.0;

	// A shape knows its index in the
	// shape array
	private int shapeAryIdx;
	
	/**
	 * No args constructor
	 */
	public FMPolygon(){ /* empty */ }


	/**
	 * Called by shape builders that extend this class.
	 * @param points
	 * @param strokeW
	 * @param stroke
	 * @param fill
	 * @param index
	 */
	public FMPolygon(double[] points, double strokeW, String stroke, String fill, int index) {
		double[] pAry = new double[points.length];
		int i = 0;
		for(double thisDbl : points) {
			pAry[i] = thisDbl;
			i++;
		}
		this.points = pAry;
		x0 = pAry[0];
		y0 = pAry[1];
		this.strokeWidth = strokeW;
		this.strokeColor = stroke;
		this.fillColor = fill;
		this.shapeAryIdx = index;
		this.strokeColor = stroke;
	}

	/**
	 * Copy constructor
	 * @param other
	 */
	public FMPolygon(FMPolygon other) {

		this.points = other.points;
		this.x0 = other.x0;
		this.y0 = other.y0;
		this.strokeColor = other.strokeColor;
		this.fillColor = other.fillColor;
		this.strokeWidth = other.strokeWidth;
		this.shapeAryIdx = other.getShapeAryIdx();
	}

	/**
	 * Returns a deep copy of this FMPolygon
	 * @return
	 */
	public FMPolygon clone() {
		double[] pts = new double[points.length];
		for(int i = 0; i < points.length; i++ ) {
			pts[i] = points[i];
		}
		return new FMPolygon(
				pts,
				this.strokeWidth,
				this.strokeColor,
				this.fillColor,
				this.shapeAryIdx
		);
	}


	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 SETTERS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	

	@Override
	public void setPoints(ObservableList<Double> pts) {
		points = new double[pts.size()];
		if(pts.size() > 1) {
			for(int i = 0; i < points.length; i++ ) {
				points[i] = pts.get(i);
			}
		}
	}
	
	@Override
	public void setX(double x0) {
		this.points[0] = x0;
		this.x0 = x0;
	}
	
	@Override
	public void setY(double y0) {
		this.points[0] = y0;
		this.y0 = y0;
	}
	
	@Override
	public void setShapeAryIdx(int i) {
		this.shapeAryIdx = i;
	}
	
	@Override
	public void setStrokeWidth(double strokeWd) {
		this.strokeWidth = strokeWd;
	}
	
	@Override
	public void setStrokeColor(String strokeClr) {
		this.strokeColor = strokeClr;
	}
	
	@Override
	public void setFillColor(String fillClr) {
		this.fillColor = fillClr;
	}



	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 GETTERS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/


	@Override
	public double[] getDblPts() {
		return new double[0];
	}

	@Override
	public double getX() {
		return this.x0;
	}
	
	@Override
	public double getY() {
		return this.y0;
	}
	
	@Override
	public int getShapeAryIdx() {
		return this.shapeAryIdx;
	}
	
	@Override
	public double getStrokeWidth() {
		return this.strokeWidth;
	}
	
	@Override
	public String getStrokeColor() {
		return this.strokeColor;
	}
	
	@Override
	public String getFillColor() {
		return this.fillColor;
	}
	
	@Override
	public Polygon getShape() {
		Polygon p = new Polygon(this.points);
		
		p.setStrokeWidth(this.strokeWidth);
		p.setStroke(convertColor(this.strokeColor));
		p.setFill(convertColor(this.fillColor));
		return p;
	}
	
	@Override
	public Polygon getScaledShape(double scaleY) {
		
		double[] pAry = new double[this.points.length];
		int i = 0;
		for(double d : this.points) {
			pAry[i] = d * scaleY;
			i++;
		}
		
		Polygon p = new Polygon(pAry);
		p.setStrokeWidth(this.getStrokeWidth() * scaleY);
		p.setStroke(convertColor(this.getStrokeColor()));
		p.setFill(convertColor(this.getFillColor()));
		return p;
	}
	
	@Override
	public GenericBuilder<FMPolygon, PolygonBuilder> getBuilder(SectionEditor editor, boolean scaled) {
		DrawTools dt = DrawTools.getInstance();
		//scaled to be used later = scaled
		return new PolygonBuilder(
				this,
				dt.getCanvas(),
				dt.getGrapContext(),
				dt.getOverlayPane(),
				editor
		);
	}

	@Override
	public GenericBuilder<FMPolygon, PolygonBuilder> getBuilder(SectionEditor editor) {
		DrawTools draw = DrawTools.getInstance();
		return new PolygonBuilder(
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
	 OTHER METHODS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/

	
	@Override
	public Canvas convertToCanvas(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		int size = this.points.length / 2;
		double[] xPoints = new double[size];
		double[] yPoints = new double[size];

		for(int i = 0; i < size; i++) {
			// x = odds
			xPoints[i] = points[i];
			// y = evens
			i++;
			yPoints[i] = points[i];
		}
		
		gc.strokePolygon(xPoints, yPoints, size);
		gc.setStroke(convertColor(this.getStrokeColor()));
		gc.setFill(convertColor(this.getFillColor()));
		gc.setLineWidth(this.getStrokeWidth());
		
		return canvas;
	}
	
	@Override
	public GenericShape convertFmCanvas(Canvas poly) {
		/*
		return new FMPolygon(
				poly.getLayoutX(),
				poly.getLayoutY(),
				poly.getWidth(),
				poly.getHeight(),
				poly.getGraphicsContext2D().getLineWidth(),
				poly.getGraphicsContext2D().getStroke().toString(),
				poly.getGraphicsContext2D().getFill().toString(),
				0);
		)
		 */
		return new FMPolygon();
	}
	
	@Override
	public boolean equals(Object other) {
		if(! other.getClass().getName().equals("FMPolygon")) {
			return false;
		}
		FMPolygon otherFMPoly = (FMPolygon) other;
		double[] othPoints = otherFMPoly.points;
		int i = 0;
		for(double pt : this.points) {
			if(pt != othPoints[i]) {
				return false;
			}
			i++;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = this.getClass().getName().hashCode();
		for(double p : points) {
			hash += p;
		}
		return hash;
	}
	
	@Override
	public String toString() {
		return " this is a FMPolygon";
	}
	

}
