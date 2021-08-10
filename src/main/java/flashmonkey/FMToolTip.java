package flashmonkey;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;


/**
 * Since we are using Java 8 and not 9!
 * and Java 8 ToolTip does not support several features that are very useful.
 * IE the delay and hide methods are private. And they are needed by developers
 * to set the delay to a desirable time.
 *
 * Note that undersireable behavoir may occur if there are multiple nodes in the
 * scene that have a popUp tooltip when the delayHide is set to default. When
 * the mouse enters a new node while the old node still has a tooltip visible,
 * there may be two tooltips visible. This is not an issue if remove() is used
 * to hide the FMtooltip.
 *
 * To avoid having two tooltips displayed simultainiously. Set the delayHide
 * to 0.
 *
 * To use this class. Use constructor once in the calling class. Set each toolTip
 * with the offsets, and parent Node. The getter sets the mouse and String text.
 *
 * Use play to play the toolTip and let it run it's course, or use run, and remove.
 *
 * @author Lowell Stadelman
 */
public class FMToolTip {

    // The offset x & y from the mouse's current position
    private int xOffSet;
    private int yOffSet;

    // The top left corner of the toolTip
    private int minX;
    private int minY;

    //The height and width of the toolTip
    private int wd;
    private int ht;

    private Pane parentPane;
    private final Pane popUpPane = new Pane();



    // Set a slight delay
    private int delayShow = 100;
    // Set a default delay for hiding
    private int delayHide = 8000;
    // Set if desired to show for duration of hover
    private boolean hideOnExitOnly;
    //private TextArea textArea;
    private Text text;

    private double lastMouseX;
    private double lastMouseY;

    // Flag if there is an FMToolTip visible
    private static boolean isVisible;

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * No args constructor. Sets a tooltip with
     * an empty string
     */
    public FMToolTip() {
        // no args constructor
    }


    public FMToolTip(Pane parentPane) {

        this(5, 3, parentPane);
    }


    /**
     * Constructor, sets the xOffset, and yOffset. Sets the parentNode.
     * To set text use getToolTip( ... )
     * Set this once for the class and set the other values in the getToolTip() method.
     * parameters
     * @param xOffSet
     * @param yOffSet
     */
    public FMToolTip( int xOffSet, int yOffSet, Pane parentPane) {

        this.text = new Text();
        this.xOffSet = xOffSet;
        this.yOffSet = yOffSet;
        this.ht = 20;
        this.wd = 60;
        this.isVisible = false;
        this.parentPane = parentPane;
    }




    /***************************************************************************
     *                                                                         *
     * Setters                                                                 *
     *                                                                         *
     **************************************************************************/

    public void setxOffSet(int xPosition) {
        this.xOffSet = xPosition;
    }

    public void setyOffSet(int yPosition) {
        this.yOffSet = yPosition;
    }

    public void setWd(int width) {
        this.wd = width;
    }

    public void setHt(int height) {
        this.ht = height;
    }

    /*public void setTextArea(String str) {
        this.textArea.setText(str);
    }*/

    public void setText(String str) {
        this.text.setText(str);
    }

    public void setDelayShow(int millis) {
        this.delayShow = millis;
    }

    public void setDelayHide(int millis) {
        this.delayHide = millis;
    }


    /***************************************************************************
     *                                                                         *
     * Getters                                                                 *
     *                                                                         *
     **************************************************************************/

    public int getxOffSet() {
        return this.xOffSet;
    }

    public int getyOffSet() {
        return this.yOffSet;
    }

    public int getHt() { return this.ht; }

    public int getWd() { return this.wd; }

   /* public String getText() {
        return this.textArea.getText();
    }*/

    public int getDelayShow() {
        return this.delayHide;
    }

    public int getDelayHide() {
        return this.delayHide;
    }

    public Text getText() {
        return this.text;
    }


    private boolean showing = false;
    public Pane getFMTooltip(MouseEvent mouse, String str) {

        //popUpPane.setBackground(Background.EMPTY);
        double x = mouse.getSceneX() + xOffSet;
        double y = mouse.getSceneY() - yOffSet;
        //int wd = text.length() * 10; // 10 pixels per letter

        this.text.setText(str);
        this.text.setFill(Color.WHITE);
        this.text.setTextAlignment(TextAlignment.CENTER);
        //this.text.setStyle("-fx-text-fill: rgba(0,0,0,0.0)");
        //this.text.set
        //this.textArea.setText(text);
        //this.textArea.setPrefWidth(wd);
        //this.textArea.setPrefHeight(ht);
        //this.textArea.setWrapText(true);
        //this.textArea.setBorder(Border.EMPTY);
        //this.textArea.setBackground(Background.EMPTY);
        //this.textArea.setEditable(false);
        //textArea.setId("graphToolTip");
        //textArea.setStyle("-fx-border-color: rgba(0,0,0,0.0); -fx-background-color: rgba(0,0,0,0.0);");
        //textArea.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: RGBA(255, 255, 255, .7);");

        popUpPane.setLayoutX(x);
        popUpPane.setLayoutY(y);
        popUpPane.setPrefHeight(ht);
        popUpPane.setPrefWidth(wd);
        popUpPane.setMaxHeight(ht);
        popUpPane.setMaxWidth(wd);
        popUpPane.setMinWidth(wd);
        popUpPane.setMinHeight(ht);
        //popUpPane.setId("graphToolTip");
        //popUpPane.setStyle("-fx-border-color: rgba(0,0,0,0.0); -fx-background-color: rgba(0,0,0,0.0);");
        popUpPane.setStyle("-fx-background-color: TRANSPARENT;");

        popUpPane.getChildren().clear();
        popUpPane.getChildren().add(text);

        return popUpPane;
    }

    private static int size = -1;
    // The pop-up must wait before it
    // recreates or deletes
    private static long wait;

    public void show() {

        if(isVisible) {
            // remove the previous toolTip
            remove();
        } else if (System.currentTimeMillis() > wait) {
            size = parentPane.getChildren().size();
            // Delay the appearance to prevent unintended appearances
            timer.getKeyFrames().add(new KeyFrame(new Duration(delayShow)));
            timer.setOnFinished(event -> {
                this.parentPane.getChildren().add(size, popUpPane);
                // update isVisible to true
                isVisible = true;
                wait = System.currentTimeMillis() + 20;
            });
            timer.play();
        }
    }

    // Ensure that the ToolTip has
    // been removed if it is no longer
    // on the object of interest
    public void update(MouseEvent mouse, Node n) {

        // if the mouse is no longer on the object of interest
        // - && this ToolTip still exists.
        // - Then delete it and reset isVisible to false
        double mouseY = mouse.getSceneY();
        double mouseX = mouse.getSceneY();

        if (size != -1) {
            if (!n.contains(mouseX, mouseY) && System.currentTimeMillis() > wait + 3000) {

                this.parentPane.getChildren().remove(size);
                isVisible = false;
                size = -1;
            }
        }
    }

    /**
     * If there is a Tooltip visible, removes the tool tip immidiatly.
     */
    public void remove() {

        if(isVisible && System.currentTimeMillis() > (wait - 5)) {

            this.parentPane.getChildren().remove(size);
           //System.out.println("removing");
            // update isVisible to false
            isVisible = false;
            size = -1;
        }
    }

    Timeline timer = new Timeline();

    public void play() {
        show();

        timer.getKeyFrames().add(new KeyFrame(new Duration(delayHide)));
        timer.setOnFinished(event -> {
            remove();
        });
        timer.play();
    }

}
