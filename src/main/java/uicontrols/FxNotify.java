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

import javafx.scene.image.ImageView;
import javafx.util.Duration;
import uicontrols.api.FMNotifications;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;


public class FxNotify {
	
	public FxNotify() {
		/* no args constructor */
	}
	
	public static synchronized void notificationPurple(String title, String msg, Pos pos, int duration, String iconPath, Stage owner) {

		Node graphic = new ImageView(iconPath);
		
		FMNotifications notificationBuilder = FMNotifications.create()
				.title(title)
				.text(msg)
				.graphic(graphic)
				.hideAfter(Duration.seconds(duration))
				.position(pos);
			
				//.onAction(e -> System.out.println("Notification clicked on!"))
				//.threshold((int) thresholdSlider.getValue(),
				//		Notifications.create().title("Threshold Notification"));
		
			notificationBuilder.owner(owner);
			notificationBuilder.purpleStyle();
			notificationBuilder.show();
	}

	public static synchronized void notificationBlue(String title, String msg, Pos pos, int duration, String iconPath, Stage owner) {

		Node graphic = new ImageView(iconPath);
		FMNotifications notificationBuilder = FMNotifications.create()
				.title(title)
				.text(msg)
				.graphic(graphic)
				.hideAfter(Duration.seconds(duration))
				.position(pos);

		notificationBuilder.owner(owner);
		notificationBuilder.blueStyle();
		notificationBuilder.show();
	}

	public static synchronized void notificationDark(String title, String msg, Pos pos, int duration, String iconPath, Stage owner) {

		Node graphic = new ImageView(iconPath);
		FMNotifications notificationBuilder = FMNotifications.create()
				.title(title)
				.text(msg)
				.graphic(graphic)
				.hideAfter(Duration.seconds(duration))
				.position(pos);

		notificationBuilder.owner(owner);
		notificationBuilder.darkStyle();
		notificationBuilder.show();
	}
}
