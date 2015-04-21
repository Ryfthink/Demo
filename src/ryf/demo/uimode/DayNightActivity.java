package ryf.demo.uimode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import ryf.demo.R;
import ryf.demo.log.LogActivity;
import ryf.demo.log.LogService;

public class DayNightActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//
//		if (savedInstanceState != null) {
//			style = savedInstanceState.getInt("theme_id");
//			setTheme(style);
//		} else {
//			setTheme(R.style.ThemeNight);
//		}
//
//		setContentView(R.layout.day_night_layout);
//		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				changeTheme();
//			}
//		});
		startActivity(new Intent(this, LogActivity.class));
		finish();
	}

	private int style;

	void changeTheme() {
		if (style == R.style.ThemeNight) {
			style = R.style.ThemeDay;
		} else {
			style = R.style.ThemeNight;
		}
		recreate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("theme_id", style);
	}

}
