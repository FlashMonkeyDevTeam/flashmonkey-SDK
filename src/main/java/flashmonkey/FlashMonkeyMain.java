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

package flashmonkey;

import ch.qos.logback.classic.Level;
import fmtree.FMWalker;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import uicontrols.ButtoniKon;
import uicontrols.SceneCntl;


/**
 * <p>Adding Multi-Media, as well as a Test type. This version has a flexable
 * layout that is planned to provide Multiple Card layout types, section types, and TestTypes. The
 * previous version, did not fully implement sections but fully implemented the
 * tree as a navigator/ progress indicator to the EncryptedUser.EncryptedUser. It provided flexibility
 * in allowing the EncryptedUser.EncryptedUser to delete cards and hide them. It prioritized cards
 * for the EncryptedUser.EncryptedUser based on thier performance on tests. It provided only two layouts.
 * A layout for the standard question and answer FlashCard, and a multiple-choice
 * test.</p>
 *
 * <p>This version, allows multiple testing types, and multiple section types.
 * Section types are key to implementing multi-media. Card types allow for multiple
 * test types that require different layouts form the Top and Bottom only
 * sections. This version also begins to look at more interactive activities
 * for learning.</p>
 *
 * @version FlashMonkey Multi-Media and Multi-Test 1.1.0
 * @author Lowell Stadelman
 */
public class FlashMonkeyMain extends Application {
    
    @SuppressWarnings("rawtypes")
    // for serializing objects to file. The serial version.
    // Used with "serialVersionUID"
    public static final long VERSION = 20210403;

    
    private static Stage window;
    private static Stage actionWindow;
    protected static Stage treeWindow;
    
    private static Image flash;
    private static Image bckgndImg;
    
    private static CreateFlash createFlash;

    private static ReadFlash readFlash;
    private static Button sceneOneButton;
    //protected static AVLTreePane avltPane = new AVLTreePane();

    private static BorderPane firstPane;
//    private static Button exitButton;
    private static Button searchRscButton;
    private static Button menuButton;
    private static Button createButton;
    // campaign
    // @TODO Add report back in
    //private static Report report;
    // The error message to be displayed in the UI
    private static String errorMsg = "";
    // FLAGS
    private static boolean isInEditMode;
    // THE LOGGER
    //private static final Logger LOGGER = LoggerFactory.getLogger(FlashMonkeyMain.class);
    private final static ch.qos.logback.classic.Logger LOGGER = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FlashMonkeyMain.class);

    
    /** Testing MemoryChartPane **/
    //private static Stage chartWindow;
    //private static Scene chartScene;
    //private static BorderPane chartPane;


    // *** Java FX UI *** STAGE *** ***
    @Override
    public void start(Stage primaryStage) {
        LOGGER.setLevel(Level.DEBUG);
        FMWalker fmWalker = FMWalker.getInstance();
        // set to call stop() when stage is closed
        //primaryStage.setOnHidden( e -> stop());
        // reporting app performace to DB
        //LOGGER.debug("start called");
       // Report.getInstance().sessionStart();
        LOGGER.debug("timeCheck line 111 fmMain. Completed Report.getInstance90.sessionStart()");
        // failure flag if authcrypt.user is
        // in edit mode. If true, save work
        // before closing.
        isInEditMode = false;

        window = primaryStage;
        treeWindow = new Stage();
        
        // Uncomment to create the memory monitor
        //chartWindow = new Stage();
        //buildChartWindow();
        // Getting image resources as a stream is convienient and neccessary for deployable jar/app applications.
        flash = new Image(getClass().getResourceAsStream("/image/fm_multiLogo_5.png"));
        Image icon = new Image(getClass().getResourceAsStream("/icon/flashMonkey_Icon.png"));
        window.getIcons().add(icon);

        setWindowToNav();

        window.setTitle("FlashMonkey");
        window.show();

        //LOGGER.debug("in FlashMonkeyMain and window x = " + window.getX());
    } // ******** END START ******* //
    
    /**
     *   The first scene is the sign-in page. Then either
     *   fileSelectPane pane (study deck) or create authcrypt.user.
     *   IF fileSelectPane, also provides the ability to create a new deck.
     * @return Returns the first scene or landing scene999
     */
    public static Scene getFirstScene(GridPane focusPane) {
        //LOGGER.debug("*** getFirstScene called ***");

        Scene firstScene;
        firstPane = new BorderPane();
        GridPane btnBox = new GridPane();

        // For the lower panel on initial window
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(50);
        btnBox.getColumnConstraints().add(col0);
        btnBox.setId("buttonBox");

        GridPane gridPane1 = new GridPane();

        // Calculate logo fit height
        int fitHeight = calcFitHeight((int) focusPane.getPrefHeight());
        int gapHeight = (int) calcGapHeight(fitHeight);
    
        ImageView flashAstronaut = new ImageView(flash);
        flashAstronaut.setFitHeight(fitHeight);
        flashAstronaut.setPreserveRatio(true);
        flashAstronaut.setSmooth(true);
        HBox imageBox = new HBox();
        imageBox.getChildren().add(flashAstronaut);
        imageBox.setAlignment(Pos.CENTER);

        gridPane1.setAlignment(Pos.CENTER);
        gridPane1.setVgap(gapHeight);

        // *** PLACE NODES IN PANE ***
        gridPane1.getChildren().clear();
        gridPane1.addRow(0, imageBox); // column 0, row 0
        //gridPane1.addRow(1, spacer);
        gridPane1.addRow(1, focusPane);
        
        // *************************************
        // The users account set in top
        firstPane.setCenter(gridPane1);
        firstPane.setBottom(getExitBox());
        firstScene = new Scene(firstPane, SceneCntl.getWd(), SceneCntl.getHt());
        firstScene.getStylesheets().addAll("css/buttons.css", "css/mainStyle.css");

        return firstScene;
    }
    
    /**
     * Calculates the fitHeight of the
     * logo based on the height of the
     * gPane that is displayed in the
     * scene.
     * @param gPaneHt
     * @return
     */
    private static int calcFitHeight(int gPaneHt) {
        System.err.println("gpane ht: " + gPaneHt);
        return 600 - gPaneHt;
    }
    
    /**
     * Calculate the gapHeight for the logo
     * between logo and pane below.
     * @param fitHeight
     * @return
     */
    private static double calcGapHeight(int fitHeight) {
        return fitHeight / 4.5;
    }

    /**
     * Creates the Navigation/Menu stage. The Navigation/MenuStage provides the buttons
     *   used to navigate to studying, back to the firstScene, create/edit scene. It is second
     *   scene unless the EncryptedUser is creating a new deck.
     * @return Returns the Navigation Scene
     */
    public static Scene getNavigationScene() {
        Scene mainScene;
        BorderPane mainPane = new BorderPane();
        GridPane tempPane = new GridPane();
        tempPane.setAlignment(Pos.CENTER);
        
        LOGGER.debug("\n *** getNavigationScene() ***");
        Label label = new Label("Action Menu");
        label.setId("label24White");

        // *** CENTER MENU BUTTONS ***
        // go back to file select pane
        Button deckSelectButton = ButtoniKon.getDeckSelectButton();
        deckSelectButton.setOnAction(e -> ReadFlash.getInstance().deckSelectButtonAction());
        // go to study menu
        Button studyButton = ButtoniKon.getStudyButton();
        studyButton.setOnAction(e -> studyButtonAction() );

        
        // go to create pane
        //createButton = new Button("create");
        createButton = ButtoniKon.getCreateButton();
        createButton.setOnAction(e -> {
            LOGGER.debug("in createButton.setOnAction()");
            createButtonAction();
        });

        tempPane.addRow(2, studyButton);
        tempPane.addRow(3, createButton);
        
        /*mainPane.setId("bckgnd_image");*/ // the background image
        mainPane.setTop(getAccountBox());
        mainPane.setCenter(tempPane);
        mainPane.setBottom(getExitBox());        
        mainScene = new Scene(mainPane, SceneCntl.getWd(), SceneCntl.getHt());
        mainScene.getStylesheets().addAll("css/mainStyle.css", "css/buttons.css");
	    menuButton.setDisable(true);
        
        return mainScene;
    } // end Scene getNavigationScene

    protected static void createButtonAction() {
        isInEditMode = true;
        createFlash = CreateFlash.getInstance();
        menuButton.setDisable(false);
        window.setScene(createFlash.createFlashScene());   // called once
    }

    /**
     * Sends EncryptedUser.EncryptedUser to a new screen and calls the
     * treePane
     */
    private static void studyButtonAction() {
        LOGGER.debug("\n*** studyButtonAction() FlashMonkeyMain ***");
        //Timer.getClassInstance().testTimeStop();
        //LOGGER.debug("testTime: " + Timer.getClassInstance().getTakeTestTime());
        readFlash = ReadFlash.getInstance();
        menuButton.setDisable(false);
        window.setScene(readFlash.readScene());
    }

    /**
     * Creates an HBox with the menu and exit buttons.
     * @return HBox
     */
    private static GridPane getExitBox() {
        GridPane buttonBox = new GridPane(); // HBox with spacing provided
        buttonBox.setHgap(2);
//        Button exitButton = ButtoniKon.getMenuButton();
//        exitButton.setOnAction(e -> {
//            try {
//                //Report.getInstance().endSessionTime();
//            } catch (NullPointerException f){
//                LOGGER.warn(f.getMessage(), f.getStackTrace());
//                f.printStackTrace();
//            } finally {
//                System.exit(0);
//            }
//
//        });
        menuButton = ButtoniKon.getMenuButton();
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(50);
        buttonBox.setId("buttonBox");
        buttonBox.getColumnConstraints().add(col0);
        buttonBox.setPadding(new Insets(15, 15, 15, 15));
        buttonBox.addColumn(1, menuButton);
        
        return buttonBox;
    }
    
    
    
    private static HBox getAccountBox() {
        HBox hBox = new HBox(2);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(2, 2, 2, 2));
        
        ImageView userImg = new ImageView();
        userImg.setFitHeight(60);
        userImg.setSmooth(true);
        userImg.setPreserveRatio(true);

        // If not send to log in pane
        // If true show user a drop down menu
        // for now show the user the StudentInfoForm
        
        hBox.getChildren().addAll(new Label("Flash"), userImg);

        return hBox;
    }


    /** Getter Setter for error message **/

    /**
     * Sets the error message to be displayed
     * in the UI.
     * @return
     */
    public static String getErrorMsg()
    {
        return errorMsg;
    }

    /**
     * Gets the error message to be displayed
     * in the UI
     * @param str
     */
    public static void setErrorMsg(String str)
    {
        errorMsg = str;
    }


    public static Stage getWindow() {
        return window;
    }

    /**
     * Returns the Main Window's current XY location
     * @return Point2D location of the current XY position
     */
    public static Point2D getWindowXY () {
        double x = window.getX();
        double y = window.getY();
        return new Point2D(x, y);
    }

    public static void setTopPane() {
        firstPane.setTop(getAccountBox());
    }

    /**
     * Sets the window to nav
     */
    public static void setWindowToNav() {

        LOGGER.debug("setWindowToNav called");

        isInEditMode = false;
        window.setScene(getNavigationScene());
    }

    /**
     * Returns if editor/creator mode is active.
     * Side effects chose AVLTreePane and TextCell.buildCell
     * @return true if in editor/creator mode
     */
    public static final boolean isInEditorMode() {
        return isInEditMode;
    }




    public static void main(String[] args)
    {
        launch(args);
    }

/*
    // *** FOR TESTING ***
    @FMAnnotations.DoNotDeployMethod
    public static Point2D getCreateButtonXY() {
        Bounds bounds = createButton.getLayoutBounds();
        return createButton.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 20);
    }
 */


}


