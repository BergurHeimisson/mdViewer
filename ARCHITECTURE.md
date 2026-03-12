# Architecture

## Overview

Single-window Swing application. No database, no network I/O. The rendering pipeline is: raw Markdown text → CommonMark AST → HTML fragment → `<br>` injection before headings → Dracula-themed HTML document → `JEditorPane`.

## Package layout

```
com.mdviewer
├── App.java               Entry point. Sets up L&F, reads CLI arg, creates MainWindow.
├── FlatDraculaTheme.java  Extends FlatDarkLaf; loads colour overrides from .properties.
├── MainWindow.java        The single JFrame. Owns the scroll pane, drop target, key bindings,
│                          sibling navigation, and window preference save/restore.
└── MarkdownRenderer.java  Stateless. Parses Markdown via CommonMark and wraps the output
                           in a self-contained HTML document with inline Dracula CSS.
```

## Key decisions

**`JEditorPane` over JavaFX WebView**
Keeps the runtime dependency footprint minimal — no JavaFX required. The tradeoff is a limited HTML/CSS engine: `em`/`rem` units are ignored, so font sizes use `px`; many modern CSS properties are unsupported. The inline stylesheet is built manually in `MarkdownRenderer.buildCss()` to work within these constraints.

**`<br>` injection before headings**
`JEditorPane`'s HTML engine ignores large `margin-top` values, so a blank line above each heading cannot be achieved via CSS alone. `MarkdownRenderer.render()` does a regex replace on the rendered HTML body to prepend `<br>` before every `<h1>`–`<h6>` tag.

**Custom `StyleSheet` on `HTMLEditorKit`**
`JEditorPane` injects its own default stylesheet (serif fonts, blue links) before rendering. This is replaced with a blank `StyleSheet` so the Dracula CSS from the HTML document takes full effect without interference.

**`FlatDraculaTheme` extending `FlatDarkLaf`**
Only the Dracula-specific colour overrides live in `FlatDraculaTheme.properties`; all other dark defaults are inherited from `FlatDarkLaf`. This avoids duplicating the full FlatLaf default palette.

**Drag-and-drop on the editor pane surface**
`DropTarget` is registered directly on the `JEditorPane`, which covers the entire visible window area. A purple border highlight on `dragEnter` provides visual feedback without any additional overlay component.

**Arrow key navigation bound on `JEditorPane` directly**
Left/right arrow keys are bound on the `JEditorPane`'s own `WHEN_FOCUSED` input map, overriding its built-in cursor-movement bindings. Binding them on the root pane (`WHEN_IN_FOCUSED_WINDOW`) does not work because the editor pane consumes arrow events first. When a file is opened, `rebuildSiblings()` scans the parent directory for `.md`/`.markdown` files, sorts them alphabetically, and stores them for O(1) index lookup during navigation.

**Absolute path normalisation on open**
All `File` arguments are resolved to absolute paths via `getAbsoluteFile()` before use. `getParentFile()` returns `null` for bare filenames (e.g. `ARCHITECTURE.md` with no directory component), which would cause a `NullPointerException` in `rebuildSiblings`.

**`java.util.prefs.Preferences`**
Cross-platform persistent storage for window bounds. No config file to manage or clean up.

## Dependencies

| Library | Purpose |
|---|---|
| `flatlaf` | Dark look-and-feel (Dracula theme base) |
| `flatlaf-extras` | `FlatAnimatedLafChange` for smooth theme transitions |
| `commonmark` | Markdown parser and HTML renderer |
| `commonmark-ext-gfm-tables` | GitHub-Flavored Markdown tables |
| `commonmark-ext-gfm-strikethrough` | `~~strikethrough~~` syntax |
| `commonmark-ext-task-list-items` | `- [x]` task lists |
| `commonmark-ext-autolink` | Auto-linkify bare URLs |
| `commonmark-ext-heading-anchor` | Anchor IDs on headings |

## Launcher script

`mdviewer` (shell script at project root) uses the Homebrew JDK directly and passes `--enable-native-access=ALL-UNNAMED` to suppress the FlatLaf restricted-method warning on Java 25. An alias in `~/.zshrc` makes it available system-wide:

```sh
alias mdviewer='/Users/bergurheimisson/ai_code/mdViewer/mdviewer'
```
