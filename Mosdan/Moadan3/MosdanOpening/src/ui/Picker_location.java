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
import android.widget.TextView;

public class Picker_location extends DialogPreference {

	// 選擇顯示位置

	private NumberPicker pickerX, pickerY, pluspicker1, pluspicker2;
	private TextView signTextView;

	public Picker_location(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setDialogLayoutResource(R.layout.numberpicker);

		setSummary(Data.Rx_locX[Data.index_rx] + " , "
				+ Data.Rx_locY[Data.index_rx]);
	}

	@Override
	protected void onBindDialogView(View view) {
		pluspicker1 = (NumberPicker) view.findViewById(R.id.pluspicker1);
		pluspicker2 = (NumberPicker) view.findViewById(R.id.pluspicker2);
		pluspicker1.setVisibility(View.GONE);
		pluspicker2.setVisibility(View.GONE);
		signTextView = (TextView) view.findViewById(R.id.fuhow2);
		pickerX = (NumberPicker) view.findViewById(R.id.picker1);
		pickerY = (NumberPicker) view.findViewById(R.id.picker2);
		getSituation_index();
		signTextView.setText(" , ");
		super.onBindDialogView(view);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setTitle("組成");
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
					t.VW_Rx_locX(Data.Rx_mac[Data.index_rx], pickerX.getValue());
				}
			}.start();

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.VW_Rx_locY(Data.Rx_mac[Data.index_rx], pickerY.getValue());
				}
			}.start();

			// 儲存設定到 SharedPreferences
			Data.Rx_locX[Data.index_rx] = pickerX.getValue();
			Data.Rx_locY[Data.index_rx] = pickerY.getValue();
			setSummary(Data.Rx_locX[Data.index_rx] + " , "
					+ Data.Rx_locY[Data.index_rx]);
		}
	};

	private void getSituation_index() {
		int ind = 0;
		// search index
		for (int i = 0; i < Data.Situation_name.length; i++)
			if (Data.Situation_name[i].equals(Data.Rx_sitsution[Data.index_rx])) {
				ind = i;
				break;
			}
		pickerX.setMinValue(1);
		pickerY.setMinValue(1);

		pickerX.setMaxValue(Data.Situation_bulidx[ind]);
		pickerY.setMaxValue(Data.Situation_bulidy[ind]);

		pickerX.setValue(Data.Rx_locX[Data.index_rx]);
		pickerY.setValue(Data.Rx_locY[Data.index_rx]);
	}

}
