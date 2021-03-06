package net.anzix.fbfeed.data;

import com.google.common.base.Objects;
import com.google.gson.JsonObject;
import net.anzix.fbfeed.input.FbFetcher;
import net.anzix.fbfeed.output.HtmlTableContainer;

/**
 * Shared link with additional note.
 */
public class Link extends Item {

    /**
     * Title of the inline box.
     */
    private String caption;

    /**
     * Text for the inline box.
     */
    private String description;

    /**
     * Link for the caption.
     */
    private String link;

    /**
     * Thumbnail image url.
     */
    private String thumbnail;

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
        if (getMessage() != null) {
            b.append("<p style=\"font-style:italic\" class=\"message\">" + linkify(getMessage().replaceAll("\\n", "<br/>")) + "</p>");
        }

        HtmlTableContainer table = new HtmlTableContainer(caption, link, filterContent(description));
        table.setThumbnail(thumbnail);
        b.append(table.getHtml());
        b.append(getHtmlComments());
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

        if (obj.get("name") != null) {
            setCaption(obj.get("name").getAsString());
        }

        if (obj.get("link") != null) {
            setLink(obj.get("link").getAsString());
        }
        if (obj.get("picture") != null) {
            setThumbnail(obj.get("picture").getAsString());
        }
    }
}
