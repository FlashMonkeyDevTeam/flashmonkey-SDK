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

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.entypo.Entypo;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * For the situation where a button must exist more then once at the same time.
 * A class for multiple objects of the same button.
 */
public class ButtoniKonClazz {
	
	private Button ikonBtn;
	private int size = 24;
	
	/**
	 * Creates a buttoniKon object with a FontType Ikon and setting based on
	 * the parameters. The Ikons are set to a size of 24 px.
	 * @param titleStr
	 * @param toolTip
	 * @param ikon
	 * @param clrStr
	 */
	public ButtoniKonClazz(String titleStr, String toolTip, Ikon ikon, String clrStr) {
		
		FontIcon rIcon = new FontIcon(ikon);
		rIcon.setIconSize(size);
		rIcon.setFill(UIColors.convertColor(clrStr));
		Button newBtn = new Button(titleStr, rIcon);
		if(!toolTip.isEmpty()) {
			newBtn.setTooltip(new Tooltip(toolTip));
		}
		if(!titleStr.isEmpty()) {
			newBtn.setTextFill(UIColors.convertColor(clrStr));
		}
		
		this.ikonBtn = newBtn;
	}
	
	
	/**
	 * Creates a buttoniKon object with an IMAGE and settings based on
	 * the parameters. Note that the image should be set
	 * to the button size. ie 24 px
	 * @param titleStr If a title is desired, enter title, if not use ""
	 * @param toolTip  The tooltip if needed
	 * @param imgPath  The path to the image, ie /icon/card_delete2.png
	 * @param clrStr   The color string.
	 */
	public ButtoniKonClazz(String titleStr, String toolTip, String imgPath, String clrStr) {
		
		Image img = new Image(imgPath);
		Button newBtn = new Button(titleStr, new ImageView(img));
		if(!toolTip.isEmpty()) {
			newBtn.setTooltip(new Tooltip(toolTip));
		}
		if(!titleStr.isEmpty()) {
			newBtn.setTextFill(UIColors.convertColor(clrStr));
		}
		
		this.ikonBtn = newBtn;
	}
	
	public Button get() {
		return this.ikonBtn;
	}
	
	public Button getPlay(Button button) {
		FontIcon icon = new FontIcon(Entypo.CONTROLLER_PLAY);
		icon.setFill(UIColors.convertColor(UIColors.FOCUS_BLUE_OPAQUE));
		icon.setIconSize(24);
		button.setGraphic(icon);
		button.setTooltip(new Tooltip("Play"));
		return button;
	}
	
	public Button getPause(Button button) {
		FontIcon icon = new FontIcon(Entypo.CONTROLLER_PAUS);
		icon.setFill(UIColors.convertColor(UIColors.FOCUS_BLUE_OPAQUE));
		icon.setIconSize(24);
		button.setGraphic(icon);
		button.setTooltip(new Tooltip("Pause"));
		return button;
	}
}
