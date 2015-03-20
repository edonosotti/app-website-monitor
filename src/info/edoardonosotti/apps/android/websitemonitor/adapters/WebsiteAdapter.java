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

package info.edoardonosotti.apps.android.websitemonitor.adapters;

import java.util.List;

import info.edoardonosotti.apps.android.websitemonitor.R;
import info.edoardonosotti.apps.android.websitemonitor.model.Website;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WebsiteAdapter extends ArrayAdapter<Website> {
	private final Context context;
	private final List<Website> objects;

	public WebsiteAdapter(Context context, int resource, List<Website> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
	}

	static class ViewHolder {
		public TextView firstLine;
		public TextView secondLine;
		public ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = getRowView(convertView, parent);
		ViewHolder viewHolder = (ViewHolder) rowView.getTag();

		Website m = objects.get(position);

		viewHolder.firstLine.setText(m.name);

		viewHolder.secondLine.setText(m.url);

		viewHolder.icon.setImageResource(R.drawable.ic_website);

		return rowView;
	}

	private View getRowView(View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_monitoring_item, parent,
					false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.firstLine = (TextView) rowView
					.findViewById(R.id.firstLine);
			viewHolder.secondLine = (TextView) rowView
					.findViewById(R.id.secondLine);
			viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
			rowView.setTag(viewHolder);
		}

		return rowView;
	}

}
