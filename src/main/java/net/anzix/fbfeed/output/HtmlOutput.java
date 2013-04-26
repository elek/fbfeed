package net.anzix.fbfeed.output;

import net.anzix.fbfeed.data.Feed;
import net.anzix.fbfeed.data.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;

/**
 * Generates simple html output.
 */
public class HtmlOutput {

    private File outputDir;

    private static final Logger LOG = LoggerFactory.getLogger(HtmlOutput.class);

    public HtmlOutput(File outputDir) {
        this.outputDir = outputDir;
        if (!this.outputDir.exists()) {
            this.outputDir.mkdirs();
        }
    }

    public void output(Feed f) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <title>" + f.getName() + "</title>\n" +
                "  <meta name=\"description\" content=\"" + f.getName() + "\">\n" +
                "  <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "  <![endif]-->\n" +
                "</head>\n" +
                "<body>\n" +
                "\n");


        for (Item i : f.getItems()) {
            builder.append("<div class=\"item\">");
            builder.append("<h3><a href=\"" + i.getHtmlLink() + "\">" + i.getTitle() + "</a></h3>");
            builder.append(i.getHtmlBody());
            builder.append("</div>");

        }
        builder.append("</body>\n" +
                "</html>");
        File outputFile = new File(outputDir, f.getNick() + ".html");
        FileWriter writer = new FileWriter(outputFile);
        LOG.debug("Writing html file to " + outputFile.getAbsolutePath());
        writer.write(builder.toString());
        writer.close();


    }
}
