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

package uicontrols.icons;

import org.kordamp.ikonli.AbstractIkonHandler;
import org.kordamp.ikonli.Ikon;

public class IkonHandler extends AbstractIkonHandler {
	
	public boolean supports(String description) {
		return description != null && description.startsWith("icon-");
	}
	
	public Ikon resolve(String description) {
		return FlashMonkeyIkon.findByDescription(description);
	}
	
	public String getFontResourcePath() {
		return "/resources/font/flashmonkey-icons.ttf"; //"/resources/flashmonkey-icons.ttf";
	}
	
	public String getFontFamily() {
		return "flashmonkey-icons";
	}
}
