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

//水平垂直延展

public class Picker_scale extends DialogPreference {

	// DialogPreference 的 Context
	private NumberPicker pickerX, pickerY, pluspicker1, pluspicker2;
	private TextView signTextView;

	public Picker_scale(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setDialogLayoutResource(R.layout.numberpicker);

		setSummary(Data.Rx_scaleX[Data.index_rx] + " , "
				+ Data.Rx_scaleY[Data.index_rx]);
	}

	@Override
	protected void onBindDialogView(View view) {
		signTextView = (TextView) view.findViewById(R.id.fuhow2);
		pickerX = (NumberPicker) view.findViewById(R.id.picker1);
		pickerY = (NumberPicker) view.findViewById(R.id.picker2);
		pluspicker1 = (NumberPicker) view.findViewById(R.id.pluspicker1);
		pluspicker2 = (NumberPicker) view.findViewById(R.id.pluspicker2);

		pickerX.setMinValue(0);
		pickerY.setMinValue(0);
		pluspicker1.setMinValue(1);
		pluspicker2.setMinValue(1);

		pickerX.setMaxValue(10000);
		pickerY.setMaxValue(10000);
		pluspicker1.setMaxValue(2);
		pluspicker2.setMaxValue(2);

		pluspicker1.setDisplayedValues(new String[] { "+", "-" });
		pluspicker2.setDisplayedValues(new String[] { "+", "-" });

		if (Data.Rx_scaleX[Data.index_rx] > 0)
			pluspicker1.setValue(1);
		else
			pluspicker1.setValue(2);

		if (Data.Rx_scaleY[Data.index_rx] > 0)
			pluspicker2.setValue(1);
		else
			pluspicker2.setValue(2);

		pickerX.setValue(Math.abs(Data.Rx_scaleX[Data.index_rx]));
		pickerY.setValue(Math.abs(Data.Rx_scaleY[Data.index_rx]));

		signTextView.setText(" , ");
		super.onBindDialogView(view);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setTitle("延展 (水平,垂直)");
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
					t.VW_Rx_scaX(Data.Rx_mac[Data.index_rx],
							pluspicker1.getValue() == 2 ? pickerX.getValue()
									* -1 : pickerX.getValue());
				}
			}.start();

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.VW_Rx_scaY(Data.Rx_mac[Data.index_rx],
							pluspicker2.getValue() == 2 ? pickerY.getValue()
									* -1 : pickerY.getValue());
				}
			}.start();

			// 儲存設定到 SharedPreferences
			Data.Rx_scaleX[Data.index_rx] = pluspicker1.getValue() == 2 ? pickerX
					.getValue() * -1
					: pickerX.getValue();
			Data.Rx_scaleY[Data.index_rx] = pluspicker2.getValue() == 2 ? pickerY
					.getValue() * -1
					: pickerY.getValue();
			setSummary(Data.Rx_scaleX[Data.index_rx] + " , "
					+ Data.Rx_scaleY[Data.index_rx]);
		}
	};

}
