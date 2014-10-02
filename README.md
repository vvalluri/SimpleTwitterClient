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
![alt tag](https://github.com/vvalluri/codepath-simpletwitterclient/blob/master/codepath-simpletwitterclient-3.gif)


