package ryf.demo.memoryleak;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MemoryLeakActivity extends Activity {
	private View view;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new MemoryLeakView(MemoryLeakApplicatoin.sContext);
//		view = new MemoryLeakView(this);
		setContentView(view);
		Log.e("BBBB", "hashCode: " + view.hashCode());
	}
}
