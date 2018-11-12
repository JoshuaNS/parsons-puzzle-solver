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
    private double dragStartY;
    private int tab = 0;

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
                dragStartY = event.getY();
            }
            event.consume();
        });
        setOnDragDone(event -> setStyle(DEFAULT_STYLE));
        setOnDragOver(event -> {
            //if (event.getGestureSource() != this) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            //}
            if (event.getGestureSource() == this) {
                int tempTab = Math.max(tab - (int) ((dragStartX - event.getX()) / 20), 0);
                setPadding(new Insets(5,5,5,5 + 20 * tempTab));
            }

            System.out.println((dragStartX - event.getX()));
            event.consume();
        });
        setOnDragDropped(event -> {
            if (event.getGestureSource() != this) {
                PuzzleFragmentLabel source = (PuzzleFragmentLabel) event.getGestureSource();
                PuzzleFragmentLabel target = (PuzzleFragmentLabel) event.getGestureTarget();
                String temp = target.getText();

                target.setLabelText(source.getText());
                source.setLabelText(temp);
            }
            else {
                setTab(Math.max(tab - (int) ((dragStartX - event.getX()) / 20), 0));
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
                setStyle(DEFAULT_STYLE);
            }
            else{
                setTab(tab);
            }
        });
    }

    public void setLabelText(String text){
        setText(text);
        if(text != null && !text.isEmpty()) {
            setTooltip(new Tooltip(text));
        }
        else{
            setTooltip(null);
        }
        setTab(0);
    }

    public void setTab(int newTabs){
        tab = newTabs;
        setPadding(new Insets(5,5,5,5 + 20 * tab));
    }
}