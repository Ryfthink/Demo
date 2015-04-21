package ryf.demo.multitouch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchActivity extends Activity {
	protected static final String DEBUG_TAG = "MultiTouch";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new ContentView(this));
	}

	public static String actionToString(int action) {
		switch (action) {

			case MotionEvent.ACTION_DOWN:
				return "Down";
			case MotionEvent.ACTION_MOVE:
				return "Move";
			case MotionEvent.ACTION_POINTER_DOWN:
				return "Pointer Down";
			case MotionEvent.ACTION_UP:
				return "Up";
			case MotionEvent.ACTION_POINTER_UP:
				return "Pointer Up";
			case MotionEvent.ACTION_OUTSIDE:
				return "Outside";
			case MotionEvent.ACTION_CANCEL:
				return "Cancel";
		}
		return "";
	}

	private class ContentView extends View {
		int xPos = -1;
		int yPos = -1;
		Paint mPaint = new Paint();

		SparseArray<Holder> mArray = new SparseArray<Holder>(10);

		class Holder {
			int x, y;
		}

		public ContentView(Context context) {
			super(context);
			mPaint.setColor(Color.RED);
			mPaint.setAntiAlias(true);
			for (int i = 0; i < 10; i++) {
				mArray.put(i, new Holder());
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int action = MotionEventCompat.getActionMasked(event);
			// Get the index of the pointer associated with the action.
			int index = MotionEventCompat.getActionIndex(event);
			xPos = -1;
			yPos = -1;

			Log.d(DEBUG_TAG, "The action is " + actionToString(action));

			if (event.getPointerCount() > 1) {
				Log.d(DEBUG_TAG, "Multitouch event");
				// The coordinates of the current screen contact, relative
				// to
				// the responding View or Activity.
				xPos = (int) MotionEventCompat.getX(event, index);
				yPos = (int) MotionEventCompat.getY(event, index);

			} else {
				// Single touch event
				Log.d(DEBUG_TAG, "Single touch event");
				xPos = (int) MotionEventCompat.getX(event, index);
				yPos = (int) MotionEventCompat.getY(event, index);
			}
			mArray.get(index).x = xPos;
			mArray.get(index).y = yPos;
			invalidate();

			return true;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			for (int i = 0; i < 10; i++) {
				int x = mArray.get(i).x;
				int y = mArray.get(i).y;
				canvas.drawCircle(x, y, 100, mPaint);
			}
		}
	};
}
