package ryf.demo.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ryf.demo.demo.LaunchReceiver;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

public class LogActivity extends Activity {

	private TextView txtView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		txtView = new TextView(this);
		setContentView(txtView);
		mReceiver = new LaunchReceiver();
		registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
	}

	private LaunchReceiver mReceiver;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		txtView.setText("");
		File dir = new File(getFilesDir(), "log.txt");
		if (dir.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(dir));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String txt;
			try {
				while ((txt = reader.readLine()) != null) {
					txtView.append(txt + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			txtView.setText("empty");
		}
	}
}
