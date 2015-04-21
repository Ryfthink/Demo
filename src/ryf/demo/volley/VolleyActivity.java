package ryf.demo.volley;

import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import ryf.demo.R;

public class VolleyActivity extends Activity {

	//@formatter:off
	String[] shots  =new String[]{
	"http://img.sfcdn.org/221e1a01567e2729c7fae181b9adc5963a159351.png",
	"http://img.sfcdn.org/9f33c077dc12521f8de7173f9a479c7e918e1fd1.png",
	"http://img.sfcdn.org/8aa99491672c2ed2de2c1c0a41aa115c13002dd2.png",
	"http://img.sfcdn.org/53729be28f5b690f3fe668978c4e7fd85967bf01.png",
	"http://img.sfcdn.org/3abf6bed947bd3c84a18dbc57119e5d5e39bbb08.png", 
	"http://img.sfcdn.org/4cf16aae2cf5e6f86cbfc920b45c4581a75120bb.png" };
	//@formatter:on

	ImageView img;
	RequestQueue requestQueue;
	ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_volley);
		img = (ImageView) findViewById(R.id.img);
		requestQueue = Volley.newRequestQueue(this);
		// requestQueue.start();
		mImageLoader = new ImageLoader(requestQueue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				Log.d("Demo", "ImageCache putBitmap: " + url + "  ,,,  " + bitmap);
			}

			@Override
			public Bitmap getBitmap(String url) {
				Log.d("Demo", "ImageCache getBitmap: " + url );
				// return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				return null;
			}
		});
	}

	// request Image By ImageRequeset
	void requestImg() {

		Listener<Bitmap> listener = new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				img.setImageBitmap(response);
				((TextView) findViewById(R.id.btn)).setText("success");
				Log.d("Demo", "ok");
			}
		};

		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				((TextView) findViewById(R.id.btn)).setText("error try again...");
				Log.d("Demo", "error: " + error.getMessage());
			}
		};
		String url = shots[new Random().nextInt(shots.length)];
		ImageRequest request = new ImageRequest(url, listener, 1280, 720, Config.RGB_565, errorListener);
		requestQueue.add(request);
	}

	// requstImageByImageLoader
	void loadImg() {
		String url = shots[new Random().nextInt(shots.length)];
		ImageContainer newContainer = mImageLoader.get(url, new ImageListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("Demo", "loadImg error: " + error.getMessage());
			}

			@Override
			public void onResponse(final ImageContainer response, boolean isImmediate) {
				// If this was an immediate response that was delivered inside
				// of a layout
				// pass do not set the image immediately as it will trigger a
				// requestLayout
				// inside of a layout. Instead, defer setting the image by
				// posting back to
				// the main thread.
				boolean isInLayoutPass = false;
				if (isImmediate && isInLayoutPass) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							onResponse(response, false);
						}
					});
					return;
				}

				if (response.getBitmap() != null) {
					img.setImageBitmap(response.getBitmap());
					Log.d("Demo", "loadImg ok");
				} else {
					img.setImageDrawable(null);
					Log.d("Demo", "loadImg no bmp");
				}
			}
		}, 1280, 720);

	}

	public void onBtnClick(View view) {
		loadImg();
	}
}
