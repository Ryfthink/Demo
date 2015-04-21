package ryf.demo.livewallpaper;

import java.io.IOException;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * <a href=
 * "http://code.tutsplus.com/tutorials/create-a-live-wallpaper-on-android-using-an-animated-gif--cms-23088"
 * >源码链接</a>
 * 
 * @author Seven
 */
public class GifWallpaperService extends WallpaperService {
	@Override
	public Engine onCreateEngine() {
		try {
			Movie movie = Movie.decodeStream(getResources().getAssets().open("breath.gif"));

			return new GifWallpaperEngine(movie);
		} catch (IOException e) {
			Log.d("GIF", "Could not load asset");
			return null;
		}
	}

	public class GifWallpaperEngine extends Engine {
		/**
		 * This integer represents the delay between re-draw operations. A value
		 * of 20 gives you 50 frames per second.
		 */
		private final int frameDuration = 15;
		/**
		 * This boolean lets the engine know if the live wallpaper is currently
		 * visible on the screen. This is important, because we should not be
		 * drawing the wallpaper when it isn't visible.
		 */
		private SurfaceHolder holder;
		/** This is the animated GIF in the form of a Movie object. */
		private Movie movie;
		/**
		 * This refers to the SurfaceHolder object available to the engine. It
		 * has to be initialized by overriding the onCreate method.
		 */
		private boolean visible;
		/**
		 * This is a Handler object that will be used to start a Runnable that
		 * is responsible for actually drawing the wallpaper.
		 */
		private Handler handler;

		public GifWallpaperEngine(Movie movie) {
			this.movie = movie;
			handler = new Handler();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			this.holder = surfaceHolder;
		}

		private Runnable drawGIF = new Runnable() {
			public void run() {
				draw();
			}
		};

		private void draw() {
			if (visible) {
				Canvas canvas = holder.lockCanvas();
				canvas.save();
				// Adjust size and position so that
				// the image looks good on your screen
				// canvas.scale(3f, 3f);
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 10; j++) {
						movie.draw(canvas, i * movie.width(), j * movie.height());
					}
				}
				canvas.restore();
				holder.unlockCanvasAndPost(canvas);
				movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

				handler.removeCallbacks(drawGIF);
				handler.postDelayed(drawGIF, frameDuration);
			}
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
				handler.post(drawGIF);
			} else {
				handler.removeCallbacks(drawGIF);
			}
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			handler.removeCallbacks(drawGIF);
		}
	}
}
