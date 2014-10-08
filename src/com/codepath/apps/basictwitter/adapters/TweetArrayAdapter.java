package com.codepath.apps.basictwitter.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ParseException;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.ProfileActivity;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetReplyActivity;
import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
	       // Get the data item for this position
	       Tweet tweet = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item, parent, false);
	          holder = new ViewHolder();
		       // Lookup view for data population
		       holder.hivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
		       holder.htvUser = (TextView) convertView.findViewById(R.id.tvUser);
		       holder.htvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		       holder.htvBody = (TextView) convertView.findViewById(R.id.tvBody);
		       holder.htweetTime = (TextView) convertView.findViewById(R.id.tvTime);
		       holder.hivTweetImage = (ImageView) convertView.findViewById(R.id.ivTweetImg);
		       holder.htvfav = (TextView) convertView.findViewById(R.id.tvFav);
		       holder.htvRetweet = (TextView) convertView.findViewById(R.id.tvRetweet);
		       holder.htvReply = (TextView) convertView.findViewById(R.id.tvReply);
		       convertView.setTag(holder);
	       }  else {
	    	   holder = (ViewHolder) convertView.getTag();
	       }
	       
	       
	       // Populate the data into the template view using the data object
	       holder.htvUser.setText(Html.fromHtml("<font color=\"#206199\"><b>" + tweet.getUser().getName()
                   + "  " + "</b></font>"));
	       //holder.htvUser.setText(tweet.getUser().getName());
	       holder.htvUserName.setText("@" + tweet.getUser().getScreenName());
	       
	       holder.htvBody.setText(tweet.getBody());
	       holder.htvBody.setMovementMethod(LinkMovementMethod.getInstance());
	       //Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/Aaargh.ttf"); 
	       //holder.htvBody.setTypeface(type);
	       
			// Get the create time
			String postdate = getRelativeTimeAgo(tweet.getCreatedAt());
			holder.htweetTime.setText(convertTimeStringtoShort(postdate));
			
	       // Favorite count
	       if (tweet.favorite_count != 0) {
	    	   holder.htvfav.setText(Integer.toString(tweet.getFavorite_count()));
	       } else {
	    	   holder.htvfav.setText("");
	       }
	       // Retweet count
	       if (tweet.retweet_count != 0) {
	    	   holder.htvRetweet.setText(Integer.toString(tweet.getRetweet_count()));
	       } else {
	    	   holder.htvRetweet.setText("");
	       }
	       // Tweet Reply
			holder.htvReply.setTag(tweet.getUser().getUid());
			holder.htvReply.setClickable(true);
			// Set profile image on click listener
			holder.htvReply.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	TextView tweetReply = (TextView) v.findViewById(R.id.tvReply);
		        	if (tweetReply != null) {
			        	//String user = (String)v.getTag();
		        		String user = tweetReply.getTag().toString();
		        		Log.d("debug", "Adapter click, Tweet reply tag: " + v.getTag() + " User: " + user);
			        	Intent intent = new Intent().setClass(v.getContext(), TweetReplyActivity.class);
			    		intent.putExtra("user", user);
			    		// Start the new activity
			    		v.getContext().startActivity(intent);
		        	} else {
		        		Log.d("debug", "Adapter click, profile image not found");
		        	}
		        }
		      });
			
	       // populate and display image
			holder.hivProfileImage.setImageResource(android.R.color.transparent);

			// Set tag for user time line view
			holder.hivProfileImage.setTag(tweet.getUser().getScreenName());
			holder.hivProfileImage.setClickable(true);
			// Set profile image on click listener
			holder.hivProfileImage.setOnClickListener(new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          //Toast.makeText(activity, children, Toast.LENGTH_SHORT).show();
		        	ImageView profileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
		        	if (profileImg != null) {
			        	//String user = (String)v.getTag();
		        		String user = profileImg.getTag().toString();
		        		Log.d("debug", "Adapter click, profile image tag: " + v.getTag() + " User: " + user);
			        	Intent intent = new Intent().setClass(v.getContext(), ProfileActivity.class);
			    		intent.putExtra("user", user);
			    		// Start the new activity
			    		v.getContext().startActivity(intent);
		        	} else {
		        		Log.d("debug", "Adapter click, profile image not found");
		        	}
		        }
		      });

	       ImageLoader imageLoader = ImageLoader.getInstance();
	       imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), holder.hivProfileImage);

	       if (tweet.mediaUrl != null) {
	    	   //holder.hivTweetImage.setImageResource(0);
	    	   holder.hivTweetImage.setVisibility(convertView.VISIBLE);
	    	   Picasso.with(getContext()).load(tweet.mediaUrl).into(holder.hivTweetImage);
	       } else {
	    	   holder.hivTweetImage.setVisibility(convertView.GONE);
	       }
	       // Return the completed view to render on screen
	       return convertView;
	}
	
	static class ViewHolder {
		ImageView hivProfileImage;
		TextView htvUser;
		TextView htvUserName;
		TextView htvBody;
		TextView htweetTime;
		ImageView hivTweetImage;
		TextView htvfav;
		TextView htvReply;
		TextView htvRetweet;
	}
	
	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis;
			try {
				dateMillis = sf.parse(rawJsonDate).getTime();
				relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
						System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	public String convertTimeStringtoShort (String timeStr) {
		String StrReplacetime1 = timeStr;
		
		String StrReplaceAgo = timeStr.replace(" ago", "");
       if (timeStr.contains("seconds")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" seconds","s" );
       } else if (timeStr.contains("second")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" second","s" );
       } else if (timeStr.contains("minutes")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" minutes","m" );
       } else if (timeStr.contains("minute")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" minute","m" );
       } else if (timeStr.contains("hours")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" hours","h" );
       } else if (timeStr.contains("hour")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" hour","h" );
       } else if (timeStr.contains("days")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" days","d" );
       }  else if (timeStr.contains("Yesterday")) {
    	   StrReplacetime1 = StrReplaceAgo.replace("Yesterday","1d" );
       } else if (timeStr.contains("day")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" day","d" );
       } else if (timeStr.contains("months")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" months","m" );
       } else if (timeStr.contains("month")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" month","m" );
       } else  if (timeStr.contains("years")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" years","y" );
       } else if (timeStr.contains("year")) {
    	   StrReplacetime1 = StrReplaceAgo.replace(" year","y" );
       }
		
       return StrReplacetime1;
	}
}
