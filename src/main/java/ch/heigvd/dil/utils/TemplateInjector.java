package ch.heigvd.dil.utils;

import ch.heigvd.dil.data_structures.Page;
import ch.heigvd.dil.data_structures.Site;
import ch.heigvd.dil.utils.parsers.MarkdownParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.*;
import java.nio.file.Files;

/** Permet de résoudre les variables Handlebars dans un fichier. */
public class TemplateInjector {
  private final Handlebars handlebars;
  private final Site site;

  /**
   * Construit un injecteur de template.
   *
   * @param site Le site concerné
   */
  public TemplateInjector(Site site) {
    String templatePath = site.getPath() + "/template";
    String suffix = ".html";
    TemplateLoader fileLoader = new FileTemplateLoader(templatePath, suffix);
    TemplateLoader classPathLoader = new ClassPathTemplateLoader(templatePath, suffix);

    handlebars = new Handlebars().with(fileLoader, classPathLoader);
    this.site = site;
  }

  /**
   * Résout les variables Handlebars (propriétés) du layout.
   *
   * @param layout Le layout à utiliser
   * @param page La page à résoudre
   * @return La page finale au format HTML, avec les propriétés résolues
   * @throws IOException si le JSON permettant de résoudre les propriétés n'a pas pu être généré
   *     correctement
   */
  public String resolveProperties(File layout, Page page) throws IOException {
    String content = Files.readString(layout.toPath());
    return resolveProperties(content, page);
  }

  /**
   * Résout les variables Handlebars (propriétés) du layout.
   *
   * @param layout Le layout à utiliser
   * @param page La page à résoudre
   * @return La page finale au format HTML, avec les propriétés résolues
   * @throws IOException si le JSON permettant de résoudre les propriétés n'a pas pu être généré
   *     correctement
   */
  public String resolveProperties(String layout, Page page) throws IOException {
    Template template = handlebars.compileInline(layout);
    JsonNode jsonNode = convertToNode(mergeJSON(page));

    Context context =
        Context.newBuilder(jsonNode)
            .resolver(
                JsonNodeValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MapValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE)
            .build();
    return template.apply(context);
  }

  /**
   * Convertit une chaine de caractère JSON en objet JSON.
   *
   * @param json La chaine de caractère JSON
   * @return L'objet JSON correspondant
   * @throws IOException si l'objet JSON n'a pas pu être généré correctement
   */
  public JsonNode convertToNode(String json) throws IOException {
    return new ObjectMapper().readTree(json);
  }

  /**
   * Fusionne les propriétés de la page avec les propriétés du site.
   *
   * @param page La page à résoudre
   * @return La chaîne de caractère JSON
   * @throws IOException si le JSON n'a pas pu être généré correctement
   */
  public String mergeJSON(Page page) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    ObjectNode rootNode = mapper.createObjectNode();

    ObjectNode page_node = mapper.readValue(page.getConfig().getJSON(), ObjectNode.class);
    rootNode.set("page", page_node);

    ObjectNode site_node = mapper.readValue(site.getConfig().getJSON(), ObjectNode.class);
    rootNode.set("site", site_node);

    String html = MarkdownParser.convertMarkdownToHTML(page.getMarkdown());
    rootNode.put("content", html);

    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
  }

  /**
   * Retourne le layout par défaut d'une page.
   *
   * @return Le layout par défaut d'une page
   * @throws IOException si la ressource n'a pas pu être lue
   */
  public String getDefaultLayout() throws IOException {
    return Resources.readAsString("template/default_page.html");
  }
}
