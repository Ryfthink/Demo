package ryf.demo.headergridview;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.animation.AnimationUtils;

public class RectScroller {
	private Rect mStartRect;
	private Rect mCurrRect;
	private Rect mDeltaRect;
	private Rect mFinalRect;
	
	private Drawable mStartDrawable;
	private Drawable mFinalDrawable;
	private Drawable mCurrDrawable;
	
	private long mStartTime;
	private int mDuration;
	private boolean mFinished;
	
	public RectScroller() {
		mStartRect = new Rect();
		mCurrRect = new Rect();
		mDeltaRect = new Rect();
		mFinalRect = new Rect();
		
		mFinished = true;
	}
	
	public boolean computeScrollOffset() {
        if (mFinished) {
            return false;
        }

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    
        if (timePassed < mDuration) {
            float x = timePassed / (float) mDuration;

        	mCurrRect.left = mStartRect.left + (int) (x * mDeltaRect.left + 0.5f);
            mCurrRect.top = mStartRect.top + (int) (x * mDeltaRect.top + 0.5f);
            mCurrRect.right = mStartRect.right + (int) (x * mDeltaRect.right + 0.5f);
            mCurrRect.bottom = mStartRect.bottom + (int) (x * mDeltaRect.bottom + 0.5f);
        } else {
        	mCurrRect.left = mFinalRect.left;
        	mCurrRect.top = mFinalRect.top;
        	mCurrRect.right = mFinalRect.right;
        	mCurrRect.bottom = mFinalRect.bottom;
            mFinished = true;
        }

        if (mStartDrawable == null || mFinalDrawable == null) {
        	mCurrDrawable = mFinished ? mFinalDrawable : null;
        } else if (timePassed * 2 < mDuration) {
        	mCurrDrawable = !(mStartDrawable instanceof NinePatchDrawable) && mFinalDrawable instanceof NinePatchDrawable ? mFinalDrawable : mStartDrawable;
        } else {
        	mCurrDrawable = !mFinished && mStartDrawable instanceof NinePatchDrawable && !(mFinalDrawable instanceof NinePatchDrawable) ? mStartDrawable : mFinalDrawable;
        }
        
        return true;
    }
	
	public void startScroll(Rect startRect, Rect finalRect, int duration) {
		startScroll(startRect, finalRect, null, null, duration);
	}
	
	public void startScroll(Rect startRect, Rect finalRect, Drawable startDrawable, Drawable finalDrawable, int duration) {
		mStartDrawable = startDrawable;
		mFinalDrawable = finalDrawable;
		
		mFinished = false;
        mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        
        mStartRect.left = startRect.left;
        mStartRect.top = startRect.top;
        mStartRect.right = startRect.right;
        mStartRect.bottom = startRect.bottom;
        
        mFinalRect.left = finalRect.left;
        mFinalRect.top = finalRect.top;
        mFinalRect.right = finalRect.right;
        mFinalRect.bottom = finalRect.bottom;
        
        mDeltaRect.left = finalRect.left - startRect.left;
        mDeltaRect.top = finalRect.top - startRect.top;
        mDeltaRect.right = finalRect.right - startRect.right;
        mDeltaRect.bottom = finalRect.bottom - startRect.bottom;
	}
	
	public void abortAnimation() {
		mCurrRect.left = mFinalRect.left;
    	mCurrRect.top = mFinalRect.top;
    	mCurrRect.right = mFinalRect.right;
    	mCurrRect.bottom = mFinalRect.bottom;
        mFinished = true;
    }
	
	public final boolean isFinished() {
        return mFinished;
    }
	
	public int getCurrLeft() {
		return mCurrRect.left;
	}
	
	public int getCurrTop() {
		return mCurrRect.top;
	}
	
	public int getCurrRight() {
		return mCurrRect.right;
	}
	
	public int getCurrBottom() {
		return mCurrRect.bottom;
	}
	
	public Rect getCurrRect() {
		return mCurrRect;
	}
	
	public Drawable getCurrDrawable() {
		return mCurrDrawable;
	}
}
