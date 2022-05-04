package ch.heigvd.dil.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileHandlerTest {
  @Test
  public void testWriteHaveCorrectContent() {
    try {
      File f = new File("test.txt");
      FileHandler.write(f, "Hello world!\nHey");
      String content = FileHandler.read(f);
      assertEquals("Hello world!\nHey\n", content);
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testDeleteExistentFile() {
    File f = new File("test.txt");
    boolean status = FileHandler.delete(f);
    assertTrue(status);
  }

  @Test
  public void testDeleteNonexistentFile() {
    File f = new File("NonExistentFile.txt");
    boolean status = FileHandler.delete(f);
    assertFalse(status);
  }
}
