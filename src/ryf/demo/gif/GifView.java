/**
 * @ GifView.java 2013-8-16
 */

package ryf.demo.gif;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import ryf.demo.R;

/**
 * 播放gif
 * @deprecated google官方未提供API文档，在某些手机上会出现解码失败
 * @author ryf
 * @date 2013-8-16
 */

public class GifView extends View {

	private Movie mMovie;
	private Context mContext;

	public GifView(Context context) {
		super(context);
		this.mContext = context;
	}

	public LayoutParams getLP() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		return lp;
	}

	public boolean init(String gifName) {
		int id = R.raw.breath;
		if (id == 0) {
			return false;
		}
		InputStream is = mContext.getResources().openRawResource(id);
		if (is != null) {
			mMovie = Movie.decodeStream(is);
			if (mMovie != null) {
				return true;
			}
		}
		return false;
	}

	private long mStartTime;// 起止时间
	int mDuration;// gif时长

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mMovie != null) {// 绘制movie每一帧，并刷新
			long now = SystemClock.uptimeMillis();
			if (mStartTime == 0) {
				mStartTime = now;
			}
			mDuration = mMovie.duration();
			int relTime = (int) ((now - mStartTime) % mDuration);
			mMovie.setTime(relTime);
			canvas.save();
			canvas.scale(2.0f, 2.0f, getWidth() / 2, getHeight() / 2);
			mMovie.draw(canvas, (getWidth() - mMovie.width()) / 2, (getHeight() - mMovie.height()) / 2);
			canvas.restore();
			invalidate();
		}
	}

}
