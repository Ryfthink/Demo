package ryf.demo.demo;

import ryf.demo.floatingwindow.FloatApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class SweepView extends View {

	private Path mPath;
	private Paint mPaint;
	private Rect mRect;
	private GradientDrawable mDrawable;
	private int mRotate;
	
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	private OnClickListener mClickListener;
	private WindowManager windowManager;// = (WindowManager)
										// getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
	private WindowManager.LayoutParams windowManagerParams = ((FloatApplication) getContext().getApplicationContext()).getWindowParams();

	public SweepView(Context context) {
		super(context);
		setFocusable(true);
		windowManager = (WindowManager) getContext().getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		mPath = new Path();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.parseColor("#ff99cc00"));
		mRect = new Rect(0, 0, 300, 300);
		// #ff0099cc
		mDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] { 0x00ffffFF, 0x500099cc,
				0xff0099cc });
		mDrawable.setShape(GradientDrawable.RADIAL_GRADIENT);
		mDrawable.setGradientRadius((float) (Math.sqrt(2) * 60));
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取到状态栏的高度
		Rect frame = new Rect();
		getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println("statusBarHeight:" + statusBarHeight);
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度
		Log.i("tag", "currX" + x + "====currY" + y);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
				// 获取相对View的坐标，即以此View左上角为原点
				mTouchX = event.getX();
				mTouchY = event.getY();
				mStartX = x;
				mStartY = y;
				Log.i("tag", "startX" + mTouchX + "====startY" + mTouchY);
				break;
			case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
				updateViewPosition();
				break;
			case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
				updateViewPosition();
				mTouchX = mTouchY = 0;
				if ((x - mStartX) < 5 && (y - mStartY) < 5) {
					if (mClickListener != null) {
						mClickListener.onClick(this);
					}
				}
				break;
		}
		return true;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);
		windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
	}

	static void setCornerRadii(GradientDrawable drawable, float r0, float r1, float r2, float r3) {
		drawable.setCornerRadii(new float[] { r0, r0, r1, r1, r2, r2, r3, r3 });
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(mRotate, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
		mDrawable.setBounds(mRect);
		canvas.translate(getMeasuredWidth() / 2 - mRect.width() / 2, getMeasuredHeight() / 2 - mRect.height() / 2);
		mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
		setCornerRadii(mDrawable, 0, 15, 0, 15);
		mDrawable.draw(canvas);
		canvas.drawCircle(mRect.width() / 2, mRect.width() / 2, mRect.width() / 2 / 3, mPaint);
		canvas.restore();
		mRotate += 6;
		if (mRotate >= 360) {
			mRotate = 0;
		}
		invalidate();
	}

}