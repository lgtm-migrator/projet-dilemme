package ch.heigvd.dil.data_structures;

import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

public class PageTest {

  private static final String RESOURCES_PATH = "src/test/resources/";
  private String validPageStr;

  @Before
  public void readTestFiles() {
    try {
      String path = RESOURCES_PATH + "testFiles/test-page/test-page-valid.md";
      validPageStr = Files.readString(Paths.get(path));
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void createPageFromValidFileShouldNotThrow() {
    try {
      new Page(validPageStr, Paths.get(""));
    } catch (Exception e) {
      fail();
    }
  }
}
