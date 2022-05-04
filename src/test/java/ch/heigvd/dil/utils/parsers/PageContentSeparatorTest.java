package ch.heigvd.dil.utils.parsers;

import static org.junit.Assert.*;

import ch.heigvd.dil.data_structures.Page;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class PageContentSeparatorTest {

  private final JSONObject validConfig = new JSONObject();

  @Before
  public void genJSONs() {
    validConfig.put("title", "titre d'exemple");
    validConfig.put("author", "titre d'exemple");
    validConfig.put("date", "2022-10-12");
  }

  @Test
  public void testValidPageFileShouldReturnCorrectContentAndConfig() {
    String md =
        "{\n"
            + "    \"title\" : \"title example\",\n"
            + "    \"author\" : \"Eliott Chytil\",\n"
            + "    \"date\" : \"2021-03-10\"\n"
            + "}\n"
            + "---\n"
            + "# Titre au format md\n"
            + "\n## sous-titre\n"
            + "\nCeci est le contenu de l'article\n"
            + "\n## sous-titre 2\n"
            + "\nCeci ...";
    try {
      PageContentSeparator separator = new PageContentSeparator(md);
      String content = separator.getContent();
      assertEquals(
          content,
          "\n# Titre au format md\n"
              + "\n## sous-titre\n"
              + "\nCeci est le contenu de l'article\n"
              + "\n## sous-titre 2\n"
              + "\nCeci ...");
      Page.Config config = separator.getConfig();
      assertEquals(config.getTitle(), "title example");
      assertEquals(config.getAuthor(), "Eliott Chytil");
      assertEquals(
          config.getDate(),
          LocalDate.parse("2021-03-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    } catch (ParseException e) {
      fail();
    }
  }
}
