package ch.heigvd.dil.cli_cmds;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for tests.
 */
public class TestUtils {
    /**
     * Generates a random path for tests.
     * @return the random path
     */
    static public Path generateRandomSitePath() {
        String siteName = "site_" +  RandomStringUtils.randomAlphanumeric(8);
        return Paths.get(siteName);
    }
}
