package com.codepath.apps.basictwitter.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

@Table(name = "TwitterTweets")
public class Tweet extends Model implements Parcelable {
	/**
	 * 
	 */
//	private static final long serialVersionUID = -6185650311498705408L;
	@Column(name = "body")
	public String body;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public long uid;
	@Column(name = "createdAt")
	public String createdAt;
	
	@Column(name = "mediaUrl")
	public String mediaUrl;
	
	@Column(name = "favorite_cnt")
	public int favorite_count;
	
	@Column(name = "retweet_cnt")
	public int retweet_count;
	
	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	public User user;

	
	public Tweet() {
		super();
	}
	
	/* Parcelable */
	@Override
    public int describeContents() {
        return 0;
    }
	
	  @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeString(getBody());
	        dest.writeLong(getUid());
	        dest.writeString(getCreatedAt());
	        dest.writeString(getMediaUrl());
	        dest.writeInt(getFavorite_count());
	        dest.writeInt(getRetweet_count());
	        user.writeToParcel(dest, flags);
	    }

	    private Tweet(Parcel in){
	        
	        setBody(in.readString());
	        setUid(in.readLong());
	        setCreatedAt(in.readString());
	        setMediaUrl(in.readString());
	        setFavorite_count(in.readInt());
	        setRetweet_count(in.readInt());
	        setUser(User.CREATOR.createFromParcel(in) );
	    }
	    
	    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {

	        @Override
	        public Tweet createFromParcel(Parcel source) {
	            return new Tweet(source);
	        }

	        @Override
	        public Tweet[] newArray(int size) {
	            return new Tweet[size];
	        }
	    };
	    /* end of parcelable */
	
	// Pass single tweet
	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		List<User> Userlist;
		
		// Extract values from json
		ActiveAndroid.beginTransaction();
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			// tweet -> entities -> media -> [x] -> media_url
			if (jsonObject.optJSONObject("entities") != null)  {
				JSONArray mediaJsonArr  = null;
				if (jsonObject.getJSONObject("entities").optJSONArray("media") != null) {
					mediaJsonArr = jsonObject.getJSONObject("entities").getJSONArray("media");
					if (mediaJsonArr.length() > 0) {
						if (mediaJsonArr.getJSONObject(0).has("media_url")) {
							tweet.mediaUrl = mediaJsonArr.getJSONObject(0).getString("media_url");
							Log.d("debug", "VV: MEDIA URL: " + tweet.mediaUrl);
						}
					}
				}
			}
			
			if (jsonObject.optJSONObject("user") != null) {
				if (jsonObject.getJSONObject("user").has("favourites_count")) {
					tweet.favorite_count = jsonObject.getJSONObject("user").getInt("favourites_count");
				}
			}
			// Since ActiveAndroid cannot allow duplicates for User uid, check if User already exists
			// Reuse the user if exists. Since UID is unique we will take the first result (only one)
			Userlist = User.getUserbyUid(jsonObject.getJSONObject("user").getLong("id"));
			if (Userlist.size() > 0) {
				tweet.user = Userlist.get(0);
			} else {
				tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
			}
			tweet.save();
			ActiveAndroid.setTransactionSuccessful();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		finally {
	        ActiveAndroid.endTransaction();
		}
		return tweet;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}
	
	public int getFavorite_count() {
		return favorite_count;
	}
	
	public int getRetweet_count() {
		return retweet_count;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public void setFavorite_count(int favorite_count) {
		this.favorite_count = favorite_count;
	}

	public void setRetweet_count(int retweet_count) {
		this.retweet_count = retweet_count;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
				
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
				
			}
		}

		return tweets;
	}
	/*
	@Override
	public String toString() {
		return getBody() + "  " + getUser().getScreenName();
	}
	*/
}
