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

package uicontrols;

import flashmonkey.FlashMonkeyMain;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;

/**
 * Us
 */
public class FMAlerts {
	
	public final static String MISSING_DELETE = "It appears that the question or answer\n"
			+ "area's are incomplete. If you\n"
			+ "continue this card will be deleted.\n\n"
			+ "Are you sure you want to delete this card?";
	
	public final static String MISSING_SAVE = "This card appears incomplete.\n"
			+ " Do you wish to save it and return later?"
			+ "\n To save and continue press \"OK\"."
			+ "\n To go back and edit press \"Cancel\".";

	public final static String MISSING_TESTTYPE = "Ooooph!\nThe Test Type has not been selected. "
			+ "\n  To go back and set the test type, press \"OK\"."
			+ "\n  To delete this card and continue, press \"Cancel\". ";


	public final static String NO_DATA = "There is no data to save.\n"
			+ " If you are finished, press quit.";
	
	public FMAlerts() {
		/* empty constructor */
	}
	
	boolean bool;
	
	
	public boolean saveAlertAction(String alertMsg, String emojiPath) {
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		
		alert.setTitle("ALERT");
		// null as a value will cause the section to not be displayed
		alert.setHeaderText(null);
		alert.setContentText(alertMsg);
		
		//ImageView sunglasses = new ImageView("emojis/flashFaces_sunglasses_60.png");
		ImageView emojiView = new ImageView(emojiPath);
		alert.setGraphic(emojiView);
		alert.setHeight(190);
		alert.setWidth(200);
		alert.getDialogPane().contentTextProperty().toString();//   setStyle(" -fx-text-fill: #FFFFFF");
		alert.getDialogPane().setStyle(" -fx-background-color: " + UIColors.BACKGROUND_BLUE);

		
		alert.showAndWait().ifPresent((btnType) -> {
			// if user clicks ok button
			if (btnType == ButtonType.OK) {
				bool = false;
				
			} else if (btnType == ButtonType.CANCEL) {
				// else user cancels
				alert.close();
				bool = true;
			}
		});
		return bool;
	}
	

	public void noDataAlert(String alertMsg) {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.setTitle("No data to save.");

		alert.setHeaderText(null);
		alert.setContentText(alertMsg);

		ImageView sunglasses = new ImageView("emojis/Flash_hmm_75.png");
		alert.setGraphic(sunglasses);
		alert.setHeight(190);
		alert.setWidth(200);
		alert.getDialogPane().setStyle("-fx-background-color: " + UIColors.BACKGROUND_ORANGE);
		alert.show();
	}

	public void sessionRestartPopup() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

		alert.setTitle("Continue Session");
		alert.setHeaderText(null);
		alert.setContentText("To continue an online session. You must login again." +
				"\n You may also continue if you are offline." +
				"\n Log in?");

		ImageView sunglasses = new ImageView("emojis/flashFaces_sunglasses_60.png");
		alert.setGraphic(sunglasses);
		alert.setHeight(190);
		alert.setWidth(200);
		alert.getDialogPane().contentTextProperty().toString();//   setStyle(" -fx-text-fill: #FFFFFF");
		alert.getDialogPane().setStyle(" -fx-background-color: " + UIColors.FM_WHITE);


		//return bool;
	}

}
