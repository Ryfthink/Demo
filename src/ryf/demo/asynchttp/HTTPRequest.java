package ryf.demo.asynchttp;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HTTPRequest {

	private String errorMessage; // 錯誤信息

	/**
	 * HTTP請求字符串資源
	 * 
	 * @param url
	 *            URL地址
	 * @return 字符串資源
	 * */
	public String httpRequestString(String url) {
		String result = null;
		try {
			HttpEntity httpEntity = httpRequest(url);
			if (httpEntity != null) {
				result = EntityUtils.toString(httpEntity, "urf-8"); // 使用UTF-8編碼
			}
		} catch (IOException e) {
			errorMessage = e.getMessage();
		}
		return result;
	}

	/**
	 * HTTP請求字節數組資源
	 * 
	 * @param url
	 *            URL地址
	 * @return 字節數組資源
	 * */
	public byte[] httpRequestByteArray(String url) {
		byte[] result = null;
		try {
			HttpEntity httpEntity = httpRequest(url);
			if (httpEntity != null) {
				result = EntityUtils.toByteArray(httpEntity);
			}
		} catch (IOException e) {
			errorMessage = e.getMessage();
		}
		return result;
	}

	/**
	 * 使用HTTP GET方式請求
	 * 
	 * @param url
	 *            URL地址
	 * @return HttpEntiry對象
	 * */
	private HttpEntity httpRequest(String url) {
		HttpEntity result = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			httpResponse = httpClient.execute(httpGet);
			int httpStatusCode = httpResponse.getStatusLine()
					.getStatusCode();
			/*
			 * 判斷HTTP狀態碼是否為200
			 */
			if (httpStatusCode == HttpStatus.SC_OK) {
				result = httpResponse.getEntity();
			} else {
				errorMessage = "HTTP: " + httpStatusCode;
			}
		} catch (ClientProtocolException e) {
			errorMessage = e.getMessage();
		} catch (IOException e) {
			errorMessage = e.getMessage();
		}
		return result;
	}

	/**
	 * 返回錯誤消息
	 * 
	 * @return 錯誤信息
	 * */
	public String getErrorMessage() {
		return this.errorMessage;
	}

}
