package activity;

import TV.Mosdan2.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MosdanOpening extends Activity implements AnimationListener
{

	private ImageView imageView = null;
	private Animation alphaAnimation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.mosdanop);
		imageView = (ImageView) findViewById(R.id.welcome_image_view);

		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/japan2.otf");
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setTypeface(tf);

		alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.mosdanopalpha);
		alphaAnimation.setFillEnabled(true); // 啟動Fill維持
		alphaAnimation.setFillAfter(true); // 設置動畫的最後Fps保持在View上面
		imageView.setAnimation(alphaAnimation);
		tv.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(this); // 動畫設置監聽
	}

	public void onAnimationStart(Animation animation)
	{

	}

	public void onAnimationEnd(Animation animation)
	{

		Intent intent = new Intent(MosdanOpening.this, Loading.class);
		startActivity(intent);
		finish();
	}

	public void onAnimationRepeat(Animation animation)
	{

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			return false;
		}
		return false;
	}

}