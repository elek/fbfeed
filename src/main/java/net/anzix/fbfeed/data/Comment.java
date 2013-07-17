package net.anzix.fbfeed.data;

/**
 * Comment for any content.
 */
public class Comment {
    /**
     * Displayable name;
     */
    String author;

    /**
     * Content of the comment;
     */
    String message;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
