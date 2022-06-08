package ch.heigvd.dil.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.json.JSONTokener;

/** Permet de facilement lire une ressource (interne au fichier JAR). */
public class Resources {

  /**
   * Récupère le contenu d'une ressource en tant que chaine de caractère.
   *
   * @param path Le chemin vers la ressource
   * @return Le contenu de la ressource
   * @throws IOException si la ressource n'a pas pu être lue
   * @throws NullPointerException si la ressource n'existe pas
   */
  public static String readAsString(String path) throws IOException, NullPointerException {
    try (BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(Resources.class.getResourceAsStream("/" + path))))) {
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }

  /**
   * Récupère le contenu d'une ressource en tant que chaine de caractère.
   *
   * @param path Le chemin vers la ressource
   * @return Le contenu de la ressource
   */
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
