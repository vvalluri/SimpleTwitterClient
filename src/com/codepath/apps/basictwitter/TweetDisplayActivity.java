package com.codepath.apps.basictwitter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class TweetDisplayActivity extends Activity {
	private Tweet tweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_display);
		
		// Custom action bar
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		bar.setTitle(" ");
		
	    // Get the tweet object
	    //tweet = (Tweet)getIntent().getSerializableExtra("tweet");
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
	       
	       // Populate the data into the template view using the data object
	       tvTweetUserName.setText("@" + tweet.getUser().getScreenName());
	       tvTweetUser.setText(tweet.getUser().getName());
	       tvTweetBody.setText(tweet.getBody());
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// OnCLick compose action
	public void onTweetReply(MenuItem mi) {
		// Launch the Compose tweet activity
		Intent i = new Intent(TweetDisplayActivity.this, TweetReplyActivity.class);
		i.putExtra("tweet", tweet);
		// Start the new actvitiy
		startActivity(i);	
	}
}
