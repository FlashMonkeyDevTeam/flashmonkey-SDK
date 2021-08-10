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

package type.draw;


import flashmonkey.FlashMonkeyMain;
import type.draw.shapes.GenericShape;
import uicontrols.UIColors;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;

import java.io.Serializable;
import java.net.URL;

public abstract class FontAttributes<T extends FontAttributes> extends GenericShape<T> implements Serializable {
	
	private static final long serialVersionUID = FlashMonkeyMain.VERSION;
	
	protected String fontFamily;
	protected String fontWeight;
	protected FontPosture fontStyle; // FontPosture.ITALIC
	protected String fontStroke;
	protected String fontFill;
	protected double fontSize;
	protected double lineSpacing;
	protected TextAlignment textAlign;
	
	protected Font font;
	
	
	/**
	 * no args constructor.
	 * Sets all variables to default values.
	 */
	public FontAttributes() {
		this.fontFamily = "Roboto-Regular";
		//this.fontWeight = "15";
		this.fontStyle  = FontPosture.REGULAR;
		this.fontStroke = UIColors.HIGHLIGHT_PINK;
		this.fontFill 	= UIColors.TRANSPARENT;
		this.fontSize   = 16;
		this.lineSpacing = 6;
		this.textAlign  = TextAlignment.LEFT;
		// works in windows!!!
		URL url = this.getClass().getClassLoader().getResource("font/" + fontFamily +".ttf");
		
		System.out.println("FontAtributes urlString: css/font/" + fontFamily + ".ttf");
		
		String urlStr = url.toExternalForm();
		this.font = Font.loadFont(urlStr, fontSize);
		
	}
	
	/**
	 * Full constructor
	 * @param fontFamily
	 * @param fontWeight
	 * @param fontStyle
	 * @param fontStroke
	 * @param fontFill
	 * @param fontSize
	 * @param lineSpacing
	 * @param textAlign
	 */
	public FontAttributes(String fontFamily, String fontWeight, FontPosture fontStyle, String fontStroke, String fontFill,
	                      double fontSize, double lineSpacing, TextAlignment textAlign) {
		this.fontFamily = fontFamily;
		this.fontWeight = fontWeight;
		this.fontStyle  = fontStyle;
		this.fontStroke = fontStroke;
		this.fontFill   = fontFill;
		this.fontSize   = fontSize;
		this.lineSpacing = lineSpacing;
		this.textAlign  = textAlign;
	}
	
	
	
	
	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 SETTERS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}
	
	public void setFontStyle(FontPosture fontStyle) {
		this.fontStyle = fontStyle;
	}

	/**
	 * Sets the Font Stroke. Colors are stored as strings
	 * Must be convertable by UIColors.convertColor(...)
	 * @param fontStroke
	 */
	public void setFontStroke(String fontStroke) {
		this.fontStroke = fontStroke;
	}

	/**
	 * Sets the font fill. Colors are stored as strings
	 * Must be convertable by UIColors.convertColor(...)
	 * @param fontFill
	 */
	public void setFontFill(String fontFill) {
		this.fontFill = fontFill;
	}


	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setLineSpacing(double lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	
	public void setTextAlign(TextAlignment textAlign) {
		this.textAlign = textAlign;
	}
	
	
	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 GETTERS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	
	private Font getSysDefaultFont() {
		return font;
	}
	
	public String getFontFamily() {
		return fontFamily;
	}
	
	public String getFontWeight() {
		return fontWeight;
	}
	
	public FontPosture getFontStyle() {
		return fontStyle;
	}

	/**
	 * Returns the FontStroke as a Color object. Expects that the
	 * string is a convertable color by UIColors.convertColor(...)
	 * @return A color
	 */
	public Color getFontStroke() {
		return UIColors.convertColor(fontStroke);
	}

	/**
	 * Returns the FontFill as a Color object. Expects that the
	 * string is a convertable color by UIColors.convertColor(...)
	 * @return A color
	 */
	public Color getFontFill() {
		return UIColors.convertColor(fontFill);
	}
	
	public double getFontSize() {
		return fontSize;
	}
	
	public double getLineSpacing() {
		return lineSpacing;
	}
	
	public TextAlignment getTextAlign() {
		return textAlign;
	}
	
	
	/** ************************************************************************************************************ ***
	 *                                                                                                                 *
	 OTHER METHODS
	 *                                                                                                                 *
	 ** ************************************************************************************************************ **/
	
}
