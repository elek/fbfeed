package net.anzix.fbfeed.data;

import com.google.gson.JsonObject;
import net.anzix.fbfeed.FbFetcher;

/**
 * A shared photo.
 */
public class Photo extends Item {

    private String image;

    private String link;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getHtmlBody() {
        StringBuilder b = new StringBuilder();
        if (image != null) {
            b.append("<img src=\"" + image + "\"/>");
        }
        if (getMessage() != null) {
            b.append("<p class=\"message\">" + linkify(getMessage().replaceAll("\\n", "<br/>")) + "</p>");
        }
        return b.toString();
    }

    @Override
    public String getHtmlLink() {
        return super.getHtmlLink();
    }

    public void readFrom(JsonObject obj, FbFetcher fetcher) {
        super.readFrom(obj, fetcher);
        if (obj.get("picture") != null) {
            setImage(obj.get("picture").getAsString().replaceAll("_s.jpg", "_n.jpg"));
        }
        if (obj.get("link") != null) {
            setLink(obj.get("link").getAsString());
        }

        String message = null;
        if (obj.get("story")!=null){
            message = obj.get("story").getAsString();
        }
        if (title == null) {
            title = message;
        } else if (this.message == null) {
            this.message = message;
        }
    }
}
