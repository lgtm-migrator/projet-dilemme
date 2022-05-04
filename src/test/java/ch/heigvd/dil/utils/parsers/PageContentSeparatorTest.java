package ch.heigvd.dil.utils.parsers;

import static org.junit.Assert.*;

import ch.heigvd.dil.data_structures.Page;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class PageContentSeparatorTest {

  private JSONObject validConfig = new JSONObject();
  private String validPageStr;
  private Page validPage;
  private static final String resourcesPath = "src/test/resources/testFiles/";

  @Before
  public void genJSONs() {
    validConfig.put("title", "titre d'exemple");
    validConfig.put("author", "titre d'exemple");
    validConfig.put("date", "2022-10-12");
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

  @Test
  public void parseValidPageFileShouldNotThrowException() {
    boolean thrown = false;
    try {
      PageContentSeparator separator = new PageContentSeparator(validPageStr);
      // validPage = new Page(separator.getConfig(), separator.getContent())
      separator.getContent();
      separator.getConfig();
    } catch (ParseException | ValidationException e) {
      thrown = true;
    }
    assertFalse(thrown);
  }
}
