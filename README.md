Fragment Twitter Client
User Stories completed:

Required:
1. Includes all required user stories from Week 3 Twitter Client

2. User can switch between Timeline and Mention views using tabs.

3. User can view their home timeline tweets.
4. User can view the recent mentions of their username.
5. User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
6. Optional: Implemented tabs in a gingerbread-compatible approach using android Support v7
7. User can navigate to view their own profile
8. User can see picture, tagline, # of followers, # of following, and tweets on their profile.
9. User can click on the profile image in any tweet to see another user's profile.
10. User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
11. Profile view includes that user's timeline

Optional:
12. Advanced: Robust error handling, check if internet is available, handle error cases, network failures

13. Advanced: When a network request is sent, user sees an indeterminate progress indicator for profile view
14. Advanced: User can "reply" to any tweet on their home timeline. The user that wrote the original tweet is automatically "@" replied in compose
15. Advanced: User can click on a tweet to be taken to a "detail view" of that tweet
16. Advanced: Improve the user interface and theme the app to feel twitter branded
     - Made the UI look like twitter UI with colors, format and icons
17. Advanced: User can search for tweets matching a particular query and see results (seeing issue with query string will try to fix)

GIF walkthrough
![alt tag](https://github.com/vvalluri/SimpleTwitterClient/blob/master/codepath-simpletwitterclient-fragments-1.gif)




Simple Twitter Client

User Stories:

The following user stories are completed:

1. User can sign in to Twitter using OAuth login
2. User can view the tweets from their home timeline
3. User is displayed the username, name, and body for each tweet
4. User is displayed the relative timestamp for each tweet "8m", "7h"
5. User can view more tweets as they scroll with infinite pagination
6. Optional: Links in tweets are clickable and will launch the web browser
7. User can compose a new tweet
8. User can click a “Compose” icon in the Action Bar on the top right
9. User can then enter a new tweet and post this to twitter
10. User is taken back to home timeline with new tweet visible in timeline
11. Optional: User can see a counter with total number of characters left for tweet
Advanced user stories completed:

1. Advanced: User can refresh tweets timeline by pulling down to refresh
2. Advanced: User can open the twitter app offline and see last loaded tweets
3. Tweets are persisted into sqlite and can be displayed from the local DB
4. Advanced: User can tap a tweet to display a "detailed" view of that tweet
5. Advanced: User can select "reply" from detail view to respond to a tweet
6. Advanced: Improved user interface and theme the app to feel "twitter branded"
   - Action bar modified
   - Icons are used
   - Added media to listview
   - Added favorites
   - Added Parcelable
   - Fixed the app closing issue by setting "noHistory" for login activity
   - Fixed the User model duplicate records in SQL database

GIF walkthrough:
![alt tag](https://github.com/vvalluri/SimpleTwitterClient/blob/master/codepath-simpletwitterclient-3.gif)


