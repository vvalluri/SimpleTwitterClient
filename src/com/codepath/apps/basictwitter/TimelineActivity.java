package com.codepath.apps.basictwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TimelineActivity extends Activity {
	private TwitterClient Client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private int totalFetched = 0;
	private long  lastMaxId = 0;
	private long  firstSinceId = 0;
	private List<Tweet> dbTweets;
    private SwipeRefreshLayout swipeContainer;
    private final int COMPOSE_REQUEST_CODE = 20; // Request for compose result to display in timeline
    private Editor editPref;
    private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		bar.setTitle("Home");
		// Get the shared preferences
		pref =   
			    PreferenceManager.getDefaultSharedPreferences(this);
		editPref = pref.edit();
			
		// Create Client with methods for various APIs (Twitter Rest Client application)
		Client = TwitterClientApp.getRestClient();
		
		// List view and adapter settings
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		lvTweets.setClickable(true);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		setupViews();
        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
        	@Override
        	public void onLoadMore(int page, int totalItemsCount) {
        		// Triggered only when new data needs to be appended to the list
        		// Add whatever code is needed to append new items to your AdapterView
        		if (totalItemsCount > 0) {
        			onScrollpopulateTimeline(totalItemsCount, lastMaxId, 0);
        		}
        	}
        });
        
 
        if (dbTweets != null) {
        	Log.d("debug", "DB Tweets count: " + dbTweets.size());
        }
        
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            	Log.d("debug", "On Swipe to Refresh Tweets Since id: " + firstSinceId);
            	onRefreshpopulateTimeline(0, firstSinceId);
            	//populateTimeline();
            } 
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);
        
     // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
        // Populate account credentials
        populateAccountCred();
        
		// Populate the views on the main screen
		populateTimeline();
	}

	public void setupViews() {
    	lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the image display activity
				Intent i = new Intent(TimelineActivity.this, TweetDisplayActivity.class);
				Tweet displaytweet = tweets.get(position);
				i.putExtra("tweet", displaytweet);
				// Start the new actvitiy
				startActivity(i);			
			}		
		});
    }
	
	public static List<Tweet> getDbTweetAll() {
	    return new Select().from(Tweet.class).execute();
	}
	
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    
	
    protected void onScrollpopulateTimeline(int totalItemsCount, long in_lastMaxId, long in_firstSinceId) {
    	
    	if ((totalItemsCount != 0) && (totalFetched > totalItemsCount)) {
    		Log.d("debug", "Still displaying last fetch items");
    		Log.d("debug", "Last max id: " + lastMaxId + "totalItems: " + totalItemsCount);
    		return;
    	}
    	
    	// Check for Internet connectivity
    	if (isNetworkAvailable() == false) {
    		Toast.makeText(this, "No INTERNET Connectivity", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
		Client.getHomeTimeLineNext(in_lastMaxId, in_firstSinceId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArr) {
				Log.d("debug", "Get Next Test the Twitter Timeline API");
				Log.d("debug", jsonArr.toString());
				JSONObject lastJson = null;
				Tweet lastTweet = null;
				if (jsonArr.length() > 0) {
					// Get the ID for last tweet
					try {
						lastJson = jsonArr.getJSONObject(jsonArr.length() - 1);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					lastTweet = Tweet.fromJSON(lastJson);
					if (lastTweet != null) {
						lastMaxId = lastTweet.getUid();
					}
					totalFetched = totalFetched + jsonArr.length();
					// Add to the listview adpater
					aTweets.addAll(Tweet.fromJSONArray(jsonArr));
				}

			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
			
		});
	}
    
	// Populate timeline
	public void populateTimeline() {
		int dbShowTweets;
    	// Check for Internet connectivity
    	if (isNetworkAvailable() == false) {
    		Toast.makeText(this, "No INTERNET Connectivity", Toast.LENGTH_SHORT).show();
    	    dbTweets = getDbTweetAll();
    	    if (dbTweets.isEmpty()) {
    	    	return;
    	    }
    	    
    	    for (int i = 0; i < dbTweets.size(); i++) {
    	    	if (i > 20) {
    	    		return;
    	    	}
    	    	aTweets.add(dbTweets.get(i));
    	    }
    	    
    		return;
    	}
		Client.getHomeTimeLine(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArr) {
				JSONObject firstJson = null;
				JSONObject lastJson = null;
				Tweet firstTweet = null;
				Tweet lastTweet = null;
				
				Log.d("debug", "Test the Twitter Timeline API");
				Log.d("debug", jsonArr.toString());
				//VV
				aTweets.clear();
				// Add to the listview adpater
				if (jsonArr.length() > 0) {
					// Get the ID for start of Tweets 
					try {
						firstJson = jsonArr.getJSONObject(0);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					firstTweet = Tweet.fromJSON(firstJson);
					if (firstTweet != null) {
						firstSinceId = firstTweet.getUid();
					}
					
					// Get the ID for last tweet
					try {
						lastJson = jsonArr.getJSONObject(jsonArr.length() - 1);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					lastTweet = Tweet.fromJSON(lastJson);
					if (lastTweet != null) {
						lastMaxId = lastTweet.getUid();
					}
					totalFetched = totalFetched + jsonArr.length();
					// Add the tweets to Array and refresh adapter
					aTweets.addAll(Tweet.fromJSONArray(jsonArr));
				}
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
			
		});
	}
	
    protected void onRefreshpopulateTimeline(long in_lastMaxId, long in_firstSinceId) {
    	if (in_firstSinceId == 0) {
    		swipeContainer.setRefreshing(false);
    		return;
    	}
    	// Check for Internet connectivity
    	if (isNetworkAvailable() == false) {
    		Toast.makeText(this, "No INTERNET Connectivity", Toast.LENGTH_SHORT).show();
    		swipeContainer.setRefreshing(false);
    		return;
    	}
    	
		Client.getHomeTimeLineNext(in_lastMaxId, in_firstSinceId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArr) {
				Log.d("debug", "On Refresh latest from the Twitter Timeline API");
				Log.d("debug", jsonArr.toString());
				JSONObject firstJson = null;
				Tweet firstTweet = null;
				int JsonArrLength = 0;
				
				JsonArrLength = jsonArr.length();
				if (JsonArrLength > 0) {
					// Get the ID for start of Tweets 
					try {
						firstJson = jsonArr.getJSONObject(0);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					firstTweet = Tweet.fromJSON(firstJson);
					if (firstTweet != null) {
						firstSinceId = firstTweet.getUid();
					}
					totalFetched = totalFetched + jsonArr.length();
					
					// Add to the listview adpater
					for (int i = 0; i < JsonArrLength; i++) {
						JSONObject tweetJson = null;
						try {
							tweetJson = jsonArr.getJSONObject((JsonArrLength - 1) - i);
						} catch (JSONException e) {
							e.printStackTrace();
							continue;
						}

						Tweet tweet = Tweet.fromJSON(tweetJson);
						if (tweet != null) {
							aTweets.insert(tweet, 0);
						}
						
					}
				}
				swipeContainer.setRefreshing(false);
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
				swipeContainer.setRefreshing(false);
			}
			
		});
	}
    
	// Populate account credentials
	public void populateAccountCred() {
    	// Check for Internet connectivity
    	if (isNetworkAvailable() == false) {
    		Toast.makeText(this, "No INTERNET Connectivity", Toast.LENGTH_SHORT).show();
    		return;
    	}
		Client.getAccountCred(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {

				Log.d("debug", "Test the Twitter Account credentials API");
				Log.d("debug", json.toString());
				// Store in shared preferences

				try {
					editPref.putString("screenName", json.getString("screen_name"));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					editPref.putString("user", json.getString("name"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				try {
					editPref.putString("profileImageUrl", json.getString("profile_image_url"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				editPref.commit();
				Log.d("debug", "VV: Profile Name:" + pref.getString("user", "n/a"));

			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
			
		});
	}
	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	// OnCLick compose action
	public void onComposeAction(MenuItem mi) {
		// Launch the Compose tweet activity
		Intent i = new Intent(TimelineActivity.this, TweetComposeActivity.class);
		// Wait for result code
		startActivityForResult(i, COMPOSE_REQUEST_CODE);
		// Start the new actvitiy
		//startActivity(i);	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent i) {
	  // REQUEST_CODE is defined above
	  if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
		  Log.d("debug", "Recived compose response result ");
	     // Extract name value from result extras
		  if (i.hasExtra("Tweetresult")) {
			  Tweet compTweet = (Tweet)i.getSerializableExtra("Tweetresult");
			  aTweets.insert(compTweet, 0);
		  }
	  }
	} 

}
