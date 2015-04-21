package ryf.demo.asynchttp;

import java.io.IOException;
import java.io.InputStream;

public class StringRequest extends Request<String> {

	public StringRequest(String path, Callback<String> callback) {
		super(path, null, callback);
	}

	@Override
	protected String response(InputStream is) {
		StringBuilder sb = new StringBuilder();
		byte[] buff = new byte[1024];

		try {
			for (int l; (l = is.read(buff)) >= 0;) {
				sb.append(new String(buff, 0, l));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		StringRequest request = new StringRequest("http://tvkoudai.com/api/profiles_data",
				new Request.Callback<String>() {

					@Override
					public void onResponse(String response) {
						System.out.println(response);
					}

					@Override
					public void onErrorResponse(int code, String msg) {
						System.out.println(msg);
					}
				});
	}
}
