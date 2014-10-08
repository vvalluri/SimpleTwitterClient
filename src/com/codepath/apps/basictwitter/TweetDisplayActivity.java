package com.codepath.apps.basictwitter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

public class TweetDisplayActivity extends ActionBarActivity {
	private Tweet tweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Progress bar support
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_tweet_display);
		
		// Custom action bar
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		bar.setTitle("Tweet");
		
        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        
	    // Get the tweet object
		tweet = (Tweet)getIntent().getParcelableExtra("tweet");
		populateTweetDisplay();
	}

	private void populateTweetDisplay() {
	       // Lookup view for data population
	       ImageView ivTweetProfileImage = (ImageView) findViewById(R.id.ivTweetProfileImage);
	       TextView tvTweetUserName = (TextView) findViewById(R.id.tvTweetUserName);
	       TextView tvTweetBody = (TextView) findViewById(R.id.tvTweetBody);
	       TextView tvTweetUser = (TextView) findViewById(R.id.tvTweetDisplayUser);
	       ImageView ivTweetPhoto = (ImageView) findViewById(R.id.ivTweetPhoto);
	       TextView tvTweetreply = (TextView) findViewById(R.id.tvDisplayReply);
	       
	       // Populate the data into the template view using the data object
	       tvTweetUserName.setText("@" + tweet.getUser().getScreenName());
	       tvTweetUser.setText(tweet.getUser().getName());
	       tvTweetBody.setText(tweet.getBody());
	       
	       // Reply handling
	       tvTweetreply.setClickable(true);
	       tvTweetreply.setTag(tweet.getUser().getUid());
			// Set profile image on click listener
	       tvTweetreply.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	TextView tweetReply = (TextView) v.findViewById(R.id.tvDisplayReply);
		        	if (tweetReply != null) {
			        	//String user = (String)v.getTag();
		        		String user = tweetReply.getTag().toString();
		        		Log.d("debug", "Display view, Tweet reply tag: " + v.getTag() + " User: " + user);
			        	//Intent intent = new Intent().setClass(v.getContext(), TweetReplyActivity.class);
		        		Intent intent = new Intent(TweetDisplayActivity.this, TweetReplyActivity.class);
			    		intent.putExtra("user", user);
			    		// Start the new activity
			    		v.getContext().startActivity(intent);
		        	} else {
		        		Log.d("debug", "Display click, profile image not found");
		        	}
		        }
		      });
			
	       // populate and display image
	       ivTweetProfileImage.setImageResource(android.R.color.transparent);
	       ImageLoader imageLoader = ImageLoader.getInstance();
	       imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivTweetProfileImage);
	       
	       if (tweet.mediaUrl != null) {
	    	   ivTweetPhoto.setImageResource(0);
	    	   Picasso.with(this).load(tweet.mediaUrl).fit().centerInside().into(ivTweetPhoto);
	       } else {
	    	   ivTweetPhoto.setVisibility(View.GONE);
	       }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.miReplyTweet) {
			//onTweetReply();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// OnCLick compose action
	public void onTweetReply() {
		// Launch the Compose tweet activity
		Intent i = new Intent(TweetDisplayActivity.this, TweetReplyActivity.class);
		i.putExtra("tweet", tweet);
		// Start the new actvitiy
		startActivity(i);	
	}
}
