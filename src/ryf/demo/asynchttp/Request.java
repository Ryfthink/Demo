package ryf.demo.asynchttp;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

import android.os.Handler;

public abstract class Request<T> implements Runnable {

	private static final int DEFAULT_TIMEOUT_MS = 5000;
	private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";

	public enum Method {
		GET, POST
	}

	public interface Callback<T> {
		public void onResponse(T response);

		public void onErrorResponse(int code, String msg);
	}

	private String path;
	private Method method;
	private Handler handler;
	private Callback<T> callback;

	public Request(String path, Method method, Callback<T> callback) {
		this.path = path;
		this.method = method;
		this.callback = callback;
	}

	public void setCallbackHandler(Handler handler) {
		this.handler = handler;
	}

	public final String getPath() {
		return path;
	}

	public int getTimeoutMs() {
		return DEFAULT_TIMEOUT_MS;
	}

	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	protected Map<String, String> getParams() {
		return null;
	}

	protected String getParamsEncoding() {
		return DEFAULT_PARAMS_ENCODING;
	}

	public String getBodyContentType() {
		return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
	}

	public byte[] getBody() {
		Map<String, String> params = getParams();
		if (params != null && params.size() > 0) {
			return encodeParameters(params, getParamsEncoding());
		}
		return null;
	}

	public InputStream getBodyStream() {
		byte[] body = getBody();
		if (body != null) {
			return new ByteArrayInputStream(body);
		}

		return null;
	}

	protected boolean isSuccess(int responseCode) {
		return responseCode >= 200 && responseCode < 300;
	}

	public void request() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		HttpURLConnection connection = null;
		InputStream body = getBodyStream();

		int responseCode = -1;
		String responseMessage = null;
		T responseContent = null;

		try {
			connection = (HttpURLConnection) new URL(
					"http://tvapi.kdyk.org/v2/search/entries?can_play=1&catalog_range=12%2C14%2C22%2C15%2C20%2C27&platform=android&kw=%E6%95%A2%E6%AD%BB%E9%98%9F&page=1&count=3").openConnection();

			int timeoutMs = getTimeoutMs();
			connection.setConnectTimeout(timeoutMs);
			connection.setReadTimeout(timeoutMs);
			connection.setUseCaches(false);
			connection.setDoInput(true);

			// additional headers
			Map<String, String> headers = getHeaders();
			if (headers != null && !headers.isEmpty()) {
				for (String key : headers.keySet()) {
					connection.addRequestProperty(key, headers.get(key));
				}
			}

			// method
			if (method == null) {
				method = body == null ? Method.GET : Method.POST;
			}
			switch (method) {
				case GET:
					connection.setRequestMethod("GET");
					break;
				case POST:
					connection.setRequestMethod("POST");
					if (body != null) {
						connection.setDoOutput(true);
						connection.addRequestProperty(HEADER_CONTENT_TYPE, getBodyContentType());
						DataOutputStream out = new DataOutputStream(connection.getOutputStream());

						byte[] buffer = new byte[1024];
						for (int l; (l = body.read(buffer)) > 0;) {
							out.write(buffer, 0, l);
						}

						out.close();
					}
					break;
			}

			responseCode = connection.getResponseCode();
			responseMessage = connection.getResponseMessage();
			if (responseCode > 0 && isSuccess(responseCode)) {
				responseContent = response(connection.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response(responseCode, responseMessage, responseContent);
			if (body != null) {
				try {
					body.close();
				} catch (IOException e) {
				}
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	protected abstract T response(InputStream is);

	private void response(final int code, final String msg, final T response) {
		Runnable cb = new Runnable() {

			@Override
			public void run() {
				try {
					if (response != null && callback != null) {
						callback.onResponse(response);
					} else if (callback != null) {
						callback.onErrorResponse(code, msg);
					}
				} catch (Exception e) {
				}
			}

		};

		if (handler != null) {
			handler.post(cb);
		} else {
			cb.run();
		}
	}

	private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString()
					.getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	public static String urlcat(String path, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		if (path != null) {
			sb.append(path);

			int index = path.indexOf("?");
			int length = path.length();

			if (index < 0) {
				sb.append('?');
			} else if (index < length - 1 && path.charAt(length - 1) != '&') {
				sb.append('&');
			} else {
				// nothing
			}
		}

		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey());
			sb.append('=');
			sb.append(entry.getValue());
			sb.append('&');
		}

		return sb.toString();
	}

}
