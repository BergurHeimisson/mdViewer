# mdViewer

A minimalistic Java Swing application for viewing Markdown files with a Dracula-themed dark UI.

## Features

- Drag-and-drop a `.md` or `.markdown` file onto the window to open it
- Pass a file path as a command-line argument
- Cycle through all `.md` files in the same folder with the arrow keys
- Open files via `Cmd+O`
- Rendered Markdown with support for tables, task lists, strikethrough, autolinks, and heading anchors
- Dracula colour theme throughout — FlatLaf for the chrome, inline CSS for the content
- Window position and size remembered across restarts

## Requirements

- Java 11+
- Maven 3.6+ (to build)

## Build

```sh
mvn package
```

Produces `target/mdviewer.jar` (fat JAR, all dependencies bundled).

## Run

```sh
mdviewer path/to/file.md
```

Or drop a file onto the window after launching with no argument.

Requires the alias in `~/.zshrc` (see Installation) or run `./mdviewer` directly from the project root.

## Installation

Add to `~/.zshrc` so the command is available everywhere:

```sh
alias mdviewer='/Users/bergurheimisson/ai_code/mdViewer/mdviewer'
```

Then reload your shell:

```sh
source ~/.zshrc
```

## Key bindings

| Key         | Action                              |
|-------------|-------------------------------------|
| `↑` / `↓`  | Scroll up / down                    |
| `←` / `→`  | Previous / next file in same folder |
| `Cmd+O`     | Open file via dialog                |
| `Cmd+R`     | Reload current file                 |
| `Cmd+W`     | Quit                                |
| `Escape`    | Quit                                |
