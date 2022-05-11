package ch.heigvd.dil.cli_cmds;
import ch.heigvd.dil.Main;
import ch.heigvd.dil.utils.FileHandler;
import org.junit.Test;
import picocli.CommandLine;

import java.nio.file.Path;

import static org.junit.Assert.*;

public class CleanCmdTest {
    @Test
    public void cleanSiteWithoutPathShouldFail() {
        int exitCode = new CommandLine(new Main()).execute("clean");
        assertNotEquals(exitCode, 0);
    }

    @Test
    public void cleanSiteWithInvalidPathShouldFail() {
        int exitCode = new CommandLine(new Main()).execute("clean", "invalid/path");
        assertNotEquals(exitCode, 0);
    }

    @Test
    public void cleanSiteWithUnbuiltSiteShouldFail() {
        Path site = TestUtils.generateRandomSitePath();
        int initExitCode = new CommandLine(new Main()).execute("init",
                site.toString());
        assertEquals(0, initExitCode);
        int cleanExitCode = new CommandLine(new Main()).execute("clean",
                site.toString());
        assertNotEquals(0, cleanExitCode);

        FileHandler.delete(site.toFile());
    }

    @Test
    public void cleanSiteWithBuiltSiteShouldSucceed() {
        Path site = TestUtils.generateRandomSitePath();
        int initExitCode = new CommandLine(new Main()).execute("init",
                site.toString());
        assertEquals(0, initExitCode);
        int buildExitCode = new CommandLine(new Main()).execute("build",
                site.toString());
        assertEquals(0, buildExitCode);
        int cleanExitCode = new CommandLine(new Main()).execute("clean",
                site.toString());
        assertEquals(0, cleanExitCode);

        FileHandler.delete(site.toFile());
    }
}
