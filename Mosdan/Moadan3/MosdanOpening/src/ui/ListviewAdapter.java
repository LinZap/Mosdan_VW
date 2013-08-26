package ui;

import java.util.ArrayList;

import TV.Mosdan2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListviewAdapter extends BaseAdapter
{

	private String[] adapter_name;
	private Integer[] adapter_pic;
	private LayoutInflater mInflater;

	public ListviewAdapter(Context context, String[] tit, Integer[] pic)
	{

		adapter_name = tit;
		adapter_pic = pic;
		mInflater = LayoutInflater.from(context);

	}

	public View getView(int position, View view, ViewGroup parent)
	{

		view = mInflater.inflate(R.layout.receivelistview, null);
		TextView picName = (TextView) view.findViewById(R.id.pic_name_txt);
		ImageView picIcon = (ImageView) view.findViewById(R.id.pic_icon_img);

		if (adapter_name != null)
		{
			picName.setText(adapter_name[position]);
			picIcon.setImageResource((int) adapter_pic[position]);
		}

		return view;

	}

	// Refresh listview
	public void ref(String[] name, Integer[] pic)
	{

		this.adapter_name = name;
		this.adapter_pic = pic;
		notifyDataSetChanged();
	}

	public int getCount()
	{
		// TODO Auto-generated method stub
		return adapter_name != null ? adapter_name.length : 0;
	}

	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return adapter_name[position];
	}

	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	public void filter(String[] tit, Integer[] pic, String text)
	{

		ArrayList<String> aname = new ArrayList<String>();
		ArrayList<Integer> apic = new ArrayList<Integer>();

		if (text.length() == 0 || tit == null)
		{
			adapter_name = tit;
			adapter_pic = pic;

		} else
		{
			for (int i = 0; i < tit.length; i++)
				if (tit[i].toLowerCase().contains(text.toLowerCase()))
				{
					aname.add(tit[i]);
					apic.add(pic[i]);
				}
			adapter_name = aname.toArray(new String[aname.size()]);
			adapter_pic = apic.toArray(new Integer[apic.size()]);

		}

		notifyDataSetChanged();
	}

}