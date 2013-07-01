package net.anzix.fbfeed.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.anzix.fbfeed.FbFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Facebook event object.
 */
public class Event extends Link {

    private static final Logger LOG = LoggerFactory.getLogger(Event.class);

    private static final Pattern numbers = Pattern.compile(".*/([0-9]+)/?");

    private long eventId;

    private Date from;


    private String location;

    @Override
    public void readFrom(JsonObject obj, FbFetcher fetcher) {
        super.readFrom(obj, fetcher);
        Matcher m = numbers.matcher(getLink());
        if (m.matches()) {
            eventId = Long.parseLong(m.group(1));
            try {
                File f = fetcher.retrieveEvent(m.group(1));
                JsonObject eventJson = new Gson().fromJson(new FileReader(f), JsonObject.class);
                if (eventJson.get("description") != null) {
                    setDescription(eventJson.get("description").getAsString());
                }
                if (title == null && eventJson.get("name") != null) {
                    setTitle("[Event] " + eventJson.get("name").getAsString());
                    setCaption(eventJson.get("name").getAsString());
                }
                if (eventJson.get("start_time") != null) {
                    setFrom(FbFetcher.DATE_FORMAT.parse(eventJson.get("start_time").getAsString()));
                }
            } catch (Exception e) {
                LOG.error("Can't load event details for the event " + m.group(1), e);
            }
        }
    }

    @Override
    public String getHtmlBody() {
        return super.getHtmlBody() + "<p>Date: " + getFrom() + "</p>";
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
