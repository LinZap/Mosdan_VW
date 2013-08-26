package ui;

import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class Picker_multihost extends DialogPreference {

	private NumberPicker picker,pluspicker;

	public Picker_multihost(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.onepicker);
		setSummary(Data.Situation_multihost[Data.index_situation] == 0 ? "無"
				: Data.Situation_multihost[Data.index_situation] + "");
	}

	@Override
	protected void onBindDialogView(View view) {

		pluspicker = (NumberPicker) view.findViewById(R.id.onepicker1);
		pluspicker.setVisibility(View.GONE);
		
		picker = (NumberPicker) view.findViewById(R.id.onepicker);
		picker.setMaxValue(30);
		picker.setMinValue(0);
		picker.setValue(Data.Situation_multihost[Data.index_situation]);

		super.onBindDialogView(view);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setTitle("多主機模式");
		// first button
		builder.setNegativeButton("取消", null);
		// second button
		builder.setPositiveButton("確定", OK_listener);
		super.onPrepareDialogBuilder(builder);
	}

	// 按下OK後要做的事
	private OnClickListener OK_listener = new OnClickListener() {

		public void onClick(DialogInterface arg0, int whitch) {	
			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.VW_multiHost(Data.Situation_name[Data.index_situation], picker.getValue());
				}
			}.start();	
				
			Data.Situation_multihost[Data.index_situation] = picker.getValue();
			setSummary(Data.Situation_multihost[Data.index_situation] == 0 ? "無"
					: Data.Situation_multihost[Data.index_situation] + "");
		}
	};

}
