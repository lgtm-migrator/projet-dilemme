package ch.heigvd.dil.cli_cmds;

import static org.junit.Assert.*;

import ch.heigvd.dil.Main;
import ch.heigvd.dil.utils.FileHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import picocli.CommandLine;

public class BuildCmdTest {

  @Test
  public void buildingInitializedSiteShouldSucceed() {
    Path site = TestUtils.generateRandomSitePath();

    int initExitCode = new CommandLine(new Main()).execute("init", site.toString());
    assertEquals(0, initExitCode);
    int buildExitCode = new CommandLine(new Main()).execute("build", site.toString());
    assertEquals(buildExitCode, 0);
    assertTrue(Files.exists(site.resolve("build/index.html")));
    try {
      assertTrue(
          Files.readString(site.resolve("build/index.html"))
              .contains("<h1>Your content here</h1>"));
    } catch (IOException e) {
      fail();
    } finally {
      FileHandler.delete(site.toFile());
    }
  }

  @Test
  public void buildingUninitializedSiteShouldFail() {
    Path site = TestUtils.generateRandomSitePath();
    assertTrue(site.toFile().mkdirs()); // create the site directory

    int exitCode = new CommandLine(new Main()).execute("build", site.toString());
    assertNotEquals(exitCode, 0);

    FileHandler.delete(site.toFile());
  }

  @Test
  public void buildingSiteWithInvalidPathShouldFail() {
    int exitCode = new CommandLine(new Main()).execute("build", "/invalid/path");
    assertNotEquals(exitCode, 0);
  }

  @Test
  public void buildingSiteWithoutPathShouldFail() {
    int exitCode = new CommandLine(new Main()).execute("build");
    assertNotEquals(exitCode, 0);
  }
}
