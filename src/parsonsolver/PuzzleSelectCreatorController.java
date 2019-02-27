package parsonsolver;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PuzzleSelectCreatorController {
    private PuzzlePaneController rootController;
    private PuzzleCreator puzzleCreator;

    @FXML
    private TextField ProblemName;

    @FXML
    private VBox PuzzleList;

    /**
     * Initializes the puzzle select.
     */
    @FXML
    public void initialize() {
        ProblemName.textProperty().addListener(this::PuzzleSetNameChanged);
    }

    /**
     * Opens the editor with a new puzzle
     */
    @FXML
    public void addNewPuzzle(ActionEvent event){
        if (rootController != null) {
            rootController.openPuzzleEditor();
        }
    }

    /**
     * Sets the root controller of the puzzle screen
     *
     * @param controller The PuzzlePane controller
     */
    public void setRootController(PuzzlePaneController controller) {
        rootController = controller;
    }

    /**
     * Sets the puzzle creator instance
     *
     * @param creator The PuzzleCreator instance
     */
    public void setPuzzleCreator(PuzzleCreator creator) {
        puzzleCreator = creator;

        PuzzleSet currentSet = puzzleCreator.getCurrentSet();
        if (currentSet != null) {
            ProblemName.setText(currentSet.getName());

            displayPuzzleSet();
        }
    }

    /**
     * Displays the current puzzle set in the GUI
     */
    private void displayPuzzleSet() {
        //Check that a puzzle has actually been loaded first
        PuzzleSet currentSet = puzzleCreator.getCurrentSet();
        if (currentSet == null) {
            throw new IllegalStateException("No puzzle selected.");
        }

        PuzzleList.getChildren().clear();

        ProblemName.setText(currentSet.getName());
        for (Puzzle p : currentSet.getPuzzles()) {
            PuzzleSelectPanel newFragment = new PuzzleSelectPanel(p);
            newFragment.setOnMouseClicked(event -> {
                if (rootController != null) {
                    rootController.openPuzzleEditor((int) newFragment.getUserData());
                }
                event.consume();
            });

            PuzzleList.getChildren().add(newFragment);
            VBox.setVgrow(newFragment, Priority.NEVER);
        }
    }

    /**
     * Handle changes to the puzzle set name
     *
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue   The old value
     * @param newValue   The new value
     */
    private void PuzzleSetNameChanged(ObservableValue<? extends String> observable,
                                      String oldValue, String newValue){
        puzzleCreator.getCurrentSet().setName(newValue);
    }
}
