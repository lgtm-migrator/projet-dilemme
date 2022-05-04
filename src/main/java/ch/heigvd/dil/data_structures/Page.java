package ch.heigvd.dil.data_structures;

import ch.heigvd.dil.utils.parsers.PageContentSeparator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.text.ParseException;
import java.time.LocalDate;

/** Représente une page. */
public class Page {
  private final Page.Config pageConfig;
  private final Site.Config siteConfig;
  private final String markdown;

  /**
   * Construit une page.
   *
   * @param markdown Contenu markdown dans le corps de la page.
   */
  public Page(Page.Config pageConfig, Site.Config siteConfig, String markdown) {
    this.pageConfig = pageConfig;
    this.siteConfig = siteConfig;
    this.markdown = markdown;
  }

  public Page(String fileContent, Site.Config siteConfig) throws ParseException {
    PageContentSeparator sep = new PageContentSeparator(fileContent);
    pageConfig = sep.getConfig();
    markdown = sep.getContent();
    this.siteConfig = siteConfig;
  }

  /**
   * @return La configuration de la page.
   */
  public Page.Config getPageConfig() {
    return pageConfig;
  }

  /**
   * @return La configuration du site.
   */
  public Site.Config getSiteConfig() {
    return siteConfig;
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
