package net.anzix.fbfeed.data;

import java.util.Date;

/**
 * Generic shared item.
 */
public class Item {

    private String title = "Item";

    private String message;

    private Date date;

    public String getTitle() {
        return title;
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
        return message;
    }

    public String getHtmlLink() {
        return "";
    }
}
