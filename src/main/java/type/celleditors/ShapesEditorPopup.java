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


import flashmonkey.CreateFlash;
import flashmonkey.FlashMonkeyMain;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.draw.shapes.FMRectangle;
import type.draw.shapes.GenericShape;
import uicontrols.SceneCntl;

import java.util.ArrayList;

import static common.FMFields.SHAPES_FULL_NAME;

public class ShapesEditorPopup {
	
	// THE LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(ShapesEditorPopup.class);

	private static ShapesEditorPopup CLASS_INSTANCE;
	private DrawTools draw;
	private ArrayList<GenericShape> editorShapeAry;
	
	
	
	private ShapesEditorPopup() {
		//drawObj = new DrawObj();
	}
	
	/**
	 * Singleton class instantiation. There
	 * should only be one instance of DrawTools
	 * Synchronized
	 * @return The class instance
	 */
	public static synchronized ShapesEditorPopup getInstance() {
		if(CLASS_INSTANCE == null) {
			CLASS_INSTANCE = new ShapesEditorPopup();
		}
		
		return CLASS_INSTANCE;
	}
	
	public void init() {
		this.draw = DrawTools.getInstance();
//		this.drawObj = new DrawObj();
		this.editorShapeAry = new ArrayList<>(4);
	}

	
	/**
	 *
	 * @return
	 */
	public ArrayList<GenericShape> getEditorShapeAry() {
		return editorShapeAry;
	}
	
	/**
	 *
	 * @param editorShapeAry
	 */
	public void setEditorShapeAry(ArrayList<GenericShape> editorShapeAry) {
		ArrayList<GenericShape> newList = new ArrayList<>(editorShapeAry.size());
		for(GenericShape s : editorShapeAry) {
			newList.add(s.clone());
		}
		this.editorShapeAry = newList;
	}
	
	
	/**
	 * Creates shapes in the drawpad popup and gets DrawTools for editing the shapes in the drawpad popup.
	 * @param shapeArray
	 * @param thisEditor
	 * @param shapePathName
	 */
	public void shapePopupHandler(ArrayList<GenericShape> shapeArray, SectionEditor thisEditor, String shapePathName,
										String deckName, String cID, char qOrA) {
		
		//this.shapesName = shapePathName;
		int num = shapePathName.lastIndexOf("/");
		String mediaPath = shapePathName.substring(0, num + 1);
		String shapesPathName1 = SHAPES_FULL_NAME.get();
		
		CreateFlash.getInstance().setFlashListChanged(true);
		CreateFlash cfp = CreateFlash.getInstance();
		cfp.disableButtons();
		
		double mediaWd = ((FMRectangle) shapeArray.get(0)).getWd();
		double mediaHt = ((FMRectangle) shapeArray.get(0)).getHt();
		double popUpX =  FlashMonkeyMain.getWindow().getX() - (mediaWd - 20);
		double popUpY =  FlashMonkeyMain.getWindow().getY() + 25;
		
		//   drawObj.setDems(popUpX, popUpY + 15, mediaWd, mediaHt);
		
		//   fileNaming = new FileNaming(deckName, data.getUserName(), cID, qOra, ".dat");
		//   String shapeFileName = fileNaming.getFileName();
		//  drawObj.setFullPathName(shapePathName);
		
		draw = DrawTools.getInstance();
		//draw.buildPopupDrawTools(drawObj, thisEditor, null);
		draw.buildDrawTools(shapesPathName1, thisEditor, null, popUpX, popUpY, mediaWd, mediaHt);
		draw.popUpTools();
		
		// If there are shapes to display
		if (shapeArray.size() > 1) {
			for (int i = 1; i < shapeArray.size(); i++) {
				// adds itself to the canvas pane?
				shapeArray.get(i).getBuilder( thisEditor, false);
			}
		}
	}
		
		
		
		
	/**
	 * Creates a popUp of the right pane with Image & shapes (if they exist)
	 * on top of the image1.
	 */
	public void imagePopupHandler(ArrayList<GenericShape> shapeArray, SectionEditor thisEditor, @NotNull String imagePathName,
									String deckName, String cID, char qOrA) {
		LOGGER.info("imagePopupHandler called");
		
		CreateFlash cfp = CreateFlash.getInstance();
		cfp.disableButtons();
		
		Image image = new Image("File:" + imagePathName);
		ImageView localIView = new ImageView(image);
		double popUpX = FlashMonkeyMain.getWindow().getX() - localIView.getFitWidth() - 450;
		double popUpY = FlashMonkeyMain.getWindow().getY() + 25;
		double mediaWd;
		double mediaHt;
		
		
		if(image != null) {
			if(image.getWidth() < SceneCntl.getScreenWd()) {
				LOGGER.info("image1 is smaller than screen width");
				mediaWd = image.getWidth();
				mediaHt = image.getHeight();
			}
			else {
				LOGGER.info("image1 is larger than screen in one dimension");
				mediaWd = SceneCntl.getScreenWd() - 300;
				mediaHt = SceneCntl.getScreenHt() - 300;
			}
			
			localIView = new ImageView(image);
			LOGGER.info("In popup, mediaWd: {}, mediaHt: {}", mediaWd, mediaHt);
			
		} else {
			mediaWd = ((FMRectangle) shapeArray.get(0)).getWd();
			mediaHt = ((FMRectangle) shapeArray.get(0)).getHt();
		}
		
		LOGGER.debug("ShapeArray has {} elements", shapeArray.size());
	
		// Display of the image1 is handled by DrawTools
		// change the shapePathName to a new name so user
		// can revert back to the original.
		int num = imagePathName.lastIndexOf("/");
		String mediaPath = imagePathName.substring(0, num + 1);
		String shapesPathName = SHAPES_FULL_NAME.get();
		
		draw = DrawTools.getInstance();
		draw.buildDrawTools(shapesPathName, thisEditor, localIView, popUpX, popUpY, mediaWd, mediaHt);
		draw.popUpTools();
		
		// If there are shapes to display
		if (shapeArray.size() > 1) {
			for (int i = 1; i < shapeArray.size(); i++) {
				// adds itself to the canvas pane?
				shapeArray.get(i).getBuilder( thisEditor, false );
				
				LOGGER.debug("shape strokeColor: " + shapeArray.get(i).getStrokeColor());
			}
		}
	}
		
	/**
	 * Close the popUpTools without saving anything
	 */
	public void justClose() {
		if(draw != null) {
			draw.justClose();
		}
	}
		
	public void onClose(SectionEditor paramEditor) {
		justClose();
		//draw = DrawTools.getInstance();
		
		//LOGGER.info("drawObj1 null: {}. paramEditor null: {} draw null: {}", drawObj == null );
		
		//draw.exitAction( drawObj.getFullPathName(), paramEditor);
		//drawObj.clearDrawObj();
	}
	
}
