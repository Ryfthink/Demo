package ryf.demo.gesture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class GestureActivity extends Activity implements OnGesturePerformedListener {

	GestureOverlayView mGestureOverlayView;

	private GestureLibrary gestureLib;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureOverlayView = new GestureOverlayView(this);
		// setContentView(mGestureOverlayView);
		// GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
		// View inflate = getLayoutInflater().inflate(R.layout.main, null);
		// gestureOverlayView.addView(inflate);
		// gestureOverlayView.addOnGesturePerformedListener(this);
		// gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
		// if (!gestureLib.load()) {
		// finish();
		// }
		setContentView(mGestureOverlayView);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			int color = 0xff000000 | new Random().nextInt(0xffffff);
			setTitle("Color: " + Integer.toHexString(color));
			mGestureOverlayView.setGestureColor(color);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		for (Prediction prediction : predictions) {
			if (prediction.score > 1.0) {
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
			}
		}
	}

	class FileGestureLibrary extends GestureLibrary {

		private final File mPath;

		final String LOG_TAG = "GestureLibrary";

		public FileGestureLibrary(File path) {
			mPath = path;
		}

		@Override
		public boolean isReadOnly() {
			return !mPath.canWrite();
		}

		public boolean save() {
			if (!mStore.hasChanged())
				return true;

			final File file = mPath;

			final File parentFile = file.getParentFile();
			if (!parentFile.exists()) {
				if (!parentFile.mkdirs()) {
					return false;
				}
			}

			boolean result = false;
			try {
				// noinspection ResultOfMethodCallIgnored
				file.createNewFile();
				mStore.save(new FileOutputStream(file), true);
				result = true;
			} catch (FileNotFoundException e) {
				Log.d(LOG_TAG, "Could not save the gesture library in " + mPath, e);
			} catch (IOException e) {
				Log.d(LOG_TAG, "Could not save the gesture library in " + mPath, e);
			}

			return result;
		}

		public boolean load() {
			boolean result = false;
			final File file = mPath;
			if (file.exists() && file.canRead()) {
				try {
					mStore.load(new FileInputStream(file), true);
					result = true;
				} catch (FileNotFoundException e) {
					Log.d(LOG_TAG, "Could not load the gesture library from " + mPath, e);
				} catch (IOException e) {
					Log.d(LOG_TAG, "Could not load the gesture library from " + mPath, e);
				}
			}
			return result;
		}

	}

}
