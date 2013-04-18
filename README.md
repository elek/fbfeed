

1. Register a new facebook application and generate an access token with the Graph Api Explorer (you can generate a short-term key even without registering new app).

2. Build with <code>gradle build</code>

3. Run with <code>java -jar build/lib/fbfeed....jar</code>

```
    --id VAL      : Id of the facebook object, or a file with one id per line.
    --key VAL     : Access token
    --output FILE : Destination directory
    --type VAL    : Output type (rss,html,sysout). Multiple format can be used with separating with ,
```