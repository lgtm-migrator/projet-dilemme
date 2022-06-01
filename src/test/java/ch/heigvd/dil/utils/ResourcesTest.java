package ch.heigvd.dil.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ResourcesTest {
  @Test
  public void shouldReadStringWithEOL() {
    try {
      String result = Resources.readAsString("testFiles/test-resources/resource_with_EOL.txt");
      assertEquals("Hey\n" + "How\n" + "Are\n" + "You ?\n", result);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void shouldReadStringWithoutEOL() {
    try {
      String result = Resources.readAsString("testFiles/test-resources/resource_without_EOL.txt");
      assertEquals("File without EOL.", result);
    } catch (Exception e) {
      fail();
    }
  }
}
