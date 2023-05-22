package uicontrols;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.*;
//import org.kordamp.ikonli.entypo.Entypo;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * For the situation where a button must exist more then once at the same time.
 * A class for multiple objects of the same button.
 */
public class ButtoniKonClazz {

      private final Button ikonBtn;
      public static final int SIZE_24 = 24;
      public static final int SIZE_16 = 16;
      private final int textEditorSize = 16;

      /**
       * Creates a buttoniKon object with a FontType Ikon and setting based on
       * the parameters. The Ikons are set to a size of 24 px.
       *
       * @param titleStr
       * @param toolTip
       * @param ikon
       * @param clrStr
       */
      public ButtoniKonClazz(String titleStr, String toolTip, Ikon ikon, String clrStr, int size) {
            FontIcon rIcon = new FontIcon(ikon);
            rIcon.setIconSize(size);
            rIcon.setFill(UIColors.convertColor(clrStr));
            Button newBtn = new Button(titleStr, rIcon);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            if (!titleStr.isEmpty()) {
                  newBtn.setTextFill(UIColors.convertColor(clrStr));
            }
            this.ikonBtn = newBtn;
      }

      /**
       * Primarily used for the RichTextEditor
       * MenuBar buttons. Size is 16 and color is eve_blue.
       *
       * @param toolTip
       * @param ikon
       */
      public ButtoniKonClazz(String toolTip, Ikon ikon) {
            FontIcon rIcon = new FontIcon(ikon);
            rIcon.setIconSize(textEditorSize);
            rIcon.setFill(UIColors.convertColor(UIColors.FOCUS_BLUE_OPAQUE));
            Button newBtn = new Button("", rIcon);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
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
       * @param imgSize If a preferred size other than the image size, set it. If not use 0;
       */
      public ButtoniKonClazz(String titleStr, String toolTip, String imgPath, String clrStr, int imgSize) {
            Image img = new Image(imgPath);
            ImageView iv = new ImageView(img);
//            if(imgSize != 0) {
//                  iv.setFitWidth(imgSize);
//                  iv.setFitHeight(imgSize);
//            }
            javafx.scene.control.Button newBtn = new Button(titleStr, iv);
            if (!toolTip.isEmpty()) {
                  newBtn.setTooltip(new Tooltip(toolTip));
            }
            if (!titleStr.isEmpty()) {
                  newBtn.setTextFill(UIColors.convertColor(clrStr));
            }

            this.ikonBtn = newBtn;
      }

      public Button get() {
            return this.ikonBtn;
      }

      public Button getPlay(Button button) {
            //FontIcon icon = new FontIcon(Entypo.CONTROLLER_PLAY);
            FontIcon icon = new FontIcon(FontAwesomeSolid.PLAY);
            icon.setFill(UIColors.convertColor(UIColors.FM_WHITE));
            icon.setIconSize(24);
            button.setGraphic(icon);
            button.setTooltip(new Tooltip("Play"));
            return button;
      }

      public Button getPause(Button button) {
            FontIcon icon = new FontIcon(FontAwesomeSolid.PAUSE);
            icon.setFill(UIColors.convertColor(UIColors.FM_WHITE));
            icon.setIconSize(24);
            button.setGraphic(icon);
            button.setTooltip(new Tooltip("Pause"));
            return button;
      }


}
