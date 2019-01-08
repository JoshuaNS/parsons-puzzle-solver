package sample;

import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tooltip;
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

    private double dragStartX;

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

                dragStartX = event.getX();
            }
            event.consume();
        });
        setOnDragDone(event -> setStyle(CURRENT_STYLE));
        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

            if (event.getGestureSource() == this) {
                //temporarily change displayed tab without changing tab value
                int tempTab = Math.max(getTabs() - (int) ((dragStartX - event.getX()) / 20), 0);
                setPadding(new Insets(5, 5, 5, 5 + 20 * tempTab));
            }

            event.consume();
        });
        setOnDragDropped(event -> {
            if (event.getGestureSource() != this) {
                PuzzleFragmentLabel source = (PuzzleFragmentLabel) event.getGestureSource();
                PuzzleFragmentLabel target = (PuzzleFragmentLabel) event.getGestureTarget();

                String tempText = target.getText();
                target.setLabelText(source.getText());
                source.setLabelText(tempText);

                Object tempUserData = target.getUserData();
                target.setUserData(source.getUserData());
                source.setUserData(tempUserData);

                target.refreshTabs();
                source.refreshTabs();

            } else {
                setTabs(getTabs() - (int) ((dragStartX - event.getX()) / 20));
            }

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
                setStyle(CURRENT_STYLE);
            } else {
                setTabs(getTabs());
            }
        });
    }

    public int getTabs() {
        if (getUserData() instanceof Block) {
            return ((Block) getUserData()).getSolutionTab();
        }
        return 0;
    }

    public void setTabs(int newTabs) {
        if (getUserData() instanceof Block) {
            //prevent negative tab
            newTabs = Math.max(newTabs, 0);
            ((Block) getUserData()).setSolutionTab(newTabs);
        }
        refreshTabs();
    }

    /**
     * Refreshes tab display
     */
    public void refreshTabs() {
        setPadding(new Insets(5, 5, 5, 5 + 20 * getTabs()));
    }
}