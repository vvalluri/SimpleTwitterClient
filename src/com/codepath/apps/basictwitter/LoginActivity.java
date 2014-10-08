package com.codepath.apps.basictwitter;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.codepath.oauth.OAuthLoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {
	private TwitterClient Client;
    private Editor editPref;
    private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActiveAndroid.initialize(this);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(0x55ACEE));
		
		// Get the shared preferences
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editPref = pref.edit();
		
		// Create Client with methods for various APIs (Twitter Rest Client application)
		Client = TwitterClientApp.getRestClient();

	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		// Populate account credentials
		populateAccountCred();
		
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
		//Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}
	
	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
			= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	     	return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
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
	
	
}
