package com.codepath.apps.basictwitter.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "TwitterUser")
public class User extends Model implements Parcelable {
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1183532215934403601L;
	@Column(name = "name")
	public String name;
	//@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	@Column(name = "uid", unique = true)
	//@Column(name = "uid")
	public long uid;
	@Column(name = "screenName")
	public String screenName;
	@Column(name = "profileImageUrl")
	public String profileImageUrl;
	@Column(name = "Description")
	public String description;
	@Column(name = "Followers")
	public int followersCnt;
	@Column(name = "Following")
	public int followingCnt;
	@Column(name = "Statuses")
	public int statusesCnt;
	

	public User() {
		super();
	}
	
	/* Parcelable */
	@Override
    public int describeContents() {
        return 0;
    }
	
	  @Override
	    public void writeToParcel(Parcel dest, int flags) {
	        dest.writeString(getName());
	        dest.writeLong(getUid());
	        dest.writeString(getScreenName());
	        dest.writeString(getProfileImageUrl());
	    }

	    private User(Parcel in){
	        
	        setName(in.readString());
	        setUid(in.readLong());
	        setScreenName(in.readString());
	        setProfileImageUrl(in.readString());
	    }
	    
	    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

	        @Override
	        public User createFromParcel(Parcel source) {
	            return new User(source);
	        }

	        @Override
	        public User[] newArray(int size) {
	            return new User[size];
	        }
	    };
	    /* end of parcelable */
	    
	// Get user info
	public static User fromJSON(JSONObject json) {
		User u = new User();
		ActiveAndroid.beginTransaction();
		try {
			u.name = json.getString("name");
			u.uid = json.getLong("id");
			u.screenName = json.getString("screen_name");
			u.profileImageUrl = json.getString("profile_image_url");
			u.description = json.getString("description");
			if (json.has("followers_count")) {
				u.followersCnt = json.getInt("followers_count");
			} else {
				u.followersCnt = 0;
			}
			if (json.has("friends_count")) {
				u.followingCnt = json.getInt("friends_count");
			} else {
				u.followersCnt = 0;
			}
			if (json.has("statuses_count")) {
				u.statusesCnt = json.getInt("statuses_count");
			} else {
				u.statusesCnt = 0;
			}
			// Save to database
			u.save();
			ActiveAndroid.setTransactionSuccessful();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		finally {
	        ActiveAndroid.endTransaction();
		}
		return u;
	}

	public int getStatusesCnt() {
		return statusesCnt;
	}

	public void setStatusesCnt(int statusesCnt) {
		this.statusesCnt = statusesCnt;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFollowingCnt() {
		return followingCnt;
	}

	public void setFollowingCnt(int following_cnt) {
		this.followingCnt = following_cnt;
	}

	public int getFollowersCnt() {
		return followersCnt;
	}

	public void setFollowersCnt(int followers_cnt) {
		this.followersCnt = followers_cnt;
	}
	
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	

    public void setName(String name) {
		this.name = name;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public List<Tweet> items() {
        return getMany(Tweet.class, "User");
    }
    
    public static List<User> getUserbyUid (long uid) {
    	return new Select()
        .from(User.class)
        .where("uid = ?", uid)
        .execute();			
    }
}
