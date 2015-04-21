package ryf.demo.asynchttp;

import org.apache.http.Header;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

public class MainAcitivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll = new LinearLayout(this);
		Button button = new Button(this);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SessionTask().execute();
			}
		});
		button.setText("TV端代码");
		ll.addView(button);

		Button button2 = new Button(this);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SessionTask2().execute();
			}
		});
		button2.setText("自定义请求");
		ll.addView(button2);

		Button button3 = new Button(this);
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				get();
			}
		});
		button3.setText("开源API");
		ll.addView(button3);
		setContentView(ll);;
	}

	String url = "http://tvapi.kdyk.org/v2/search/entries?can_play=1&catalog_range=12%2C14%2C22%2C15%2C20%2C27&platform=android&kw=%E6%95%A2%E6%AD%BB%E9%98%9F&page=1&count=10";

	void get() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(MainAcitivity.this, url, new BaseJsonHttpResponseHandler<Object>() {

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData,
					Object errorResponse) {
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
				Log.e("result","-----------"+rawJsonResponse.length());
				Log.e("result", "onSuccess\n" + rawJsonResponse);
			}

			@Override
			protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
				return null;
			}

		});
	}

	class SessionTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// String result = new
			// HTTPRequest().httpRequestString("http://tvkoudai.com/api/profiles_data");
			// Log.e("result", "result\n" + result);
			//

			StringRequest request = new StringRequest(url, new Request.Callback<String>() {

				@Override
				public void onResponse(String response) {
					Log.e("result","-----------"+response.length());
					Log.e("result", "onResponse\n" + response);
				}

				@Override
				public void onErrorResponse(int code, String msg) {
					Log.e("result", "onErrorResponse\n" + msg);
				}
			});
			request.request();

			return null;
		}

	}

	class SessionTask2 extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String result = new HTTPRequest().httpRequestString(url);
			Log.e("result","-----------"+result.length());
			Log.e("result", "result\n" + result);
			return null;
		}

	}
}
