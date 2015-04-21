package ryf.demo.headergridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SFFrameLayout extends FrameLayout implements IParent {
	
	protected Drawable mFocusDrawable;
	private Rect mCurrentRect;
	private RectScroller mScroller;
	private int mFocusAnimDuration = 200;// 焦点移动耗时

	public SFFrameLayout(Context context) {
		super(context);
		init();
	}
	
	public SFFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public SFFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		mCurrentRect = new Rect();
		mScroller = new RectScroller();
		setFocusAnimable(false);
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mCurrentRect.set(mScroller.getCurrRect());
			mFocusDrawable = mScroller.getCurrDrawable();
			invalidate();
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mFocusDrawable != null && mCurrentRect != null && !isInTouchMode()) {
			mFocusDrawable.setBounds(mCurrentRect);
			mFocusDrawable.draw(canvas);
		}
	}
	
	public void setFocusAnimable(boolean animable) {
		mFocusAnimDuration = animable ? 300 : 0;
	}
	public void setFocusAnimable(boolean animable , int duration) {
		mFocusAnimDuration = animable ? duration : 0;
	}
	
	@Override
	public void notifyFocusChange(boolean focused, IFocusable item, Rect rect) {
		removeCallbacks(mFocusRunnalbe);
		if (focused && rect != null && !rect.isEmpty() && mCurrentRect != null && !mCurrentRect.isEmpty()) {
			mScroller.startScroll(mCurrentRect, rect, mFocusDrawable, item.getFocusedBackground(), mFocusAnimDuration);
			invalidate();
		} else if (focused && rect != null && !rect.isEmpty()) {
			mCurrentRect.set(rect);
			mFocusDrawable = item.getFocusedBackground();
			invalidate();
		} else if (!focused && (isInTouchMode())) {
			mScroller.abortAnimation();
			mCurrentRect.setEmpty();
			invalidate();
		} else if (!focused) {
			postDelayed(mFocusRunnalbe, 50);
		}
	}
	
	private Runnable mFocusRunnalbe = new Runnable() {
		
		@Override
		public void run() {
			if (findFocus() == null) {
				mScroller.abortAnimation();
				mCurrentRect.setEmpty();
				invalidate();
			}
		}
	};
	
	@Override
	public Rect getCurrentRect() {
		return new Rect(mCurrentRect);
	}

}
