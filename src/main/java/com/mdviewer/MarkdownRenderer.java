package com.mdviewer;

import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * Converts raw Markdown text to a self-contained HTML document
 * with Dracula-themed inline CSS.
 *
 * All rendering is stateless and thread-safe after construction.
 */
public class MarkdownRenderer {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownRenderer() {
        List<org.commonmark.Extension> extensions = Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                TaskListItemsExtension.create(),
                AutolinkExtension.create(),
                HeadingAnchorExtension.create()
        );
        parser = Parser.builder()
                .extensions(extensions)
                .build();
        renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();
    }

    /**
     * Renders the given Markdown source to a complete HTML document string.
     *
     * @param markdown raw Markdown text
     * @return full HTML document with embedded Dracula CSS
     */
    public String render(String markdown) {
        Node document = parser.parse(markdown);
        String body = renderer.render(document);
        // JEditorPane ignores large margin-top values, so inject a <br> before each heading
        body = body.replaceAll("<h([1-6])", "<br><h$1");
        return wrapHtml(body);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private String wrapHtml(String body) {
        return "<!DOCTYPE html>\n"
             + "<html>\n"
             + "<head>\n"
             + "<meta charset=\"UTF-8\">\n"
             + "<style>\n"
             + buildCss()
             + "</style>\n"
             + "</head>\n"
             + "<body>\n"
             + body
             + "\n</body>\n"
             + "</html>";
    }

    /**
     * Returns a CSS string using the Dracula colour palette.
     * Font sizes are in px for predictable rendering in JEditorPane's
     * limited HTML engine (which ignores em/rem).
     */
    private String buildCss() {
        final String bg       = App.COLOR_BG;
        final String fg       = App.COLOR_FG;
        final String comment  = App.COLOR_COMMENT;
        final String cyan     = App.COLOR_CYAN;
        final String green    = App.COLOR_GREEN;
        final String orange   = App.COLOR_ORANGE;
        final String pink     = App.COLOR_PINK;
        final String purple   = App.COLOR_PURPLE;
        final String current  = App.COLOR_CURRENT;
        final String yellow   = App.COLOR_YELLOW;

        return "body {\n"
             + "  background-color: " + bg + ";\n"
             + "  color: " + fg + ";\n"
             + "  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', "
             +           "Helvetica, Arial, sans-serif;\n"
             + "  font-size: 13px;\n"
             + "  line-height: 1.7;\n"
             + "  margin: 32px 48px;\n"
             + "  max-width: 860px;\n"
             + "}\n"

             // Headings — Dracula pink/purple gradient via individual colours
             + "h1, h2, h3, h4, h5, h6 {\n"
             + "  color: " + purple + ";\n"
             + "  font-weight: 600;\n"
             + "  margin-top: 0.4em;\n"
             + "  margin-bottom: 0.4em;\n"
             + "}\n"
             + "h1 { font-size: 2em; color: " + fg + "; border-bottom: 2px solid " + current + "; padding-bottom: 0.25em; }\n"
             + "h2 { font-size: 1.5em; border-bottom: 1px solid " + current + "; padding-bottom: 0.2em; }\n"
             + "h3 { font-size: 1.25em; }\n"

             // Links
             + "a { color: " + cyan + "; text-decoration: none; }\n"
             + "a:hover { text-decoration: underline; }\n"

             // Inline code
             + "code {\n"
             + "  background-color: " + current + ";\n"
             + "  color: " + green + ";\n"
             + "  font-family: 'JetBrains Mono', 'Fira Code', 'Consolas', monospace;\n"
             + "  font-size: 0.875em;\n"
             + "  padding: 2px 5px;\n"
             + "  border-radius: 4px;\n"
             + "}\n"

             // Fenced code blocks
             + "pre {\n"
             + "  background-color: " + current + ";\n"
             + "  color: " + green + ";\n"
             + "  font-family: 'JetBrains Mono', 'Fira Code', 'Consolas', monospace;\n"
             + "  font-size: 0.875em;\n"
             + "  padding: 16px 20px;\n"
             + "  border-radius: 6px;\n"
             + "  overflow-x: auto;\n"
             + "  line-height: 1.5;\n"
             + "}\n"
             + "pre code {\n"
             + "  background: none;\n"
             + "  padding: 0;\n"
             + "  border-radius: 0;\n"
             + "}\n"

             // Blockquotes
             + "blockquote {\n"
             + "  border-left: 4px solid " + comment + ";\n"
             + "  margin: 1em 0;\n"
             + "  padding: 4px 20px;\n"
             + "  color: " + comment + ";\n"
             + "}\n"

             // Tables
             + "table {\n"
             + "  border-collapse: collapse;\n"
             + "  width: 100%;\n"
             + "  margin: 1em 0;\n"
             + "}\n"
             + "th {\n"
             + "  background-color: " + current + ";\n"
             + "  color: " + pink + ";\n"
             + "  padding: 8px 12px;\n"
             + "  text-align: left;\n"
             + "  border-bottom: 2px solid " + comment + ";\n"
             + "}\n"
             + "td {\n"
             + "  padding: 7px 12px;\n"
             + "  border-bottom: 1px solid " + current + ";\n"
             + "}\n"
             + "tr:hover { background-color: " + current + "; }\n"

             // Horizontal rule
             + "hr { border: none; border-top: 1px solid " + current + "; margin: 2em 0; }\n"

             // Strikethrough (from GFM extension)
             + "del { color: " + comment + "; }\n"

             // Lists
             + "ul, ol { padding-left: 1.8em; }\n"
             + "li { margin: 0.3em 0; }\n"

             // Strong / em
             + "strong { color: " + orange + "; font-weight: 700; }\n"
             + "em { color: " + yellow + "; font-style: italic; }\n";
    }
}
