package net.anzix.fbfeed;

import net.anzix.fbfeed.input.FbFetcher;

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
