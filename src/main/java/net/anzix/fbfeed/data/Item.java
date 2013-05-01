package net.anzix.fbfeed.data;

import com.google.gson.JsonObject;
import net.anzix.fbfeed.FbFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic shared item.
 */
public abstract class Item {

    private static final Logger LOG = LoggerFactory.getLogger(Item.class);

    public static final Pattern link = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    String title;

    String message;

    private Date date;





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

    public void readFrom(JsonObject obj, FbFetcher fetcher) {
        if (obj.get("name") != null) {
            setTitle(obj.get("name").getAsString());
        }

        try {
            if (obj.get("created_time") != null) {
                setDate(FbFetcher.DATE_FORMAT.parse(obj.get("created_time").getAsString()));
            }
        } catch (Exception ex) {
            LOG.error("Can't parse date: " + obj.get("created_time"), ex);
        }

        if (obj.get("message") != null) {
            setMessage(obj.get("message").getAsString());
        }
    }


    /**
     * Replace Urls with <a>url</a> tags.
     *
     * @param text
     * @return
     */
    public String linkify(String text) {
        Matcher m = link.matcher(text);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, "<a href=\"" + m.group(0) + "\">" + m.group(0) + "</a>");
        }
        m.appendTail(b);
        return b.toString();
    }

}
