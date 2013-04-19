package net.anzix.fbfeed.data;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.LocaleServiceProviderPool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generic shared item.
 */
public abstract class Item {

    String title;

    String message;

    private Date date;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private static final Logger LOG = LoggerFactory.getLogger(Item.class);

    public String getTitle() {
        if (title != null) {
            return title;
        } else {
            return getClass().getSimpleName();
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHtmlBody() {
        return message.replaceAll("\\n", "<br/>");
    }

    public String getHtmlLink() {
        return "";
    }

    public void readFrom(JsonObject obj) {
        if (obj.get("name") != null) {
            setTitle(obj.get("name").getAsString());
        }

        try {
            if (obj.get("created_time") != null) {
                setDate(dateFormat.parse(obj.get("created_time").getAsString()));
            }
        } catch (Exception ex) {
            LOG.error("Can't parse date: " + obj.get("created_time"), ex);
        }

        if (obj.get("message") != null) {
            setMessage(obj.get("message").getAsString());
        }
    }
}
