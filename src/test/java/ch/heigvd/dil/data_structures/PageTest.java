package ch.heigvd.dil.data_structures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import org.everit.json.schema.ValidationException;
import org.junit.Before;
import org.junit.Test;

public class PageTest {

  private static final String resourcesPath = "src/test/resources/testFiles/";
  private String validPageStr;
  private Site.Config siteConf;

  @Test
  public void createPageFromValidFileShouldNotThrow() {
    try {
      new Page(validPageStr, siteConf);
    } catch (Exception e) {
      fail();
    }
  }

  @Before
  public void createSiteConfig() {
    siteConf = new Site.Config("title", "author", "example.ch");
  }

  @Before
  public void readTestFiles() {
    try (BufferedReader br =
        new BufferedReader(
            new FileReader(
                resourcesPath + "test-page/test-page-valid.md", StandardCharsets.UTF_8))) {
      StringBuilder buffer = new StringBuilder();
      while (br.ready()) {
        buffer.append(br.readLine());
      }
      validPageStr = buffer.toString();
    } catch (Exception e) {
      fail();
    }
  }
}
