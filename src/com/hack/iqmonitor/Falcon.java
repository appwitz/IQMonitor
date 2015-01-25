package com.hack.iqmonitor;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.Result;

/**
 * Created by rhyeal on 1/24/2015.
 */
public class Falcon extends Activity {
	User user;
	JSONObject json_result;
	private static final String HASH_ALGORITHM = "HmacSHA256";
	private static final String API_BASE = "http://dev.learnwithhiq.com";

	public void onCreate() {
		user = new User();
	}

	public String send(String verb, String endPoint, JSONObject message) {
		String[] options = new String[] { verb, endPoint, message.toString() };
		HttpAsyncTask myHA = new HttpAsyncTask();
		myHA.execute(options);
		try {
			String result = myHA.get();
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static byte[] hashMac(String text, String secretKey)
			throws SignatureException {
		try {
			Key sk = new SecretKeySpec(secretKey.getBytes(), HASH_ALGORITHM);
			Mac mac = Mac.getInstance(sk.getAlgorithm());
			mac.init(sk);
			final byte[] hmac = mac.doFinal(text.getBytes());
			return hmac;
		} catch (NoSuchAlgorithmException e1) {
			throw new SignatureException("error in sig, no such algo on device"
					+ HASH_ALGORITHM);
		} catch (InvalidKeyException e) {
			throw new SignatureException("error building sig, invalid key"
					+ HASH_ALGORITHM);
		}
	}

	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		Formatter formatter = new Formatter(sb);
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		return sb.toString();
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... options) {
			String verb = options[0];
			String endPoint = options[1];
			JSONObject message = null;
			try {
				message = new JSONObject(options[2]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String result = "false";
			try {
				result = POST(verb, endPoint, message, user);
			} catch (SignatureException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return result;
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// do success stuff here
			Log.d("result", result);
			

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			this.cancel(true);
		}
	}

	public String POST(String verb, String endPoint, JSONObject message,
			User user) throws ClientProtocolException, IOException,
			SignatureException {
		InputStream inputStream = null;
		String result = "";
		String url = API_BASE + endPoint;
		user = new User();
		// try {

		// 1. create HttpClient
		HttpClient httpclient = new DefaultHttpClient();

		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(url);

		// 4. convert JSONObject to JSON to String
		String json = message.toString();

		// ** Alternative way to convert Person object to JSON string usin
		// Jackson Lib
		// ObjectMapper mapper = new ObjectMapper();
		// json = mapper.writeValueAsString(person);

		// 5. set json to StringEntity
		StringEntity se = new StringEntity(json);

		// 6. set httpPost Entity
		httpPost.setEntity(se);

		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSZZ", Locale.US);
		String ftime = sdf.format(new Date());
		Log.d("time", ftime.toString());
		Log.d("message", message.toString());
		Log.d("endpoint", endPoint);
		Log.d("verb", verb);
		Log.d("user", user.getLoginId());
		Log.d("secret", user.getSecret());

		byte[] hashed = hashMac(
				verb + "\n" + endPoint + "\n" + MD5(message.toString()) + "\n"
						+ ftime, user.getSecret());
		String signature = URLEncoder.encode(
				Base64.encodeToString(hashed, Base64.DEFAULT).trim(), "UTF-8");
		Log.d("sig", signature);

		// 7. Set some headers to inform server about the type of the content
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Authorization",
				"FALCON FALCON_USER=" + user.getLoginId()
						+ ";FALCON_SIGNATURE=" + signature + ";FALCON_TIME="
						+ ftime);

		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = httpclient.execute(httpPost);

		// 9. receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// 10. convert inputstream to string
		if (inputStream != null)
			result = convertInputStreamToString(inputStream);
		else
			result = "Did not work!";

		Log.d("result", result.toString());
		// } catch (Exception e) {
		// Log.d("InputStream", e.getLocalizedMessage());
		// }

		// 11. return result
		return result;
	}
}