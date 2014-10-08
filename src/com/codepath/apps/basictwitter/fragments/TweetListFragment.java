package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.codepath.apps.basictwitter.EndlessScrollListener;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetDisplayActivity;
import com.codepath.apps.basictwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.basictwitter.models.Tweet;

public class TweetListFragment extends Fragment {
	private ArrayList<Tweet> tweets;
	public TweetArrayAdapter aTweets;
	private ListView lvTweets;
	public List<Tweet> dbTweets;
    public SwipeRefreshLayout swipeContainer;
    public Context thisContext = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// View does not exist yet. Don't use view related functions
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout not attach it yet
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		// Assign view references
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setClickable(true);
		lvTweets.setAdapter(aTweets);
		
		//Set up view listeners
		setupViewListeners(v);
		
		// Return layout view
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    thisContext = activity;
	}
	
	// Set up listeners
	public void setupViewListeners(View v) {
		// Refresh view listener
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            	onRefreshPopulateTimeline();

            } 
        });
        // Configure the refreshing colors
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);
		
        // Attach the on scroll listener
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
        	@Override
        	public void onLoadMore(int page, int totalItemsCount) {
        		// Triggered only when new data needs to be appended to the list
        		if (totalItemsCount > 0) {
        			onScrollPopulateTimeline(totalItemsCount);
        		}
        	}
        });
        
        // Set up on click listener for detail view
    	lvTweets.setOnItemClickListener(new OnItemClickListener() {
    		// VV Should this be in OnActivityCreated()
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the image display activity
				Intent i = new Intent(getActivity(), TweetDisplayActivity.class);
				Tweet displaytweet = tweets.get(position);
				i.putExtra("tweet", displaytweet);
				// Start the new activity
				startActivity(i);			
			}		
		});
    }
	
	
	public void onRefreshPopulateTimeline () {
		return;
	}
	
	public void onScrollPopulateTimeline (int count) {
		return;
	}
	
	public TweetArrayAdapter getAdapter() {
		return aTweets;
	}
	
	public void addAll(ArrayList<Tweet> tweets) {
		aTweets.addAll(tweets);
	}
	
	// Check network availability
    public Boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) thisContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
