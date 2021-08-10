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

package type.draw.shapes;

import javafx.scene.shape.Polyline;

public class FMArrow extends FMPolyLine
{

    /**
     * No args constructor
     */
    public FMArrow() {
    }

    /**
     * Full constructor
     *
     * @param pts     the ObservableList of points
     * @param strokeW
     * @param stroke  stroke color
     * @param fill    fill color
     * @param index   this objects index in the arrayOfShapes/arrayOfFMShapes
     */
    public FMArrow(double[] pts, double strokeW, String stroke, String fill, int index) {
        super(pts, strokeW, stroke, fill, index);
    }

    /**
     * <p> Creates an FMLine using a shallow copy of the parameters values.
     * if the parameter does not have a stroke or fill color, sets stroke to <@code>UIColors.
     * HIGHLIGHT_PINK</@code> and fill to <@code>UIColors.TRANSPARENT</@code></p>
     *
     * @param line
     */
    public FMArrow(Polyline line) {
        super(line);
    }

    /**
     * FMLine Copy constructor
     *
     * @param other
     */
    public FMArrow(FMPolyLine other) {
        super(other);
    }


}
