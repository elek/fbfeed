
package net.anzix.fbfeed.input;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.anzix.fbfeed.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

public class Facebook {

    private static final Logger LOG = LoggerFactory.getLogger(Facebook.class);

    private FbFetcher fetcher;

    public Facebook(File cachedDir, String authToken) {
        fetcher = new FbFetcher(cachedDir, authToken);
    }

    public Feed get(String id, String type, Map<String, String> params) throws Exception {
        if (type.equals("page")) {
            File file = fetcher.retrievePage(id);
            return parse(file, type, params);
        } else {
            File file = fetcher.retrievePosts(id);
            return parse(file, type, params);
        }
    }

    /**
     * Parse a cached feed file to a java Pojo.
     */
    public Feed parse(File f, String clazz, Map<String, String> params) throws Exception {
        Feed feed = new Feed();
        Gson gson = new Gson();
        JsonObject e = gson.fromJson(new FileReader(f), JsonObject.class);
        String id = e.get("id").getAsString();
        feed.setLink("http://facebook.com/" + id);
        feed.setName(e.get("name").getAsString());
        if (e.get("username") != null) {
            feed.setNick(e.get("username").getAsString());
        } else {
            feed.setNick(id);
        }
        feed.setId(id);

        String feedTag = clazz.equals("page") ? "feed" : "posts";

        for (JsonElement o : e.get(feedTag).getAsJsonObject().get("data").getAsJsonArray()) {
            JsonObject obj = (JsonObject) o;
            String type = "";
            if (obj.get("type") != null) {
                type = obj.get("type").getAsString();
            }
            if (!params.containsKey("all") && obj.get("from") != null && !obj.get("from").getAsJsonObject().get("id").getAsString().equals(id)) {
                continue;
            }

            if (type.equals("photo")) {
                Photo p = new Photo();
                p.readFrom((JsonObject) obj, fetcher);
                feed.addItem(p);
            } else if (type.equals("link")) {
                if (obj.get("link") != null && obj.get("link").getAsString().contains("www.facebook.com/events")) {
                    Event event = new Event();
                    event.readFrom((JsonObject) obj, fetcher);
                    feed.addItem(event);
                } else {
                    Link l = new Link();
                    l.readFrom((JsonObject) obj, fetcher);
                    feed.addItem(l);
                }
            } else if (type.equals("video")) {
                Video v = new Video();
                v.readFrom((JsonObject) obj, fetcher);
                feed.addItem(v);
            } else if (type.equals("status")) {
                if (obj.get("message") != null) {
                    Status s = new Status();
                    s.readFrom((JsonObject) obj, fetcher);
                    feed.addItem(s);
                } else {
                    LOG.debug("Ignored status message: " + obj);
                }
            } else {
                LOG.warn("Unhandled object: " + obj);
            }


        }
        return feed;
    }
}
