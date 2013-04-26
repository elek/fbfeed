package net.anzix.fbfeed.data;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;
import net.anzix.fbfeed.FbFetcher;

/**
 * Shared link with additional note.
 */
public class Link extends Item {

    private String caption;

    private String link;

    private String thumbnail;

    private String description;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("link", link)
                .add("thumbnail", thumbnail)
                .add("description", description)
                .toString();
    }

    @Override
    public String getHtmlBody() {
        StringBuilder b = new StringBuilder();
        if (thumbnail != null) {
            b.append("<img src=\"" + thumbnail + "\"/>");
        }
        if (getMessage() != null) {
            b.append("<p class=\"message\">" + getMessage().replaceAll("\\n", "<br/>") + "</p>");
        }
        if (description != null) {
            b.append("<q style=\"font-style: italic;\">" + description.replaceAll("\\n", "<br/>") + "</q>");
        }
        return b.toString();
    }

    @Override
    public String getHtmlLink() {
        return getLink();
    }

    public void readFrom(JsonObject obj, FbFetcher fetcher) {
        super.readFrom(obj, fetcher);
        if (obj.get("description") != null) {
            setDescription(obj.get("description").getAsString());
        }

        if (obj.get("caption") != null) {
            setCaption(obj.get("caption").getAsString());
        }

        if (obj.get("link") != null) {
            setLink(obj.get("link").getAsString());
        }
        if (obj.get("picture") != null) {
            setThumbnail(obj.get("picture").getAsString());
        }
    }
}
