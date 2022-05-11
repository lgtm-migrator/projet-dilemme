package ch.heigvd.dil.data_structures;

import ch.heigvd.dil.utils.parsers.PageContentSeparator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.everit.json.schema.ValidationException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;

/** Représente une page. */
public class Page {
  // TODO: retirer la propriété siteConfig
  private final Page.Config pageConfig;
  private final String markdown;
  private final Path path;

  /**
   * Construit une page.
   *
   * @param markdown Contenu markdown dans le corps de la page.
   */
  public Page (Config pageConfig, String markdown) {
    this.pageConfig = pageConfig;
    this.markdown = markdown;
    path = Paths.get("");
  }

  public Page(String fileContent, Path path) throws ParseException, ValidationException {
    PageContentSeparator sep = new PageContentSeparator(fileContent);
    pageConfig = sep.getConfig();
    markdown = sep.getContent();
    this.path = path;
  }

  /**
   * @return La configuration de la page.
   */
  public Page.Config getPageConfig() {
    return pageConfig;
  }

  public Path getPath() {
    return path;
  }

  /**
   * @return Le contenu markdown de la page.
   */
  public String getMarkdown() {
    return markdown;
  }

  /** Représente la configuration d'une page. */
  public static class Config {
    private String title;
    private String author;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * Construit une configuration de page.
     *
     * @param title Le titre de la page.
     * @param author L'auteur de la page.
     * @param date La date de création de la page.
     */
    public Config(String title, String author, LocalDate date) {
      this.title = title;
      this.author = author;
      this.date = date;
    }

    public Config() {}

    /**
     * @return Le titre de la page.
     */
    public String getTitle() {
      return title;
    }

    /**
     * @return L'auteur de la page.
     */
    public String getAuthor() {
      return author;
    }

    /**
     * @return La date de création de la page.
     */
    public LocalDate getDate() {
      return date;
    }

    /**
     * @return Un string représentant la configuration de la page au format JSON.
     */
    public String getJSON() {
      return "{\n  \"title\": \""
          + title
          + "\",\n  \"author\": \""
          + author
          + "\",\n  "
          + "\"date\": \""
          + date.toString() // au format yyyy-MM-dd
          + "\"\n}";
    }
  }
}
