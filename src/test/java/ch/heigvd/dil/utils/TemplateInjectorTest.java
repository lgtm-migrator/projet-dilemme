package ch.heigvd.dil.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import ch.heigvd.dil.data_structures.Page;
import ch.heigvd.dil.data_structures.Site;
import ch.heigvd.dil.utils.parsers.PageContentSeparator;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

public class TemplateInjectorTest {
  private static final String RESOURCES_PATH = "src/test/resources/";
  private Page page;
  private TemplateInjector injector;
  private final File layout =
      new File(RESOURCES_PATH + "testFiles/test-template/layout-valid.html");

  private final Site.Config siteConfig = new Site.Config("Mon Blog", "Charles", "http://localhost");

  @Before
  public void setUpPage() {
    try {
      String path = RESOURCES_PATH + "testFiles/test-page/test-page-valid.md";
      String content = Files.readString(Paths.get(path));
      PageContentSeparator pcs = new PageContentSeparator(content);
      page = new Page(pcs.getConfig(), pcs.getContent());
    } catch (Exception e) {
      fail();
    }
  }

  private void shouldInjectString(String layout, String expected) {
    try {
      String result = injector.resolveProperties(layout, page);
      assertEquals(expected, result);
    } catch (Exception e) {
      fail();
    }
  }

  @Before
  public void setUpTemplateInjector() {
    injector = new TemplateInjector(siteConfig);
  }

  @Test
  public void shouldInjectPageTitle() {
    shouldInjectString("{{ page.title }}", "title example");
  }

  @Test
  public void shouldInjectPageAuthor() {
    shouldInjectString("{{ page.author }}", "Eliott Chytil");
  }

  @Test
  public void shouldInjectPageDate() {
    shouldInjectString("{{ page.date }}", "2021-03-10");
  }

  @Test
  public void shouldInjectPageContent() {
    shouldInjectString(
        "{{{ content }}}",
        "<h1>Titre au format md</h1>\n"
            + "<h2>sous-titre</h2>\n"
            + "<p>Ceci est le contenu de l'article</p>\n"
            + "<h2>sous-titre 2</h2>\n"
            + "<p>Ceci ...</p>\n");
  }

  @Test
  public void shouldInjectSiteTitle() {}

  @Test
  public void shouldInjectSiteOwner() {}

  @Test
  public void shouldInjectSiteDomain() {}

  @Test
  public void shouldInjectPageProperties() {
    try {
      String result = injector.resolveProperties(layout, page);
      assertEquals(
          "<html lang=\"en\">\n"
              + "<head>\n"
              + "    <meta charset=\"utf-8\">\n"
              + "    <title>Mon Blog | title example</title>\n"
              + "</head>\n"
              + "<body>\n"
              + "<h1>Titre au format md</h1>\n"
              + "<h2>sous-titre</h2>\n"
              + "<p>Ceci est le contenu de l'article</p>\n"
              + "<h2>sous-titre 2</h2>\n"
              + "<p>Ceci ...</p>\n\n"
              + "</body>\n"
              + "</html>",
          result);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void testMergeJSON() {
    try {
      String result = injector.mergeJSON(page).toString();
      assertEquals(
          result,
          "{\n"
              + "  \"page\" : {\n"
              + "    \"title\" : \"title example\",\n"
              + "    \"author\" : \"Eliott Chytil\",\n"
              + "    \"date\" : \"2021-03-10\"\n"
              + "  },\n"
              + "  \"site\" : {\n"
              + "    \"title\" : \"Mon Blog\",\n"
              + "    \"owner\" : \"Charles\",\n"
              + "    \"domain\" : \"http://localhost\"\n"
              + "  },\n"
              + "  \"content\" : \"<h1>Titre au format md</h1>\\n"
              + "<h2>sous-titre</h2>\\n"
              + "<p>Ceci est le contenu de l'article</p>\\n"
              + "<h2>sous-titre 2</h2>\\n"
              + "<p>Ceci ...</p>\\n"
              + "\"\n"
              + "}");
    } catch (Exception e) {
      fail();
    }
  }
}
