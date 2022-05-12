package ch.heigvd.dil.data_structures;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class SiteTest {

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
  public void createSiteFromValidConfigShouldNotThrow() {
    try{
      new Site(validConfig.toString(), Paths.get(""));
    } catch (Exception e) {
      fail();
    }

  }


}
