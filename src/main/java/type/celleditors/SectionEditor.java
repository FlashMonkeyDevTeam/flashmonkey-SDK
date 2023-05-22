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


import fileops.FileOpsShapes;
import flashmonkey.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.fontawesome5.*;
import type.celltypes.TextCell;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import type.draw.DrawObj;
import type.draw.shapes.FMRectangle;
import type.draw.shapes.GenericShape;
import type.tools.imagery.Fit;
import uicontrols.*;


/**
 * <p>SectionEditor is the default editor shown in CreateCard and EditCard ?. It contains the buttons to create/ edit an image,
 * add or delete video or audio files, add drawings to images, and delete. It contains the ability to take a screen snap
 * shot or drag and drop files into the left pane.</p>
 *
 *  <p>!!! *** NOTE *** !!!<br>
 *  Image: Images are either created in snapshot and set using setImage(). setImage() creates an image from an BufferedImage
 *  that is created by SnapShot. If cards are being edited, the image is set in setCard() method. </p>
 *
 * Algorithm,
 * - Each section of a card (not cells) is edited by the section editor. Each cell type contains its own editing methods
 *  - EncryptedUser creates a card. The default card is a doubleVertical card containing two sections.
 *  - The default section view is a singleSection containing a tCell.textVBox (TextCell)
 *
 *  !!!*** NOTE ***!!!
 *  fields in this class should not be static. There are two instances of this class
 *  in an edited card.
 *
 *  !!!***  NOTE ***!!!
 *  Methods that contain the SnapShot() constructor call do
 *  not return to that method. Calls after the SnapShot constructor call may not
 *  behave correctly or may not execute until later. The Runnable statement may be needed
 *  to execute them.
 *
 *  - (SnapShot) If the EncryptedUser clicks on snapShot,
 *      - SnapShot creates a full screen stage and contains the methods and variables to allow the EncryptedUser to draw a dashed
 *      rectangle anywhere on the screen. When the EncryptedUser releases the mouse button, an image of the area within the
 *      rectangle is saved to the buffer. The size and location of the snapshot rectangle is saved and passed to DrawTools.
 *          - The image is saved to file, and the fileName is saved to the arrayOfFiles which is stored in each FlashCard
 *          object.
 *          - An image is shown in the rightPane, The rightpane is contained in the right StackPane.
 *          - A delete button is added to the right StackPane.
 *          - A delete button is added to the left StackPane. Left StackPane contains the textVBox.
 *      - DrawTools recreates the rectangle the EncryptedUser created to take the snapshot. The rectangle is solid blue and is the
 *      same shape/size and in the same location.
 *      - The DrawTools provides access to shape classes, methods and variables to create, edit, and delete shapes.
 *      - Shapes are added to a shapeArray that is saved to each FlashCard object so they are editable later.
 *      - See DrawTools for further details when needed.
 *      - When the EncryptedUser closes drawtools, the blue rectangle is removed from the screen.
 *   - (Drag and Drop) If the EncryptedUser drags an image, audio, or video file onto the TextArea
 *      - If the file is of the right type, not all files are supported. The file is saved to to its location on the users
 *      computer. The shape is saved to the arrayOfBuilderShapes according to its location on the screen. IE upper area,
 *      lower area.
 *      - If an image is added, it has all the features of an image meaning it has a pop-up view capability which has
 *      the abilty to add and edit shapes. (not yet)
 *      - If an audio or video file is added, then the multi-media player is shown to the viewer and the EncryptedUser can
 *      play the file (but not edit).
 *
 *      @author Lowell Stadelman
 *
 *
 */
public class SectionEditor
{
    // THE LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(SectionEditor.class);
    // made changes to this class and it created a problem with serialization.
    // added serial versionUID.
    private static final long serialVersionUID = FlashMonkeyMain.VERSION;
    private static final String MEDIA_PATH = "../";


    /**
     * The array of FM shapes as opposed to the arrayOfBuilderShapes which are JavaShapes. FM
     * shapes have added variables and methods
     */
    // do not change
    private ArrayList<GenericShape> arrayOfFMShapes = new ArrayList<>(5);

    /**
     * VARIABLES
     **/

    public  HBox sectionHBox;
    private VBox txtVBox;
    private Button clearBtn;
    private Button snapShotBtn;
    private Button drawpadBtn;
    private Button deleteTCellBtn;
    private Button deleteMMCellBtn;
    private Button textAreaBtn;
    private Button cameraBtn;
    private Button findBtn;
    
      private final ButtoniKonClazz FIND = new ButtoniKonClazz("FIND", "Search for images, videos, animations, and tools", FontAwesomeSolid.SEARCH, UIColors.FM_WHITE, ButtoniKonClazz.SIZE_24);
      private final ButtoniKonClazz CAMERA = new ButtoniKonClazz("", "Take a snapshot from your camera", FontAwesomeSolid.CAMERA, UIColors.EDITOR_BTNS, ButtoniKonClazz.SIZE_24);
      private final ButtoniKonClazz SNAPSHOT = new ButtoniKonClazz("", "Take a snapshot from your screen", "icon/24/snapshot_blue4.png", UIColors.EDITOR_BTNS, 0);
      private final ButtoniKonClazz DRAWPAD = new ButtoniKonClazz("", "Draw shapes only", FontAwesomeSolid.DRAW_POLYGON, UIColors.EDITOR_BTNS, ButtoniKonClazz.SIZE_24);
      private final ButtoniKonClazz CLEAR_TEXT = new ButtoniKonClazz("", "Clear text", FontAwesomeSolid.BACKSPACE, UIColors.EDITOR_BTNS, ButtoniKonClazz.SIZE_24);
      private final ButtoniKonClazz CLEAR_T_AREA = new ButtoniKonClazz("", "Remove text area", FontAwesomeSolid.MINUS_CIRCLE, UIColors.FOCUS_BLUE_OPAQUE, ButtoniKonClazz.SIZE_24);
      private final ButtoniKonClazz CLEAR_RIGHT = new ButtoniKonClazz("", "Remove right area", FontAwesomeSolid.MINUS_CIRCLE, UIColors.FOCUS_BLUE_OPAQUE,ButtoniKonClazz.SIZE_24);
      // upper section area.
      private final ButtoniKonClazz ADD_TEXT_CELL = new ButtoniKonClazz("", "Add a text cell", FontAwesomeSolid.FONT, UIColors.EDITOR_BTNS, ButtoniKonClazz.SIZE_16);
      private final ButtoniKonClazz HASHTAG = new ButtoniKonClazz("", "Add searchable keywords about this media", FontAwesomeSolid.HASHTAG, UIColors.EDITOR_BTNS, ButtoniKonClazz.SIZE_16);
      private final ButtoniKonClazz DELETE_T_CELL = new ButtoniKonClazz("", "Remove text cell", FontAwesomeSolid.TIMES_CIRCLE, UIColors.EDITOR_BTNS,ButtoniKonClazz.SIZE_16);
      private final ButtoniKonClazz DELETE_MM_CELL = new ButtoniKonClazz("", "Remove multi-media cell", FontAwesomeSolid.TIMES_CIRCLE, UIColors.EDITOR_BTNS,ButtoniKonClazz.SIZE_16);
    
    
    //    private String dAndDImgPath;
    private char qOra;
    private String cID;
//	private ShapesEditorPopup edPopup = null;

    /**
     * Double cell sections are upper-case
     * 'M' text cell on left and Media to right
     * 'C' Text cell on left and Canvas on right
     * 'D' text cell on left and DrawPad/shapes on right
     *
     * Single cell sections are lower-case
     * 'm' media only
     * 't' text only
     * 'd' drawpad/shapes only
     * 'c' canvas only
     * The default is 't' for single cell text.
     */
    private char sectionType = 't'; // double or single section
    private static DrawTools draw;
    private Image image;
    private ImageView iView;
    private Pane rightPane; // pane showing shape and image
    private StackPane stackL;
    private  StackPane stackR;

    public TextCell tCell;
 //   private AVCell avCell;

    // This objects image or mediaFileName used
    // by several classes in getMediaFiles[0]
    private String mediaFileName;
    // This objects shape File Name used
    // by several classes in getMediaFiles[1]
    private String shapesFileName;

    // Disable flag for Drag n Drop
    private boolean dNdIsdisabled;

    /* ------------------------------------------------------- **/

    /**
     * No args constructor
     */
    public SectionEditor()
    {
        this.drawpadBtn = DRAWPAD.get();
        this.snapShotBtn = SNAPSHOT.get();
        this.clearBtn = CLEAR_TEXT.get();
        this.cameraBtn = CAMERA.get();
        
    }

    /* ------------------------------------------------------- **/


    /**
     * Constructor called for a new FlashCard
     * @param prompt
     * @param qOrA
     */
    public SectionEditor(String prompt, char qOrA, String cID)
    {
        LOGGER.info("constructor called");

        // Set here for new cards
        // for existing cards array is set
        // later.
        this.arrayOfFMShapes = new ArrayList<>(5);
        shapesFileName = null;
        mediaFileName = null;
        rightPane = new Pane();

        //if(edPopup == null) {
        //    edPopup = ShapesEditorPopup.getInstance();
        //}
        
        this.tCell       = new TextCell();
        this.sectionHBox = new HBox();
        this.stackL      = new StackPane();
        this.stackR      = new StackPane();
        // create a deep copy of tCellVBox
        this.txtVBox = new VBox(tCell.buildCell("", prompt, true, 0));

        // The stackpanes containing left and right items. Allows
        // delete buttons on the layer above the panes.
        stackL.getChildren().add(txtVBox);
        stackL.setAlignment(Pos.TOP_RIGHT);

        stackR.getChildren().add(this.rightPane);
        stackR.setAlignment(Pos.TOP_RIGHT);
        tCell.getTextArea().setEditable(true);
        tCell.getTextArea().setPrefHeight(Double.MAX_VALUE);
        tCell.getTextArea().setOnKeyPressed(e -> CreateFlash.getInstance().setFlashListChanged(true));
    
        this.cID  = cID;
        this.qOra = qOrA;
    
        
        // Get the sectionEditorButtons
        HBox buttonBox = sectionEditorButtons();
        buttonBox.setId("editorButtonBox");
        txtVBox.getChildren().add(buttonBox);

        sectionHBox.setSpacing(6);
        sectionHBox.setPadding(new Insets(6, 6, 6, 6));
        sectionHBox.setStyle("-fx-background-color: white");
        sectionHBox.setAlignment(Pos.BOTTOM_LEFT);

        // Set the initial textCell and container size
        double no = SceneCntl.calcCenterHt(40, 150, FlashMonkeyMain.getWindow().getHeight());
        sectionHBox.setPrefHeight(no);
        txtVBox.setPrefHeight(no);
        tCell.getTextArea().setPrefHeight(no - 150);
        tCell.getTextCellVbox().setPrefHeight(no - 125);

        // Provide a responsive UI.
        // Responsive height
        FlashMonkeyMain.getWindow().heightProperty().addListener((obs, oldVal, newVal) -> {
            double n = SceneCntl.calcCenterHt(40, 150, (double) newVal);
            sectionHBox.setPrefHeight(n);
            txtVBox.setPrefHeight(n);
            tCell.getTextArea().setPrefHeight(n - 150);
            tCell.getTextCellVbox().setPrefHeight(n - 125);
        });
        // Responsive width
        CreateFlash.getInstance().getCFPCenter().widthProperty().addListener((obs, oldval, newVal)  -> txtVBox.setPrefWidth(newVal.doubleValue()));
        
        // add stackL (stackLeft) to sectionHBox
        this.sectionHBox.getChildren().addAll(this.stackL);
    } // END CONSTRUCTOR

    /* ------------------------------------------------------- **/


    /**
     * Contains the buttons for this class
     * @return HBox containing the buttons
     * for this class along with actions.
     */
    private HBox sectionEditorButtons()
    {
        // Clear text in text area button
        // Add the Find button
        this.findBtn = FIND.get();
        this.findBtn.setId("navButtonWhtLetters");
        this.findBtn.setFocusTraversable(false);
        //this.findBtn.setPadding(new Insets(0, 20, 0, 0));
        this.findBtn.setOnAction(e -> {
            /* stub for later */
        });
        this.clearBtn = CLEAR_TEXT.get();
        this.clearBtn.setId("navButtonLight");
        this.clearBtn.setFocusTraversable(false);

        this.clearBtn.setOnAction((ActionEvent e) ->
        {
            tCell.getTextArea().setText("");
            tCell.getTextArea().requestFocus();
        });
        
        // SnapShot button
        this.snapShotBtn = SNAPSHOT.get();
        this.snapShotBtn.setId("navButtonLight");
        this.snapShotBtn.setFocusTraversable(false);
        // SnapShot action
        this.snapShotBtn.setOnAction((ActionEvent e) ->
        {
            LOGGER.debug("\n\t snapshot button called in SectionEditor");
            //this.snapShotBtnAction();
        });

        // Drawpad button
        this.drawpadBtn = DRAWPAD.get();
        this.drawpadBtn.setId("navButtonLight");
        this.drawpadBtn.setFocusTraversable(false);
        //this.drawpadBtn.setTooltip(new Tooltip("Create a drawing"));
        // drawpad action
        this.drawpadBtn.setOnAction((ActionEvent e) -> {
            LOGGER.info("\n\t drawpad button called in SectionEditor");
            this.drawpadBtnAction(sectionHBox);
        });

        // Camera Button
        this.cameraBtn = CAMERA.get();
        this.cameraBtn.setId("navButtonLight");
        this.cameraBtn.setFocusTraversable(false);
        //this.cameraBtn.setTooltip(new Tooltip("Local Camera"));
        this.cameraBtn.setOnAction((ActionEvent) -> {
            //this.cameraBtnAction();
        });

        // Text area button
        this.textAreaBtn = new Button("add text");
        this.textAreaBtn.setFocusTraversable(false);
        this.textAreaBtn.setTooltip(new Tooltip("Add a text area to the\n card"));
        // Action when textAreaBtn is pressed.
        this.textAreaBtn.setOnAction((ActionEvent e) -> {
    
            addTCellAction();
            
        });

        // Delete text cell & multi media cell buttons
        this.deleteTCellBtn = new Button("x");
        this.deleteMMCellBtn = new Button("x");
        deleteTCellBtn.setFocusTraversable(false);
        deleteMMCellBtn.setFocusTraversable(false);
        deleteTCellBtn.setTooltip(new Tooltip("Remove TextArea"));
        deleteMMCellBtn.setTooltip(new Tooltip("Remove Multi-Media"));
        deleteTCellBtn.setId("clrBtn");
        deleteMMCellBtn.setId("clrBtn");

        // Clear stackL from sectionHBox & remove
        // deleteMMCellBtn from right stackR
        this.deleteTCellBtn.setOnAction((ActionEvent e) -> {
            deleteTCellAction();
            LOGGER.debug("textAreaBtn width {}", this.textAreaBtn.getBoundsInLocal().getWidth());
        });

        // Clear stackR from sectionHBox & remove
        this.deleteMMCellBtn.setOnAction((ActionEvent e) -> deleteMMcellAction() );

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(this.findBtn, this.clearBtn, this.cameraBtn, this.snapShotBtn, this.drawpadBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        

        return buttonBox;
    } // End Button settings


    /* ------------------------------------------------------- **/


    /**
     * Deletes the TextCell and replaces it with a single section
     * containing a Media or Image cell. Used by deleteTCellBtn.
     */
    private void deleteTCellAction()
    {
        LOGGER.info(" DeleteTCellButton pressed ");
        LOGGER.info("masterBox width setting to: " + this.sectionHBox.getWidth());
        
        CreateFlash.getInstance().setFlashListChanged(true);

        if(this.sectionType == 'D') {
            LOGGER.info("\tsetting type to 'd'");
            this.sectionType = 'd';
        } else if(this.sectionType == 'M') {
            LOGGER.info("\tsetting type to'm'");
            this.sectionType = 'm';
        } else if (this.sectionType == 'C'){ // this is a drawing only
            LOGGER.info("\t setting type to 'c'");
            this.sectionType = 'c';
        } else {
            LOGGER.info("this section type is: " + this.sectionType);
        }

        this.sectionHBox.getChildren().clear();

        stackR.getChildren().clear();
        // if this section is not a drawing type
        if(this.sectionType != 'D' && this.sectionType != 'd') {
            // for images
            if(this.iView == null) {
                this.iView = new ImageView(image);
            }
            LOGGER.debug("deleteTCellAction, setting iView");
            
            int width = (int) this.sectionHBox.getWidth() - 90;
            int height = (int) this.sectionHBox.getHeight() - 20;
            // Scale image to the pane
            this.iView = Fit.viewResize(iView.getImage(), width, height);

            rightPane.getChildren().clear();
            rightPane.getChildren().add(iView);

            if(iView != null) {
                // responsive width
            sectionHBox.widthProperty().addListener((obs, oldval, newVal) -> iView.setFitWidth(newVal.doubleValue() - 90));
                // repsonsive height
            sectionHBox.heightProperty().addListener((obs, oldval, newVal) -> iView.setFitHeight(newVal.doubleValue() - 20));
            }
        } else {
            // for media
            LOGGER.info("section is 'd' or 'D'");
            this.rightPane.setMaxWidth(this.sectionHBox.getWidth() - 90);
        }

        stackR.getChildren().add(this.rightPane);
        this.sectionHBox.getChildren().addAll(textAreaBtn, stackR);
    }

    /* ------------------------------------------------------- **/


    /**
     * Removes the TCell from the left pane. Called by CreateFlash
     * when a card is edited as opposed to created.
     */
    public void removeTCell() {
        this.sectionHBox.getChildren().clear();
        stackR.getChildren().clear();
        stackR.getChildren().add(this.rightPane);
        this.sectionHBox.getChildren().addAll(textAreaBtn, stackR);

    }
    
    /* ------------------------------------------------------- **/
    
    /**
     * Adds a text cell to the left pane.
     */
    private void addTCellAction() {
        LOGGER.info("\ntextAreaBtn pressed");
    
        if(this.sectionType == 'd') {
            //LOGGER.info("\tsetting type to 'D'");
            this.sectionType = 'D';
        } else if(this.sectionType == 'm') {
            //LOGGER.info("\tsetting type to'M'");
            this.sectionType = 'M';
        } else if (this.sectionType == 'c'){ // this is a drawing only
            //LOGGER.info("\t setting type to 'C'");
            this.sectionType = 'C';
        } else {
            LOGGER.info("this section type is: " + this.sectionType);
        }
        
        this.rightPane.setMaxWidth(100);
        this.sectionHBox.getChildren().clear();
        this.sectionHBox.getChildren().addAll(stackL, stackR);
        this.stackR.getChildren().add(deleteMMCellBtn);
    }


    /* ------------------------------------------------------- **/


    /**
     * Clear stackR from sectionHBox & remove
     * deleteTCellbtn from stackL.
     */
    public void deleteMMcellAction()
    {
        CreateFlash.getInstance().setFlashListChanged(true);
        this.sectionType = 't';
        if(arrayOfFMShapes != null) {
            arrayOfFMShapes.clear();
        }
        // set textArea width to full Width
        setTextCellWidthFull();
        rightPane.getChildren().clear();
        sectionHBox.getChildren().clear();
        stackL.getChildren().clear();
        stackL.getChildren().add(txtVBox);
        sectionHBox.getChildren().add(this.stackL);
    }


    /* ------------------------------------------------------- **/


    /**
     * Clears the text area, removes the clear button
     * and removes stackR.
     */
    public void resetSection()
    {
        LOGGER.info("called");
        image = null;
        iView = null;
        //avCell = null;
        mediaFileName = null;

        rightPane.getChildren().clear();
        stackR.getChildren().clear();
        shapesFileName = null;
        tCell.getTextArea().setText("");
        setTextCellWidthFull();

        deleteMMcellAction();
    }



    /****************************************************************************
                                        GETTERS
    *****************************************************************************/


    public HBox getSectionHBox() {
        return this.sectionHBox;
    }

    /**
     * Creates an array containing the mediaFileName, and shapesFileName,
     * and returns it.
     * @return
     */
    public String[] getMediaFiles()
    {
        LOGGER.info(" **** called getMediaFiles() ****"
                + "\nmediaFileName: " + mediaFileName + "    |      shapesFile: " + shapesFileName);

        String[] s = {this.mediaFileName, this.shapesFileName};
        return s;
    }
    
    public String getMediaFileName() {
        return this.mediaFileName;
    }
  

    /* ------------------------------------------------------- **/


    /**
     * Returns the text from the textCell textArea.
     * Convienience method.
     * @return
     */
    public String getText()
    {
        return this.tCell.getTextArea().getText();
    }


    /* ------------------------------------------------------- **/


    /**
     * Returns the rightPane containing the image and shapes
     * @return Returns the rightPane containing the image and shapes.
     */
    public Pane getRightPane()
    {
        return this.rightPane;
    }


    /* ------------------------------------------------------- **/


    /**
     * Returns the DrawTools object
     * @return Returns the DrawTools object set in setDrawTools().
     */
    public DrawTools getDrawTools()
    {
        return this.draw;
    }



    /* ------------------------------------------------------- **/


    /**
     * Returns the mediaType used as well as
     * the sectionType for this section. Section being
     * either a double or singl cell section.
     * @return Returns the media type.
     */
    public char getMediaType()
    {
        return this.sectionType;
    }


    /* ------------------------------------------------------- **/


    /**
     * Returns the iView object for this editor
     *
     * @return Returns an ImageView
     */
    public ImageView getImageView()
    {
        return this.iView;
    }


    /* ------------------------------------------------------- **/


    public double getScale() {

        double imgWd;
        double imgHt;
        if(iView != null) {
            imgWd = iView.getImage().getWidth();
            imgHt = iView.getImage().getHeight();
            return Fit.calcScale(imgWd, imgHt, 100, 100);
        } else {
            imgWd = draw.getOverlayWd();
            imgHt = draw.getOverlayHt();
            return Fit.calcScale(imgWd, imgHt, 100, 100);
        }
    }




    /* ************************************************************************************************************ **
     *                                                                                                                 *
                                                    SETTERS
     *                                                                                                                 *
     ** ************************************************************************************************************ ***/



    /**
     * Sets the shapesFile string name representing
     * the shapesFile to the name in the parameter.
     * @param name
     */
    public void setShapeFile(String name) {
        this.shapesFileName = name;
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets the character color and style to
     * a prompt appearance
     */
    public void styleToPrompt() {
        this.tCell.getTextArea().setStyle("-fx-prompt-text-fill: rgba(0,0,0,.5); ");
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets the character color and appearance
     * to normal.
     */
    public void styleToNormal() {
        this.tCell.getTextArea().setStyle("-fx-prompt-text-fill: rgba(f,f,f,1); ");
    }


    /* ------------------------------------------------------- **/


    /**
     * @return returns the array of FM shapes for this card.
     */
    public ArrayList<GenericShape> getArrayOfFMShapes() {
        return this.arrayOfFMShapes;
    }


    /* ------------------------------------------------------- **/
    
    public char getQorA() {
        return this.qOra;
    }
    
    /* ------------------------------------------------------- **/
    
    public String getCID() {
        return this.cID;
    }
    
    /* ------------------------------------------------------- **/


    /**
     * Sets the text in the textCells textArea.
     * Convienience method
     * @param text
     */
    public void setText(String text) {
        this.tCell.getTextArea().setText(text);
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets the prompt text in the textCells textArea
     * @param text
     */
    public void setPrompt(String text) {
        this.tCell.getTextArea().setPromptText(text);
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets this section to the section type in the parameter.
     * @param sectionType
     */
    public void setSectionType(char sectionType) {
        this.sectionType = sectionType;
        if( ! hasTextCell(sectionType)) {
            deleteTCellAction();
        }
    }


    /* ------------------------------------------------------- **/


    public boolean hasTextCell(char sectionType) {
	    return sectionType == 't' || sectionType == 'C' || sectionType == 'M' || sectionType == 'D';
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets the TextCell width to allow the rightPane
     * in the HBox
     */
    public void setTextCellWdForMedia() {
        double w = FlashMonkeyMain.getWindow().getWidth() - 144;
        this.txtVBox.setPrefWidth(w);
    }


    /* ------------------------------------------------------- **/


    public void setTextCellWidthFull() {
        double w = FlashMonkeyMain.getWindow().getWidth();
        this.txtVBox.setPrefWidth(w);
    }


    /* ------------------------------------------------------- **/


    /**
     * Used for editing cards that exist.
     *
     * Sets the section width, sets media (Image and shapes, audio, video)
     * if available, in the rightPane for this section.
     * If there is no media sets the width to normal
     * for the text box. Also adds
     * the delete cell buttons if media is present
     *
     * @param mediaFileNames Expects the imageName
     * @param mediaType drawing only = 'D' 'd', Media = 'm' 'M', Canvas (image and drawing or image only) = 'c' or 'C'
     * @param qOrA
     * @param cID
     */
    public void setSectionMedia(String[] mediaFileNames, char mediaType, char qOrA, final String cID)
    {
        String mediaPath = MEDIA_PATH;

        this.sectionType = mediaType;

        double num = SceneCntl.calcCenterHt(40, 150, FlashMonkeyMain.getWindow().getHeight());
        sectionHBox.setPrefHeight(num);
        txtVBox.setPrefHeight(num);
        tCell.getTextArea().setPrefHeight(num - 150);
        tCell.getTextCellVbox().setPrefHeight(num - 125);

        switch(mediaType) {
            // text
            // 'T' is never used
            case 't':
            {
                double w = FlashMonkeyMain.getWindow().getWidth() - 16;
                this.txtVBox.setPrefWidth(w);
                CreateFlash.getInstance().getCFPCenter().widthProperty().addListener((obs, oldval, newVal)  -> txtVBox.setPrefWidth(newVal.doubleValue()));
                break;
            }
            // image with or without shapes
            case 'C':
            case 'c':
            {
                break;
            }
            // drawings only
            case 'D':
            case 'd':
            {
                LOGGER.debug(" Shapes Only .. This is a DrawPad");
                this.arrayOfFMShapes.clear();
                this.shapesFileName = mediaFileNames[1];

                String shapesPath = mediaPath + shapesFileName;

                LOGGER.debug("mediaPath: " + mediaPath + " .get(1): " + shapesFileName);
                LOGGER.debug("mediaType: " + mediaType);

                FileOpsShapes fo = new FileOpsShapes();
                this.arrayOfFMShapes = fo.getListFromFile(shapesPath);
                double ht = ((FMRectangle)arrayOfFMShapes.get(0)).getWd();
                double wd = ((FMRectangle)arrayOfFMShapes.get(0)).getHt();
                addDrawRPane(this);
                setShapesInRtPane(this.arrayOfFMShapes, wd, ht);
                this.rightPane.setOnMouseClicked(e -> {
                    ShapesEditorPopup edPopup = ShapesEditorPopup.getInstance();
                    edPopup.init();
                    String deckName = "testDeckName.dat";
                    edPopup.shapePopupHandler(this.arrayOfFMShapes, this, mediaFileName, deckName, cID, qOrA);
                });

                // for responsive text pane with the right pane.
                CreateFlash.getInstance().getCFPCenter().widthProperty().addListener((obs, oldval, newVal)  -> txtVBox.setPrefWidth(newVal.doubleValue() - 124));

                break;
            }
            // media Video and Sound
            case 'M':
            case 'm':
            {
                break;
            }
            // default is do nothing
        }
    }


    /* ------------------------------------------------------- **/


    /**
     * If stackR is empty, adds rightPane and delete btn to stackR.
     * Clears and adds if it is not clear.
     * @param rStack
     */
    private void addDeleteToRPane(Pane rStack) {
        if(rStack.getChildren().isEmpty())
        {
            rStack.getChildren().addAll(this.rightPane, deleteMMCellBtn);

        } else {
            rStack.getChildren().clear();
            rStack.getChildren().addAll(this.rightPane, deleteMMCellBtn);
        }
    }


    /* ------------------------------------------------------- **/


    /**
     * If LeftPane does not contain a deleteTCell Btn
     * Add it to the pane.
     * @param lPane
     */
    private void addDeleteToLPane(Pane lPane) {
        if(lPane.getChildren().contains(deleteTCellBtn))
        {
            // do nothing
        } else {
            lPane.getChildren().add(deleteTCellBtn);
        }
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets iView (photo) in rightPane. Checks and clears rightPane if not empty
     * @param iView
     */
    private void setViewInRPane(Node iView) {
        LOGGER.info("\n *** setViewInRPane() ***");
        if(iView == null) {
            LOGGER.warn("WARNING: \tiView is null");
        }
        if(rightPane.getChildren().isEmpty())
        {
            rightPane.getChildren().add(iView);
        } else {
            rightPane.getChildren().clear();
            rightPane.getChildren().add(iView);
        }
    }


    /* ------------------------------------------------------- **/
    
    public void clearShapes(ArrayList<GenericShape> fmShapes) {
        fmShapes.clear();
    
        this.rightPane.getChildren().clear();
        if(iView != null) {
            this.rightPane.getChildren().add(iView);
        }
    }
    
    /* ------------------------------------------------------- **/
    
    /**
     * Adds Shapes to the rightPane.
     * Called from SnapShot when they
     * are created by the EncryptedUser.EncryptedUser.
     * @param fmShapes
     */
    public void setShapesInRtPane(ArrayList<GenericShape> fmShapes, double origWd, double origHt) {
        LOGGER.info("setShapesInRtPane called ");
        double scale;
        this.rightPane.getChildren().clear();
        if(iView != null) {
            this.rightPane.getChildren().add(iView);
        } else {
            this.rightPane.setMinWidth(100);
            this.rightPane.setMinHeight(100);
        }
        scale = Fit.calcScale(origWd, origHt, 100, 100);
        for(int i = 1; i < fmShapes.size(); i++)
        {
            this.rightPane.getChildren().add(fmShapes.get(i).getScaledShape(scale));
        }
    }


    /* ------------------------------------------------------- **/


    /**
     * For drawings that are not over images. Sets the shapes in the rightPane
     * and scales them for the size of the drawPane.
     * @param editor Uses the editor from the caller
     */
    public void addDrawRPane(SectionEditor editor) {
        editor.sectionHBox.getChildren().clear();
        setTextCellWdForMedia();
        // sets the textVBox and delete button in the left stackPane
        addDeleteToLPane(this.stackL);
        // Adds the delete button to the right stackPane
        addDeleteToRPane(this.stackR);
        editor.sectionHBox.getChildren().addAll(editor.stackL, editor.stackR);
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets the textArea style to red
     */
    public void setStyleError() {
        tCell.getTextArea().setStyle("-fx-text-fill: RED");
    }


    /* ------------------------------------------------------- **/


    /**
     * Sets the textArea style to black
     */
    public void setStyleNormal() {
        tCell.getTextArea().setStyle("-fx-text-fill: BLACK");
    }


    /*******************************************************************************
                                        OTHER METHODS
    ********************************************************************************/


    /**
     * Call this when leaving or opening a new window.
     *   Ensure Stage/window instances are closed and users work is saved.
     */
    public void onClose() {
        
        //MainGenericsPaneTest.onClose();
        //window.close();
        //if(window != null) { window.close() }
        //super.getTextCellVbox().getChildren().clear();
        //super.getTextCellVbox().getChildren().add(super.getTextArea());
        //super.getTextArea().setEditable(false);
    }


    /* ------------------------------------------------------- **/


    /**
     * Alternate correct Answers that would be correct answers
     * to this question.
     * @return Returns an arrayList of correctAnswers
     */
    public ArrayList<Integer> getCorrect(TextField ansSet) {
        ArrayList<Integer> intList = new ArrayList<>(1);
        String temp = ansSet.getText();

        if( !temp.isEmpty())
        {
            temp = temp.replaceAll("[\\p{Ps}\\p{Pe}]", "");
            temp = temp.trim();

            LOGGER.info("\n *** In getCorrect, temp: " + temp + " ***");

            String[] parts = temp.split(",");
            int intPart;
            for(String e : parts)
            {
                LOGGER.debug("\tin for loop: {}", e);

                e = e.replaceAll("\\D", "");
                e = e.replace("\\s+", "");

                LOGGER.debug("\tafter clean : {}", e);

                if(!e.equals("") && !e.equals(" "))
                {
                    intPart = Integer.parseInt(e);
                    intList.add(intPart);
                }
            }
            return intList;
        }
        // add this questions number
        return  intList;
    }


    /* ------------------------------------------------------- **/


    /**
     * Creates the drawPad for the EncryptedUser.EncryptedUser to draw on.
     * @param parentPane
     */
    protected void drawpadBtnAction(Pane parentPane) {
        
        this.rightPane.setMinWidth(100);
        this.rightPane.setMaxWidth(100);
        this.rightPane.setMinHeight(100);

        LOGGER.info("*** drawpad action called ***");
        this.sectionType = 'D';
        this.arrayOfFMShapes.clear();
        mediaFileName = null;
        this.iView = null;

        Bounds bounds = parentPane.getBoundsInLocal();
        Bounds screenBounds = parentPane.localToScreen(bounds);

        CreateFlash cfp = CreateFlash.getInstance();
        cfp.disableButtons();

        int minX = (int) screenBounds.getMinX() - 450;
        int minY = (int) screenBounds.getMinY() - 50;
        
        DrawObj drawObj = new DrawObj();
        drawObj.setDems(minX, minY, 400, 400);

        String shapeFileName = "./shapeFileName.dat";
        shapesFileName = shapeFileName;
        
        draw = DrawTools.getInstance();
        draw.buildDrawTools( minX, minY, shapeFileName, this);
        draw.popUpTools();

        this.rightPane.getChildren().clear();
        // set drawing in rightPane of sectionEditor
        addDrawRPane(this);
    
        CreateFlash.getInstance().setFlashListChanged(true);
    }


    /**
     * Disables buttons for this object. To disable all buttons for the
     * Card editor, call CreateFlash.DisableButtons();
     */
    public void disableEditorBtns() {
        LOGGER.debug("\n *~*~* disableEditorBtns called *~*~*");
        // disable drag and drop
        dNdIsdisabled = true;

        if(deleteMMCellBtn != null )
        {
            deleteMMCellBtn.setDisable(true);
            deleteTCellBtn.setDisable(true);
        }
        snapShotBtn.setDisable(true);
        drawpadBtn.setDisable(true);
        clearBtn.setDisable(true);
        rightPane.setDisable(true);
        cameraBtn.setDisable(true);
    }


    /* ------------------------------------------------------- **/


    /**
     * Enables buttons for this object. To enable all buttons for the
     * SectionEditor, call CreateFlash.EnableButtons();
     */
    public void enableEditorBtns() {
        dNdIsdisabled = false;
        if(deleteMMCellBtn != null )
        {
            deleteMMCellBtn.setDisable(false);
            deleteTCellBtn.setDisable(false);
        }
        clearBtn.setDisable(false);
        snapShotBtn.setDisable(false);
        drawpadBtn.setDisable(false);
        cameraBtn.setDisable(false);

        if(rightPane != null)
        {
            rightPane.setDisable(false);
        }
    }



    /* ------------------------------------------------------- **/



    // *********** for testing methods ****************
    /*

    @FMAnnotations.DoNotDeployMethod
    public Point2D getSnapShotBtnXY() {

        Bounds bounds = snapShotBtn.getLayoutBounds();
        return snapShotBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getTextAreaXY() {
        Bounds bounds = tCell.getTextArea().getLayoutBounds();
        return tCell.getTextArea().localToScreen(bounds.getMinX() + 40, bounds.getMinY() + 40);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getClearTextBtnXY() {

        Bounds bounds = clearBtn.getLayoutBounds();
        return clearBtn.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public Point2D getRightPaneXY() {
        Bounds bounds = rightPane.getLayoutBounds();
        return rightPane.localToScreen(bounds.getMinX() + 10, bounds.getMinY() + 10);
    }

    @FMAnnotations.DoNotDeployMethod
    public ArrayList getNodesFmRPane() {
        ArrayList<Node> shapeAry = new ArrayList<>(5);
        for(int i = 0; i < rightPane.getChildren().size(); i++) {
            shapeAry.add( rightPane.getChildren().get(i));
        }
        return shapeAry;
    }

    @FMAnnotations.DoNotDeployMethod
    public Image getImage() {
        return this.image;
    }

    @FMAnnotations.DoNotDeployMethod
    public String getShapesFileNameTestMethod() {
        return this.shapesFileName;
    }

     */
}

