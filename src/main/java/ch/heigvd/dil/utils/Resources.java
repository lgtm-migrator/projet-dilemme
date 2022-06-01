package ch.heigvd.dil.utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Resources {

  private static InputStream getResourceAsStream(String path) {
    return Resources.class.getResourceAsStream(path);
  }

  public static String readAsString(String path) throws IOException {
    StringBuilder result = new StringBuilder();
    try (BufferedReader in = new BufferedReader(new InputStreamReader(getResourceAsStream("/" + path)))) {
      String line;
      while ((line = in.readLine()) != null) result.append(line);
    }
    return result.toString();
  }

  public static JSONObject readAsJSON(String path) {
    JSONObject out;
    try (InputStream in = getResourceAsStream("/" + path)) {
      if (in != null) {
        out = new JSONObject(new JSONTokener(in));
      } else {
        throw new RuntimeException("Application resource not found : JSON schema not found");
      }
    } catch (IOException e) {
      // Cette erreur n'est pas dépendante de l'utilisateur et ne devrait pas se produire,
      // sauf si on fait des erreurs de programmation.
      throw new RuntimeException(e.getMessage());
    }
    return out;
  }
}
