package ch.heigvd.dil.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Resources {

  public static String readAsString(String path) throws IOException, NullPointerException {
    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(Objects.requireNonNull(Resources.class.getResourceAsStream("/" + path))))) {
      ;
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }

  public static JSONObject readAsJSON(String path) {
    JSONObject out;
    try (InputStream in = Resources.class.getResourceAsStream("/" + path)) {
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
