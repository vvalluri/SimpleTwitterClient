package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterClientApp;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends TweetListFragment {
	private TwitterClient Client;
	private int totalFetched = 0;
	private long  lastMaxId = 0;
	private long  firstSinceId = 0;
	private boolean onScroll = false;
	private boolean onRefresh = false;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create Client with methods for various APIs (Twitter Rest Client application)
		Client = TwitterClientApp.getRestClient();
		// Populate the views on the main screen
		onScroll = false;
		onRefresh = false;
		populateMentionsTimeline(0);
	}
	
    
    @Override
	public void onRefreshPopulateTimeline () {
		onScroll = false;
		onRefresh = true;
    	populateMentionsTimeline(0);
		return;
	}
	
    @Override
	public void onScrollPopulateTimeline (int count) {
		onScroll = true;
		onRefresh = false;
    	populateMentionsTimeline(count);
		return;
	}

    protected void populateMentionsTimeline(int totalItemsCount) {
    	long local_maxid = lastMaxId;
    	long local_sinceid = firstSinceId;
    	
    	// Check for Internet connectivity
    	if (isNetworkAvailable() == false) {
    		Toast.makeText(getActivity(), "No INTERNET Connectivity", Toast.LENGTH_SHORT).show();
    		if (onRefresh) {
    			swipeContainer.setRefreshing(false);
    			return;
    		}
    		return;
    	}
    	
    	// Check for boundary conditions for onScroll and onRefresh
    	if (onScroll) {
    		Log.d("debug", "Mentions On scroll populate method invoked");
	    	if ((totalItemsCount != 0) && (totalFetched > totalItemsCount)) {
	    		Log.d("debug", "Still displaying last fetch items");
	    		Log.d("debug", "Last max id: " + lastMaxId + "totalItems: " + totalItemsCount);
	    		return;
	    	}
	    	local_maxid = local_maxid - 1;
	    	local_sinceid = 0;
    	}
    	if (onRefresh) {
    		Log.d("debug", "Mentions On refresh populate method invoked");
	    	if (local_sinceid == 0) {
	    		Log.d("debug", "On refresh populate method SINCEID is 0");
	    		swipeContainer.setRefreshing(false);
	    		return;
	    	}
	    	local_maxid = 0;
    	}

		// Get the Home time line messages
		Client.getMentionsLineNext(local_maxid, local_sinceid, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonArr) {
				Log.d("debug", jsonArr.toString());
				JSONObject firstJson = null;
				Tweet firstTweet = null;
				JSONObject lastJson = null;
				Tweet lastTweet = null;
				int JsonArrLength = 0;
				/*
				if ((onRefresh == false) && (onScroll == false) ) {
					aTweets.clear();
				}
				*/
				JsonArrLength = jsonArr.length();
				if (JsonArrLength > 0) {
					// Get the ID for start of Tweets  for on Refresh
					try {
						firstJson = jsonArr.getJSONObject(0);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					firstTweet = Tweet.fromJSON(firstJson);
					if ((firstTweet != null) && (!onScroll)) {
						firstSinceId = firstTweet.getUid();
					}
					
					// Get the ID for last tweet for on Scroll
					try {
						lastJson = jsonArr.getJSONObject(JsonArrLength - 1);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					lastTweet = Tweet.fromJSON(lastJson);
					if ((lastTweet != null) && (!onRefresh)) {
						lastMaxId = lastTweet.getUid();
					}
					
					// Update the totalfetch count
					totalFetched = totalFetched + JsonArrLength;
					
					// Add to the messages listview adpater
					if (!onRefresh) {
						aTweets.addAll(Tweet.fromJSONArray(jsonArr));
					} else {
						// Add to the listview adpater in the latest order for on refresh case
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
				}
				if (onRefresh) {
					swipeContainer.setRefreshing(false);
				}
			}
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
				if (onRefresh) {
					swipeContainer.setRefreshing(false);
				}
			}
			
		});
	}
}
