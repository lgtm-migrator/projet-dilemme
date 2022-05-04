package ch.heigvd.dil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VersionTest {
  @Test
  public void testVersionShouldReturnCorrectVersion() {
    Version v = new Version(5, 2, 4);
    assertEquals("5.2.4", v.toString());
    assertEquals(5, v.getMajor());
    assertEquals(2, v.getMinor());
    assertEquals(4, v.getPatch());
  }
}
