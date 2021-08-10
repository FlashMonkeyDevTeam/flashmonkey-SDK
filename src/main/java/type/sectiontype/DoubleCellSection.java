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

import uicontrols.SceneCntl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import type.celltypes.GenericCell;

/**
 * @author Lowell Stadelman
 */
public class DoubleCellSection //extends GenericSection
{
    private Pane leftCell = new Pane();
    private Pane rightCell = new Pane();
    private HBox sectionHBox = new HBox();


    //@Override
    public HBox sectionView(String txt, char type, int numHSections, boolean isEqual, double otherHt, String ... fileName)
    {
        GenericCell gc = new GenericCell();

       //System.out.println("DoubleCellSection.sectionView() ");
        int wd = SceneCntl.getCenterWd();
        int ht = (int) CreateFlash.getInstance().getMasterPane().getHeight();
        // text cell
        leftCell = gc.cellFactory(txt, wd, ht, numHSections, isEqual, otherHt);
        rightCell.getChildren().clear();
        // Set the rightCell initial width and height from SceneCntl
        rightCell = gc.cellFactory(type, rightCell, SceneCntl.getRightCellWd(), SceneCntl.getCellHt(), false, fileName);
        rightCell.setMinWidth(SceneCntl.getRightCellWd());

//        HBox.setHgrow(leftCell, Priority.ALWAYS);

       //System.out.println("DoubleCell.sectionView filename: image = " + fileName[0] + "; shapes = " +fileName[1]);
       //System.out.println("DoubleCell.sectionView type: " + type);
        sectionHBox.getChildren().addAll(leftCell, rightCell);
        //sectionHBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Set the initial section height
        double no = SceneCntl.calcCenterHt(30, 214, FlashMonkeyMain.getWindow().getHeight());
        sectionHBox.setPrefHeight(no / numHSections);
        leftCell.setPrefHeight(no / numHSections);

        leftCell.setPrefWidth(FlashMonkeyMain.getWindow().getWidth() - 124);

        // RESPONSIVE SIZING for width and height
        CreateFlash.getInstance().getMasterPane().widthProperty().addListener((obs, oldval, newVal) -> {
            leftCell.setMinWidth(newVal.doubleValue() - 108);
            leftCell.setMaxWidth(newVal.doubleValue() - 108);
        });

        CreateFlash.getInstance().getMasterPane().widthProperty().addListener((obs, oldval, newVal) -> {
            double val = newVal.doubleValue() / numHSections;
            val -= 214 / numHSections; // grrrrrr!
            sectionHBox.setPrefHeight(val);
            sectionHBox.setMaxHeight(val);
            leftCell.setPrefHeight(val);
            leftCell.setMaxHeight(val);
        });

        return sectionHBox;
    }


    /**
     * Returns the HBox containing this section.
     * @return Returns the HBox containing this section
     */
    public HBox getSectionHBox()
    {
        return sectionHBox;
    }

}
