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

package info.edoardonosotti.apps.android.websitemonitor;

import info.edoardonosotti.apps.android.websitemonitor.adapters.MonitoringAdapter;
import info.edoardonosotti.apps.android.websitemonitor.managers.ConfigurationManager;
import info.edoardonosotti.apps.android.websitemonitor.model.Website;
import info.edoardonosotti.apps.android.websitemonitor.services.CheckWebsiteService;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	private static final String TAG = "MainActivity";
	
	private ConfigurationManager mConfig;
	private BroadcastReceiver mBroadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mConfig = new ConfigurationManager(getApplicationContext());
		setContentView(R.layout.activity_main);
		checkLicense();
		startService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setListAdapter(getAdapter());
		setIntentReceiver();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem runAtBoot = menu.findItem(R.id.action_run_boot);
		if (runAtBoot != null) {
			runAtBoot.setChecked(mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_START_AT_BOOT, false));
		}
		
		MenuItem enableMonitoring = menu.findItem(R.id.action_enable_monitoring);
		if (enableMonitoring != null) {
			enableMonitoring.setChecked(mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_ENABLE_MONITORING, true));
		}
		
		MenuItem runInBackground = menu.findItem(R.id.action_run_background);
		if (runInBackground != null) {
			runInBackground.setChecked(mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_RUN_IN_BACKGROUND, true));
		}
		
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		if (id == R.id.action_docs) {
			Intent intent = new Intent(getApplicationContext(), DocsActivity.class);
			startActivity(intent);
			return true;
		}
		else if (id == R.id.action_settings) {
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		else if (id == R.id.action_add) {
			Intent intent = new Intent(getApplicationContext(), ConfigureWebsiteActivity.class);
			startActivity(intent);
			return true;
		}
		else if (id == R.id.action_refresh) {
			checkWebsites();
			return true;
		}
		else if (id == R.id.action_run_boot) {
			item.setChecked(!item.isChecked());
			mConfig.set(ConfigurationManager.SETTING_START_AT_BOOT, item.isChecked());
			startService();
			
			return true;
		}
		else if (id == R.id.action_enable_monitoring) {
			item.setChecked(!item.isChecked());
			mConfig.set(ConfigurationManager.SETTING_ENABLE_MONITORING, item.isChecked());
			startService();
			
			return true;
		}
		else if (id == R.id.action_run_background) {
			item.setChecked(!item.isChecked());
			mConfig.set(ConfigurationManager.SETTING_RUN_IN_BACKGROUND, item.isChecked());
			startService();
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Website w = (Website) getListAdapter().getItem(position);
		Intent intent = new Intent(getApplicationContext(), WebsiteHistoryActivity.class);
		intent.putExtra(WebsiteHistoryActivity.PARAM_WEBSITE_ID, w.getId());
		intent.putExtra(WebsiteHistoryActivity.PARAM_WEBSITE_NAME, w.name);
		startActivity(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unsetIntentReceiver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isFinishing() && !mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_RUN_IN_BACKGROUND, true)) {
			stopService();
		}
	}
	
	protected MonitoringAdapter getAdapter() {
		MonitoringAdapter adapter = new MonitoringAdapter(
				getApplicationContext(), 0, Website.getAll());
		
		return adapter;
	}
	
	private void startService() {
		Intent serviceIntent = new Intent(getApplicationContext(), CheckWebsiteService.class);
		serviceIntent.putExtra(CheckWebsiteService.COMMAND_ID, CheckWebsiteService.COMMAND_ID_APPLY_PREFERENCES);
		startService(serviceIntent);
	}
	
	private void stopService() {
		Intent serviceIntent = new Intent(getApplicationContext(), CheckWebsiteService.class);
		serviceIntent.putExtra(CheckWebsiteService.COMMAND_ID, CheckWebsiteService.COMMAND_ID_STOP);
		startService(serviceIntent);
	}
	
	private void checkWebsites() {
		Intent serviceIntent = new Intent(getApplicationContext(), CheckWebsiteService.class);
		serviceIntent.putExtra(CheckWebsiteService.COMMAND_ID, CheckWebsiteService.COMMAND_ID_CHECK_WEBSITES);
		startService(serviceIntent);
	}
	
	private void setIntentReceiver() {
		if (mBroadcastReceiver == null) {
			mBroadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					Log.d(TAG, "Received update");
					
					setListAdapter(getAdapter());
				}
				
			};
		}
		
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadcastReceiver, new IntentFilter(CheckWebsiteService.ACTION_UPDATE));
	}
	
	private void unsetIntentReceiver() {
		if (mBroadcastReceiver != null) {
			LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadcastReceiver);
		}
	}
	
	private void checkLicense() {
		boolean approved = mConfig.getPreferences().getBoolean(ConfigurationManager.SETTING_LICENSE, false);
		if (!approved) {
			new AlertDialog.Builder(this)
			.setTitle(R.string.welcome_title)
			.setMessage(R.string.accept_license)
			.setPositiveButton(R.string.button_accept,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mConfig.set(ConfigurationManager.SETTING_LICENSE, true);
						}
					})
			.setNegativeButton(R.string.button_decline,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							stopService();
							finish();
						}
					}).create().show();
		}
	}
	
}
 