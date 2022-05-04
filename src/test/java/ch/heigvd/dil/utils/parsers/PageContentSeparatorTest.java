package ch.heigvd.dil.utils.parsers;

import static org.junit.Assert.*;

import ch.heigvd.dil.data_structures.Page;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class PageContentSeparatorTest {

  private final JSONObject validConfig = new JSONObject();
  private final JSONObject invalidConfig = new JSONObject();
  private final JSONObject incompleteConfig = new JSONObject();

  @Before
  public void genJSONs() {
    validConfig.put("title", "titre d'exemple");
    validConfig.put("author", "titre d'exemple");
    validConfig.put("date", "2022-10-12");

    invalidConfig.put("title", "titre d'exemple");
    invalidConfig.put("author", 1);
    invalidConfig.put("date", "2022-10-12");

    incompleteConfig.put("title", "titre d'exemple");
    incompleteConfig.put("author", "Jeanne");
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
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testInvalidPageHeaderShouldFail() {
    String md = invalidConfig + "---\n" + "Ceci est le contenu de l'article";
    try {
      new PageContentSeparator(md);
      fail();
    } catch (ValidationException e) {
      assertTrue(true);
    } catch (ParseException e) {
      fail();
    }
  }

  @Test
  public void testIncompletePageHeaderShouldFail() {
    String md = incompleteConfig + "---\n" + "Ceci est le contenu de l'article";
    try {
      new PageContentSeparator(md);
      fail();
    } catch (ValidationException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }
}
