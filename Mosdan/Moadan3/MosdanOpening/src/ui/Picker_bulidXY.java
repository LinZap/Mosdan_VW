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

public class Picker_bulidXY extends DialogPreference {
	private NumberPicker pickerX, pickerY,pluspicker1,pluspicker2;
	private TextView signTextView;

	public Picker_bulidXY(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.numberpicker);
		setSummary(Data.Situation_bulidx[Data.index_situation] + " X "
				+ Data.Situation_bulidy[Data.index_situation]);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {

		builder.setTitle("組成");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("確定", OK_listener);
		super.onPrepareDialogBuilder(builder);

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

		pickerX.setMaxValue(30);
		pickerY.setMaxValue(30);
		pickerX.setMinValue(1);
		pickerY.setMinValue(1);
		signTextView.setText(" X ");
		
		pickerX.setValue(Data.Situation_bulidx[Data.index_situation]);
		pickerY.setValue(Data.Situation_bulidy[Data.index_situation]);

		super.onBindDialogView(view);
	}
	private OnClickListener OK_listener = new OnClickListener() {

		public void onClick(DialogInterface arg0, int whitch) {
			

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.VW_X(Data.Situation_name[Data.index_situation], pickerX.getValue());
				}
			}.start();	
			
			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.VW_Y(Data.Situation_name[Data.index_situation], pickerY.getValue());
				}
			}.start();

			Data.Situation_bulidx[Data.index_situation] = pickerX.getValue();
			Data.Situation_bulidy[Data.index_situation] = pickerY.getValue();
			setSummary(pickerX.getValue() + " X " + pickerY.getValue());
		
			
		}
	};
}
