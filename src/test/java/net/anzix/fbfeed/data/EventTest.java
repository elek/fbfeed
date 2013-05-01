package net.anzix.fbfeed.data;


import org.junit.Assert;
import org.junit.Test;

/**
 * User: eszti
 */
public class EventTest {

    @Test
    public void testLinkify() throws Exception {
        String t = "lahos http://index.hu/qwe balamber\n qwe";
        Assert.assertEquals("lahos <a href=\"http://index.hu/qwe\">http://index.hu/qwe</a> balamber\n qwe", new Event().linkify(t));
    }
}
