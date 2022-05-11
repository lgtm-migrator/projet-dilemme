package ch.heigvd.dil.utils.parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarkdownParserTest {
  @Test
  public void testConvertMarkdownToHtml() {
    String md =
        "# Heading\n"
            + "## Sub-heading\n"
            + "### Sub-sub-heading\n"
            + "*italic*\n"
            + "**bold**\n"
            + "*[link](http://www.google.com)*\n"
            + "*[link with title](http://www.google.com \"Google\")*\n";
    String html = MarkdownParser.convertMarkdownToHTML(md);
    System.out.println(html);
    assertEquals(
        html,
        "<h1>Heading</h1>\n"
            + "<h2>Sub-heading</h2>\n"
            + "<h3>Sub-sub-heading</h3>\n"
            + "<p><em>italic</em>\n"
            + "<strong>bold</strong>\n"
            + "<em><a href=\"http://www.google.com\">link</a></em>\n"
            + "<em><a href=\"http://www.google.com\" title=\"Google\">link with"
            + " title</a></em></p>\n");
  }
}
