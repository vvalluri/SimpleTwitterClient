package com.codepath.apps.basictwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "jx6Qh8JoGxR2HNMBlezYNFk0F";       // Change this
	public static final String REST_CONSUMER_SECRET = "RTi4Lzo447ZpP2fCmOfJNmTrBfvHFWckJo8MOOJgkFi00Jo5tu"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	// Every Endpoint has a method
	
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
	
	// API to get home timeline
	public void getAccountCred(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");

		// VV client.get and client.post. If no params pass null instead of params***
		client.get(apiUrl, handler);
	}
	
	// API to get home timeline
	public void getHomeTimeLine(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		//params.put("since_id",  "1");
		params.put("count",  "8");
		// VV client.get and client.post. If no params pass null instead of params***
		client.get(apiUrl, params, handler);
	}

	// API to get home timeline
	public void getHomeTimeLineNext(long Maxid, long Sinceid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",  "8");
		//params.put("since_id",  "1");
		//params.put("since_id", Integer.toString(Maxid));
		if ((Maxid != 0) || (Sinceid != 0)) {
			if (Maxid != 0) {
				params.put("max_id",  Long.toString(Maxid));
			}
			if (Sinceid != 0) {
				params.put("since_id",  Long.toString(Sinceid));
			}
		} /* else {
			params = null;
		} */
		// VV client.get and client.post. If no params pass null instead of params***
		client.get(apiUrl, params, handler);
	}
	
	// API to update/post statuses to Twitter
	public void postStatusUpdate(String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",  tweet);
		// VV client.get and client.post. If no params pass null instead of params***
		client.post(apiUrl, params, handler);
	}
	
	// API to update/post statuses to Twitter
	public void postStatusReply(long reply_id, String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",  tweet);
		params.put("in_reply_to_status_id",  Long.toString(reply_id));
		// VV client.get and client.post. If no params pass null instead of params***
		client.post(apiUrl, params, handler);
	}
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	/* public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	} */


}