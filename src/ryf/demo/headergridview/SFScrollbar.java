package ryf.demo.headergridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class SFScrollbar extends View {
	public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
	public static final int VERTICAL = LinearLayout.VERTICAL;
	
	private int mOrientation;
	private int mThumbPosition;
	private int mThumbLength;
	private int mTrackLength;
	
	private Drawable mThumb;
	private Drawable mTrack;
	
	public SFScrollbar(Context context) {
		super(context);
	}

	public SFScrollbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setOrientation(int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            invalidate();
        }
    }
	
	public void setTrackLength(int length) {
		if (mTrackLength != length) {
			mTrackLength = length;
			invalidate();
		}
	}
	
	public void setThumbLength(int length) {
		if (mThumbLength != length) {
			mThumbLength = length;
			invalidate();
		}
	}
	
	public void setThumbPosition(int position) {
		if (mThumbPosition != position) {
			mThumbPosition = position;
			invalidate();
		}
	}
	
	public void scrollTo(int position) {
		setThumbPosition(position);
	}
	
	public void scrollBy(int d) {
		setThumbPosition(mThumbPosition + d);
	}
	
	public void setTrack(Drawable track) {
		mTrack = track;
		invalidate();
	}
	
	public void setThumb(Drawable thumb) {
		mThumb = thumb;
		invalidate();
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		if (mTrack != null && mThumb != null && mTrackLength > mThumbLength && mThumbLength > 0) {
			mTrack.setBounds(0, 0, w, h);
			mTrack.draw(canvas);
			
			int left, top, right, bottom;
			
			switch (mOrientation) {
			case HORIZONTAL:
				left = w * mThumbPosition / mTrackLength;
				top = 0;
				right = w * (mThumbPosition + mThumbLength) / mTrackLength;
				bottom = h;
				break;
			case VERTICAL:
			default:
				left = 0;
				top = h * mThumbPosition / mTrackLength;
				right = w;
				bottom = h * (mThumbPosition + mThumbLength) / mTrackLength;
				break;
			}
			
			mThumb.setBounds(left, top, right, bottom);
			mThumb.draw(canvas);
		}
	}

}
