package com.codepath.apps.basictwitter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
	
	// API to get User credentials
	public void getAccountCred(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}
	
	// API to get User credentials
	public void getUserInfo(String screenname, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/lookup.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenname);
		client.get(apiUrl, params, handler);
	}	
	
	// API to get User timeline
	public void getUserTimeline(String screenname, long Maxid, long Sinceid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",  "8");
		params.put("screen_name", screenname);
		if ((Maxid != 0) || (Sinceid != 0)) {
			if (Maxid != 0) {
				params.put("max_id",  Long.toString(Maxid));
			}
			if (Sinceid != 0) {
				params.put("since_id",  Long.toString(Sinceid));
			}
		} 
		client.get(apiUrl, params, handler);
	}
	
	// API to get home timeline
	public void getHomeTimeLine(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",  "8");
		client.get(apiUrl, params, handler);
	}

	// API to get home timeline
	public void getHomeTimeLineNext(long Maxid, long Sinceid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",  "8");

		if ((Maxid != 0) || (Sinceid != 0)) {
			if (Maxid != 0) {
				params.put("max_id",  Long.toString(Maxid));
			}
			if (Sinceid != 0) {
				params.put("since_id",  Long.toString(Sinceid));
			}
		} 

		client.get(apiUrl, params, handler);
	}
	
	// API to get Mentions timeline
	public void getMentionsLineNext(long Maxid, long Sinceid, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",  "8");
		if ((Maxid != 0) || (Sinceid != 0)) {
			if (Maxid != 0) {
				params.put("max_id",  Long.toString(Maxid));
			}
			if (Sinceid != 0) {
				params.put("since_id",  Long.toString(Sinceid));
			}
		} 
		client.get(apiUrl, params, handler);
	}
	
	// API to get Search timeline
	public void getSearchTimeline(String searchstr, long Maxid, long Sinceid, AsyncHttpResponseHandler handler) {
		//"https://api.twitter.com/1.1/search/tweets.json?"
		String apiUrl = getApiUrl("search/tweets.json?");
		String searchUrlencoded;

		RequestParams params = new RequestParams();
		try {
			searchUrlencoded = URLEncoder.encode(searchstr, "UTF-8");
			params.put("q", searchUrlencoded);
			params.put("count",  "8");
			if ((Maxid != 0) || (Sinceid != 0)) {
				if (Maxid != 0) {
					params.put("max_id",  Long.toString(Maxid));
				}
				if (Sinceid != 0) {
					params.put("since_id",  Long.toString(Sinceid));
				}
			} 
			client.get(apiUrl, params, handler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	// API to update/post statuses to Twitter
	public void postStatusUpdate(String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",  tweet);
		client.post(apiUrl, params, handler);
	}
	
	// API to update/post statuses to Twitter
	public void postStatusReply(long reply_id, String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",  tweet);
		params.put("in_reply_to_status_id",  Long.toString(reply_id));
		client.post(apiUrl, params, handler);
	}

}