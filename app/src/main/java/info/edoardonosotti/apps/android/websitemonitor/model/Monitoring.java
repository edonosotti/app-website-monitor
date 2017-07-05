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

package info.edoardonosotti.apps.android.websitemonitor.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "monitorings")
public class Monitoring extends Model {

	@Column(name = "checked_at")
	public long checkTimestamp;
	
	@Column(name = "response_time")
	public long responseTime;
	
	@Column(name = "return_code")
	public int returnCode;
	
	@Column(name = "last_error")
	public String lastError;
	
	// Foreign key
	@Column(name = "website")
	public Website website;
	
	public static List<Monitoring> getByWebsiteId(long id) {
		return new Select().from(Monitoring.class).where("Website = ?", id).orderBy("checked_at DESC").execute();
	}
	
	public static void cleanWebsiteHistory(long id, long thresold) {		
		if (thresold > 0) {
			Monitoring m = new Select()
				.from(Monitoring.class)
				.where("Website = ?", id)
				.orderBy("checked_at DESC")
				.limit(String.valueOf(thresold) + ", 1")
				.executeSingle();
			if (m != null) {
				new Delete().from(Monitoring.class).where("Website = ? AND checked_at <= ?", id, m.checkTimestamp).execute();
			}
			
		} else {
			new Delete().from(Monitoring.class).where("Website = ?", id).execute();
		}
	}
	
}
