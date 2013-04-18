package net.anzix.fbfeed.data;

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
            b.append("<p class=\"message\">" + getMessage().replaceAll("\\n", "<br/>") + "</p>");
        }
        return b.toString();
    }

    @Override
    public String getHtmlLink() {
        return super.getHtmlLink();
    }
}
