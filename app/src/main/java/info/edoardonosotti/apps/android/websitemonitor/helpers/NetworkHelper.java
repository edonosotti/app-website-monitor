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

import info.edoardonosotti.apps.android.websitemonitor.Common;
import info.edoardonosotti.apps.android.websitemonitor.model.Monitoring;
import info.edoardonosotti.apps.android.websitemonitor.model.Website;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

public class NetworkHelper {
	
	public static int testConnection(Context context, Website website) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    
	    if (netInfo != null && netInfo.isConnected()) {
	    	
	    	Monitoring m = new Monitoring();
        	m.website = website;
        	
            try {
	        	
	        	URL url = new URL(website.url);
	            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	            
	            if (website.enableBasicAuth) {
	            	urlc.setRequestProperty("Authorization", "Basic " + getBasicAuthHeader(website.username, website.password));
	            }
	            
	            urlc.setConnectTimeout(Common.CONNECTION_TIMEOUT);
	            
	            long startTime = Calendar.getInstance().getTimeInMillis();
	            
	            urlc.connect();
	            
	            long endTime = Calendar.getInstance().getTimeInMillis();
	            
	            m.returnCode = urlc.getResponseCode();
	            m.checkTimestamp = Calendar.getInstance().getTimeInMillis();
	            m.responseTime = endTime - startTime;
	            
	        } catch (MalformedURLException e1) {
	            
	        	m.lastError = e1.getMessage();
	        	
	        } catch (IOException e) {
	            
	        	m.lastError = e.getMessage();
	        	
	        }

            m.save();
	        
            return (m.lastError == null || m.lastError.length() == 0) ? 1 : 0;
	    }
	    
	    return -1;
	}
	
	public static String getBasicAuthHeader(String username, String password) {
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
		return new String(authEncBytes);
	}
	
	public static boolean isResponseValid(int statusCode) {
		for (int code : Common.VALID_HTTP_STATUS_CODES) {
			if (statusCode == code) {
				return true;
			}
		}
		
		return false;
	}
}
 