package ryf.demo.memoryleak;

import android.app.Application;

public class MemoryLeakApplicatoin extends Application {
	public static Application sContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sContext = this;
	}
}
