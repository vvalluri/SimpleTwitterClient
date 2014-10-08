package com.codepath.apps.basictwitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends ActionBarActivity {
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Progress bar support
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
		setContentView(R.layout.activity_profile);
		username = (String)getIntent().getStringExtra("user");
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Profile");
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		// Load fragment dynamically
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		UserTimelineFragment Userfragment = UserTimelineFragment.newInstance(username);
		ft.replace(R.id.fragmentUserTimeline, Userfragment);
		ft.commit();

		showProgressBar();
		loadUserInfo();
	}

	private void loadUserInfo() {
		TwitterClientApp.getRestClient().getUserInfo(username,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonArr) {
						if (jsonArr.length() > 0) {
							JSONObject json;
							try {
								json = jsonArr.getJSONObject(0);
								User u = User.fromJSON(json);
								populateProfileHeader(u);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						hideProgressBar();
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						Log.d("debug", s.toString());
						hideProgressBar();
					}
					
				});
	}
	

	private void populateProfileHeader(User user) {

		TextView tvName = (TextView) findViewById(R.id.tvProfileName);
		TextView tvScreenName = (TextView) findViewById(R.id.tvProfileScreenName);
		TextView tvTagline = (TextView) findViewById(R.id.tvProfileTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		TextView tvStatuses = (TextView) findViewById(R.id.tvStatuses);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage2);
		
	    tvName.setText(user.getName());
	    // Modified to match twitter profile UI
	    tvScreenName.setText("@" + user.getScreenName());
	     String tagLine;
	     if (user.getDescription().length() > 27) {
	    	 tagLine = user.getDescription().substring(0, 27)+ "...";
	     } else {
	    	 tagLine = user.getDescription();
	    }
	    tvTagline.setText(tagLine);


	    tvFollowers.setText(Html.fromHtml("<font color=\"#206199\"><b>" + Integer.toString(user.getFollowersCnt())
                + "<br>" + "</b></font>" + "<font color=\"#206199\">" + "FOLLOWERS"+ "</font>"));
	    
	    tvFollowing.setText(Html.fromHtml("<font color=\"#206199\"><b>" + Integer.toString(user.getFollowingCnt())
                + "<br>" + "</b></font>" + "<font color=\"#206199\">" + "FOLLOWING"+ "</font>"));
	    
	    tvStatuses.setText(Html.fromHtml("<font color=\"#206199\"><b>" + Integer.toString(user.getStatusesCnt())
                + "<br>" + "</b></font>" + "<font color=\"#206199\">" + "TWEETS"+ "</font>"));
	    
	    ImageLoader imageLoader = ImageLoader.getInstance();
	    imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
		
		
	}
	
	public void showProgressBar() {
	    setProgressBarIndeterminateVisibility(true); 
	}
	   
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false); 
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
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
}
