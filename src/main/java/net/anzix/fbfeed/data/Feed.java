package net.anzix.fbfeed.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Feed with multiple type of shared items.
 */
public class Feed {

    private String id;

    private String name;

    private String link;

    private List<Item> items = new ArrayList<Item>();

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
