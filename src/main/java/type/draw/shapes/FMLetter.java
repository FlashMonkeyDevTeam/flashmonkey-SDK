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
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import type.celleditors.DrawTools;
import type.celleditors.SectionEditor;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.draw.FMTextEditor;
import type.draw.FontAttributes;


import static uicontrols.UIColors.convertColor;

/**
 * A serializable text. Holds the values minX, minY, Also holds the index in the shapeArray
 * Constructor builds a Rectagle with Text
 * @author Lowell Stadelman
 */
public class FMLetter extends FontAttributes<FMLetter> implements Serializable
{
	private static final long serialVersionUID = FlashMonkeyMain.VERSION;
	private static final Logger LOGGER = LoggerFactory.getLogger(FMLetter.class);
	
	private FontAttributes fontAttributes;
	// This shapes x and y positions
	private double x; // minX
	private double y; // minY
	private double wd;
	private double ht;

	
	// Stroke and fill
	private double strokeWidth;
	// The shape's stroke color
	private String strokeColor;
	// The shape's fill color
	private String fillColor;
	
	// A shape knows its index in the
	// shape array
	private int shapeAryIdx;
	
	private String text;
	
	public FMLetter() {
		super();
	}
	
	/**
	 * Builds a TextShape providing for stroke color/border color
	 * and backGround color. Text uses the default font color and size.
	 * To set font, color, and size use the setXXX(...) methods. By
	 * default word wrap is true.
	 * @param x1
	 * @param y1
	 * @param wd
	 * @param ht
	 * @param strokeW
	 * @param stroke
	 * @param fill
	 * @param index
	 * @param text
	 */
	public FMLetter(double x1, double y1, double wd, double ht,
	                double strokeW, String stroke, String fill, int index, String text)
	{
		super();
		this.text = text;
		this.x = x1;
		this.y = y1;
		this.strokeWidth = strokeW;
		this.strokeColor = stroke;
		this.fillColor = fill;
		this.shapeAryIdx = index;
		this.wd = wd;
		this.ht = ht;

		super.fontStroke = stroke;
		
		

		//fxDrawText.setBackground(Background.EMPTY);
		//fxDrawText.setLineSpacing(lineSpacing);
		//fxDrawText.setTextAlignment(textAlign);
	//	fxDrawText.setFont(font); // does not use font here.

		//fxDrawText.setWrappingWidth(wd);
		//fxDrawText.setFill(fontFill);
		//fxDrawText.setStroke(fontStroke);
		//fxDrawText.
		
		
		System.out.println(" \n *** FMText full constructor called ***");
	}
	
	@Override
	public GenericShape clone() {
		return null;
	}
	
	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	                                                SETTERS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void setX(double x1) {
		this.x = x1;
	}
	
	@Override
	public void setY(double y1) {
		this.y = y1;
	}
	
	@Override
	public void setPoints(ObservableList<Double> pts) {
		/* stub */
	}
	
	@Override
	public void setShapeAryIdx(int i) {
		this.shapeAryIdx = i;
	}
	
	@Override
	public void setStrokeWidth(double strokeWd) {
		this.strokeWidth = strokeWd;
	}

	/**
	 * The shapes stroke/border color
	 * @param strokeClr
	 */
	@Override
	public void setStrokeColor(String strokeClr) {
		this.strokeColor = strokeClr;
	}

	/**
	 * The shapes fill color
	 * @param fillClr
	 */
	@Override
	public void setFillColor(String fillClr) {
		this.fillColor = fillClr;
	}

	/**
	 * GETTERS
	 **/
	@Override
	public double[] getDblPts() {
		return new double[0];
	}

	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 *                                              GETTERS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/

	/**
	 * @return Returns a user editable TextArea with the
	 * text, if any and some attributes of the JavaFX Text
	 * shape in getShape();
	 */
	public FMTextEditor getTextEditor() {
		String prompt = "Enter text. To exit and save, press cmd or control and enter ";
		FMTextEditor txtArea = new FMTextEditor(prompt, x, y, wd, ht);
		txtArea.setText(text);
		
		return txtArea;
	}
	
	/**
	 *
	 * @param shape A javaFX Text shape.
	 * @return Returns a user editable TextArea with the
	 * 	  text, if any, and the attributes of the JavaFX Text
	 * 	  shape in the param;
	 */
	public FMTextEditor getTextEditor(Shape shape) {
		// The javaFX text object
		Text fxTextObj = (Text) shape;
		String prompt = "Enter text. To exit and save, press cmd or control and enter ";
		
		FMTextEditor txtArea = new FMTextEditor(
				prompt,
				fxTextObj.getBoundsInLocal().getMinX(),
				fxTextObj.getBoundsInLocal().getMinY(),
				fxTextObj.getBoundsInLocal().getWidth(),
				fxTextObj.getBoundsInLocal().getHeight() + 40);
		
		txtArea.setText(fxTextObj.getText());
		
		return txtArea;
	}

	/**
	 * A Text is a shape, and the Text contained within it,
	 * is not editable.
	 * @return Returns a JavaFX Text with the attributes provided
	 * to the constructor or through setXXX(...).
	 */
	@Override
	public Text getShape() {
		
		LOGGER.info("The text in FMLetter getShape now says: ", text);
		// @TODO getShape is set to 100, 100 for x & y. change back!!
		Text txt = new Text(x, y, this.text);
		txt.setLineSpacing(lineSpacing);
		txt.setTextAlignment(textAlign);
		txt.setFont(font);
		
		txt.setWrappingWidth(wd);
	//	txt.setFill(super.getFontFill());
		txt.setStroke(super.getFontStroke());
		return txt;
	}

	/**
	 * A Text is a shape, and the Text contained within it,
	 * is not editable.
	 * @return Returns a scaled JavaFX Text with the attributes provided
	 * to the constructor or through setXXX(...).
	 * @param scaleY The scale of the shape.
	 */
	@Override
	public Shape getScaledShape(double scaleY) {
		Text t = new Text(
				(this.getX() * scaleY) - 100,
				(this.getY() * scaleY) - 20,
				this.text
		);
		t.setScaleY(scaleY);
		t.setScaleX(scaleY);
		
		t.setLineSpacing(lineSpacing);
		t.setTextAlignment(textAlign);
		t.setFont(font);
		t.setWrappingWidth(wd);
		t.setStroke(super.getFontStroke());
		
		return t;
	}
	
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public double getX() {
		return this.x;
	}
	
	@Override
	public double getY() {
		return this.y;
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
	public GenericBuilder getBuilder(SectionEditor editor, boolean scaled) {
		
		System.out.println("FMLetter. getBuilder(SectionEditor, boolean) called");
		
		DrawTools dt = DrawTools.getInstance();
		return new LetterBuilder(
				this,
				dt.getCanvas(),
				dt.getGrapContext(),
				dt.getOverlayPane(),
				editor
		);
	}
	
	
	@Override
	public GenericBuilder getBuilder(SectionEditor editor) {
		
		System.out.println("FMLetter. getBuilder(SectionEditor) called");
		
		DrawTools draw = DrawTools.getInstance();
		return new LetterBuilder(
				draw.getCanvas(),
				draw.getGrapContext(),
				draw.getOverlayPane(),
				editor,
				this.strokeColor,
				this.fillColor
		);
	}
	
	
	public double getWd() {
		return wd;
	}

	public double getHt() {
		return ht;
	}
	
	
	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 OTHER METHODS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	
	/**
	 * X, y, Compares text, fontFamily, strokeColor, and fontSize
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(Object other) {
		if(other.getClass() != FMLetter.class) {
			return false;
		}
		FMLetter otherFMLet = (FMLetter) other;
		if(this.x == otherFMLet.x &&
				this.y == otherFMLet.y &&
				this.text == otherFMLet.text &&
				this.fontFamily == otherFMLet.fontFamily &&
				this.strokeColor == otherFMLet.strokeColor &&
				this.fontSize == otherFMLet.fontSize
		) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().getName().hashCode()
				+ (int) this.x
				+ (int) this.y
				+ this.text.hashCode()
				+ this.fontFamily.hashCode()
				+ this.strokeColor.hashCode()
				+ (int) this.fontSize;
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
		
		gc.fillText(text, x, y, wd);
		gc.setStroke(convertColor(this.getStrokeColor()));
		gc.setFill(convertColor(this.getFillColor()));
		gc.setLineWidth(this.getStrokeWidth());
		
		return canvas;
	}
	
	//@Override
	public GenericShape convertFmCanvas(Canvas letter) {
		return new FMLetter(
				letter.getLayoutX(),
				letter.getLayoutY(),
				letter.getWidth(),
				letter.getHeight(),
				letter.getGraphicsContext2D().getLineWidth(),
				letter.getGraphicsContext2D().getStroke().toString(),
				letter.getGraphicsContext2D().getFill().toString(),
				0,
				letter.getAccessibleText()
		);
	}
	
	@Override
	public String toString() {
		return " This is an FMLetter"
				+ "\n rightPaneIndex = " + getShapeAryIdx()
				+ "\n text = " + getText()
				+ "\n x = " + getX()
				+ "\n y = " + getY()
				+ "\n wd = " + wd
				+ "\n lineWd = " + getStrokeWidth()
				+ "\n lineColor = " + getStrokeColor()
				+ "\n fillColor = " + getFillColor();
	}
}
