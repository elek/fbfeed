1. Get a facebook access token.

a.) generate one on http://developers.facebook.com/tools/explorer (valid for a few hours!!)

b.) or generate one (valid for 60 days) with the facebook api. You need a registered facebook application and get two url.

```
https://www.facebook.com/dialog/oauth?client_id=APP_ID&redirect_uri=http%3A%2F%2Fanything.com%2F
https://graph.facebook.com/oauth/access_token?client_id=APP_ID&redirect_uri=SAME_AS_ABOVE&client_secret=APP_SECRET&code=USE_FROM_THE_PREV_RESPONSE
```
2. Build with <code>./gradlew jar</code>

3. Run with <code>java -jar build/lib/fbfeed....jar</code>

```
    --id VAL      : Id of the facebook object, or a file with one id per line.
    --key VAL     : Access token (see above)
    --output FILE : Destination directory
    --type VAL    : Output type (rss,html,sysout). Multiple format can be used with separating with ,
```