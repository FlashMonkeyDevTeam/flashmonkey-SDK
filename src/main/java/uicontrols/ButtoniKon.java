package uicontrols;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.*;
//import org.kordamp.ikonli.entypo.Entypo;
//import org.kordamp.ikonli.feather.Feather;
//import org.kordamp.ikonli.fontelico.Fontelico;
import org.kordamp.ikonli.javafx.FontIcon;


public enum ButtoniKon { //extends Button {

      /**
       * Using FontAwesome5 :)
       * To see what the icons are goto: https://kordamp.org/ikonli/#_fontawesome5_latest
       * The ending of the icon is used. IE where the icon is called icon-erase we use FontAwesomeSolid.REPLY
       * NOTE: Uses buttons.css NOT consumerButtons.css
       */
      // FlashMonkey Main Buttons
      BACK("BACK", "Select Files", FontAwesomeSolid.REPLY, UIColors.FM_WHITE, 16),
      // Answer Nav buttons for example Multi-choice Tests
      QA_ANS_SELECT("SHOW ANSWER", "Show the answer", FontAwesomeRegular.CIRCLE, UIColors.FOCUS_BLUE_OPAQUE),
      ANS_SELECT("SELECT", "Select this answer", FontAwesomeRegular.CIRCLE, UIColors.FOCUS_BLUE_OPAQUE),
      ANS_PREV("", "Previous answer", FontAwesomeSolid.CHEVRON_LEFT, UIColors.FOCUS_BLUE_OPAQUE),
      ANS_NEXT("", "Next answer", FontAwesomeSolid.CHEVRON_RIGHT, UIColors.FOCUS_BLUE_OPAQUE),
      // Question nav buttons
      Q_FIRST("", "Go to first card", FontAwesomeSolid.UNDO_ALT, UIColors.FOCUS_BLUE_OPAQUE, 20),
      Q_PREV("", "Previous card", FontAwesomeSolid.CHEVRON_LEFT, UIColors.FOCUS_BLUE_OPAQUE, 20),
      Q_NEXT("", "Next card", FontAwesomeSolid.CHEVRON_RIGHT, UIColors.FOCUS_BLUE_OPAQUE, 20),
      Q_LAST("", "Go to last card", FontAwesomeSolid.REDO_ALT, UIColors.FOCUS_BLUE_OPAQUE, 20),
      // MENU buttons
      TEST("TEST", "Ready to learn", "icon/24/t_white.png", UIColors.FM_WHITE),
      Q_AND_A("FLASH CARD", "Question and Answer with search", "icon/24/qa_white.png", UIColors.FM_WHITE),
      CREATE("Create or Edit", "Create or Edit cards", "icon/24/card_add_white.png", UIColors.FM_WHITE),
      // First scene unique buttons
      DECK_SELECT("Study deck selection", "Return to select a different deck", FontAwesomeSolid.REPLY, UIColors.FM_WHITE, 20),
      STUDY_BUTTON("Start studying", "Go to the study selection menu", FontAwesomeSolid.SPACE_SHUTTLE, UIColors.FM_WHITE),
      SEARCH_RSC("Search", "Search for study decks or resources by subject, class, professor or several other topics", FontAwesomeSolid.SEARCH, UIColors.FM_WHITE, 16),
      NEW_DECK("New Deck", "Create a new study resource or deck", FontAwesomeSolid.PLUS, UIColors.FM_WHITE, 16),
      // Bottom button box
      EXIT_BUTTON("EXIT", "Done! Finito!, I'll be back!", FontAwesomeSolid.SHARE_SQUARE, UIColors.FM_WHITE, 20),
      MENU("MENU", "Back to the Select, study, or create menu. Leaves this study session.", FontAwesomeSolid.REPLY, UIColors.FM_WHITE, 20),
      // Create card unique buttons
      NEW_CARD("", "Add a new card", FontAwesomeSolid.PLUS, UIColors.FM_WHITE, 20),
      INSERT_CARD("", "Insert a new card to the end of the deck", FontAwesomeSolid.CHEVRON_DOWN, UIColors.FM_WHITE, 20),
      DELETE_CARD("", "Delete this card", FontAwesomeSolid.TIMES, UIColors.FM_WHITE, 20),
      SAVE_DECK_RETURN("", "Save changes to the deck\n& return to menu", FontAwesomeSolid.SHARE_SQUARE, UIColors.FM_WHITE, 20),
      UNDO_DECK_CHANGES("", "Don't save any changes to the deck\n and return to menu", FontAwesomeSolid.TRASH_RESTORE, UIColors.FM_WHITE, 20),
      RESET_ORDER("", "resets the deck back\nto it's original order", FontAwesomeSolid.RETWEET, UIColors.FM_WHITE, 20),
      CREATE_Q_PREV("", "Previous card\n Saves edits to the \n current card", FontAwesomeSolid.CHEVRON_LEFT, UIColors.FM_WHITE, 20),
      CREATE_Q_NEXT("", "Next card\n Saves edits to the \n current card", FontAwesomeSolid.CHEVRON_RIGHT, UIColors.FM_WHITE, 20),
      UNDO_CARD_CHANGES("", "Undo changes to \nthis card", FontAwesomeSolid.ERASER, UIColors.FM_WHITE, 20),
      SELL_BUTTON("EARN", "Add a description, and select to earn pay from this deck.", FontAwesomeSolid.MONEY_BILL, UIColors.HIGHLIGHT_GREEN, 28),
      PURCHASE(" CHECKOUT ", "Go to checkout.", FontAwesomeSolid.SHOPPING_CART, UIColors.FM_WHITE),
      // I got paid pane buttons
      WALLET_BUTTON("WALLET", "See what you can spend", FontAwesomeSolid.MONEY_BILL_WAVE, UIColors.FM_WHITE),
      SHARE_BUTTON("SHARE", "Share with your friends", FontAwesomeSolid.SHARE_ALT, UIColors.FM_WHITE),
      // Profile menu buttons
      PROFILE_BUTTON("My Profile", "Update your profile information.", FontAwesomeSolid.USER_COG, UIColors.FM_WHITE),
      PAY_SYS_BUTTON("Pay System", "Edit or update your account information with Stripe.", FontAwesomeSolid.MONEY_BILL_WAVE, UIColors.FM_WHITE),
      SUBSCRIBE_BUTTON("Set Up", "Subscribe and Set Up to earn on FlashMonkey.", FontAwesomeSolid.USERS, UIColors.FM_WHITE),
      ACCOUNT_BUTTON("Access and update your profile and Stripe information.", FontAwesomeSolid.COGS, UIColors.FM_WHITE, 40),
      STATS_BUTTON("My Stats", "Your study and earning stats", FontAwesomeSolid.CHART_BAR, UIColors.FM_WHITE),
      PAY_ACCT_BUTTON("", "Disabled: Shows when you've been paid", "/image/qww_1.png", UIColors.FM_WHITE, 48),
      // MetaDataForm button
      QR_SHARE_BUTTON("SAVE TO DESKTOP", "Share this QR-Code so others can purchase your deck", FontAwesomeSolid.QRCODE, UIColors.FM_WHITE),
      // VIDEO PLAYER BUTTON;
      VID_BACK_5("", "Skip back 5 seconds", FontAwesomeSolid.FAST_BACKWARD, UIColors.FM_WHITE),
      VID_FWD_5("", "Skip forward 5 seconds", FontAwesomeSolid.FAST_FORWARD, UIColors.FM_WHITE),
      VID_RESET("", "Reset", FontAwesomeSolid.UNDO_ALT, UIColors.FM_WHITE),
      VID_PLAY("", "Play", FontAwesomeSolid.PLAY, UIColors.FM_WHITE),
      VID_PAUSE("", "Pause", FontAwesomeSolid.PAUSE, UIColors.FM_WHITE),
      // IMAGE EDITING BUTTONS
      IMAGE_ROTATE_R("", "Rotate right 90 degrees", FontAwesomeSolid.REDO_ALT, UIColors.FM_WHITE, 20),
      IMAGE_ROTATE_L("", "Rotate left 90 degrees", FontAwesomeSolid.UNDO_ALT, UIColors.FM_WHITE, 20),
      IMAGE_CROP("", "Crop the image", FontAwesomeSolid.CROP, UIColors.FM_WHITE, 20),
      IMAGE_SAVE("", "Save changes", FontAwesomeSolid.SHARE_SQUARE, UIColors.FM_WHITE, 20),
      IMAGE_UNDO_CHANGES("", "Exit and don't save changes", FontAwesomeSolid.TRASH_RESTORE, UIColors.FM_WHITE, 20);



      // --------------------------------- --------------------------------- //
      //                             CONSTRUCTORS
      // --------------------------------- --------------------------------- //

      private final Button ikonBtn;
      private final int size = 24;

      /**
       * Creates a buttoniKon object with a FontType Ikon and setting based on
       * the parameters. The Ikons are set to a size of 24 px.
       *
       * @param titleStr
       * @param toolTip
       * @param ikon
       * @param clrStr
       */
      ButtoniKon(String titleStr, String toolTip, Ikon ikon, String clrStr) {
            FontIcon rIcon = new FontIcon(ikon);
            rIcon.setIconSize(size);
            rIcon.setFill(UIColors.convertColor(clrStr));
            javafx.scene.control.Button newBtn = new Button(titleStr, rIcon);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            if (!titleStr.isEmpty()) {
                  newBtn.setTextFill(UIColors.convertColor(clrStr));
            }

            this.ikonBtn = newBtn;
      }

      /**
       * Creates a buttoniKon basd on it's size. Uses a Font Image. For when we do not
       * provide the image.
       *
       * @param toolTip
       * @param ikon
       * @param clrStr
       * @param size
       */
      ButtoniKon(String toolTip, Ikon ikon, String clrStr, int size) {
            FontIcon rIcon = new FontIcon(ikon);
            rIcon.setIconSize(size);
            rIcon.setFill(UIColors.convertColor(clrStr));
            javafx.scene.control.Button newBtn = new Button("", rIcon);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            newBtn.setTextFill(UIColors.convertColor(clrStr));

            this.ikonBtn = newBtn;
      }

      /**
       * Creates a buttoniKon object with an IMAGE and settings based on
       * the parameters. Note that the image should be set
       * to the button size. ie 24 px
       *
       * @param titleStr If a title is desired, enter title, if not use ""
       * @param toolTip  The tooltip if needed
       * @param imgPath  The path to the image, ie /icon/card_delete2.png
       * @param clrStr   The color string.
       */
      ButtoniKon(String titleStr, String toolTip, String imgPath, String clrStr) {
            Image img = new Image(imgPath);
            javafx.scene.control.Button newBtn = new Button(titleStr, new ImageView(img));
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            if (!titleStr.isEmpty()) {
                  newBtn.setTextFill(UIColors.convertColor(clrStr));
            }
            this.ikonBtn = newBtn;
      }

      ButtoniKon(String titleStr, String toolTip, String imgPath, String clrStr, int size) {
            Image img = new Image(imgPath);
            ImageView view = new ImageView(img);
            view.setFitHeight(size);
            view.setPreserveRatio(true);
            view.setSmooth(true);
            javafx.scene.control.Button newBtn = new Button(titleStr, view);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            if (!titleStr.isEmpty()) {
                  newBtn.setTextFill(UIColors.convertColor(clrStr));
            }

            this.ikonBtn = newBtn;
      }

      ButtoniKon(String titleStr, String toolTip, Ikon ikon, String clrStr, int size) {
            FontIcon rIcon = new FontIcon(ikon);
            rIcon.setIconSize(size);
            rIcon.setFill(UIColors.convertColor(clrStr));
            javafx.scene.control.Button newBtn = new Button(titleStr, rIcon);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            newBtn.setTextFill(UIColors.convertColor(clrStr));

            this.ikonBtn = newBtn;
      }


      // --------------------------------- --------------------------------- //
      //                            ACTION BUTTONS
      // --------------------------------- --------------------------------- //

      private Button get() {
            return this.ikonBtn;
      }

      public static Button getWrongAns(Button button, String btnTitle) {
            FontIcon icon = new FontIcon(FontAwesomeSolid.TIMES);
            icon.setFill(UIColors.convertColor(UIColors.FLASH_RED));
            icon.setIconSize(24);
            button.setGraphic(icon);
            button.setText(btnTitle);
            return button;
      }

      public static Button getRightAns(Button button, String btnTitle) {
            FontIcon icon = new FontIcon(FontAwesomeSolid.CHECK);
            icon.setFill(UIColors.convertColor(UIColors.HIGHLIGHT_GREEN));
            icon.setIconSize(24);
            button.setGraphic(icon);
            button.setText(btnTitle);
            return button;
      }

      public static Button getJustAns(Button button, String btnTitle) {
            FontIcon icon = new FontIcon(FontAwesomeRegular.CIRCLE);
            icon.setFill(UIColors.convertColor(UIColors.FOCUS_BLUE_OPAQUE));
            icon.setIconSize(24);
            button.setGraphic(icon);
            button.setText(btnTitle);
            return button;
      }


      // --------------------------------- --------------------------------- //
      //                               BUTTONS
      // --------------------------------- --------------------------------- //
      private static int paneForFileBtnWd = 210;

      public static Button getBackButton() {
            Button button = BACK.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(186);
            button.setMinWidth(186);
            return button;
      }

      // Deck select on first scene

      public static Button getNewDeck() {
            Button button = NEW_DECK.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(paneForFileBtnWd);
            button.setMinWidth(paneForFileBtnWd);
            button.setMaxHeight(40);
            return button;
      }

      public static Button getSearchRsc() {
            Button button = SEARCH_RSC.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(paneForFileBtnWd);
            button.setMinWidth(paneForFileBtnWd);
            button.setMaxHeight(40);
            return button;
      }

      // I GOT PAID BUTTONS
      public static Button getWalletButton() {
            Button button = WALLET_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(128);
            button.setMinWidth(128);
            return button;
      }

      public static Button getShareButton() {
            Button button = SHARE_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(128);
            button.setMinWidth(128);
            return button;
      }

      // META DATA FORM BUTTON
      public static Button getQRShareButton() {
            Button button = QR_SHARE_BUTTON.get();
            button.setId("orangeButtonSquare");
            return button;
      }

      // Profile menu buttons
      public static Button getFMAccountButton() {
            Button button = ACCOUNT_BUTTON.get();
            button.setId("clearButtonSquare");
            button.setMaxWidth(52);
            button.setMaxWidth(52);
            button.setMinWidth(52);
            button.setMinHeight(52);
            return button;
      }

      public static Button getPayAcctButton() {
            Button button = PAY_ACCT_BUTTON.get();
            button.setId("clearButtonSquare");
            button.setMaxWidth(52);
            button.setMaxWidth(52);
            button.setMinWidth(52);
            button.setMinHeight(52);
            return button;
      }

      public static Button getProfileButton() {
            Button button = PROFILE_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(SceneCntl.getFileSelectPaneWd());
            button.setMinWidth(SceneCntl.getFileSelectPaneWd());
            return button;
      }

      public static Button getPaySysButton() {
            Button button = PAY_SYS_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(SceneCntl.getFileSelectPaneWd());
            button.setMinWidth(SceneCntl.getFileSelectPaneWd());
            return button;
      }

      public static Button getSubscriptStatusButton() {
            Button button = SUBSCRIBE_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(SceneCntl.getFileSelectPaneWd());
            button.setMinWidth(SceneCntl.getFileSelectPaneWd());
            return button;
      }

      public static Button getStatsPaneButton() {
            Button button = STATS_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(SceneCntl.getFileSelectPaneWd());
            button.setMinWidth(SceneCntl.getFileSelectPaneWd());
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
            button.setId("navButtonAns");
            return button;
      }

      public static Button getQAAnsSelect() {
            Button button = QA_ANS_SELECT.get();
            button.setId("navButtonAns");
            return button;
      }

      public static Button getAnsNext() {
            Button button = ANS_NEXT.get();
            button.setId("navButtonAns");
            return button;
      }

      public static Button getAnsPrev() {
            Button button = ANS_PREV.get();
            button.setId("navButtonAns");
            return button;
      }

      // MENU BUTTONS
      private static final int menuBtnWd = 300;
      private static final int btnHt = 40;

      public static Button getDeckSelectButton() {
            Button button = DECK_SELECT.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(menuBtnWd);
            button.setMinWidth(menuBtnWd);
            button.setMaxHeight(btnHt);
            return button;
      }

      public static Button getCreateButton() {
            Button button = CREATE.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(menuBtnWd);
            button.setMinWidth(menuBtnWd);
            button.setMaxHeight(btnHt);
            return button;
      }

      public static Button getStudyButton() {
            Button button = STUDY_BUTTON.get();
            button.setId("blueButtonSquare");
            button.setMaxWidth(menuBtnWd);
            button.setMinWidth(menuBtnWd);
            button.setMaxHeight(btnHt);
            return button;
      }


      // EXIT BOX BUTTONS

      public static Button getMenuButton() {
            Button button = MENU.get();
            button.setId("blueButtonSquare");
            return button;
      }


      // STUDY MENU QUESTIONS

      public static Button getTestButton() {
            Button b = TEST.get();
            b.setId("blueButtonSquare");
            b.setMaxWidth(menuBtnWd);
            b.setMinWidth(menuBtnWd);
            b.setMaxHeight(btnHt);

            return b;
      }

      public static Button getQandAButton() {
            Button b = Q_AND_A.get();
            b.setId("blueButtonSquare");
            b.setMaxWidth(menuBtnWd);
            b.setMinWidth(menuBtnWd);
            b.setMaxHeight(btnHt);
            return b;
      }


      // ****** CREATE CARD BUTTONS ******

      public static Button getSellButton() {
            Button b = SELL_BUTTON.get();
            b.setWrapText(true);
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getNewCardButton() {
            Button b = NEW_CARD.get();
            //b.setTooltip(new Tooltip("Add a card to the \nend of the deck"));
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getInsertCardButton() {
            Button b = INSERT_CARD.get();
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            return b;
      }

      /**
       * Saves the changes to the deck
       *
       * @return
       */
      public static Button getSaveDeckButton() {
            Button b = SAVE_DECK_RETURN.get();
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            return b;
      }

      /**
       * Resets the deck order to the original order
       *
       * @return
       */
      public static Button getResetOrderButton() {
            Button b = RESET_ORDER.get();
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            return b;
      }

      /**
       * The user abandons all changes made to the deck.
       *
       * @return
       */
      public static Button getQuitChangesButton() {
            Button b = UNDO_DECK_CHANGES.get();
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getCreateQPrevButton() {
            Button b = CREATE_Q_PREV.get();
            b.setId("blueButtonRndBdr");
            b.setFocusTraversable(false);
            // start with prevQButton enabled
            //b.setDisable(false);
            return b;
      }

      public static Button getCreateQNextButton() {
            Button b = CREATE_Q_NEXT.get();
            b.setId("blueButtonRndBdr");
            b.setFocusTraversable(false);
            // start with nextQButton disabled
            //b.setDisable(true);
            return b;
      }

      public static Button getUndoChangesButton() {
            Button b = UNDO_CARD_CHANGES.get();
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            // start with revertQButton disabled
            //b.setDisable(true);
            return b;
      }

      public static Button getDeleteCardButton() {
            Button b = DELETE_CARD.get();
            b.setId("blueButtonRnd");
            b.setFocusTraversable(false);
            //b.setDisable(false);
            return b;
      }

      // **** END CREATE BUTTONS ****

      public static Button getPurchasButton() {
            Button b = PURCHASE.get();
            b.setId("orangeButtonSquare");
            return b;
      }

      // Video buttons
      public static Button getVidBackButton() {
            Button b = VID_BACK_5.get();
            b.setId("clearButtonSquare");
            return b;
      }

      public static Button getVidFwdButton() {
            Button b = VID_FWD_5.get();
            b.setId("clearButtonSquare");
            return b;
      }

      public static Button getVidResetButton() {
            Button b = VID_RESET.get();
            b.setId("clearButtonSquare");
            return b;
      }

      public static Button getVidPlayButton() {
            Button b = VID_PLAY.get();
            b.setId("clearButtonSquare");
            return b;
      }

      public static Button getVidPauseButton() {
            Button b = VID_PAUSE.get();
            b.setId("clearButtonSquare");
            return b;
      }

      // *** IMAGE EDITING ***

      public static Button getImgCropButton() {
            Button b = IMAGE_CROP.get();
            b.setId("clearButtonSquare");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getImgSaveButton() {
            Button b = IMAGE_SAVE.get();
            b.setId("clearButtonSquare");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getImgAbandButton() {
            Button b = IMAGE_UNDO_CHANGES.get();
            b.setId("clearButtonSquare");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getRotate_L_Button() {
            Button b = IMAGE_ROTATE_L.get();
            b.setId("clearButtonSquare");
            b.setFocusTraversable(false);
            return b;
      }

      public static Button getRotate_R_Button() {
            Button b = IMAGE_ROTATE_R.get();
            b.setId("clearButtonSquare");
            b.setFocusTraversable(false);
            return b;
      }


}
