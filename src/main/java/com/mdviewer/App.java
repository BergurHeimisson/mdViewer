package com.mdviewer;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * Entry point for the mdViewer application.
 * Handles L&F setup, preference restoration, and command-line file argument.
 */
public class App {

    // Preference keys for persisting window state
    static final String PREF_X      = "window.x";
    static final String PREF_Y      = "window.y";
    static final String PREF_WIDTH  = "window.width";
    static final String PREF_HEIGHT = "window.height";

    // Dracula colour palette used by the custom HTML stylesheet
    static final String COLOR_BG        = "#282a36";
    static final String COLOR_CURRENT   = "#44475a";
    static final String COLOR_FG        = "#f8f8f2";
    static final String COLOR_COMMENT   = "#6272a4";
    static final String COLOR_CYAN      = "#8be9fd";
    static final String COLOR_GREEN     = "#50fa7b";
    static final String COLOR_ORANGE    = "#ffb86c";
    static final String COLOR_PINK      = "#ff79c6";
    static final String COLOR_PURPLE    = "#bd93f9";
    static final String COLOR_RED       = "#ff5555";
    static final String COLOR_YELLOW    = "#f1fa8c";

    public static void main(String[] args) {
        // Resolve file argument before touching the EDT
        final File initialFile;
        if (args.length > 0) {
            File candidate = new File(args[0]);
            initialFile = candidate.exists() ? candidate : null;
        } else {
            initialFile = null;
        }

        // Install FlatLaf Dracula theme before any component is created
        FlatLaf.registerCustomDefaultsSource("com.mdviewer");
        try {
            UIManager.setLookAndFeel(new FlatDraculaTheme());
        } catch (UnsupportedLookAndFeelException e) {
            // Fall back to plain dark L&F
            FlatDarkLaf.setup();
        }

        SwingUtilities.invokeLater(() -> {
            Preferences prefs = Preferences.userNodeForPackage(App.class);

            MainWindow window = new MainWindow(prefs);
            window.setVisible(true);

            if (initialFile != null) {
                window.openFile(initialFile);
            }
        });
    }
}
