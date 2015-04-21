package ryf.demo.parceltest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityPacelTest2 extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText(getIntent().getExtras()
				.getParcelable("parcelBean")
				.toString());
		textView.append("\n");
		textView.append(getIntent().getExtras()
				.getParcelable("parcelEnum")
				.toString());
		setContentView(textView);
	}
}
