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

public final class Common {
	public static final int[] VALID_HTTP_STATUS_CODES = new int[] { 200, 301, 302 };
	public static final int SLOW_RESPONSE_THRESOLD = 10000;
	public static final int CONNECTION_TIMEOUT = 30000;
	
	public static final int DEFAULT_INTERVAL = 60000;
	public static final int DEFAULT_HISTORY = 100;
	
	public static final String URL_DOCS_REMOTE = "http://edoardonosotti.info/en/apps/website-monitor?embed=true";
	public static final String URL_DOCS_LOCAL = "file:///android_asset/docs/index.html";
}
