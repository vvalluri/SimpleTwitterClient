package com.codepath.apps.basictwitter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.codepath.apps.basictwitter.fragments.SearchFragment;

public class SearchActivity extends ActionBarActivity {
	private String search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Progress bar support
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        
		setContentView(R.layout.activity_search);
		search = (String)getIntent().getStringExtra("search");
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle("Search");
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55ACEE")));
		// Load fragment dynamically
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SearchFragment Searchfragment = SearchFragment.newInstance(search);
		ft.replace(R.id.fragmentSearchResults, Searchfragment);
		ft.commit();
	}
  
}
