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

package uicontrols;

import javafx.scene.paint.Color;

/**
 * Provides String constants for the colors used in
 * FlashMonkey.
 * <p>For easy conversion: https://www.w3schools.com/colors/colors_converter.asp</p>
 * <p>For color selection: https://color.adobe.com/create/color-wheel</p>
 * @author Lowell Stadelman
 */
public class UIColors
{
    public static final String BELIZE_BLUE          = "rgba(48,128,185,0.4)";// "#2980b9"; 0096C9
    public static final String BELIZE_BLUE_QUARTER  = "rgba(48,128,185,0.2)";
    public static final String BELIZE_BLUE_OPAQUE   = "rgba(48,128,185,1)";// "#2980b9";
    public static final String FOCUS_BLUE_OPAQUE    = "rgba(0,150,201,1)";// 0096C9
    public static final String ELECTRIC_BLUE        = "rgba(76,237,245,1.0)"; // #4CEDF5
    public static final String CREATE_PANE_BLUE     = "rgba(41,128,185,1.0)";// #2980B9
    public static final String FILE_PANE_BLUE       = "rgba()"; //
    public static final String FM_RED_WRONG_OPAQUE  = "rgba(210,0,53,1)"; // D20035
    public static final String HIGHLIGHT_ORANGE     = "rgba(255, 83, 13, 0.9)";
    public static final String HIGHLIGHT_PINK       = "rgba(255, 13, 255, 1.0)";
    public static final String HIGHLIGHT_PINK_QUARTER = "rgba(255, 13, 255, 0.4)";
    public static final String HIGHLIGHT_YELLOW     = "rgba(204, 255, 0, 0.9)"; //CCFF00
    public static final String HIGHLIGHT_GREEN      = "rgba(50, 205, 50, 0.9)"; // 32CD32
    public static final String EDITOR_BLUE_BG       = "rgba(52, 152, 219, 1.0"; // 3498db
    public static final String EDITOR_COMP_CLR      = "rgba(63, 74, 219, 1.0)"; // 3F4ADB
    public static final String FM_PURPLE            = "rgba(129, 70, 182, 1.0)";// #8146B6
    public static final String FM_GREY              = "rgba(57, 62, 70, 1.0)";  // 393E46
    public static final String FM_WHITE             = "rgba(255, 244, 224, 1.0)";// FFFE40
    public static final String FLASH_PURPLE         = "rgba(146, 118, 172, 1.0)";// 9276AC light purple
    public static final String GRAPH_BGND           = "rgba(178, 178, 178, 0.6)";// 170117
    public static final String GRID_GREY            = "rgba(114, 114, 114, 0.8)";// B2B2B2
    public static final String AXIS_GREY            = "rgba(38, 38, 38, 0.8)";   // 727272
    public static final String FLASH_RED            = "rgba(252, 60, 60, 1.0)";  // FC3C3C;
    public static final String ROSE_RED             = "rgba(227, 46, 104, 1)"; // #E32E68;
    public static final String FLASH_BLACK          = "rgba(0, 0, 0, 1.0)";
    public static final String MESSAGE_BGND         = "rgba(76, 56, 96, 1.0)";   // 9276AC Compliment to FM_Purple
    public static final String BUTTON_PURPLE        = "rgba(72, 32, 110, 1.0)";  // #48206E;
    public static final String BUTTON_PURPLE_50     = "rgba(72, 32, 110, .5)";
    public static final String BACKGROUND_BLUE      = "rgba(127, 192, 236, 1)";  //7FC0EC;
    public static final String BACKGROUND_ORANGE    = "rgba(232, 150, 32, 1)";  //E89620
    public static final String BUTTON_COMPLIMENT    = "rgba(130,72, 185, 1.0)"; //#8248B9;
    public static final String TRANSPARENT          = "rgba(0, 0, 0, 0.0)";
    public static final String WEBSITE_GREEN         = "rgb(190, 224, 22)";// #BEE016
    public static final String WEBSITE_BLUE         = "rgb(0, 100, 225)";// #0064E1
    
    
    /**
     * Converts a Color String representation from hex ( 0x........ )
     * to rgba( xxx, xxx, xxx, .xx)
     *
     * @param colorString A 10 digit color string beginning with 0x
     * @return Returns the rgba color.
     */
    public static Color convertColor(String colorString)
    {
        if (colorString == null) {
            throw new NullPointerException("The color components name must be specified");
            //colorString = "rgba(0,0,0,0)";
        }
        if (colorString.isEmpty()) {
            throw new IllegalArgumentException("Invalid color specification");
            ///colorString = "rgba(0,0,0,0)";
        }
        
        int r;
        int g;
        int b;
        int a;
        
        if(colorString.startsWith("r"))
        {
            return Color.web(colorString);
        }
        
        if(colorString.startsWith("0x"))
        {
            colorString = colorString.substring(2);
            int len = colorString.length();
            
            try
            {
                if (len == 6)
                {
                    r = Integer.parseInt(colorString.substring(0, 2), 16);
                    g = Integer.parseInt(colorString.substring(2, 4), 16);
                    b = Integer.parseInt(colorString.substring(4, 6), 16);
                    
                    return Color.rgb(r, g, b, 1);
                    
                } else if (len == 8)
                {
                    r = Integer.parseInt(colorString.substring(0, 2), 16);
                    g = Integer.parseInt(colorString.substring(2, 4), 16);
                    b = Integer.parseInt(colorString.substring(4, 6), 16);
                    a = Integer.parseInt(colorString.substring(6, 8), 16);
                    
                    return Color.rgb(r, g, b, a / 255.0);
                    
                } else
                {
                    
                    System.err.println("ERROR: ColorString is: " + colorString);
                    
                }
            } catch (NumberFormatException nfe)
            {
                System.err.println("\nERROR:  NumberFormatException in GenericShape line 190\n");
            }
            
            throw new IllegalArgumentException("Invalid color specification: Digits not 6 or 8 ");
        }
        System.out.println("\n using Color.web(colorString");
        try
        {
            return Color.web(colorString);
            
        } catch (IllegalArgumentException e) {
            
            System.out.println("ERROR: ColorString invalid, IllegalArgumentException at convertColor in GenericSHape. " +
                    "\n colorString is: " + colorString);
            System.out.println(e.getStackTrace());
        }
        // Default
        return Color.ALICEBLUE;
    }

}
