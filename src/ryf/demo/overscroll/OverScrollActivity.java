package ryf.demo.overscroll;

import android.app.Activity;
import android.os.Bundle;

public class OverScrollActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new WorksheetView(this));
	}
}
