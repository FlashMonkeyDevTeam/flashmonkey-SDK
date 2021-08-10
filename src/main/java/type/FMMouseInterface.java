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

package type;


import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import type.celleditors.SectionEditor;
import type.draw.shapes.GenericBuilder;
import type.draw.shapes.GenericShape;

import java.util.ArrayList;

/**
 * @author Lowell Stadelman
 */
public interface FMMouseInterface<T extends GenericShape<T>, B extends GenericBuilder<T,B>>
{
    void deleteButtonAction(Shape s, GenericShape gs);

    void mousePressed(MouseEvent mouse);

    void mouseDragged(MouseEvent mouse);

    void mouseReleased(MouseEvent mouse);

    void shapePressed(MouseEvent mouse, GenericShape gs, Shape s);

    void shapeDragged(MouseEvent mouse, GenericShape gs, Shape s);

    void shapeReleased(MouseEvent mouse, GenericShape gs, Shape shape);

    void verticyPressed(int idx, MouseEvent mouse, Shape s);

    void verticyReleased(GenericShape gs);

    /**
     * Use for shapes for moving the horizontal verticy
     * @param mouse
     * @param gs
     * @param vertH A Circle for the verticy
     * @param vertHOther A Circle for the verticy
     * @param vertH1 A Circle for the verticy
     * @param vertH2 A Circle for the verticy
     * @param shape The shape being modified
     */
    void verticyHDragged(MouseEvent mouse, GenericShape gs, Shape vertH, Shape vertHOther, Shape vertH1, Shape vertH2, Shape shape);

    /**
     * Use for shapes moving the verticle verticy.
     * @param mouse
     * @param gs
     * @param vertV A Circle for the verticy
     * @param vertVOther A Circle for the verticy
     * @param vertV1 A Circle for the verticy
     * @param vertV2 A Circle for the verticy
     * @param shape The shape being modified
     */
    void verticyVDragged(MouseEvent mouse, GenericShape gs, Shape vertV, Shape vertVOther, Shape vertV1, Shape vertV2, Shape shape);

    /**
     * Use for shapes that are polygons or for lines
     * @param mouse
     * @param gs
     * @param vertArry An array containing verticies
     * @param shape The Shape or Polygon being moved.
     */
    void verticyXYDragged(MouseEvent mouse, GenericShape gs, ArrayList<Circle> vertArry, Shape shape);

    /**
     * Clears the listeners for shape fill and stroke color changes
     */
    void clearListeners();
}
