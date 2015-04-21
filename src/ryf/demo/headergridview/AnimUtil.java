package ryf.demo.headergridview;

import android.view.KeyEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimUtil {
	
	public static final int DURATION = 200;
	
	public static final int LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
	
	public static final int UP = KeyEvent.KEYCODE_DPAD_UP;
	
	public static final int RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
	
	public static final int DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
	
	public static Animation newScaleAnimation(float scale) {
		return newScaleAnimation(scale, DURATION);
	}
	
	public static Animation newScaleAnimation(float scale,int duration) {
		Animation anim = new ScaleAnimation(
				1f, 
				scale, 
				1f, 
				scale, 
				Animation.RELATIVE_TO_SELF, 
				0.5f, 
				Animation.RELATIVE_TO_SELF, 
				0.5f
		);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		
		return anim;
	}
	
	public static AnimationSet newScaleAlfAnimation(float scale, float alpha){
	    AnimationSet set = new AnimationSet(true);
	    
	    Animation alphaAnimation = new AlphaAnimation(0.5f,1.0f); 
	    alphaAnimation.setDuration(DURATION);
	    alphaAnimation.setFillAfter(true);
	    
	    Animation anim = new ScaleAnimation(
                1f, 
                scale, 
                1f, 
                scale, 
                Animation.RELATIVE_TO_SELF, 
                0.5f, 
                Animation.RELATIVE_TO_SELF, 
                0.5f
        );
        anim.setDuration(DURATION);
        anim.setFillAfter(true);
	    
        set.addAnimation(alphaAnimation);   
        set.addAnimation(anim);  
        set.setFillAfter(true);         
        set.setFillEnabled(true); 
	    return set;
	}
	
	public static Animation newEdgeAnimation(int direction) {
		float toX = 0f, toY = 0f;
		
		switch (direction) {
		case LEFT:
			toX = 0.01f;
			break;
		case UP:
			toY = 0.01f;
			break;
		case RIGHT:
			toX = -0.01f;
			break;
		case DOWN:
			toY = -0.01f;
			break;
		}
		
		Animation anim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 
				0, 
				Animation.RELATIVE_TO_SELF, 
				toX, 
				Animation.RELATIVE_TO_SELF, 
				0, 
				Animation.RELATIVE_TO_SELF, 
				toY
		);
		anim.setDuration(300);
		
		return anim;
	}
	
	public static Animation newBreathAnim() {
		AlphaAnimation anim = new AlphaAnimation(1f, 0.3f);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(2000);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setStartOffset(500);
		return anim;
	}

}
