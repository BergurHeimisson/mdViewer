package com.mdviewer;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * A FlatLaf-based theme that applies the Dracula colour palette
 * to all standard Swing components via FlatLaf's property system.
 *
 * FlatLaf resolves theme properties from a file named
 * <ClassName>.properties on the classpath next to the class.
 * We extend FlatDarkLaf so we inherit all sensible dark defaults
 * and only override what Dracula specifies.
 */
public class FlatDraculaTheme extends FlatDarkLaf {

    public static final String NAME = "Dracula";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "Dracula dark theme";
    }

    public static boolean setup() {
        return FlatDarkLaf.setup(new FlatDraculaTheme());
    }
}
