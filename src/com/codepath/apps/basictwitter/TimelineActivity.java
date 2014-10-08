package com.codepath.apps.basictwitter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.Window;

import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.listeners.SupportFragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TimelineActivity extends ActionBarActivity {

	 // Request for compose result to display in timeline
    private final int COMPOSE_REQUEST_CODE = 20;
    private final int PROFILE_REQUEST_CODE = 27;
    private final int SEARCH_REQUEST_CODE = 36;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Progress bar support
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_timeline);
		
		//Action bar setting for Home timeline
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		bar.setTitle("");
		
		// Set up tabs
		setupTabs();
		
        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);

	}

	private void setupTabs() {
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		android.support.v7.app.ActionBar.Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new SupportFragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "Hometab",
								HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		android.support.v7.app.ActionBar.Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new SupportFragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "Mentionstab",
								MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}

	public void showProgressBar() {
	    setProgressBarIndeterminateVisibility(true); 
	}
	   
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false); 
    }
    
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.miSearch));
        if (searchView != null) {
        	searchView.setOnQueryTextListener(mOnQueryTextListener);
        }
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case R.id.miCompose:
            	onComposeAction(item);
                return true;
            case R.id.miProfile:
            	onProfileView(item);
                return true;
                
            case R.id.miSearch:
            	//onSearchView(item);
                return true;    
        }
        return false;
    }

	// OnCLick compose action
	public void onComposeAction(MenuItem mi) {
		// Launch the Compose tweet activity
		Intent i = new Intent(TimelineActivity.this, TweetComposeActivity.class);
		// Wait for result code
		startActivityForResult(i, COMPOSE_REQUEST_CODE);
	}
	
	// OnCLick Profile action
	public void onProfileView(MenuItem mi) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		// Launch the Profile activity
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("user", pref.getString("screenName", " "));
		// Wait for result code
		startActivityForResult(i, PROFILE_REQUEST_CODE);
	}
	
	// OnCLick Search action
	 private final SearchView.OnQueryTextListener mOnQueryTextListener =
	            new SearchView.OnQueryTextListener() {
		       		@Override
	        		public boolean onQueryTextSubmit(String query) {
	        			// Launch the Search activity
		       			Log.d("debug", "Search query:" + query);
	        			Intent i = new Intent(getApplicationContext(), SearchActivity.class);
	        			i.putExtra("search", query);
	        			// Start activity
	        			startActivity(i);
	        			return true;
	        		}
	
	        		@Override
	        		public boolean onQueryTextChange(String newText) {
	        			return false;
	        		} 
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
		  Log.d("debug", "Recived compose response result ");
	     // Extract name value from result extras
		  if (i.hasExtra("Tweetresult")) {
			  Tweet compTweet = (Tweet)i.getParcelableExtra("Tweetresult");
			  // Get the tweets adapter from fragment using fragment tag
			  HomeTimelineFragment homeFragment =  (HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag("Hometab");
		      if (homeFragment != null) {
		    	  homeFragment.getAdapter().insert(compTweet, 0);
		      }
		  }
	  }
	} 

}
