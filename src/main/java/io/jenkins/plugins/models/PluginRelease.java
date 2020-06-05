package io.jenkins.plugins.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.issues.GfmIssuesExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.gfm.users.GfmUsersExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PluginRelease {
  private static final HtmlRenderer HTML_RENDERER;
  private static final Parser MARKDOWN_PARSER;
  static {
    MutableDataSet options = new MutableDataSet();

    options.set(Parser.EXTENSIONS, Arrays.asList(
      TablesExtension.create(),
      StrikethroughExtension.create(),
      GfmUsersExtension.create(),
      TaskListExtension.create(),
      GfmIssuesExtension.create(),
      EmojiExtension.create()
    ));
    options.set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB);

    MARKDOWN_PARSER = Parser.builder(options).build();
    HTML_RENDERER = HtmlRenderer.builder(options).escapeHtml(true).build();
  }
  @JsonProperty("tag_name") final private String tagName;
  @JsonProperty("name") final private String name;
  @JsonProperty("published_at") final private Date publishedAt;
  private String body;

  @JsonProperty("bodyHTML") public String getBodyHTML() {
    return HTML_RENDERER.render(MARKDOWN_PARSER.parse(this.body.replaceAll("<!--.*?-->", "")));
  }


  @JsonCreator
  public PluginRelease(@JsonProperty("tag_name") String tagName, @JsonProperty("name") String name, @JsonProperty("published_at") Date publishedAt, @JsonProperty("body") String body) {
    this.tagName = tagName;
    this.name = name;
    this.publishedAt = publishedAt;
    this.body = body;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PluginRelease)) {
      return false;
    }
    return this.tagName.equals(((PluginRelease) obj).tagName) &&
      this.name.equals(((PluginRelease) obj).name) &&
      this.publishedAt.equals(((PluginRelease) obj).publishedAt) &&
      this.body.equals(((PluginRelease) obj).body);
  }
}
