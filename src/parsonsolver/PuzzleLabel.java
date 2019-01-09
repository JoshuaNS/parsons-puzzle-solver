package parsonsolver;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * An extension of the JavaFX Label which sets standardized style for the application by default
 * Reduces code duplication and improves maintainability for application
 * @author Travis
 */
public class PuzzleLabel extends Label {
    public static final String DEFAULT_STYLE = "-fx-background-color: aliceblue; -fx-border-color: black;"; //The default component style

    private static Insets padding = new Insets(5, 5, 5, 5);


    public PuzzleLabel(String text) {
        this(text, true);
    }

    public PuzzleLabel(String text, boolean showTooltip) {
        super(text);
        if (showTooltip && text != null && !text.isEmpty()) {
            setTooltip(new Tooltip(text));
        }
        setStyle(DEFAULT_STYLE);
        setPadding(padding);
        setMaxWidth(Double.MAX_VALUE);
    }

    public void setLabelText(String text) {
        setText(text);
        if (text != null && !text.isEmpty()) {
            setTooltip(new Tooltip(text));
        } else {
            setTooltip(null);
        }
    }
}
