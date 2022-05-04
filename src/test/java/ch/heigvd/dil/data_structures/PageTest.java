package ch.heigvd.dil.data_structures;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import static org.junit.Assert.assertFalse;

public class PageTest {

  private static final String resourcesPath = "src/test/resources/testFiles/";
  private String validPageStr;
  private Site.Config siteConf;


  @Test
  public void createPageFromValidFileShouldNotThrow() {
    boolean thrown = false;
    try {
      new Page(validPageStr, siteConf);
    } catch (ParseException e) {
      thrown = true;
    }
    assertFalse(thrown);
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
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException();
    }
  }




}