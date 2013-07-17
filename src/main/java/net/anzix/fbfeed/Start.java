package net.anzix.fbfeed;

import ch.qos.logback.classic.Level;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.anzix.fbfeed.data.*;
import net.anzix.fbfeed.input.Facebook;
import net.anzix.fbfeed.output.HtmlOutput;
import net.anzix.fbfeed.output.RssOutput;
import net.anzix.fbfeed.output.SysOutput;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Start {

    private static final Logger LOG = LoggerFactory.getLogger(Start.class);

    private File cacheLocation = new File(System.getProperty("java.io.tmpdir"), "fbcache");

    @Option(name = "--id", required = true, usage = "Id of the facebook object, or a file with one id per line.")
    private String id;

    @Option(name = "--key", required = true, usage = "Access token")
    private String access_key;

    @Option(name = "-v", usage = "Use if you need debug level logging.")
    private boolean verbose;

    @Option(name = "--type", usage = "Output type (rss,html,sysout). Multiple format can be used with separating with ,")
    private String type = "rss";

    @Option(name = "--output", usage = "Destination directory")
    private File outputDir = new File(".");

    public static void main(String args[]) throws Exception {
        Start prog = new Start();
        CmdLineParser parser = new CmdLineParser(prog);
        try {
            parser.parseArgument(args);
            prog.run();
        } catch (CmdLineException ex) {
            System.err.println("Error in the arguments: " + ex.getMessage());
            parser.printUsage(System.err);
        } catch (Exception ex) {
            LOG.error("Error during the generation", ex);
        }
    }

    public void run() throws Exception {

        if (verbose) {
            ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.DEBUG);
        } else {
            ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
        }


        if (id.contains("://")) {
            generate(id);
        } else {
            File idFile = new File(id);
            if (idFile.exists()) {
                final List<File> results = new ArrayList<File>();
                Files.readLines(idFile, Charset.defaultCharset(), new LineProcessor<Object>() {
                    @Override
                    public boolean processLine(String line) throws IOException {
                        if (line.trim().length() > 0 && !line.trim().startsWith("#")) {
                            try {
                                generate(line.trim());
                            } catch (Exception ex) {
                                LOG.error("Can't process line " + line, ex);
                            }
                        }
                        return true;
                    }

                    @Override
                    public Object getResult() {
                        return null;
                    }
                });
            } else {
                LOG.error("ID file doesn't exist: " + id);
            }

        }
    }

    public void generate(String uriString) throws Exception {
        Facebook fb = new Facebook(new File(outputDir, ".fbcache"), access_key);
        URI uri = new URI(uriString);
        Map<String, String> params = parseURIParams(uri);
        Feed feed = fb.get(uri.getPath(), uri.getHost(), params);

        for (String t : type.split(",")) {
            String oneType = t.trim();
            LOG.info("Generating " + oneType + " output");
            if (oneType.equals("rss")) {
                new RssOutput(outputDir).output(feed);
            } else if (oneType.equals("sysout")) {
                new SysOutput().output(feed);
            } else if (oneType.equals("html")) {
                new HtmlOutput(outputDir).output(feed);
            } else {
                LOG.error("Unknown output type: " + type);
                System.exit(-1);
            }
        }
    }

    private Map<String, String> parseURIParams(URI uri) {
        Map<String, String> result = new HashMap<String, String>();
        String query = uri.getQuery();
        if (query != null) {
            for (String element : query.split("&")) {
                String[] parts = element.split("=");
                result.put(parts[0], parts[1]);
            }
        }
        return result;
    }


}
