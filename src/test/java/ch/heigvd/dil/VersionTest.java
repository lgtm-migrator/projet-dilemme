package ch.heigvd.dil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
