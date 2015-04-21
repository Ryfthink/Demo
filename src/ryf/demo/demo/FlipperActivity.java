package ryf.demo.demo;

import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import ryf.demo.R;

public class FlipperActivity extends Activity {
	private Animation leftInAnimation;
	private Animation leftOutAnimation;
	private Animation rightInAnimation;
	private Animation rightOutAnimation;
	private GestureDetector detector; // 手势检测
	ViewFlipper viewFlipper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewFlipper = new ViewFlipper(this);
		viewFlipper.addView(getImageView());
		viewFlipper.addView(getImageView());
		viewFlipper.addView(getImageView());
		viewFlipper.addView(getImageView());
		viewFlipper.addView(getImageView());
		viewFlipper.addView(getImageView());

		setContentView(viewFlipper);

		detector = new GestureDetector(this, mOnGestureListener);

		// 动画效果
		leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
		leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
		rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
		rightOutAnimation = AnimationUtils.loadAnimation(this, R.anim.right_out);
	}

	static final int max = 0xFFFFFF;

	private View getImageView() {
		ImageView img = new ImageView(this);
		int color = new Random().nextInt(max);
		img.setBackgroundColor(color);
		return img;
	}

	OnGestureListener mOnGestureListener = new OnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

			if (e1.getX() - e2.getX() > 120) {
				viewFlipper.setInAnimation(leftInAnimation);
				viewFlipper.setOutAnimation(leftOutAnimation);
				viewFlipper.showNext();// 向右滑动
				return true;
			} else if (e1.getX() - e2.getY() < -120) {
				viewFlipper.setInAnimation(rightInAnimation);
				viewFlipper.setOutAnimation(rightOutAnimation);
				viewFlipper.showPrevious();// 向左滑动
				return true;
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event); // touch事件交给手势处理。
	}

}
