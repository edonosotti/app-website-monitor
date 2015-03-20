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

import info.edoardonosotti.apps.android.websitemonitor.managers.ConfigurationManager;
import info.edoardonosotti.apps.android.websitemonitor.services.CheckWebsiteService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private ConfigurationManager mConfig;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		mConfig = new ConfigurationManager(getApplicationContext());
		updateView();
		setListeners();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isFinishing()) {
			resetService();
		}
	}
	
	private void setListeners() {
		SeekBar skbHours = (SeekBar) findViewById(R.id.skbHours);
		SeekBar skbMinutes = (SeekBar) findViewById(R.id.skbMinutes);
		SeekBar skbHistory = (SeekBar) findViewById(R.id.skbHistory);
		
		skbHours.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				saveConfiguration();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
			}
		});
		
		skbMinutes.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				saveConfiguration();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
			}
		});
		
		skbHistory.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				saveConfiguration();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
			}
		});
	}
	
	private void saveConfiguration() {
		SeekBar skbHours = (SeekBar) findViewById(R.id.skbHours);
		SeekBar skbMinutes = (SeekBar) findViewById(R.id.skbMinutes);
		SeekBar skbHistory = (SeekBar) findViewById(R.id.skbHistory);
		
		long interval = ((skbHours.getProgress() * 60 * 60) + (skbMinutes.getProgress() * 60)) * 1000;
		interval = (interval < 60000) ? 60000 : interval;
		mConfig.set(ConfigurationManager.SETTING_MONITORING_INTERVAL, interval);
		
		int history = skbHistory.getProgress();
		history =  (history < 1) ? 10 : history * 10;
		mConfig.set(ConfigurationManager.SETTING_HISTORY_LENGTH_PURGE, history);
		
		updateView();
	}
	
	private void updateView() {
		TextView txtHours = (TextView) findViewById(R.id.txtHours);
		TextView txtMinutes = (TextView) findViewById(R.id.txtMinutes);
		TextView txtHistory = (TextView) findViewById(R.id.txtHistory);
		
		long interval = mConfig.getPreferences().getLong(ConfigurationManager.SETTING_MONITORING_INTERVAL, Common.DEFAULT_INTERVAL);
		int history = mConfig.getPreferences().getInt(ConfigurationManager.SETTING_HISTORY_LENGTH_PURGE, Common.DEFAULT_HISTORY);
		history = history / 10;
		
		int h = (int) interval / (60 * 60 * 1000);
		int m = (int) (interval - (h * 60 * 60 * 1000)) / (60 * 1000);
		txtHours.setText(getString(R.string.time_hours) + ": " + String.valueOf(h));
		txtMinutes.setText(getString(R.string.time_minutes) + ": " + String.valueOf(m));
		txtHistory.setText(getString(R.string.history_length) + ": " + String.valueOf(history * 10));

		SeekBar skbHours = (SeekBar) findViewById(R.id.skbHours);
		SeekBar skbMinutes = (SeekBar) findViewById(R.id.skbMinutes);
		SeekBar skbHistory = (SeekBar) findViewById(R.id.skbHistory);
		
		skbHours.setProgress(h);
		skbMinutes.setProgress(m);
		skbHistory.setProgress(history);
	}
	
	private void resetService() {
		Intent serviceIntent = new Intent(getApplicationContext(), CheckWebsiteService.class);
		serviceIntent.putExtra(CheckWebsiteService.COMMAND_ID, CheckWebsiteService.COMMAND_ID_RESET);
		startService(serviceIntent);
	}
}
