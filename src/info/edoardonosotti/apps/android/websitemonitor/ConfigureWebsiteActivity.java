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

import info.edoardonosotti.apps.android.websitemonitor.helpers.FormHelper;
import info.edoardonosotti.apps.android.websitemonitor.model.Website;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigureWebsiteActivity extends Activity {
	
	public static final String PARAM_WEBSITE_ID = "PARAM_WEBSITE_ID";
	
	private long mId;
	private Website mWebsite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure_website);
		mId = getIntent().getLongExtra(PARAM_WEBSITE_ID, 0);
		loadWebsite();
		setEventListeners();
	}
	
	private void setEventListeners() {
		// OK
		Button btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveWebsite();
				
				finish();
			}
			
		});
		
		// Cancel
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
	}
	
	private void saveWebsite() {
		EditText txtName = (EditText) findViewById(R.id.txtName);
		EditText txtUrl = (EditText) findViewById(R.id.txtUrl);
		EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		CheckBox chkUseAuth = (CheckBox) findViewById(R.id.chkUseAuth);
		
		if (FormHelper.checkMandatory(txtName, txtUrl)) {
			String url = txtUrl.getText().toString();
			
			if (!url.contains("://")) {
				url = "http://" + url;
			}
			
			mWebsite.name = txtName.getText().toString();
			mWebsite.url = url;
			mWebsite.enableBasicAuth = chkUseAuth.isChecked();
			mWebsite.username = txtUsername.getText().toString();
			mWebsite.password = txtPassword.getText().toString();
			mWebsite.save();
			
			Toast.makeText(getApplicationContext(), R.string.data_saved, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), R.string.form_validation_error, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void loadWebsite() {
		EditText txtName = (EditText) findViewById(R.id.txtName);
		EditText txtUrl = (EditText) findViewById(R.id.txtUrl);
		EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		CheckBox chkUseAuth = (CheckBox) findViewById(R.id.chkUseAuth);
		
		if (mId > 0) {
			mWebsite = Website.load(Website.class, mId);
		}
		
		if (mWebsite != null && mWebsite.getId() > 0) {
			txtName.setText(mWebsite.name);
			txtUrl.setText(mWebsite.url);
			chkUseAuth.setChecked(mWebsite.enableBasicAuth);
			txtUsername.setText(mWebsite.username);
			txtPassword.setText(mWebsite.password);
		} else {
			mWebsite = new Website();
		}
	}
}
 