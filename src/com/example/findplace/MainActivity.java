package com.example.findplace;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends Activity {
	private static final int SPEECH_REQUEST = 0;
	private CardScrollView mCardScrollView;
	private VenueCardScrollViewAdapter mAdapter;
	private List<Card> venueCards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		this.venueCards = new ArrayList<Card>();
		mCardScrollView = new CardScrollView(this);
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
		mAdapter = new VenueCardScrollViewAdapter();
		mCardScrollView.setAdapter(mAdapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);
		Log.e("bb", "getting here");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void updateCardScrollView(List<Venue> result) {
		// TODO Auto-generated method stub
		this.venueCards.clear();
		for (Venue venue : result) {
			Card newCard = new Card(this);
			newCard.setText(venue.getName());
			this.venueCards.add(newCard);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			spokenText = spokenText.split(" ")[1];
			String url = "https://api.foursquare.com/v2/venues/search"
					+ "?client_id=ORXSWCXK1RFANFOWLDEFGGIB0ZERUON4E3UVHB2N4FMLDVIQ"
					+ "&client_secret=S3NG3EADI320BITTFBSLYNSD5C4JEZLLOUWRJG5RN2EHXLMR"
					+ "&v=20140504" + "&near=" + "seattle" + "&query="
					+ spokenText;
			new LoadVenueResultsTask().execute(url);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private class VenueCardScrollViewAdapter extends CardScrollAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return venueCards.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return venueCards.get(arg0);
		}

		@Override
		public int getPosition(Object arg0) {
			// TODO Auto-generated method stub
			return venueCards.indexOf(arg0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return venueCards.get(position).getView(convertView, parent);
		}

	}

	private class LoadVenueResultsTask extends
			AsyncTask<String, Integer, List<Venue>> {

		@Override
		protected List<Venue> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			JsonNetworkAdapter jsonNetworkAdapter = new JsonNetworkAdapter();
			JSONObject response = jsonNetworkAdapter.getJsonData(url);
			try {
				JSONArray venueArray = ((JSONObject)response.get("response")).getJSONArray("venues");
				ArrayList<Venue> venues = new ArrayList<Venue>();
				for (int i = 0; i < venueArray.length(); i++) {
					JSONObject venueObject = venueArray.getJSONObject(i);
					venues.add(new Venue(venueObject.getString("id"),
							venueObject.getString("name"), null));
				}
				return venues;

			} catch (JSONException e) {
				Log.e("error", "no such key");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Venue> result) {
			updateCardScrollView(result);
		}

	}

}
