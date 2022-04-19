package ch.heigvd.dil.utils.parsers;

import ch.heigvd.dil.data_structures.Page;
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

    ConfigValidator validator = new ConfigValidator(configStr, "schema/page-config-schema.json");
    JSONObject conf = validator.getValidConfig();
    String dateStr = conf.getString("date");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date;

    try {
      date = sdf.parse(dateStr);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException("Bad date formatting in page config");
    }

    return new Page.Config(conf.getString("title"), conf.getString("author"), date);
  }
}
