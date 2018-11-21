package sample;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;

/**
 * An extension of the PuzzleLabel used for drag and drop code blocks
 * Reduces code duplication and improves maintainability for application
 * @author Travis
 */
public class PuzzleFragmentLabel extends PuzzleLabel {
    public static final String DRAG_SOURCE_STYLE = "-fx-background-color: lightgray; -fx-border-color: black;"; //The default component style
    public static final String DRAG_TARGET_STYLE = "-fx-background-color: steelblue; -fx-border-color: black;"; //The default component style

    /**
     * Creates a PuzzleLabel with a series of mouse events added
     *
     * @param text The text to be displayed in the label and tooltip
     */
    public PuzzleFragmentLabel(String text) {
        super(text);

        setOnDragDetected(event -> {
            if ((getText() != null && !getText().isEmpty())) {
                //Create Dragboard content
                ClipboardContent content = new ClipboardContent();
                content.putString(getText());
                SnapshotParameters snapshotParams = new SnapshotParameters();
                WritableImage image = snapshot(snapshotParams, null);

                Dragboard db = startDragAndDrop(TransferMode.ANY);
                db.setDragView(image, event.getX(), event.getY());
                db.setContent(content);

                setStyle(DRAG_SOURCE_STYLE);
            }
            event.consume();
        });
        setOnDragDone(event -> setStyle(DEFAULT_STYLE));
        setOnDragOver(event -> {
            if (event.getGestureSource() != this) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });
        setOnDragDropped(event -> {
            PuzzleLabel source = (PuzzleLabel) event.getGestureSource();
            PuzzleLabel target = (PuzzleLabel) event.getGestureTarget();

            String tempText = target.getText();
            target.setLabelText(source.getText());
            source.setLabelText(tempText);

            Object tempUserData = target.getUserData();
            target.setUserData(source.getUserData());
            source.setUserData(tempUserData);

            event.setDropCompleted(true);
            event.consume();
        });
        setOnDragEntered(event -> {
            if (event.getGestureSource() != this) {
                setStyle(DRAG_TARGET_STYLE);
            }
        });
        setOnDragExited(event -> {
            if (event.getGestureSource() != this) {
                setStyle(DEFAULT_STYLE);
            }
        });
    }
}