package ch.heigvd.dil.utils.parsers;

import ch.heigvd.dil.utils.Resources;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Permet de générer un objet configuration à partir d'une configuration au format JSON.
 *
 * @param <T> Le type de la configuration à générer.
 */
public class ConfigGenerator<T> {

  private final JSONObject validConfig;
  private final Class<T> configType;

  /**
   * Construit un générateur de configuration.
   *
   * @param config La configuration au format JSON
   * @param schemaPath Le chemin vers le schéma de validation
   * @param targetClass Le type de la configuration à générer
   * @throws ValidationException si la configuration JSON n'est pas valide
   */
  public ConfigGenerator(String config, String schemaPath, Class<T> targetClass)
      throws ValidationException {
    configType = targetClass;
    validConfig = validateConfig(config, Resources.readAsJSON(schemaPath));
  }

  /**
   * Construit l'objet configuration par interpolation entre la classe passée en paramètre du
   * constructeur et la structure de la configuration. Lève une erreur Runtime si l'interpolation ne
   * fonctionne pas.
   *
   * @return L'objet de configuration
   */
  public T getConfigObject() {
    ObjectMapper objectMapper = new ObjectMapper();
    T out;
    try {
      out = objectMapper.readValue(validConfig.toString(), configType);
    } catch (JsonProcessingException e) {
      // Cette erreur n'est pas dépendante de l'utilisateur et ne devrait pas se produire,
      // sauf si on fait des erreurs de programmation
      System.err.println(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
    return out;
  }

  /**
   * Valide la configuration au format JSON selon le schéma.
   *
   * @param strConfig La configuration au format JSON
   * @param jsonSchema Le schéma de validation
   * @return Un objet JSON
   * @throws ValidationException si la configuration JSON n'est pas valide
   */
  private static JSONObject validateConfig(String strConfig, JSONObject jsonSchema)
      throws ValidationException {
    Schema schema = SchemaLoader.load(jsonSchema);
    JSONObject jsonSubject = new JSONObject(new JSONTokener(strConfig));
    schema.validate(jsonSubject);
    return jsonSubject;
  }
}
