package ch.heigvd.dil.utils.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
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

  public ConfigGenerator(String config, String schemaPath, Class<T> targetClass)
      throws ValidationException {
    configType = targetClass;
    validConfig = validateConfig(config, readSchema(schemaPath));
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
      // sauf si on fait des erreurs de programmation.
      throw new RuntimeException(e.getMessage());
    }
    return out;
  }

  private static JSONObject readSchema(String schemaPath) {
    JSONObject out;
    try (InputStream in = ConfigGenerator.class.getResourceAsStream("/" + schemaPath)) {
      if (in != null) {
        out = new JSONObject(new JSONTokener(in));
      } else {
        throw new RuntimeException("Application resource not found : JSON schema not found");
      }
    } catch (IOException e) {
      // Cette erreur n'est pas dépendante de l'utilisateur et ne devrait pas se produire,
      // sauf si on fait des erreurs de programmation.
      throw new RuntimeException(e.getMessage());
    }
    return out;
  }

  private static JSONObject validateConfig(String strConfig, JSONObject jsonSchema)
      throws ValidationException {
    Schema schema = SchemaLoader.load(jsonSchema);
    JSONObject jsonSubject = new JSONObject(new JSONTokener(strConfig));
    schema.validate(jsonSubject);
    return jsonSubject;
  }
}
