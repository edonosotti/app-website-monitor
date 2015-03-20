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

package info.edoardonosotti.apps.android.websitemonitor.broadcast;

import info.edoardonosotti.apps.android.websitemonitor.services.CheckWebsiteService;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	public static final String TAG = "AlarmReceiver";
	public static final int CODE = 1000000;
	public static final String ACTION = "info.edoardonosotti.apps.android.websitemonitor.broadcast.AlarmReceiver.DEFAULT";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received broadcast " + String.valueOf(Calendar.getInstance().getTimeInMillis()));
		Intent serviceIntent = new Intent(context, CheckWebsiteService.class);
		serviceIntent.putExtra(CheckWebsiteService.COMMAND_ID, CheckWebsiteService.COMMAND_ID_CHECK_WEBSITES);
		context.startService(serviceIntent);
	}

}
