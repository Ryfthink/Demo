package ryf.demo.gif;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import ryf.demo.R;
import ryf.demo.gifview.GifView;

public class MoiveActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ryf.demo.gifview.GifView gifView = new GifView(this);
		gifView.setGifImage(R.raw.breath);
		int width = 420 * 4;
		int height = 218 * 4;
		gifView.setShowDimension(width, height);
		setContentView(gifView);
	}

	private class MovieView extends View {
		private Movie mMovie;
		private long mMovieStart;

		public MovieView(Context context) {
			super(context);
			InputStream is = context.getResources().openRawResource(R.drawable.animated_gif);
			byte[] array = streamToBytes(is);
			mMovie = Movie.decodeByteArray(array, 0, array.length);
		}

		private byte[] streamToBytes(InputStream is) {
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int len;
			try {
				while ((len = is.read(buffer)) >= 0) {
					os.write(buffer, 0, len);
				}
			} catch (java.io.IOException e) {
			}
			return os.toByteArray();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			// mMovie.draw(canvas, 0, 0);
			long now = android.os.SystemClock.uptimeMillis();
			if (mMovieStart == 0) { // first time
				mMovieStart = now;
			}
			if (mMovie != null) {
				int dur = mMovie.duration();
				if (dur == 0) {
					dur = 1000;
				}
				int relTime = (int) ((now - mMovieStart) % dur);
				mMovie.setTime(relTime);
				mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
				invalidate();
			}
		}
	}

	private static class SampleView extends View {
		private Bitmap mBitmap;
		private Bitmap mBitmap2;
		private Bitmap mBitmap3;
		private Bitmap mBitmap4;
		private Drawable mDrawable;

		private Movie mMovie;
		private long mMovieStart;

		// Set to false to use decodeByteArray
		private static final boolean DECODE_STREAM = true;

		private static byte[] streamToBytes(InputStream is) {
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int len;
			try {
				while ((len = is.read(buffer)) >= 0) {
					os.write(buffer, 0, len);
				}
			} catch (java.io.IOException e) {
			}
			return os.toByteArray();
		}

		public SampleView(Context context) {
			super(context);
			setFocusable(true);

			java.io.InputStream is;
			is = context.getResources().openRawResource(R.drawable.ic_launcher);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			Bitmap bm;

			opts.inJustDecodeBounds = true;
			bm = BitmapFactory.decodeStream(is, null, opts);

			// now opts.outWidth and opts.outHeight are the dimension of the
			// bitmap, even though bm is null

			opts.inJustDecodeBounds = false; // this will request the bm
			opts.inSampleSize = 4; // scaled down by 4
			bm = BitmapFactory.decodeStream(is);

			mBitmap = bm;

			// decode an image with transparency
			is = context.getResources().openRawResource(R.drawable.ic_launcher);
			mBitmap2 = BitmapFactory.decodeStream(is);

			// create a deep copy of it using getPixels() into different configs
			int w = mBitmap2.getWidth();
			int h = mBitmap2.getHeight();
			int[] pixels = new int[w * h];
			mBitmap2.getPixels(pixels, 0, w, 0, 0, w, h);
			mBitmap3 = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);
			mBitmap4 = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_4444);

			mDrawable = context.getResources().getDrawable(R.drawable.ic_launcher);
			mDrawable.setBounds(150, 20, 300, 100);

			is = context.getResources().openRawResource(R.drawable.animated_gif);

			if (DECODE_STREAM) {
				mMovie = Movie.decodeStream(is);
			} else {
				byte[] array = streamToBytes(is);
				mMovie = Movie.decodeByteArray(array, 0, array.length);
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFFCCCCCC);

			Paint p = new Paint();
			p.setAntiAlias(true);

			// canvas.drawBitmap(mBitmap, 10, 10, null);
			canvas.drawBitmap(mBitmap2, 10, 170, null);
			canvas.drawBitmap(mBitmap3, 110, 170, null);
			canvas.drawBitmap(mBitmap4, 210, 170, null);

			mDrawable.draw(canvas);

			long now = android.os.SystemClock.uptimeMillis();
			if (mMovieStart == 0) { // first time
				mMovieStart = now;
			}
			if (mMovie != null) {
				int dur = mMovie.duration();
				if (dur == 0) {
					dur = 1000;
				}
				int relTime = (int) ((now - mMovieStart) % dur);
				mMovie.setTime(relTime);
				mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
				invalidate();
			}
		}
	}

}
