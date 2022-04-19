package ch.heigvd.dil.utils.parsers;

import ch.heigvd.dil.data_structures.Page;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PageContentSeparator {
  private static final String SEP = "---";
  private final String content;
  private final String config;

  public PageContentSeparator(String file) throws ParseException {
    int index = file.indexOf(SEP);
    if (index == -1) {
      throw new ParseException("Invalid page format", 0);
    }
    config = file.substring(0, index);
    content = file.substring(index + SEP.length());
  }

  public String getContent() {
    return content;
  }

  public Page.Config getConfig() {
    return genPageConfig(config);
  }

  private static JSONObject readSchema() {
    JSONObject out;
    try (FileInputStream fis = new FileInputStream("schema/page-config-schema.json")) {
      out = new JSONObject(new JSONTokener(fis));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException();
    }
    return out;
  }

  private static JSONObject validateConfig(String strConfig, JSONObject jsonSchema) {
    Schema schema = SchemaLoader.load(jsonSchema);
    JSONObject jsonSubject = new JSONObject(new JSONTokener(strConfig));
    try {
      schema.validate(jsonSubject);
    } catch (ValidationException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException("Error in page config");
    }
    return jsonSubject;
  }

  private static Page.Config genPageConfig(String configStr) {
    JSONObject conf = validateConfig(configStr, readSchema());
    String dateStr = conf.getString("date");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date date;

    try {
      date = sdf.parse(dateStr);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      throw new RuntimeException("Bad date formatting in page config");
    }

    return new Page.Config(conf.getString("title"), conf.getString("author"), date);
  }
}
