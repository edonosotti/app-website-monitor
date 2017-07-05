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

import info.edoardonosotti.apps.android.websitemonitor.adapters.MonitoringHistoryAdapter;
import info.edoardonosotti.apps.android.websitemonitor.model.Monitoring;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class WebsiteHistoryActivity extends ListActivity {

	public static final String PARAM_WEBSITE_ID = "PARAM_WEBSITE_ID";
	public static final String PARAM_WEBSITE_NAME = "PARAM_WEBSITE_NAME";

	private long mId;
	private BroadcastReceiver mBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_website_history);

		mId = getIntent().getLongExtra(PARAM_WEBSITE_ID, -1);

		if (mId <= 0) {
			Toast.makeText(getApplicationContext(), R.string.error_generic,
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
		setIntentReceiver();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.website_history, menu);
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
		else if (id == R.id.action_refresh) {
			checkWebsites();
			return true;
		} else if (id == R.id.action_edit) {
			Intent intent = new Intent(getApplicationContext(), ConfigureWebsiteActivity.class);
			intent.putExtra(ConfigureWebsiteActivity.PARAM_WEBSITE_ID, mId);
			startActivity(intent);
			return true;
		} else if (id == R.id.action_clean_history) {
			Monitoring.cleanWebsiteHistory(mId, 0);
			setListAdapter(getAdapter());
			return true;
		} else if (id == R.id.action_delete) {
			delete();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Monitoring m = (Monitoring) getListAdapter().getItem(position);
		if (m.lastError != null && m.lastError.length() > 0) {
			Toast.makeText(getApplicationContext(), m.lastError,
					Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unsetIntentReceiver();
	}
	
	protected MonitoringHistoryAdapter getAdapter() {
		return new MonitoringHistoryAdapter(getApplicationContext(), 0,
				Monitoring.getByWebsiteId(mId));
	}

	protected void delete() {
		new AlertDialog.Builder(this)
				.setMessage(R.string.confirm_delete_webstite)
				.setPositiveButton(R.string.button_yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Monitoring.delete(Website.class, mId);
								finish();
							}
						})
				.setNegativeButton(R.string.button_no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						}).create().show();

	}
	
	protected void loadData() {
		Website w = Website.load(Website.class, mId);
		
		if (w != null) {
			setTitle(w.name);
		}
		
		setListAdapter(getAdapter());
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
}
