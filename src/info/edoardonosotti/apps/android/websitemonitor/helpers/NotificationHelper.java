/*
 * Website Monitor, a simple tool to check your websites availability
 * Copyright (C) 2015, Edoardo Nosotti (ask@edoardonosotti.info)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package info.edoardonosotti.apps.android.websitemonitor.helpers;

import info.edoardonosotti.apps.android.websitemonitor.MainActivity;
import info.edoardonosotti.apps.android.websitemonitor.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper {

	public static final int NOTIFICATION_NO_NETWORK = -1;
	public static final int NOTIFICATION_NO_WEBSITES = -2;

	private static final int NOTIFICATION_ID = 1;

	private Context mContext;

	public NotificationHelper(Context context) {
		super();
		this.mContext = context;
	}

	public void sendNotification(int errors) {
		String text = mContext.getString(R.string.empty_text);
		int number = 0;
		int icon = android.R.drawable.ic_media_play;

		switch (errors) {
		case NOTIFICATION_NO_NETWORK:
			text = mContext.getString(R.string.notification_text_network);
			icon = android.R.drawable.ic_delete;
			break;
		case NOTIFICATION_NO_WEBSITES:
			text = mContext.getString(R.string.notification_text_empty);
			icon = android.R.drawable.ic_delete;
			break;
		default:
			text = mContext
					.getString(((errors == 0) ? R.string.notification_text_ok
							: R.string.notification_text_ko));
			number = errors;
			icon = ((errors == 0) ? android.R.drawable.ic_media_play
					: android.R.drawable.ic_delete);
			break;
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext)
				.setSmallIcon(icon)
				.setContentTitle(
						mContext.getString(R.string.notification_title))
				.setContentText(text).setNumber(number);

		Intent resultIntent = new Intent(mContext, MainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
				0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotifyMgr = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification n = mBuilder.build();
		n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

		mNotifyMgr.notify(NOTIFICATION_ID, n);
	}

	public void sendNotification() {
		sendNotification(0);
	}

	public void cancelNotification() {
		NotificationManager mNotifyMgr = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotifyMgr.cancel(NOTIFICATION_ID);
	}

}
