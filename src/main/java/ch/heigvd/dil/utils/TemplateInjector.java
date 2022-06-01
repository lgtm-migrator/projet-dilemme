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
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.*;
import java.nio.file.Files;

public class TemplateInjector {
  private final Handlebars handlebars;
  private final Site site;

  public TemplateInjector(Site site) {
    TemplateLoader loader = new ClassPathTemplateLoader(site.getPath() + "/template", ".html");
    handlebars = new Handlebars(loader);
    this.site = site;
  }

  public String resolveProperties(File layout, Page page) throws IOException {
    String content = Files.readString(layout.toPath());
    return resolveProperties(content, page);
  }

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

  public JsonNode convertToNode(String json) throws IOException {
    return new ObjectMapper().readTree(json);
  }

  public String mergeJSON(Page page) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    ObjectNode rootNode = mapper.createObjectNode();

    ObjectNode page_node = mapper.readValue(page.getPageConfig().getJSON(), ObjectNode.class);
    rootNode.set("page", page_node);

    ObjectNode site_node = mapper.readValue(site.getConfig().getJSON(), ObjectNode.class);
    rootNode.set("site", site_node);

    String html = MarkdownParser.convertMarkdownToHTML(page.getMarkdown());
    rootNode.put("content", html);

    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
  }

  public String getDefaultLayout() throws IOException {
    return Resources.readAsString("template/default_page.html");
  }
}
