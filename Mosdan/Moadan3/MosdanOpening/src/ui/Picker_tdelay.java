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

public class Picker_tdelay extends DialogPreference {

	private NumberPicker picker, pluspicker;

	public Picker_tdelay(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.onepicker);
		setSummary(Data.Rx_tdelay[Data.index_rx] + "");
	}

	@Override
	protected void onBindDialogView(View view) {

		pluspicker = (NumberPicker) view.findViewById(R.id.onepicker1);
		pluspicker.setDisplayedValues(new String[] { "+", "-" });
		pluspicker.setVisibility(View.VISIBLE);
		pluspicker.setMinValue(1);
		pluspicker.setMaxValue(2);
		

		if (Data.Rx_tdelay[Data.index_rx] > 0)
			pluspicker.setValue(1);
		else
			pluspicker.setValue(2);

		
		
		picker = (NumberPicker) view.findViewById(R.id.onepicker);
		picker.setMaxValue(30000);
		picker.setMinValue(0);
		picker.setValue(Math.abs(Data.Rx_tdelay[Data.index_rx]));

		super.onBindDialogView(view);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setTitle("撕裂延展幅度");
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
					t.VW_Rx_TearDelay(Data.Rx_mac[Data.index_rx],
							pluspicker.getValue() == 2 ? picker.getValue() * -1
									: picker.getValue());
				}
			}.start();

			Data.Rx_tdelay[Data.index_rx] = pluspicker.getValue() == 2 ? picker
					.getValue() * -1 : picker.getValue();
			setSummary(Data.Rx_tdelay[Data.index_rx] + "");
		}
	};

}
