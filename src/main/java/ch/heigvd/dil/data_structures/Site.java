package ch.heigvd.dil.data_structures;

import java.nio.file.Path;
import java.util.ArrayList;

import ch.heigvd.dil.utils.parsers.ConfigGenerator;

/** Représente un site. */
public class Site {
  private final Site.Config config;
  private final Path path;
  private ArrayList<Page> pages;

  /**
   * @param config La configuration du site
   * @param path Le chemin source du site
   */
  public Site(Config config, Path path) {
    this.config = config;
    this.path = path;
  }

  public Site(String configFileContent, Path path) {
    this.path = path;
    String schema = "schema/site-config-schema.json";
    ConfigGenerator<Config> cg = new ConfigGenerator<>(configFileContent, schema, Config.class);
    config = cg.getConfigObject();
  }

  /**
   * @return Les pages contenues dans le site
   */
  public ArrayList<Page> retrievePages() {
    return pages;
  }


  public String getTitle() {
    return config.getTitle();
  }


  public String getOwner() {
    return config.getOwner();
  }

  public String getDomain() {
    return config.getDomain();
  }

  public String configToJSON() {
    return config.getJSON();
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
    private String getTitle() {
      return title;
    }

    /**
     * @return Le propriétaire du site
     */
    private String getOwner() {
      return owner;
    }

    /**
     * @return Le domaine du site
     */
    private String getDomain() {
      return domain;
    }

    /**
     * @return Un string représentant la configuration du site au format JSON
     */
    public String getJSON() {
      return "{"
          + "\n  \"title\": \""
          + title
          + "\",\n  \"owner\": \""
          + owner
          + "\",\n  \"domain\":"
          + " \""
          + domain
          + "\"\n}";
    }
  }
}
