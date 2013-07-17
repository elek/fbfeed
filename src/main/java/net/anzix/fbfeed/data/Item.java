package net.anzix.fbfeed.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.anzix.fbfeed.input.FbFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generic shared item.
 */
public abstract class Item {

    private static final Logger LOG = LoggerFactory.getLogger(Item.class);

    public static final Pattern link = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

    List<Comment> comments = new ArrayList<Comment>();

    /**
     * Title of the rss event.
     */
    String title;

    /**
     * Custom message written by the user.
     */
    String message;

    String author;

    Date date;

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
        String body = filterContent(linkify(getMessage()));
        body += getHtmlComments();
        return body;
    }

    public String getHtmlComments() {
        StringBuilder b = new StringBuilder();
        if (comments.size() > 0) {
            b.append("<table style=\"margin: 20px; border: 1px solid gray; padding: 3px; \">");
            b.append("<tr><td><h2>Comments</h2></td></tr>");
            for (Comment c : comments) {
                b.append("<tr><td><p><b>" + c.getAuthor() + "</b>:" + linkify(c.getMessage()) + "</p></td></tr>");
            }
            b.append("</table>");
        }
        return b.toString();

    }

    public String getHtmlLink() {
        return "";
    }

    public void readFrom(JsonObject obj, FbFetcher fetcher) {
        if (obj.get("from") != null) {
            JsonObject from = obj.getAsJsonObject("from");
            author = from.get("name").getAsString();
        }

        if (obj.get("comments") != null) {
            JsonArray cmts = obj.get("comments").getAsJsonObject().get("data").getAsJsonArray();
            if (cmts != null) {
                for (JsonElement cmt : cmts) {
                    Comment c = new Comment();
                    if (cmt.getAsJsonObject().get("from") != null) {
                        c.setAuthor(cmt.getAsJsonObject().get("from").getAsJsonObject().get("name").getAsString());
                    }
                    if (cmt.getAsJsonObject().get("message") != null) {
                        c.setMessage(cmt.getAsJsonObject().get("message").getAsString());
                    }
                    comments.add(c);
                }
            }

        }
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

    public String filterContent(String message) {
        if (message != null) {
            return message.replaceAll("\\n", "<br/>");
        } else {
            return null;
        }
    }

    /**
     * Replace Urls with <a>url</a> tags.
     *
     * @param text
     * @return
     */
    public static String linkify(String text) {
        Matcher m = link.matcher(text);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, "<a href=\"" + m.group(0) + "\">" + m.group(0) + "</a>");
        }
        m.appendTail(b);
        return b.toString();
    }

    public String getAuthor() {
        return author;
    }
}
