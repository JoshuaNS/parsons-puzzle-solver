package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 * Controller class for the Puzzle selection GUI
 * @author Travis Ridge
 */
public class PuzzleSelectController {
    private PuzzlePaneController rootController;
    private PuzzleSet puzzleSet;

    @FXML
    private Label ProblemName;

    @FXML
    private VBox PuzzleList;

    /**
     * Initializes the puzzle select.
     */
    @FXML
    public void initialize(){

    }

    /**
     * Sets the root controller of the puzzle screen
     * @param controller The PuzzlePane controller
     */
    public void setRootController(PuzzlePaneController controller){
        rootController = controller;
    }

    /**
     * Sets a new puzzle set, and loads the puzzle set data to the GUI.
     * @param p The set of the puzzles to be loaded
     */
    public void setPuzzleSet(PuzzleSet p){
        puzzleSet = p;

        displayPuzzleSet();
    }

    /**
     * Displays the current puzzle set in the GUI
     */
    private void displayPuzzleSet(){
        //Check that a puzzle has actually been loaded first
        if(puzzleSet == null){
            throw new IllegalStateException("No puzzle selected.");
        }

        PuzzleList.getChildren().clear();;

        Insets paddings = new Insets(5,5,5,5);

        ProblemName.setText(puzzleSet.getName());
        for(Puzzle p : puzzleSet.getPuzzles()){
            Label newFragment = new Label(p.getName());
            newFragment.setTooltip(new Tooltip(p.getDescription()));
            newFragment.setUserData(p.getIndex());
            newFragment.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
            newFragment.setPadding(paddings);
            newFragment.setMaxWidth(Double.MAX_VALUE);
            newFragment.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    try {
                        if (rootController != null) {
                            rootController.openPuzzleSolver((int) newFragment.getUserData());
                        }
                    }
                    catch (Exception e) {}

                    event.consume();
                }
            });

            PuzzleList.getChildren().add(newFragment);
            PuzzleList.setVgrow(newFragment, Priority.NEVER);
        }
    }
}
