package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
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

        PuzzleList.getChildren().clear();

        ProblemName.setText(puzzleSet.getName());
        for(Puzzle p : puzzleSet.getPuzzles()){
            PuzzleLabel newFragment = new PuzzleLabel(p.getName());
            newFragment.setUserData(p.getIndex());
            newFragment.setOnMouseClicked(event -> {
                if (rootController != null) {
                    rootController.openPuzzleSolver((int) newFragment.getUserData());
                }
                event.consume();
            });

            PuzzleList.getChildren().add(newFragment);
            VBox.setVgrow(newFragment, Priority.NEVER);
        }
    }
}
