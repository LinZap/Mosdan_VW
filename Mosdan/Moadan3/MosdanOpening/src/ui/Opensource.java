package ui;

import TV.Mosdan2.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class Opensource extends DialogPreference {

	public Opensource(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDialogLayoutResource(R.layout.opensource);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {

		builder.setTitle("開放原始碼授權");
		builder.setPositiveButton("確定", OK_listener);
		super.onPrepareDialogBuilder(builder);

	}

	private OnClickListener OK_listener = new OnClickListener() {

		public void onClick(DialogInterface arg0, int whitch) {

			arg0.dismiss();

		}
	};
}
