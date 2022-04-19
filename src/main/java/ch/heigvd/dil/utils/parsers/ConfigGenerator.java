package ch.heigvd.dil.utils.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Permet de générer un objet configuration à partir d'une configuration au format JSON.
 * @param <T> Le type de la configuration à générer.
 */
public class ConfigGenerator<T> {

    private final JSONObject validConfig;
    private final Class<T> configType;

    public ConfigGenerator (String config, String schemaPath, Class<T> targetClass) {
        configType = targetClass;
        try {
            validConfig = validateConfig(config, readSchema(schemaPath));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error in config");
        }

    }

    /**
     * Construit l'objet configuration par interpolation entre la classe passée en paramètre du
     * constructeur et la structure de la configuration.
     * Lève une erreur Runtime si l'interpolation ne fonctionne pas.
     * @return L'objet de configuration
     */
    public T getConfigObject() {
        ObjectMapper objectMapper = new ObjectMapper();
        T config;
        try {
            config = objectMapper.readValue(validConfig.toString(), configType);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error while creating Page.Config instance.");
        }
        return config;
    }

    private static JSONObject readSchema(String schemaPath) {
        JSONObject out;
        try (FileInputStream fis = new FileInputStream(schemaPath)) {
            out = new JSONObject(new JSONTokener(fis));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
        return out;
    }

    private static JSONObject validateConfig(String strConfig, JSONObject jsonSchema) throws ValidationException {
        Schema schema = SchemaLoader.load(jsonSchema);
        JSONObject jsonSubject = new JSONObject(new JSONTokener(strConfig));
        schema.validate(jsonSubject);
        return jsonSubject;
    }


}
