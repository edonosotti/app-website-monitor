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
import com.activeandroid.annotation.*;
import com.activeandroid.query.Select;

@Table(name = "websites")
public class Website extends Model {

	@Column(name = "name")
	public String name;
	
	@Column(name = "url")
	public String url;
	
	@Column(name = "ba_user")
	public String username;
	
	@Column(name = "ba_pass")
	public String password;
	
	@Column(name = "ba_enabled")
	public boolean enableBasicAuth;
	
	public List<Monitoring> monitorings() {
		return getMany(Monitoring.class, "Website");
	}
	
	public Monitoring getLastMonitoring() {
		return new Select().from(Monitoring.class).where("Website = ?", getId()).orderBy("checked_at DESC").executeSingle();
	}
	
	public static List<Website> getAll() {
		return new Select().from(Website.class).orderBy("name").execute();
	}
}
