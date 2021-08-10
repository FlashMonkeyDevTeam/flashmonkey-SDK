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
import javafx.scene.paint.Color;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.entypo.Entypo;
//import org.kordamp.ikonli.feather.Feather;
//import org.kordamp.ikonli.fontelico.Fontelico;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;


public enum ButtoniKon { //extends Button {
	
	/**
	 * FontIcon icons using iKonli :)
	 */
	// Answer Nav buttons for example Multi-choice Tests
	ANS_SELECT  ("Select", "Select this answer", Entypo.CIRCLE, UIColors.BUTTON_PURPLE),
	ANS_PREV    ("", "Previous answer", Entypo.CHEVRON_LEFT, UIColors.BUTTON_PURPLE),
	ANS_NEXT    ("", "Next answer", Entypo.CHEVRON_RIGHT, UIColors.BUTTON_PURPLE),
	// Question nav buttons
	Q_FIRST     ("", "Go to first card", Entypo.CCW, UIColors.FOCUS_BLUE_OPAQUE),
	Q_PREV      ("", "Previous card", Entypo.CHEVRON_LEFT, UIColors.FOCUS_BLUE_OPAQUE),
	Q_NEXT      ("", "Next card", Entypo.CHEVRON_RIGHT, UIColors.FOCUS_BLUE_OPAQUE),
	Q_LAST      ("", "Go to last card", Entypo.CW, UIColors.FOCUS_BLUE_OPAQUE),
	
	// MENU buttons
	TEST ("TEST", "Ready to learn", "icon/24/t_white.png", UIColors.FM_WHITE),
	Q_AND_A (" Q AND A", "Question and Answer with search", "icon/24/qa_white.png", UIColors.FM_WHITE),
	CREATE      ("Create or Edit", "Create or Edit cards", "icon/24/card_add_white.png", UIColors.FM_WHITE),
	
	// First scene unique buttons
	DECK_SELECT ("Study deck selection", "Return to select a different deck", Entypo.REPLY, UIColors.FM_WHITE),
	STUDY_BUTTON( "Start studying", "Go to the study selection menu", Entypo.ARROW_WITH_CIRCLE_RIGHT, UIColors.FM_WHITE),
	SEARCH_RSC  ("Search", "Search for study decks or resources by subject, class, professor or several other topics", Entypo.MAGNIFYING_GLASS, UIColors.FM_WHITE),
	NEW_DECK    ("New Deck", "Create a new study resource or deck", Entypo.PLUS, UIColors.FM_WHITE),
	
	// Bottom button box
	EXIT_BUTTON ("EXIT", "Done! Finito!, I'll be back!", Entypo.EXPORT, UIColors.BUTTON_PURPLE),
	MENU        ("MENU", "Back to the Select, study, or create menu. Leaves this study session.", Entypo.REPLY, UIColors.BUTTON_PURPLE),
	
	// Create card unique buttons
	NEW_CARD    ("  ADD  ", "Add a new card to the end of the deck", "/icon/24/card_add2.png", UIColors.FOCUS_BLUE_OPAQUE),
	INSERT_CARD ("INSERT", "Insert a new card to the end of the deck", "/icon/24/card_insert2.png", UIColors.FOCUS_BLUE_OPAQUE),
	DELETE_CARD ("DELETE", "Delete this card", "/icon/24/card_delete2.png", UIColors.FOCUS_BLUE_OPAQUE),
	SAVE_DECK   ("Save deck", "Save changes to the deck\n& return to menu", Entypo.REPLY, UIColors.BUTTON_PURPLE),
	QUIT_CHANGES("Bail out", "Don't save any changes\n and return to menu", Entypo.TRASH, UIColors.BUTTON_PURPLE),
	RESET_ORDER ("Reset deck order", "resets the deck back\nto it's original order", Entypo.SWAP, UIColors.BUTTON_PURPLE),
	CREATE_Q_PREV("", "Previous card\n Saves edits to the \n current card", Entypo.CHEVRON_LEFT, UIColors.FOCUS_BLUE_OPAQUE),
	CREATE_Q_NEXT("", "Next card\n Saves edits to the \n current card", Entypo.CHEVRON_RIGHT, UIColors.FOCUS_BLUE_OPAQUE),
	UNDO_CHANGES("", "undo changes in \nthis card", Entypo.REPLY, UIColors.FOCUS_BLUE_OPAQUE),
	//SELL_BUTTON("Sell in Market", "Earn money and credibility from your hard work.", Entypo.CREDIT, UIColors.FM_WHITE);
	SELL_BUTTON("Describe this deck", "Describe this deck.", Entypo.REPLY, UIColors.FM_WHITE),
	PURCHASE(" CHECKOUT ", "Go to checkout.", Entypo.SHOPPING_CART, UIColors.FM_WHITE);
	// --------------------------------- --------------------------------- //
	//                             CONSTRUCTORS
	// --------------------------------- --------------------------------- //
	
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
	ButtoniKon(String titleStr, String toolTip, Ikon ikon, String clrStr) {
		
		FontIcon rIcon = new FontIcon(ikon);
		rIcon.setIconSize(size);
		rIcon.setFill(UIColors.convertColor(clrStr));
		//Button newBtn = new Button(titleStr, rIcon);
		Button newBtn = new Button(titleStr);
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
	ButtoniKon(String titleStr, String toolTip, String imgPath, String clrStr) {
		
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
	
	
	// --------------------------------- --------------------------------- //
	//                            ACTION BUTTONS
	// --------------------------------- --------------------------------- //
	
	private Button get() {
		return this.ikonBtn;
	}
	
	public static Button getWrongAns(Button button, String btnTitle) {
		FontIcon icon = new FontIcon(Entypo.CROSS);
		icon.setFill(UIColors.convertColor(UIColors.FLASH_RED));
		icon.setIconSize(24);
		button.setGraphic(icon);
		button.setText(btnTitle);
		return button;
	}
	
	public static Button getRightAns(Button button, String btnTitle) {
		FontIcon icon = new FontIcon(Entypo.CHECK);
		icon.setFill(UIColors.convertColor(UIColors.HIGHLIGHT_GREEN));
		icon.setIconSize(24);
		button.setGraphic(icon);
		button.setText(btnTitle);
		return button;
	}
	
	public static Button getJustAns(Button button, String btnTitle) {
		FontIcon icon = new FontIcon(Entypo.CIRCLE);
		icon.setFill(UIColors.convertColor(UIColors.BUTTON_PURPLE));
		icon.setIconSize(24);
		button.setGraphic(icon);
		button.setText(btnTitle);
		return button;
	}
	
	
	// --------------------------------- --------------------------------- //
	//                               BUTTONS
	// --------------------------------- --------------------------------- //
	
	// Deck select on first scene
	
	public static Button getNewDeck() {
		Button button = NEW_DECK.get();
		button.setId("purpleButtonSquare");
		button.setMaxWidth(186.5);
		button.setMinWidth(186.5);
		button.setMaxHeight(40);
		return button;
	}
	
	
	// READ BUTTONS
	
	public static Button getQFirstButton() {
		Button button = Q_FIRST.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getQNextButton() {
		Button button = Q_NEXT.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getQPrevButton() {
		Button button = Q_PREV.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getQLastButton() {
		Button button = Q_LAST.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getAnsSelect() {
		Button button = ANS_SELECT.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getAnsNext() {
		Button button = ANS_NEXT.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getAnsPrev() {
		Button button = ANS_PREV.get();
		button.setId("navButtonLight");
		return button;
	}
	
	public static Button getSearchRsc() {
		Button button = SEARCH_RSC.get();
		button.setId("purpleButtonSquare");
		button.setMaxWidth(186.5);
		button.setMinWidth(186.5);
		button.setMaxHeight(40);
		return button;
	}
	
	
	// MENU BUTTONS
	private static int menuBtnWd = 300;
	private static int btnHt = 40;
	
	public static Button getDeckSelectButton() {
		Button button = DECK_SELECT.get();
		button.setId("purpleButtonSquare");
		button.setMaxWidth(menuBtnWd);
		button.setMinWidth(menuBtnWd);
		button.setMaxHeight(btnHt);
		return button;
	}
	
	public static Button getCreateButton() {
		Button button = CREATE.get();
		button.setId("purpleButtonSquare");
		button.setMaxWidth(menuBtnWd);
		button.setMinWidth(menuBtnWd);
		button.setMaxHeight(btnHt);
		return button;
	}
	
	public static Button getStudyButton() {
		Button button = STUDY_BUTTON.get();
		button.setId("purpleButtonSquare");
		button.setMaxWidth(menuBtnWd);
		button.setMinWidth(menuBtnWd);
		button.setMaxHeight(btnHt);
		return button;
	}
	
	
	// EXIT BOX BUTTONS
	
	public static Button getExitButton() {
		Button button = EXIT_BUTTON.get();
		button.setId("purpleButton");
		//button.setMaxWidth(150);
		//button.setMinWidth(240);
		return button;
	}
	
	public static Button getMenuButton() {
		Button button = MENU.get();
		button.setId("purpleButton");
		//button.setMaxWidth(240);
		//button.setMinWidth(240);
		return button;
	}
	
	
	// STUDY MENU QUESTIONS
	
	public static Button getTestButton() {
		Button b = TEST.get();
		b.setId("purpleButtonSquare");
		b.setMaxWidth(menuBtnWd);
		b.setMinWidth(menuBtnWd);
 		b.setMaxHeight(btnHt);
		
		return b;
	}
	
	public static Button getQandAButton() {
		Button b = Q_AND_A.get();
		b.setId("purpleButtonSquare");
		b.setMaxWidth(menuBtnWd);
		b.setMinWidth(menuBtnWd);
		b.setMaxHeight(btnHt);
		
		return b;
	}
	
	
	// CREATE CARD BUTTONS

	public static Button getSellButton() {
		Button b = SELL_BUTTON.get();
		b.setWrapText(true);
		b.setId("sellButton");
		b.setFocusTraversable(false);
		b.setMinHeight(110);
		b.setMaxHeight(110);
		b.setMaxWidth(110);
		b.setMinWidth(110);
		return b;
	}
	
	public static Button getNewCardButton() {
		Button b = NEW_CARD.get();
		b.setTooltip(new Tooltip("Add a card to the \nend of the deck"));
		b.setId("navButtonBlue");
		b.setFocusTraversable(false);
		return b;
	}
	
	public static Button getInsertCardButton() {
		Button b = INSERT_CARD.get();
		b.setId("navButtonBlue");
		b.setFocusTraversable(false);
		return b;
	}
	
	/**
	 * Saves the changes to the deck
	 * @return
	 */
	public static Button getSaveDeckButton() {
		Button b = SAVE_DECK.get();
		// Use modena CSS for now
		b.setFocusTraversable(false);
		return b;
	}
	
	/**
	 * Resets the deck order to the original order
	 * @return
	 */
	public static Button getResetOrderButton() {
		Button b = RESET_ORDER.get();
		// Use modena CSS for now
		b.setFocusTraversable(false);
		return b;
	}
	
	/**
	 * The user abondons all changes made to the deck.
	 * @return
	 */
	public static Button getQuitChangesButton() {
		Button b = QUIT_CHANGES.get();
		// Use modena CSS for now
		b.setFocusTraversable(false);
		return b;
	}
	
	public static Button getCreateQPrevButton() {
		Button b = CREATE_Q_PREV.get();
		b.setId("navButtonLight");
		b.setFocusTraversable(false);
		// start with prevQButton enabled
		b.setDisable(false);
		return b;
	}
	
	public static Button getCreateQNextButton() {
		Button b = CREATE_Q_NEXT.get();
		b.setId("navButtonLight");
		b.setFocusTraversable(false);
		// start with prevQButton disabled
		b.setDisable(true);
		return b;
	}
	
	public static Button getUndoChangesButton() {
		Button b = UNDO_CHANGES.get();
		b.setId("navButtonLight");
		b.setFocusTraversable(false);
		// start with revertQButton disabled
		b.setDisable(true);
		return b;
	}
	
	public static Button getDeleteCardButton() {
		Button b = DELETE_CARD.get();
		b.setId("navButtonBlue");
		b.setFocusTraversable(false);
		b.setDisable(false);
		return b;
	}

	public static Button getPurchasButton() {
		Button b = PURCHASE.get();
		b.setId("roseButtonSquare");
		return b;
	}
}
