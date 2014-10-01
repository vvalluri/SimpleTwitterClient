package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetComposeActivity extends Activity {
	private EditText etTweet;
	private TextView tvDisplay;
	private TwitterClient Client;
	//static Tweet tweetComposeReturn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_compose);
		// Custom action bar
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		bar.setTitle(" ");
		
		tvDisplay = (TextView) findViewById(R.id.tvDisplayLimit);
		etTweet = (EditText) findViewById(R.id.etComposeTweet);
		etTweet.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Fires right as the text is being changed (even supplies the range of text)
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Fires right before text is changing
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// Fires right after the text has changed
				tvDisplay.setText(String.valueOf(140-s.length()) + " characters left");
			}
		});
		
		// Create Client with methods for various APIs (Twitter Rest Client application)
		Client = TwitterClientApp.getRestClient();
		populateTweetDisplay();
	}

	private void populateTweetDisplay() {
		 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
	       // Lookup view for data population
	       ImageView ivTweetProfileImage = (ImageView) findViewById(R.id.ivTweetCompProfileImage);
	       TextView tvTweetUserName = (TextView) findViewById(R.id.tvTweetCompUserName);
	       TextView tvTweetUser = (TextView) findViewById(R.id.tvTweetCompUser);
	       
	       // Populate the data into the template view using the data object
	       tvTweetUserName.setText("@" + pref.getString("screenName", " "));
	       tvTweetUser.setText(pref.getString("user", " "));
	       // populate and display image
	       ivTweetProfileImage.setImageResource(android.R.color.transparent);
	       ImageLoader imageLoader = ImageLoader.getInstance();
	       imageLoader.displayImage(pref.getString("profileImageUrl", "n/a"), ivTweetProfileImage);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_compose, menu);
		return true;
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
	
	// OnCLick compose action
	public void onTweetAction(MenuItem mi) {
		String tweetstr;
		
		// Launch the Compose tweet activity
		tweetstr = etTweet.getText().toString();
		
		// Send the Tweet Status
		if ((tweetstr != null) && !tweetstr.isEmpty()){
			// POST new tweet to twitter
			Client.postStatusUpdate(tweetstr,new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					Tweet tweetComposeReturn;
					Log.d("debug", "Posted tweet to the Twitter Statuses API");
					Log.d("debug", json.toString());
					
					// Add the Tweet info
					tweetComposeReturn = Tweet.fromJSON(json);
					Log.d("debug", "VV: Received Tweet composed response");
					// Notify timeline activity about the new Tweet
					Intent i = new Intent(TweetComposeActivity.this, TimelineActivity.class);
					i.putExtra("Tweetresult", tweetComposeReturn);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// Activity finished ok, return the data
					setResult(RESULT_OK, i); // set result code and bundle data for response
					finish(); 
				}
				@Override
				public void onFailure(Throwable e, String s) {
					Log.d("debug", e.toString());
					Log.d("debug", s.toString());
				}
				
			});
		} else {
			Log.d("debug", "Tweet String is NULL ");
		}
		
	}
}
