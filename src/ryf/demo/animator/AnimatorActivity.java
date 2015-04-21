package ryf.demo.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import ryf.demo.R;

public class AnimatorActivity extends Activity implements OnClickListener {
	ImageView img;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animator);
		img = (ImageView) findViewById(R.id.img);
		img.setOnClickListener(this);
		
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img:
				getAnimatorSet().start();
				break;
				
			case R.id.btn:
				viewPropertyAnimator();
				break;
				
			default:
				break;
		}
	}

	private Animator getValueAnimator() {
		ValueAnimator animator = ValueAnimator.ofFloat(1, 0, 1);
		animator.setDuration(3000);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Object value = animation.getAnimatedValue();
				Log.i("update", ((Float) value).toString());
				img.setAlpha(((Float) value));
			}
		});
		return animator;
	}

	private Animator getObjectAnimator() {
		ObjectAnimator animator = ObjectAnimator.ofInt(img, "padding", 0, 50, 0, 50, 0);
		animator.setDuration(3000);
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				ViewGroup.LayoutParams params = img.getLayoutParams();
				params.width = params.width - ((Integer) animation.getAnimatedValue());
				img.requestLayout();
			}
		});
		return animator;
	}

	private Animator getAnimatorSet() {
		AnimatorSet animatorSet = new AnimatorSet();
		Animator anim1 = getValueAnimator();
		Animator anim2 = getObjectAnimator();
		animatorSet.play(anim1);
		animatorSet.play(anim2).after(anim1);
		return animatorSet;
	}
	
	private void viewPropertyAnimator(){
//		ObjectAnimator animX = ObjectAnimator.ofFloat(btn, "x", 50f);
//		ObjectAnimator animY = ObjectAnimator.ofFloat(btn, "y", 100f);
//		AnimatorSet animSetXY = new AnimatorSet();
//		animSetXY.playTogether(animX, animY);
//		animSetXY.start();
		btn.animate().translationX(100);
	}

}
