package ch.heigvd.dil.data_structures;

import java.util.Date;

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
    private final String title;
    private final String author;
    private final Date date;

    /**
     * Construit une configuration de page.
     *
     * @param title Le titre de la page.
     * @param author L'auteur de la page.
     * @param date La date de création de la page.
     */
    public Config(String title, String author, Date date) {
      this.title = title;
      this.author = author;
      this.date = date;
    }

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
    public Date getDate() {
      return date;
    }
  }
}
