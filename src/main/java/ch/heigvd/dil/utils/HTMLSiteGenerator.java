package ch.heigvd.dil.utils;

import ch.heigvd.dil.data_structures.Site;

/**
 * Permet de générer un fichier HTML à partir d'un site.
 */
public class HTMLSiteGenerator {
  private final Site site;

  /**
   * @param site Site pour lequel on veut générer le code HTML.
   */
  public HTMLSiteGenerator(Site site) {
    this.site = site;
  }

  /**
   * Genère le site sous forme de code HTML.
   * @param path Chemin de destination où sera généré le site.
   */
  public void generate(String path) {
    // TODO: Implement this method
  }
}
