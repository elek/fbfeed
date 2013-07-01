package net.anzix.fbfeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util class to retrieve (and cache) facebook objects.
 */
public class FbFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(FbFetcher.class);

    private File cacheDir;

    private String authToken;

    private boolean forceDownload = false;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public FbFetcher(File cacheDir, String authToken) {
        this.cacheDir = cacheDir;
        this.authToken = authToken;
    }

    public File retrievePosts(String fbId) throws Exception {
        return retrieveFeed(fbId, "fields=name,username,id,posts");
    }

    public File retrieveEvent(String fbId) throws Exception {
        return retrieveFeed(fbId, "");
    }

    public boolean isUpToDate(File cacheFile) {
        boolean refresh = false;
        if (cacheFile.exists()) {
            long updated = (new Date().getTime() - cacheFile.lastModified()) / (1000l * 60);
            if (updated > 60) {
                LOG.debug("Cached file has been  modified " + updated + " minutes ago. Should be updated.");
                refresh = true;
            } else {
                LOG.debug("Using cache file " + cacheFile.getAbsolutePath() + "(" + updated + " minutes old)");
            }

        }
        return !refresh;
    }

    public File retrieveFeed(String fbId, String params) throws Exception {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        File cacheFile = new File(cacheDir, fbId + ".json");
        if (!isUpToDate(cacheFile) || forceDownload) {
            URL website = new URL("https://graph.facebook.com/" + fbId + "?" + params + (params.length() > 0 ? "&" : "") + "access_token=" + authToken);
            LOG.debug("(Re)downloading " + website);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(cacheFile);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        }
        return cacheFile;

    }
}
