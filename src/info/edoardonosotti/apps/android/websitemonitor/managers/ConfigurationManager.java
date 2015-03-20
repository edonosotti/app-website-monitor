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

package info.edoardonosotti.apps.android.websitemonitor.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigurationManager {
	
	public static final String SETTING_START_AT_BOOT = "SETTING_START_AT_BOOT";
	public static final String SETTING_RUN_IN_BACKGROUND = "SETTING_RUN_IN_BACKGROUND";
	public static final String SETTING_MONITORING_INTERVAL = "SETTING_MONITORING_INTERVAL";
	public static final String SETTING_HISTORY_LENGTH_PURGE = "SETTING_HISTORY_LENGTH_PURGE";
	public static final String SETTING_ENABLE_MONITORING = "SETTING_ENABLE_MONITORING";
	public static final String SETTING_LICENSE = "SETTING_LICENSE";
	public static final String SETTING_NETWORK = "SETTING_NETWORK";
	
	private static final String SETTINGS_KEY = "info.edoardonosotti.apps.android.websitemonitor";
	
	private Context mContext;
	private SharedPreferences mPreferences;

	public ConfigurationManager(Context context) {
		super();
		this.mContext = context;
	}
	
	public void set(String key, boolean value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(key, value);
		editor.commit();
		loadPreferences();
	}
	
	public void set(String key, int value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(key, value);
		editor.commit();
		loadPreferences();
	}
	
	public void set(String key, long value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putLong(key, value);
		editor.commit();
		loadPreferences();
	}
	
	public void set(String key, String value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putString(key, value);
		editor.commit();
		loadPreferences();
	}
	
	public void set(String key, float value) {
		SharedPreferences.Editor editor = getEditor();
		editor.putFloat(key, value);
		editor.commit();
		loadPreferences();
	}
	
	public SharedPreferences.Editor getEditor() {
		return getPreferences().edit();
	}
	
	public SharedPreferences getPreferences() {
		if (mPreferences == null) {
			loadPreferences();
		}
		
		return mPreferences;
	}
	
	private void loadPreferences() {
		mPreferences = mContext.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE);
	}
}
