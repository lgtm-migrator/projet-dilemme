package ch.heigvd.dil.utils.parsers;

import static org.junit.Assert.*;

import java.text.ParseException;
import org.junit.Test;

public class PageContentSeparatorTest {
  @Test
  public void exampleMustBeSeparatedInTwoParts() {
    String configPart = "{'author':'JF', 'date': 01.01.2022'}";
    String content = "# Titre \n Ceci est un paragraphe";
    String separator = "---";
    String whole = configPart + separator + content;
    try {
      PageContentSeparator parser = new PageContentSeparator(whole);
      assertEquals(configPart, parser.getConfig());
      assertEquals(content, parser.getContent());
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
  }
}
