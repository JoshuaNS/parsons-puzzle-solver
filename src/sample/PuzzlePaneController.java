package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.Console;
import java.io.File;
import java.io.IOException;

/**
 * Controller class for the Puzzle GUI shell
 * @author Travis Ridge
 */
public class PuzzlePaneController {
    private PuzzleSet currentPuzzleSet;

    private Pane currentView;

    @FXML
    private BorderPane PuzzlePane;

    /**
     * Selects a new PuzzleSet file to be used, and loads the file if valid.
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void LoadPuzzleSet(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File f = fileChooser.showOpenDialog(null);

        if(f != null && f.exists()){ //if null no file was selected
            setPuzzleSet(f);
        }
    }

    /**
     * Initializes the puzzle screen.
     */
    @FXML
    public void initialize(){
        File f = new File("testfiles/puzzlesamp.xml");
        setPuzzleSet(f);
    }

    /**
     * Exits the application
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void Exit(ActionEvent event){
        System.exit(0);
    }

    /**
     * Loads a new puzzle set from a file.
     * @param f The file containing the new puzzle set.
     */
    private void setPuzzleSet(File f){
        try {
            currentPuzzleSet = new PuzzleSet(f);
            openPuzzleSelect();
            //openPuzzleSolver(1);
        }
        catch(Exception e) {
            System.err.println(e.toString());
            System.err.println(e.getMessage());
            for(StackTraceElement se : e.getStackTrace()){
                System.err.println(se.toString());
            }
        }
    }

    /**
     * Opens the puzzle select for the selected puzzle set
     * @throws IOException
     */
    public void openPuzzleSelect() throws IOException {
        //Remove currently open view if applicable
        if(currentView != null){
            PuzzlePane.getChildren().remove(currentView);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "PuzzleSelect.fxml"));
        currentView =  loader.load();
        PuzzleSelectController controller = loader.getController();

        controller.setRootController(this);
        controller.setPuzzleSet(currentPuzzleSet);

        PuzzlePane.setCenter(currentView);
    }

    /**
     * Opens the puzzle solver for the selected puzzle
     * @param index The index of the puzzle within the puzzle set
     * @throws IOException
     */
    public void openPuzzleSolver(int index) throws IOException {
        if(!puzzleIndexValid(index)){
            throw new IllegalArgumentException("Puzzle Index: " + index);
        }

        //Remove currently open view if applicable
        if(currentView != null){
            PuzzlePane.getChildren().remove(currentView);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "PuzzleScreen.fxml"));
        currentView =  loader.load();
        PuzzleScreenController controller = loader.getController();

        controller.setRootController(this);
        controller.setPuzzleSet(currentPuzzleSet, index);

        PuzzlePane.setCenter(currentView);
    }

    /**
     * Determines whether a given integer value is a valid puzzle index for the current puzzle set.
     * @param index The index of the puzzle to be checked
     * @return A boolean value of it the puzzle index is valid
     */
    private boolean puzzleIndexValid(int index){
        return (index <= currentPuzzleSet.getPuzzles().size() && index >= 1);
    }
}
