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

package type.sectiontype;

import flashmonkey.CreateFlash;
import flashmonkey.FlashMonkeyMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uicontrols.SceneCntl;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import type.celltypes.GenericCell;

/**
 * Provides a single cell section that can either be text only, MM or
 * any other type of card that requires a file type.
 *
 * @author Lowell Stadelman
 */
public class SingleCellSection //extends GenericSection
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleCellSection.class);
    
    private HBox sectionHBox = new HBox();
    private Pane pane;// = new Pane();
    // Effects responsive height.
    // if a section ht is the same for all sections
    private boolean notEqual = false;
    // if an upper section is the mate for a
    // restricted lower/other section. Provide the
    // ht of the lower/other section.
    private double otherHt = 100;

    /* *** GETTERS ans SETTERS *** */

    public boolean isNotEqual() {
        return notEqual;
    }

    public void setNotEqual(boolean notEqual) {
        this.notEqual = notEqual;
    }

    public double getOtherHt() {
        return otherHt;
    }

    public void setOtherHt(double otherHt) {
        this.otherHt = otherHt;
    }





    /**
     * Provide a 't' in the mmType and this will be a text box,
     * else it is a view type that can use a file.
     * NOTE: The default for this method is that all sections are
     * an equal height. If you need to provide for a section where
     * a card has two sections, and one section has a non-adjustable
     * height, as in the case for TrueOrFalse, set notEqual to true,
     * and provide the ht of the other section. The other section
     * should use the sectionView(Pane) method.
     * @param txt
     * @param type The type of section
     * @param numHSections Number of Horizontal sections if they are
     *                     equal ht or a single section. Is not used
     *                     if this section is notEqual.
     * @param paths Media paths
     * @return Returns an HBox containing this section if it is
     */
    //@Override
    public HBox sectionView(String txt, char type, int numHSections, boolean isEqual, double otherHt, String ... paths)
    {
        GenericCell gc = new GenericCell();

        LOGGER.info("in SingleCellSection.sectionView(string,char,string... )");

        switch(type)
        {
            // Audio Video = m / media
            case 'M':
            case 'm':
            {
                pane = gc.cellFactory(type, pane,
                        (int) CreateFlash.getInstance().getMasterPane().getWidth() - 8,
                        (int) CreateFlash.getInstance().getMasterPane().getHeight(),
                        true,
                        paths);
                //pane.setMaxWidth(Double.MAX_VALUE);
                sectionHBox.getChildren().add(pane);
                //@todo fix case 'm' in SingleCellSection, size should not be set here
                break;
            }
            // Canvas = images and shapes/drawings
            case 'C':
            case 'c':
            {

                LOGGER.debug("Case c in SingleCellSection");
                pane = new GridPane();


                // set initial width and height
                pane = gc.cellFactory(type, pane,
                        (int) CreateFlash.getInstance().getMasterPane().getWidth() - 8,
                        (int) (CreateFlash.getInstance().getMasterPane().getHeight() - 214) / numHSections,
                        true,
                        paths);

                break;
            }
            case 't': // Text only in this view
            default:
            {
                LOGGER.debug("SingleSection switch: at default == text pane. ");
             //   SceneCntl.setCellHt((int) ReadFlash.getInstance().getRPCenter().getHeight());
                int wd = (int) CreateFlash.getInstance().getMasterPane().getWidth() - 8;
                int ht = (int) (CreateFlash.getInstance().getMasterPane().getHeight() - 214) / numHSections;

                LOGGER.debug("in SingleSection, setting ht to: " + ht);

                pane = gc.cellFactory(txt, wd, ht, numHSections, isEqual, otherHt);

                break;
            }
        }

    // RESPONSIVE SIZING for width and height

        // Set the initial section height
        double calcHt = SceneCntl.calcCenterHt(30, 218, FlashMonkeyMain.getWindow().getHeight());
        sectionHBox.setPrefHeight(calcHt / numHSections);
        pane.setPrefHeight(calcHt / numHSections);

        // Bind section width with a listener for responsive width
        CreateFlash.getInstance().getMasterPane().widthProperty().addListener((obs, oldval, newVal) -> {
            double val = (double) newVal - 8;
            sectionHBox.setMinWidth(val);
            pane.setMinWidth(val);
        });
        // Make section height responsive
        CreateFlash.getInstance().getMasterPane().heightProperty().addListener((obs, oldval, newVal) -> {
            double val;
            if(!notEqual) {
                val = (double) newVal / numHSections;
            val -= 214 / numHSections; // grrrrrr!
            } else {
                LOGGER.debug("SingleSection notEqual is true");
                val = (newVal.doubleValue() - 4) - (otherHt * 2);
            }
            sectionHBox.setPrefHeight(val);
            sectionHBox.setMaxHeight(val);
            pane.setPrefHeight(val);
            pane.setMaxHeight(val);

        });
        sectionHBox.getChildren().add(pane);
        sectionHBox.setAlignment(Pos.CENTER);
        return sectionHBox;
    }

    /**
     * section view provides the sizing for the panes in a single section
     * to comply with the rest of the FM sizing. Provide a pane with the
     * contents if they are non-standard. IE not text nor media. Used by
     * TrueOrFalse lower pane.
     * @param lowerPane
     * @return
     */
    public HBox sectionView(Pane lowerPane) {

        LOGGER.debug("in singleCellSection.sectionView(Pane localPane)");
        HBox sectionHBox = new HBox();

        // Make width responsive
        CreateFlash.getInstance().getMasterPane().widthProperty().addListener((obs, oldval, newVal) -> {
            sectionHBox.setMinWidth(newVal.doubleValue() - 8);
            lowerPane.setMinWidth(newVal.doubleValue() - 8);
        });

        LOGGER.debug("in singleCellSection.sectionView()");
        sectionHBox.getChildren().add(lowerPane);

        return sectionHBox;
    }

}
