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
    public static final String INCORRECT_STYLE = "-fx-background-color: steelblue; -fx-border-color: black; -fx-text-fill: white;"; //The default component style
    private static final Insets padding = new Insets(5);

    protected String CURRENT_STYLE = DEFAULT_STYLE;
    private boolean useTooltip;

    /**
     * Creates a PuzzleLabel for a given text
     *
     * @param text The text to be displayed in the label and tooltip
     */
    public PuzzleLabel(String text) {
        this(text, true);
    }

    /**
     * Creates a PuzzleLabel for a given text with Tooltip optional
     *
     * @param text        The text to be displayed
     * @param showTooltip If a tooltip should be displayed for the label
     */
    public PuzzleLabel(String text, boolean showTooltip) {
        super(text);
        useTooltip = showTooltip;
        if (showTooltip && text != null && !text.isEmpty()) {
            setTooltip(new Tooltip(text));
        }
        setStyle(CURRENT_STYLE);
        setPadding(padding);
        setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * A method to set the text and tooltip of the label at the same time
     *
     * @param text The text to be displayed
     */
    public void setLabelText(String text) {
        setText(text);
        if (useTooltip && text != null && !text.isEmpty()) {
            setTooltip(new Tooltip(text));
        } else {
            setTooltip(null);
        }
    }

    /**
     * Sets the style that will be displayed in the default state
     *
     * @param style
     */
    public void setCurrentSyle(String style) {
        CURRENT_STYLE = style;
        setStyle(CURRENT_STYLE);
    }
}
