package parsonsolver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * Controller class for the Puzzle GUI shell
 * @author Travis Ridge
 */
public class PuzzlePaneController {
    private PuzzleSet currentPuzzleSet;
    private Pane currentView;

    private static final boolean IS_TEST_MODE = true; //Enables demo options in GUI

    @FXML
    private MenuItem NavPuzzleSelect;

    @FXML
    private VBox TitlePane;

    @FXML
    private BorderPane PuzzlePane;

    /**
     * Selects a new PuzzleSet file to be used, and loads the file if valid.
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void LoadPuzzleSet(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File f = fileChooser.showOpenDialog(null);

        if (f != null && f.exists()) { //if null no file was selected
            setPuzzleSet(f);
        }
    }

    /**
     * Initializes the puzzle screen.
     */
    @FXML
    public void initialize() {
        currentView = TitlePane;

        //TEST MODE OPTIONS
        if (IS_TEST_MODE) {
            //Only allow demo if demo file exists
            if (new File("testfiles/puzzlesamp.xml").exists()) {
                Button demoButton = new Button("Puzzle Demo");
                demoButton.setOnAction(this::LoadDemo);
                TitlePane.getChildren().add(demoButton);
            }
        }
    }

    /**
     * Exits the application
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void Exit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Opens the sample file
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void LoadDemo(ActionEvent event) {
        File f = new File("testfiles/puzzlesamp.xml");
        setPuzzleSet(f);
    }

    /**
     * Loads a new puzzle set from a file.
     *
     * @param f The file containing the new puzzle set.
     */
    private void setPuzzleSet(File f) {
        try {
            currentPuzzleSet = new PuzzleSet(f);

            if (NavPuzzleSelect.isDisable()) {
                NavPuzzleSelect.setDisable(false);
                Button demoButton = new Button("Go to Puzzle Select");
                demoButton.setOnAction(this::openPuzzleSelect);
                TitlePane.getChildren().add(demoButton);
            }

            openPuzzleSelect();
        } catch (InvalidInputFileException e){
            //TODO Add feedback that input file was invalid.
        } catch (Exception e) {
            System.err.println(e.toString());
            System.err.println(e.getMessage());
            for (StackTraceElement se : e.getStackTrace()) {
                System.err.println(se.toString());
            }
        }
    }

    /**
     * Opens the title screen
     */
    @FXML
    public void openTitleScreen(ActionEvent event) {
        //Remove currently open view if applicable
        if (currentView != null) {
            PuzzlePane.getChildren().remove(currentView);
        }

        PuzzlePane.setCenter(TitlePane);
    }

    /**
     * Opens the puzzle select for the selected puzzle set
     */
    @FXML
    public void openPuzzleSelect(ActionEvent event) {
        openPuzzleSelect();
    }

    /**
     * Opens the puzzle select for the selected puzzle set
     */
    public void openPuzzleSelect() {
        PuzzleSelectController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleSelect.fxml"));
            currentView = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            System.err.println("PuzzleSelect could not be loaded.");
            return;
        }

        //Remove currently open view if applicable
        if (currentView != null) {
            PuzzlePane.getChildren().remove(currentView);
        }

        controller.setRootController(this);
        controller.setPuzzleSet(currentPuzzleSet);

        PuzzlePane.setCenter(currentView);
    }

    /**
     * Opens the puzzle solver for the selected puzzle
     *
     * @param index The index of the puzzle within the puzzle set
     */
    public void openPuzzleSolver(int index) {
        if (!puzzleIndexValid(index)) {
            throw new IllegalArgumentException("Puzzle Index: " + index);
        }

        PuzzleScreenController controller;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleScreen.fxml"));
            currentView = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            System.err.println("PuzzleScreen could not be loaded.");
            return;
        }

        //Remove currently open view if applicable
        if (currentView != null) {
            PuzzlePane.getChildren().remove(currentView);
        }

        controller.setRootController(this);
        controller.setPuzzleSet(currentPuzzleSet, index);

        PuzzlePane.setCenter(currentView);
    }

    /**
     * Determines whether a given integer value is a valid puzzle index for the current puzzle set.
     *
     * @param index The index of the puzzle to be checked
     * @return A boolean value of it the puzzle index is valid
     */
    private boolean puzzleIndexValid(int index) {
        return (index <= currentPuzzleSet.getPuzzles().size() && index >= 1);
    }
}