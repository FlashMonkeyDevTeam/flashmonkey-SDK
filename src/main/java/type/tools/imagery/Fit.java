/*
 * Copyright (c) 2019 - 2023. FlashMonkey Inc. (https://www.flashmonkey.co) All rights reserved.
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

package type.tools.imagery;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A stateless class
 * Assists with scaling images and shapes so that they are consistent
 * with each other and with the size of the enclosing pane.
 */
public abstract class Fit {

    private static double h;
    private static double w;

    public Fit() { /* no args constructor */ }


    /**
     * Restrict image to fit inside of the Hbox, VBox, Pane... it is contained in,
     * and returns it scaled to fit in the width and heigth provided in the
     * parameters.
     * @param image The image to be set in the box
     * @param paneW The desired width of the image
     * @param paneH The desired height of the image
     * @return Returns an imageView object containing the resized image
     *
     * Algorithm:
     *  If imageW greater than imageH
     *      w = the smaller of paneW or imageW
     *      w = (paneW greater than imageW ? imageW : paneW)
     *  else
     *      imageH = paneH
     */
    public static ImageView viewResize(Image image, double paneW, double paneH) {
        ImageView view = new ImageView(image);
        view.setPreserveRatio(true);
        view.setSmooth(true);

        double imageW = image.getWidth();
        double imageH = image.getHeight();

        boolean widthLarger = mediaFit(imageW, imageH, paneW, paneH);
        if (widthLarger) {
            view.setFitWidth(w);
        } else {
            view.setFitHeight(h);
        }

        return view;
    }

    public static Image imageResize(Image image, double wd, double ht) {
        return viewResize(image, wd, ht).getImage();
    }




    /**
     * Restrict a mediaplayer/view to fit inside of the HBox, VBox, Pane... etc that it is
     * contained in. Returns it scaaled to fit in the lesser of the width or height privided
     * in the parameters.
     * @param player A Media Player containing the media to be played
     * @param paneW The pane width
     * @param paneH The pane height
     * @return Mediaview with a preserved ratio, containing the player with sized to fit in
     * the deminsions provided.
     */
    public static MediaView mediaResize(MediaPlayer player, double paneW, double paneH) {
        double mediaW = player.getMedia().getHeight();
        double mediaH = player.getMedia().getHeight();

        MediaView view = new MediaView(player);
        boolean widthIsLarger = mediaFit(mediaW, mediaH, paneW, paneH);

        if (widthIsLarger) {
           //System.out.println("media resize() setting media by wd");
            view.setFitWidth(w);
        } else {
           //System.out.println("media resize() setting media by ht: " + h);
            view.setFitHeight(h);
        }
        return view;
    }


    /**
     * Fit the media within the pane. Resets the internal w and h values to
     * the size that ensures the image fits inside of ht and wd. Returns true
     * if it is fit by width, false if fit by height.
     * @param mediaW
     * @param mediaH
     * @param paneW
     * @param paneH
     * @return boolean as stated above.
     */
    private static boolean mediaFit(double mediaW, double mediaH, double paneW, double paneH) {
       //
       //     Image width will change its size. this scales to media width, but does not
       //             work for height.

        // if image width is >= image height
        // use the smaller of image or pane width. otherwise,
        // use the smaller of image or pane heigth.
        if(mediaW >= mediaH) {
            return sizeByWidth(mediaW, paneW, mediaH, paneH);
        } else {
            return sizeByHeight(mediaH, paneH, mediaW, paneW);
        }
    }

    /**
     * Used by mediaFit above
     * @param mediaW
     * @param paneW
     * @param mediaH
     * @param paneH
     * @return
     */
    private static boolean sizeByWidth(double mediaW, double paneW, double mediaH, double paneH) {

        // Set width to the narrower of iether the pane
        // or the image.
        w = Math.min(paneW, mediaW);

        double ratio = w / mediaW;
        double rHt = mediaH * ratio;
        // Ensure that the new height is not larger than
        // the height of the screen. Use the smaller
        // of the two.
        if(rHt > paneH) {
            h = paneH;
            return false;
        }
        return true;
    }

    /**
     * Used by mediaFit above
     * @param mediaH
     * @param paneH
     * @param mediaW
     * @param paneW
     * @return
     */
    private static boolean sizeByHeight(double mediaH, double paneH, double mediaW, double paneW) {
        // Set the height to the narrower of either the
        // pane or the image.
        h = Math.min(mediaH, paneH);

        double ratio = h / mediaH;
        double rWd = mediaW * ratio;
        // Ensure that the new height is not larger than
        // the height of the screen. Use the smaller
        // of the two.
        if(rWd > paneW) {
            w = paneW;
            return true;
        }
        return false;
    }

    /**
     * Returns the scale used to resize a shape from the original, to the new
     * desired width and height. Ensures that the shape will fit in the new
     * container and does not care if the new contianer's height or width
     * matches with the same ratio of the original. Ensures that it will
     * maintain the same appearance.
     * @param originalWd
     * @param originalHt
     * @param newWd
     * @param newHt
     * @return returns the scale to resize the shape
     */
    public static double calcScale(double originalWd, double originalHt, double newWd, double newHt) {
        if(originalWd >= originalHt) {
            double w = (newWd > originalWd ? originalWd : newWd);
            return w / originalWd;
        } else {
            double h = ( originalHt > newHt ? newHt : originalHt);
            return h / originalHt;
        }
    }
}
