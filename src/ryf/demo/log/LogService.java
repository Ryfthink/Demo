package ryf.demo.log;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

public class LogService extends Service {

	private static final String TAG = "LogService";
	private WindowManager mWindowManager;
	private LogView mLogView;

	@Override
	public void onCreate() {
		super.onCreate();
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Point sizePoint = new Point();
		mWindowManager.getDefaultDisplay().getSize(sizePoint);
		WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams();
		windowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		windowManagerParams.format = PixelFormat.RGBA_8888;
		windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
		windowManagerParams.x = 0;
		windowManagerParams.y = 0;
		windowManagerParams.width = sizePoint.x / 2;
		windowManagerParams.height = sizePoint.y;
		mWindowManager.addView(inflateViews(), windowManagerParams);
		initializeLogging();
	}

	/** Create a chain of targets that will receive log data */
	public void initializeLogging() {
		// Wraps Android's native log framework.
		LogWrapper logWrapper = new LogWrapper();
		// Using Log, front-end to the logging chain, emulates android.util.log
		// method signatures.
		Log.setLogNode(logWrapper);

		// Filter strips out everything except the message text.
		MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
		logWrapper.setNext(msgFilter);

		// On screen logging via a fragment with a TextView.
		msgFilter.setNext(mLogView);

		Log.i(TAG, "Ready");
	}

	public View inflateViews() {
		final ScrollView mScrollView = new ScrollView(this);
		ViewGroup.LayoutParams scrollParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mScrollView.setLayoutParams(scrollParams);

		mLogView = new LogView(this);
		ViewGroup.LayoutParams logParams = new ViewGroup.LayoutParams(scrollParams);
		logParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		mLogView.setLayoutParams(logParams);
		mLogView.setClickable(true);
		mLogView.setFocusable(true);
		mLogView.setTypeface(Typeface.MONOSPACE);

		// Want to set padding as 16 dips, setPadding takes pixels. Hooray math!
		int paddingDips = 16;
		double scale = getResources().getDisplayMetrics().density;
		int paddingPixels = (int) ((paddingDips * (scale)) + .5);
		mLogView.setPadding(paddingPixels, paddingPixels, paddingPixels, paddingPixels);
		mLogView.setCompoundDrawablePadding(paddingPixels);

		mLogView.setGravity(Gravity.BOTTOM);
		mLogView.setTextAppearance(this, android.R.style.TextAppearance_Holo_Small);
		mLogView.setTextColor(Color.MAGENTA);

		mLogView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});

		mScrollView.addView(mLogView);
		mLogView.setBackgroundColor(0xa0000000);
		return mScrollView;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		dispatchCommand(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void dispatchCommand(Intent intent) {
		if (intent != null) {
			String msg = intent.getStringExtra("msg");
			Log.i(TAG, msg);
		}
	}

	public static void log(Context context, String msg) {
		Intent intent = new Intent(context, LogService.class);
		intent.putExtra("msg", msg);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(intent);
	}
}
