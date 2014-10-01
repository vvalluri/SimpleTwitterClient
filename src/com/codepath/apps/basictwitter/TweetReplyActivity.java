package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetReplyActivity extends Activity {
	private EditText etTweetReply;
	private TwitterClient Client;
	private long reply_id;
	private Tweet tweet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_reply);
		// Custom action bar
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		bar.setTitle(" ");
		
		etTweetReply = (EditText) findViewById(R.id.etReplyMsg);
	    // Get the tweet object
	    //tweet = (Tweet)getIntent().getSerializableExtra("tweet");
		tweet = (Tweet)getIntent().getParcelableExtra("tweet");
		// Create Client with methods for various APIs (Twitter Rest Client application)
		Client = TwitterClientApp.getRestClient();
		populateTweetDisplay();
	}

	private void populateTweetDisplay() {
	       // Lookup view for data population
	       ImageView ivTweetProfileImage = (ImageView) findViewById(R.id.ivTweetReplyProfileImage);
	       TextView tvTweetUserName = (TextView) findViewById(R.id.tvTweetReplyUserName);
	       TextView tvTweetUser = (TextView) findViewById(R.id.tvTweetReplyUser);
	       
	       // Populate the data into the template view using the data object
	       tvTweetUserName.setText("@" + tweet.getUser().getScreenName());
	       tvTweetUser.setText(tweet.getUser().getName());
	       // populate and display image
	       ivTweetProfileImage.setImageResource(android.R.color.transparent);
	       ImageLoader imageLoader = ImageLoader.getInstance();
	       imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivTweetProfileImage);
	       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_reply, menu);
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
	
	// OnCLick Reply action
	public void onSendTweetReply(MenuItem mi) {
		String tweetstr;

		// Launch the Compose tweet activity
		tweetstr = etTweetReply.getText().toString();
		// Send the Tweet Status
		if ((tweetstr != null) && !tweetstr.isEmpty()){
			// POST new tweet to twitter
			Client.postStatusReply(tweet.getUid(), tweetstr,new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject json) {
					Log.d("debug", "Posted tweet to the Twitter Statuses API");
					Log.d("debug", json.toString());
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
		
		
		// Notify timeline activity about the new Tweet
		Intent i = new Intent(TweetReplyActivity.this, TimelineActivity.class);
		//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Start the new actvitiy
		startActivity(i);	
	}
}
