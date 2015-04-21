package ryf.demo.animator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import ryf.demo.R;

public class AnimatinglayoutChange extends Activity {
	private ViewGroup mContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animatinglayout);
		mContainer = (ViewGroup) findViewById(R.id.container);
		findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContainer.addView(generateView(String.valueOf(mContainer.getChildCount())), 0);
			}
		});

		findViewById(R.id.btn2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mContainer.getChildCount() > 0) {
					mContainer.removeViewAt(0);
				}
			}
		});
	}

	private View generateView(String txt) {
		Button btn = new Button(this);
		btn.setGravity(Gravity.CENTER);
		btn.setText(txt);
		return btn;
	}

}
