package ch.heigvd.dil.utils.parsers;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Permet de convertir un texte markdown dans un format différent.
 */
public class MarkdownParser {
  /**
   * Convertit une chaine de caractères markdown en HTML.
   *
   * @param markdown La chaine de caractères markdown
   * @return La chaine de caractères HTML
   */
  public static String convertMarkdownToHTML(String markdown) {
    Parser parser = Parser.builder().build();
    Node document = parser.parse(markdown);
    HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
    return htmlRenderer.render(document);
  }
}
