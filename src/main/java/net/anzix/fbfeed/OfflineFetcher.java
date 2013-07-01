package net.anzix.fbfeed;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Fetcher for offline/test usage.
 */
public class OfflineFetcher extends FbFetcher {

    public OfflineFetcher(File dir) {
        super(dir, "");
    }

    @Override
    public boolean isUpToDate(File cacheFile) {
        if (!cacheFile.exists()) {
            throw new RuntimeException(new FileNotFoundException(cacheFile.getAbsolutePath() + " is missing."));
        }
        return true;
    }
}
