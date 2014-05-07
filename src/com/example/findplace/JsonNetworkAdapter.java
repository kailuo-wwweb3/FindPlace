package com.example.findplace;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Simple helper class to perform JSON exchanges with the backend. Expects the
 * backend server to always return JSON data. Expects the backend server to
 * always receive JSON data in the POST body.
 * 
 * @author Dave Fisher (fisherds@gmail.com)
 * @author Jimmy Theis (theis.jimmy@gmail.com)
 */
public class JsonNetworkAdapter {

	private static final String TAG = "JsonNetworkAdapter";

	/**
	 * Performs a GET request to a URL. Expects the backend to return a JSON
	 * response.
	 * 
	 * @param url
	 *            The URL to send the POST to.
	 * @return The JSON object response from the backend.
	 */
	public JSONObject getJsonData(String url) {
		Log.d(TAG, "GET request to " + url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			// Execute HTTP "get" request
			String responseBody = httpclient.execute(httpget,
					new BasicResponseHandler());
			Log.d(TAG, "GET response body = " + responseBody);
			return new JSONObject(responseBody);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Error in JSON (Client Protocol): " + e.getMessage());
			return new JSONObject();
		} catch (IOException e) {
			Log.e(TAG, "Error in JSON (IOException): " + e.getMessage());
			return new JSONObject();
		} catch (JSONException e) {
			Log.e(TAG, "Error in JSON (JSONException): " + e.getMessage());
			return new JSONObject();
		}
	}

	/**
	 * Sends a POST to a URL with a JSON object as the POST body. Expects the
	 * backend to return a JSON response.
	 * 
	 * @param url
	 *            The URL to send the POST to.
	 * @param jsonData
	 *            The JSON object to send as the POST body.
	 * @return The JSON object response from the backend.
	 */
	public JSONObject postJsonData(String url, JSONObject jsonData) {
		Log.d(TAG, "POST request to " + url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			// Execute HTTP "post" request
			Log.d(TAG, "POST sending JSON data = " + jsonData.toString());
			StringEntity newMessageStringEntity = new StringEntity(
					jsonData.toString());
			newMessageStringEntity
					.setContentType("application/json;charset=UTF-8");
			httppost.setEntity(newMessageStringEntity);
			String responseBody = httpclient.execute(httppost,
					new BasicResponseHandler());
			Log.d(TAG, "POST response body = " + responseBody);
			return new JSONObject(responseBody);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Error in JSON (Client Protocol): " + e.getMessage());
			return new JSONObject();
		} catch (IOException e) {
			Log.e(TAG, "Error in JSON (IOException): " + e.getMessage());
			return new JSONObject();
		} catch (JSONException e) {
			Log.e(TAG, "Error in JSON (JSONException): " + e.getMessage());
			return new JSONObject();
		}
	}
}
