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

- Java 17+
- Maven 3.6+ (to build)

## Installation

Clone the repo and run the install script:

```sh
git clone https://github.com/BergurHeimisson/mdViewer.git
cd mdViewer
./install.sh
```

This builds the JAR, copies it to `/usr/local/lib/mdviewer/`, and symlinks the launcher into `/usr/local/bin/` so `mdviewer` is available in every terminal session without any shell configuration.

**Uninstall:**

```sh
sudo rm -rf /usr/local/lib/mdviewer /usr/local/bin/mdviewer
```

## Usage

```sh
mdviewer path/to/file.md
```

Or launch with no argument and drop a file onto the window.

## Development

Build without installing:

```sh
mvn package
./mdviewer path/to/file.md
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
