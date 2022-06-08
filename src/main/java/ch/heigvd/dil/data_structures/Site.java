package ch.heigvd.dil.data_structures;

import ch.heigvd.dil.utils.parsers.ConfigGenerator;
import java.nio.file.Path;
import java.util.ArrayList;
import org.everit.json.schema.ValidationException;

/** Représente un site. */
public class Site {
  private final Site.Config config;
  private final Path path;
  private final ArrayList<Page> pages = new ArrayList<>();

  /**
   * Construit un site ayant pour chemin (répertoire) le chemin passé en paramètre.
   *
   * @param config La configuration du site
   * @param path Le chemin source du site
   */
  public Site(Site.Config config, Path path) {
    this.config = config;
    this.path = path;
  }

  public Site(String configFileContent, Path path) throws ValidationException {
    this.path = path;
    String schema = "schema/site-config-schema.json";
    ConfigGenerator<Site.Config> cg =
        new ConfigGenerator<>(configFileContent, schema, Site.Config.class);
    config = cg.getConfigObject();
  }

  /**
   * @return Les pages contenues dans le site
   */
  public ArrayList<Page> retrievePages() {
    return pages;
  }

  /**
   * Ajout une page au site.
   *
   * @param page Page à ajouter
   */
  public void addPage(Page page) {
    pages.add(page);
  }

  /**
   * @return la configuration du site
   */
  public Site.Config getConfig() {
    return config;
  }

  /**
   * @return Le chemin du site
   */
  public Path getPath() {
    return path;
  }

  /** Représentes la configuration d'un site */
  public static class Config {
    private String owner;
    private String domain;
    private String title;

    /** Constructeur par défaut nécessaire à l'instanciation au moyen d'un JSON */
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
     * Défini le propriétaire du site.
     *
     * @param owner Le propriétaire du site
     */
    public void setOwner(String owner) {
      this.owner = owner;
    }

    /**
     * Défini le domaine du site.
     *
     * @param domain Le domain du site
     */
    public void setDomain(String domain) {
      this.domain = domain;
    }

    /**
     * Défini le titre du site.
     *
     * @param title Le titre du site
     */
    public void setTitle(String title) {
      this.title = title;
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
