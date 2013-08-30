package ui;

import Data.Data;
import TV.Mosdan2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	// group1,2 text and icon data
	private String[] group1_text = { "首頁", "一般連線", "電視牆情境", "群組廣播" },
			group2_text = { "傳送端", "接收端" };
	private int[] group1_icon = { R.drawable.home, R.drawable.gconn_icon2,
			R.drawable.vw_icon2, R.drawable.vw_icon2}, group2_icon = {
			R.drawable.txred, R.drawable.rxorange };

	// group name
	private String[] group_name = { "功能", "機器" };

	// elements on view
	private ImageView mIcon;
	private TextView mChildName, mGroupName;

	public ExpandAdapter(Context ctx) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.group_item_layout, null);
		mGroupName = (TextView) convertView.findViewById(R.id.group_name);
		mGroupName.setText(group_name[groupPosition]);
		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null)
			convertView = mInflater.inflate(R.layout.child_item_layout, null);

		mIcon = (ImageView) convertView.findViewById(R.id.img);
		mChildName = (TextView) convertView.findViewById(R.id.item_name);

		switch (groupPosition) {

		case 0:

			mIcon.setBackgroundResource(group1_icon[childPosition]);
			mChildName.setText(group1_text[childPosition]);
			break;

		case 1:

			mIcon.setBackgroundResource(group2_icon[childPosition]);
			mChildName.setText(group2_text[childPosition]);
			break;

		default:

		}
		if (groupPosition == Data.groupSelected
				&& childPosition == Data.childSelected)
			convertView.setBackgroundResource(R.drawable.select2);
		else
			convertView.setBackgroundResource(0);
		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		switch (groupPosition) {
		case 0:
			return group1_text[childPosition];
		case 1:
			return group2_text[childPosition];
		default:
			return 0;
		}
	}

	public int getChildrenCount(int groupPosition) {

		switch (groupPosition) {
		case 0:
			return group1_text.length;
		case 1:
			return group2_text.length;
		default:
			return 0;
		}
	}

	public Object getGroup(int groupPosition) {

		return group_name[groupPosition];
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group_name.length;
	}

	public void setPositionSelected(int groupPosition, int childPosition) {
		Data.groupSelected = groupPosition;
		Data.childSelected = childPosition;
		this.notifyDataSetChanged();
	}

}
