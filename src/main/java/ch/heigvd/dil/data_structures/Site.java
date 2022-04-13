package ch.heigvd.dil.data_structures;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

/** Représente un site. */
public class Site {
  private Site.Config config;
  private final String path;

  /**
   * @param config La configuration du site
   * @param path Le chemin source du site
   */
  public Site(Site.Config config, String path) {
    this.config = config;
    this.path = path;
  }

  /**
   * @return Les pages contenues dans le site
   */
  public List<Page> retrievePages() {
    throw new NotImplementedException("You must implement this method");
  }

  /** Représentes la configuration d'un site */
  public static class Config {
    private final String title;
    private final String owner;
    private final String domain;

    /**
     * @param title Le titre du site
     * @param owner Le propriétaire du site
     * @param domain Le domaine du site
     */
    public Config(String title, String owner, String domain) {
      this.title = title;
      this.owner = owner;
      this.domain = domain;
    }

    /**
     * @return Le titre du site
     */
    public String getTitle() {
      return title;
    }

    /**
     * @return Le propriétaire du site
     */
    public String getOwner() {
      return owner;
    }

    /**
     * @return Le domaine du site
     */
    public String getDomain() {
      return domain;
    }
  }
}
