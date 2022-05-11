package ch.heigvd.dil.data_structures;

import ch.heigvd.dil.utils.parsers.ConfigGenerator;
import org.everit.json.schema.ValidationException;

import java.nio.file.Path;
import java.util.ArrayList;

/** Représente un site. */
public class Site {
  private final Site.Config config;
  private final Path path;
  private final ArrayList<Page> pages = new ArrayList<>();

  /**
   * @param config La configuration du site
   * @param path Le chemin source du site
   */
  public Site(Config config, Path path) {
    this.config = config;
    this.path = path;
  }

  public Site(String configFileContent, Path path) throws ValidationException {
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

  public void addPage(Page page) {
    pages.add(page);
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

  public Path getPath() {
    return path;
  }

  /** Représentes la configuration d'un site */
  public static class Config {
    private String title;
    private String owner;
    private String domain;

    /**
     * Constructeur par défaut nécessaire à l'instanciation au moyen d'un JSON
     */
    public Config() {}

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
