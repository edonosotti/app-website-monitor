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

package info.edoardonosotti.apps.android.websitemonitor.services;

import info.edoardonosotti.apps.android.websitemonitor.Common;
import info.edoardonosotti.apps.android.websitemonitor.broadcast.AlarmReceiver;
import info.edoardonosotti.apps.android.websitemonitor.broadcast.BootReceiver;
import info.edoardonosotti.apps.android.websitemonitor.helpers.NetworkHelper;
import info.edoardonosotti.apps.android.websitemonitor.helpers.NotificationHelper;
import info.edoardonosotti.apps.android.websitemonitor.managers.ConfigurationManager;
import info.edoardonosotti.apps.android.websitemonitor.model.Monitoring;
import info.edoardonosotti.apps.android.websitemonitor.model.Website;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CheckWebsiteService extends IntentService {
	
	public static final String COMMAND_ID = "COMMAND_ID";
	public static final String ACTION_UPDATE = "info.edoardonosotti.apps.android.websitemonitor.services.CheckWebsiteService.UPDATE";
	
	public static final int COMMAND_ID_APPLY_PREFERENCES = 0;
	public static final int COMMAND_ID_CHECK_WEBSITES = 1;
	public static final int COMMAND_ID_RESET = 998;
	public static final int COMMAND_ID_STOP = 999;
	
	private static final String TAG = "CheckWebsiteService";
	
	private ConfigurationManager mConfig;
	private AlarmManager mAlarmManager;
	private PendingIntent mAlarmIntent;
	
	public CheckWebsiteService() {
		super("CheckWebsiteService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		mConfig = new ConfigurationManager(getApplicationContext());
		
		switch (intent.getIntExtra(COMMAND_ID, -1)) {
			case COMMAND_ID_APPLY_PREFERENCES:
				applySettings();
				break;
			case COMMAND_ID_CHECK_WEBSITES:
				checkWebsites();
				break;
			case COMMAND_ID_RESET:
				reset();
				break;
			case COMMAND_ID_STOP:
				stopSelf();
				break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void checkWebsites() {
		Log.d(TAG, "Checking websites");
		
		int errors = 0;
		int thresold = mConfig.getPreferences().getInt(ConfigurationManager.SETTING_HISTORY_LENGTH_PURGE, Common.DEFAULT_HISTORY);
		
		List<Website> websites = Website.getAll();
		if (!websites.isEmpty()) {
			
			outerloop:
			for (Website w : websites) {
				int result = NetworkHelper.testConnection(getApplicationContext(), w);
				Monitoring.cleanWebsiteHistory(w.getId(), thresold);
				
				switch (result) {
					case 0:
						errors++;
						break;
					case -1:
						new NotificationHelper(getApplicationContext()).sendNotification(NotificationHelper.NOTIFICATION_NO_NETWORK);
						break outerloop;
				}
			}
		
			new NotificationHelper(getApplicationContext()).sendNotification(errors);
			
		} else {
			
			new NotificationHelper(getApplicationContext()).sendNotification(NotificationHelper.NOTIFICATION_NO_WEBSITES);
			
		}
		
		notifyUpdates();
	}
	
	private void startMonitoringService() {
		if (mAlarmManager == null) {
			Log.d(TAG, "Enabling monitoring service");
			
			Calendar calendar = Calendar.getInstance();
			Intent intent = new Intent("info.edoardonosotti.apps.android.websitemonitor.broadcast.AlarmReceiver.DEFAULT");
			mAlarmIntent = PendingIntent.getBroadcast(this, AlarmReceiver.CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			mAlarmManager.setRepeating(
						AlarmManager.RTC_WAKEUP, 
						calendar.getTimeInMillis(), 
						mConfig.getPreferences().getLong(ConfigurationManager.SETTING_MONITORING_INTERVAL, Common.DEFAULT_INTERVAL), 
						mAlarmIntent
					);
			
			new NotificationHelper(getApplicationContext()).sendNotification();
		}
	}
	
	private void stopMonitoringService() {
		if (mAlarmManager != null) {
			Log.d(TAG, "Disabling monitoring service");
			
			mAlarmManager.cancel(mAlarmIntent);
			mAlarmManager = null;
		}
		
		new NotificationHelper(getApplicationContext()).cancelNotification();
	}
	
	private void enableBootReceiver() {
		Log.d(TAG, "Enabling boot service");
		
		ComponentName receiver = new ComponentName(getApplicationContext(), BootReceiver.class);
		PackageManager pm = getApplicationContext().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void disableBootReceiver() {
		Log.d(TAG, "Disabling boot service");
		
		ComponentName receiver = new ComponentName(getApplicationContext(), BootReceiver.class);
		PackageManager pm = getApplicationContext().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);
	}
	
	private void applySettings() {
		Log.d(TAG, "Applying settings");
		
		// Start at boot time
		if (mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_START_AT_BOOT, false)) {
			enableBootReceiver();
		} else {
			disableBootReceiver();
		}
		
		// Enable monitoring
		if (mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_ENABLE_MONITORING, true)) {
			startMonitoringService();
		} else {
			stopMonitoringService();
		}
	}
	
	private void notifyUpdates() {
		Intent localIntent = new Intent(ACTION_UPDATE);
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
	}
	
	private void reset() {
		stopMonitoringService();
		applySettings();
	}
}



