package ch.heigvd.dil.utils.parsers;

import ch.heigvd.dil.data_structures.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Permet de lire un fichier représentant une page et de parser
 * la partie contenu et la partie configuration.
 */
public class PageContentSeparator {
  private static final String SEP = "---";
  private final String content;
  private final Page.Config config;

  /**
   * Le constructeur sépare la partie contenu de la partie configuration
   * et se charge de vérifier la conformité de la configuration.
   * @param file String contenant la page non parsée
   * @throws ParseException si la page ne contient pas de séparateur
   */
  public PageContentSeparator(String file) throws ParseException {
    int index = file.indexOf(SEP);
    if (index == -1) {
      throw new ParseException("Invalid page format", 0);
    }
    config = genPageConfig(file.substring(0, index));
    content = file.substring(index + SEP.length());
  }

  /**
   * Récupère la partie contenu de la page
   * @return contenu de la page
   */
  public String getContent() {
    return content;
  }

  /**
   * Récupère la partie configuration de la page
   * @return configuration valide
   */
  public Page.Config getConfig() {
    return config;
  }

  private static Page.Config genPageConfig(String configStr) {
    String schema = "schema/page-config-schema.json";
    ConfigGenerator<Page.Config> validator = new ConfigGenerator<>(configStr, schema, Page.Config.class);
    return validator.getConfigObject();
  }

}
