package net.anzix.fbfeed.output;

import net.anzix.fbfeed.data.Feed;
import net.anzix.fbfeed.data.Item;

/**
 * Print the feed to the standard output.
 *
 */
public class SysOutput {

    public void output(Feed f) {
        for (Item i : f.getItems()) {
            System.out.println("----");
            System.out.println(i);
        }
    }
}
