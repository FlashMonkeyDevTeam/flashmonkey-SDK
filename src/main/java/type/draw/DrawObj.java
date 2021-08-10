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

package type.draw;

/**
 * Class used to pass values from SnapShot to DrawTools. 
 */
public class DrawObj {
	
	private int minX;
	private int minY;
	private int deltaX;
	private int deltaY;
	private String fullPathName;
	private String imageName;
	
	/**
	 * Default constructor. Sets all values to a default.
	 */
	public DrawObj() { /*default constructor*/ }
	
	/**
	 * Contains demensions set in SnapShot to be used by
	 * DrawTools.
	 * @param minX
	 * @param minY
	 * @param deltaX
	 * @param deltaY
	 */
	public void setDems(int minX, int minY, int deltaX, int deltaY) {
		this.minX = minX;
		this.minY = minY;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	/**
	 * Sets the full path name
	 * @param fullPathName
	 */
	public void setFullPathName(String fullPathName) {
		this.fullPathName = fullPathName;
	}
	
	public void setImageName(String imgName) { imageName = imgName; }
	
	public String getImageName() { return imageName; }
	
	public int getMinX() {
		return minX;
	}
	
	public int getMinY() {
		return minY;
	}
	
	public int getDeltaX() {
		return deltaX;
	}
	
	public int getDeltaY() {
		return deltaY;
	}
	
	public String getFullPathName() {
		return fullPathName;
	}
	
	public void clearDrawObj() {
		minX = 0;
		minY = 0;
		deltaX = 0;
		deltaY = 0;
		fullPathName = "";
	}
}
