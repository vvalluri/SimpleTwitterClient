package com.codepath.apps.basictwitter;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetReplyActivity extends ActionBarActivity {
	private EditText etTweetReply;
	private TwitterClient Client;
	private long replyId;
	private Tweet tweet;
	private String userid;
	private List<User> Userlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Progress bar support
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_tweet_reply);
		// Custom action bar
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		bar.setTitle(" ");
		
		etTweetReply = (EditText) findViewById(R.id.etReplyMsg);
		/* 
	    // Get the tweet object
		tweet = (Tweet)getIntent().getParcelableExtra("tweet");
		*/
		// Get the User from database
		tweet = new Tweet();
		userid = (String)getIntent().getStringExtra("user");
		replyId = Long.parseLong(userid);
		Log.d("debug", "VV: Tweet reply id: " + replyId + "String userid: " + userid);
		Userlist = User.getUserbyUid(replyId);
		if (Userlist.size() > 0) {
			tweet.user = Userlist.get(0);
		} else {
			Log.d("debug", "VV: Tweet reply could not find User");
		}
		
		
		// Create Client with methods for various APIs (Twitter Rest Client application)
		Client = TwitterClientApp.getRestClient();
		populateTweetDisplay();
	}

	private void populateTweetDisplay() {
	       // Lookup view for data population
	       ImageView ivTweetProfileImage = (ImageView) findViewById(R.id.ivTweetReplyProfileImage);
	       TextView tvTweetUserName = (TextView) findViewById(R.id.tvTweetReplyUserName);
	       TextView tvTweetUser = (TextView) findViewById(R.id.tvTweetReplyUser);
	       
	       etTweetReply.setText("@" + tweet.getUser().getScreenName() + " ");
			int text_length = etTweetReply.length();
			etTweetReply.setSelection(text_length);
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
		if (id == R.id.miSendTweetReply) {
			onSendTweetReply(item);
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
