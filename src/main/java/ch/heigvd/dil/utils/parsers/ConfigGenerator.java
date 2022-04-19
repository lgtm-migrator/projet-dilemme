package ch.heigvd.dil.utils.parsers;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;

public class ConfigGenerator {

    private final JSONObject validConfig;

    public ConfigGenerator (String config, String schemaPath) {
        try {
            validConfig = validateConfig(config, readSchema(schemaPath));
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error in config");
        }
    }

    public JSONObject getValidConfig() {
        return validConfig;
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
