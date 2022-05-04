package ch.heigvd.dil.utils.parsers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ch.heigvd.dil.data_structures.Site;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class ConfigGeneratorTest {
  private JSONObject validConfig;
  private JSONObject missingDomain;

  @Before
  public void genJSONs() {
    missingDomain = new JSONObject();
    missingDomain.put("title", "titre d'exemple");
    missingDomain.put("owner", "Jean Dupont");
    validConfig = new JSONObject(missingDomain.toString());
    validConfig.put("domain", "example.ch");
  }

  @Test
  public void parseValidConfigShouldNotThrowException() {
    boolean thrown = false;
    String schemaPath = "schema/site-config-schema.json";
    try {
      ConfigGenerator<Site.Config> generator =
          new ConfigGenerator<>(validConfig.toString(), schemaPath, Site.Config.class);
    } catch (ValidationException e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

  @Test
  public void parseInvalidConfigShouldThrowException() {
    boolean thrown = false;
    String schemaPath = "schema/site-config-schema.json";
    try {
      ConfigGenerator<Site.Config> generator =
          new ConfigGenerator<>(missingDomain.toString(), schemaPath, Site.Config.class);
    } catch (ValidationException e) {
      thrown = true;
    }
    assertTrue(thrown);
  }
}
