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

import java.util.Date;
import java.util.List;

import info.edoardonosotti.apps.android.websitemonitor.Common;
import info.edoardonosotti.apps.android.websitemonitor.R;
import info.edoardonosotti.apps.android.websitemonitor.helpers.NetworkHelper;
import info.edoardonosotti.apps.android.websitemonitor.model.Monitoring;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MonitoringHistoryAdapter extends ArrayAdapter<Monitoring> {
	private final Context context;
	private final List<Monitoring> objects;

	public MonitoringHistoryAdapter(Context context, int resource, List<Monitoring> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
	}

	static class ViewHolder {
		public TextView firstLine;
		public TextView secondLine;
		public TextView thirdLine;
		public ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = getRowView(convertView, parent);
		ViewHolder viewHolder = (ViewHolder) rowView.getTag();

		Monitoring m = objects.get(position);

		// viewHolder.firstLine.setText(w.name);
		
		viewHolder.firstLine.setVisibility(View.GONE);
		
		if (m != null) {
			
			if (m.lastError == null || m.lastError.length() <= 0) {
				
				viewHolder.secondLine.setText(String.format(context.getString(R.string.format_check_details),
						m.returnCode, m.responseTime));
				
				viewHolder.thirdLine.setText(context.getString(R.string.checked_at) + 
						": " + DateFormat.format(context.getString(R.string.format_date), new Date(m.checkTimestamp)));
				
				if (NetworkHelper.isResponseValid(m.returnCode) && m.responseTime <= Common.SLOW_RESPONSE_THRESOLD) {
					viewHolder.icon.setImageResource(R.drawable.ic_check_ok);
					viewHolder.secondLine.setTextColor(Color.rgb(42, 110, 0));
				} else if (NetworkHelper.isResponseValid(m.returnCode)) {
					viewHolder.icon.setImageResource(R.drawable.ic_check_problem);
					viewHolder.secondLine.setTextColor(Color.rgb(255, 119, 0));
				} else {
					viewHolder.icon.setImageResource(R.drawable.ic_check_ko);
					viewHolder.secondLine.setTextColor(Color.rgb(210, 0, 0));
				}
				
			} else {
				
				viewHolder.secondLine.setText(m.lastError);
				viewHolder.icon.setImageResource(R.drawable.ic_check_ko);
				viewHolder.secondLine.setTextColor(Color.rgb(210, 0, 0));
				
			} 
			
		} else {
			
			viewHolder.secondLine.setText(context.getString(R.string.not_checked_yet));
			viewHolder.secondLine.setTextColor(Color.rgb(110, 110, 110));
			
		}

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
			viewHolder.thirdLine = (TextView) rowView
					.findViewById(R.id.thirdLine);
			viewHolder.icon = (ImageView) rowView.findViewById(R.id.icon);
			rowView.setTag(viewHolder);
		}

		return rowView;
	}

}
