package ch.heigvd.dil.utils.parsers;

import ch.heigvd.dil.data_structures.Site;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ConfigGeneratorTest {
  private JSONObject validConfig = new JSONObject();

  @Before
  public void genJSONs() {
    validConfig.put("title", "titre d'exemple");
    validConfig.put("owner", "Jean Dupont");
    validConfig.put("domain", "example.ch");
  }

  @Test
  public void parseValidConfigShouldNotThrowException() {
    boolean thrown = false;
    String schemaPath = "schema/site-config-schema.json";
    try{
      ConfigGenerator<Site.Config> generator = new ConfigGenerator<>(validConfig.toString(), schemaPath, Site.Config.class);
    } catch (ValidationException e) {
      thrown = true;
    }
    assertFalse(thrown);
  }

}
