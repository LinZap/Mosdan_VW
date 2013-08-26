package ui;



import Data.Data;
import TV.Mosdan2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyFlow_View_Adapter extends BaseAdapter {

    private LayoutInflater mInflater;

    public MyFlow_View_Adapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public int getCount() {
        return Data.Gconn_title.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    
    // put in layout
    public View getView(int position, View convertView , ViewGroup parent) {
        int view = getItemViewType(position);
        if (convertView == null) {
            switch (view) {
                case 0:
                	convertView = mInflater.inflate(R.layout.gconn_main, null);
                break;
                case 1:
                	 
                	convertView = mInflater.inflate(R.layout.gconn_trconn, null);
                break;
                case 2:
                	convertView = mInflater.inflate(R.layout.gconn_group, null);
                	
                	
                break;
                
            }
        }
        return convertView;
    }

	

}
