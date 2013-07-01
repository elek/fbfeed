package net.anzix.fbfeed.data.output;

import net.anzix.fbfeed.OfflineFetcher;
import net.anzix.fbfeed.Start;
import net.anzix.fbfeed.data.Feed;
import net.anzix.fbfeed.output.HtmlOutput;
import org.junit.Test;

import java.io.File;

public class HtmlOutputTest {

    @Test
    public void test() throws Exception {

        HtmlOutput t = new HtmlOutput(new File("build"));
        Feed f = Start.parse(new File("src/test/resources/eszt.json"), new OfflineFetcher(new File("src/test/resources")));
        t.output(f);

    }
}
