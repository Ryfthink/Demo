package ryf.demo.contentprovider;

import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import ryf.demo.R;
import ryf.demo.overscroll.OverScrollActivity;

public class SettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);
		SwitchPreference notifySwitch = (SwitchPreference) findPreference("nofity");
		notifySwitch.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Log.e("BBBB", "value:" + newValue);
				return true;
			}
		});
		
		SwitchPreference screenSwitch = (SwitchPreference) findPreference("screen");
		screenSwitch.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Log.e("BBBB", "value:" + newValue);
				return true;
			}
		});
		
		DialogPreference editPreference = (DialogPreference) findPreference("edit");
		editPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				Log.e("BBBB", "value:" + newValue);
				return true;
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, "Show current settings");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				startActivity(new Intent(this, OverScrollActivity.class));
				return true;
		}
		return false;

	}
	
}
