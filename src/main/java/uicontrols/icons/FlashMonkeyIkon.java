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

import org.kordamp.ikonli.Ikon;

public enum FlashMonkeyIkon implements Ikon {

	CARD_ADD("icon-card_add", '\ue800'),
	CARD_INSERT("icon-card_insert", '\ue801'),
	CARD_DELETE("icon-card_delete", '\ue802'),
	CARD_UNDO("icon-undo", '\ue803');
	
	private String description;
	private char icon;
	
	FlashMonkeyIkon(String description, char icon) {
		this.description = description;
		this.icon = icon;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public char getCode() {
		return icon;
	}
	
	public static FlashMonkeyIkon findByDescription(String description) {
		for (FlashMonkeyIkon icon : values()) {
			if (icon.description.equals(description)) {
				return icon;
			}
		}
		throw new IllegalArgumentException("Icon not supported: " + description);
	}
}

