package parsonsolver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controller class for the Puzzle GUI shell
 * @author Travis Ridge
 */
public class PuzzlePaneController {
    private PuzzleSet currentPuzzleSet;
    private PuzzleCreator currentPuzzleCreator = new PuzzleCreator();
    private Pane currentView;
    private PuzzleScreenController currentPuzzleSolver = null;

    private File puzzleCreatorFile = null;

    private static final boolean IS_TEST_MODE = true; //Enables demo options in GUI
    private static final boolean IS_TEACHER_MODE = true; //Enables teacher edition options (e.g. Puzzle Creator)

    ///FXML COMPONENTS

    @FXML
    private MenuItem NavPuzzleSelect;

    @FXML
    private MenuItem NavPuzzleEditor;

    @FXML
    private VBox TitlePane;

    @FXML
    private BorderPane PuzzlePane;

    @FXML
    private Button PuzzleSelectButton;

    @FXML
    private Button NewEditorButton;

    @FXML
    private Button LoadEditorButton;

    @FXML
    private Button PuzzleEditorButton;

    @FXML
    private Button PuzzleDemoButton;

    @FXML
    private Button CreatorDemoButton;

    @FXML
    private Separator EditorSeparator;

    @FXML
    private Separator DemoSeparator;

    ///FXML METHODS

    /**
     * Initializes the puzzle screen.
     */
    @FXML
    public void initialize() {
        currentView = TitlePane;

        //TEACHER MODE OPTIONS
        if (!IS_TEACHER_MODE) {
            NavPuzzleEditor.setVisible(false);

            EditorSeparator.setVisible(false);
            NewEditorButton.setVisible(false);
            LoadEditorButton.setVisible(false);
            CreatorDemoButton.setVisible(false); //Will be hidden if either mode is false
            EditorSeparator.setManaged(false);
            NewEditorButton.setManaged(false);
            LoadEditorButton.setManaged(false);
            CreatorDemoButton.setManaged(false); //Will be hidden if either mode is false
        }

        //TEST MODE OPTIONS
        //Also validates that demo file exists
        if (!IS_TEST_MODE || !new File("testfiles/puzzlesamp.xml").exists()) {
            DemoSeparator.setVisible(false);
            PuzzleDemoButton.setVisible(false);
            CreatorDemoButton.setVisible(false); //Will be hidden if either mode is false
            DemoSeparator.setManaged(false);
            PuzzleDemoButton.setManaged(false);
            CreatorDemoButton.setManaged(false); //Will be hidden if either mode is false
        }

        PuzzleSelectButton.setVisible(false);
        PuzzleEditorButton.setVisible(false);
        PuzzleSelectButton.setManaged(false);
        PuzzleEditorButton.setManaged(false);
    }

    /**
     * Selects a new PuzzleSet file to be used, and loads the file if valid.
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void LoadPuzzleSet(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle Set File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
        File f = fileChooser.showOpenDialog(null);

        if (f != null && f.exists()) { //if null no file was selected
            setPuzzleSet(f);
        }
    }

    /**
     * Selects a new PuzzleSet file to be used by the editor, and loads the file if valid.
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void LoadPuzzleCreatorSet(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Puzzle Set File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
        File f = fileChooser.showOpenDialog(null);

        if (f != null && f.exists()) { //if null no file was selected
            setPuzzleCreatorSet(f);
        }
    }

    /**
     * Creates a new PuzzleSet to be used by the editor.
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void NewPuzzleCreatorSet(ActionEvent event) {
        setPuzzleCreatorSet();
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
     * Opens the sample file
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void CreatorDemo(ActionEvent event) {
        File f = new File("testfiles/puzzlesamp.xml");
        setPuzzleCreatorSet(f);
    }

    /**
     * Export the results of puzzle solving
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void ExportResults(ActionEvent event) {
        if (currentPuzzleSet == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExportResults.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Parson's Programming Puzzles - Results");
            stage.setScene(new Scene(root));

            ExportResultsController controller = loader.getController();
            controller.setExportResultsText(String.join("", currentPuzzleSet.exportResults()));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the title screen
     */
    @FXML
    public void openTitleScreen(ActionEvent event) {
        setCurrentView(TitlePane);
    }

    /**
     * Opens the puzzle select for the selected puzzle set
     */
    @FXML
    public void openPuzzleSelect(ActionEvent event) {
        openPuzzleSelect();
    }

    /**
     * Opens the puzzle creator
     */
    @FXML
    public void openPuzzleCreator(ActionEvent event) {
        openPuzzleCreator();
    }

    /**
     * Opens the puzzle creator
     */
    @FXML
    public void openPuzzleCreator() {
        PuzzleSelectCreatorController controller;
        Pane newView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleSelectCreator.fxml"));
            newView = loader.load();
            controller = loader.getController();
            controller.setRootController(this);

            controller.setPuzzleCreator(currentPuzzleCreator);
            setCurrentView(newView);
        } catch (IOException e) {
            System.err.println("PuzzleCreator could not be loaded.");
            return;
        }
    }

    /**
     * Opens the puzzle creator for a selected puzzle
     *
     * @param index The index of the puzzle within the puzzle set
     */
    @FXML
    public void openPuzzleEditor(int index) {
        if (index > currentPuzzleCreator.getCurrentSet().getPuzzles().size() || index < 1) {
            throw new IllegalArgumentException("Puzzle Creator Index: " + index);
        }

        PuzzleCreatorController controller;
        Pane newView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleCreator.fxml"));
            newView = loader.load();
            controller = loader.getController();
            controller.setRootController(this);
            setCurrentView(newView);
            currentPuzzleCreator.openPuzzle(index);
            controller.setPuzzleCreator(currentPuzzleCreator, false);
        } catch (IOException e) {
            System.err.println("PuzzleCreator could not be loaded.");
            return;
        }
    }

    /**
     * Opens the puzzle creator for a new puzzle
     */
    @FXML
    public void openPuzzleEditor() {
        PuzzleCreatorController controller;
        Pane newView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleCreator.fxml"));
            newView = loader.load();
            controller = loader.getController();
            controller.setRootController(this);
            setCurrentView(newView);
            controller.setPuzzleCreator(currentPuzzleCreator, true);
        } catch (IOException e) {
            System.err.println("PuzzleCreator could not be loaded.");
            return;
        }
    }

    ///PUBLIC METHODS

    /**
     * Set the currently displayed view within the pane
     *
     * @param newView
     */
    public void setCurrentView(Pane newView) {
        //Remove currently open view if applicable
        if (currentView != null) {
            //Leaving the puzzle solver
            if (currentPuzzleSolver != null) {
                currentPuzzleSolver.endPuzzle();
                currentPuzzleSolver = null;
            }
            //Leaving the puzzle editor
            else if(currentPuzzleCreator.getCurrentPuzzle() != null){
                //TODO: Check for unsaved changes first, and prompt to cancel
                currentPuzzleCreator.closeEdit();
            }

            PuzzlePane.getChildren().remove(currentView);
        }

        currentView = newView;
        PuzzlePane.setCenter(currentView);
    }

    /**
     * Opens the puzzle select for the selected puzzle set
     */
    public void openPuzzleSelect() {
        PuzzleSelectController controller;
        Pane newView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleSelect.fxml"));
            newView = loader.load();
            controller = loader.getController();
            controller.setRootController(this);
            controller.setPuzzleSet(currentPuzzleSet);
        } catch (IOException e) {
            System.err.println("PuzzleSelect could not be loaded.");
            return;
        }

        setCurrentView(newView);
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
        Pane newView;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "PuzzleScreen.fxml"));
            newView = loader.load();
            controller = loader.getController();
            controller.setRootController(this);
            controller.setPuzzleSet(currentPuzzleSet, index);
        } catch (IOException e) {
            System.err.println("PuzzleScreen could not be loaded.");
            return;
        }

        setCurrentView(newView);
        currentPuzzleSolver = controller;
    }

    /**
     * Export Puzzle Set to an XML file
     */
    public void exportCreatorToXML() {
        if (currentPuzzleCreator != null && currentPuzzleCreator.getCurrentSet() != null) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Export Puzzle Set");
                if(puzzleCreatorFile != null) {
                    fileChooser.setInitialDirectory(puzzleCreatorFile.getParentFile());
                    fileChooser.setInitialFileName(puzzleCreatorFile.getName());
                }
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));

                File f = fileChooser.showSaveDialog(null);
                if (f != null) {
                    currentPuzzleCreator.getCurrentSet().exportToXML(f);
                    puzzleCreatorFile = f.getCanonicalFile();
                }
            } catch (Exception e) {
                System.err.println(e.toString());
                System.err.println(e.getMessage());
                for (StackTraceElement se : e.getStackTrace()) {
                    System.err.println(se.toString());
                }
            }
        }
    }

    ///PRIVATE METHODS

    /**
     * Loads a new puzzle set from a file.
     *
     * @param f The file containing the new puzzle set.
     */
    private void setPuzzleSet(File f) {
        try {
            currentPuzzleSet = new PuzzleSet(f);

            NavPuzzleSelect.setDisable(false);
            PuzzleSelectButton.setVisible(true);
            PuzzleSelectButton.setManaged(true);

            openPuzzleSelect();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.err.println(e.getMessage());
            for (StackTraceElement se : e.getStackTrace()) {
                System.err.println(se.toString());
            }
        }
    }

    /**
     * Loads a new puzzle set into the editor.
     */
    private void setPuzzleCreatorSet() {
        try {
            currentPuzzleCreator.closeSession();
            currentPuzzleCreator.createNewSet("");
            puzzleCreatorFile = null;

            NavPuzzleEditor.setDisable(false);
            PuzzleEditorButton.setVisible(true);
            PuzzleEditorButton.setManaged(true);

            openPuzzleCreator();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.err.println(e.getMessage());
            for (StackTraceElement se : e.getStackTrace()) {
                System.err.println(se.toString());
            }
        }
    }

    /**
     * Loads a puzzle set into the editor from a file.
     *
     * @param f The file containing the new puzzle set.
     */
    private void setPuzzleCreatorSet(File f) {
        try {
            currentPuzzleCreator.closeSession();
            currentPuzzleCreator.setCurrentSet(new PuzzleSet(f));
            puzzleCreatorFile = f.getCanonicalFile();

            NavPuzzleEditor.setDisable(false);
            PuzzleEditorButton.setVisible(true);
            PuzzleEditorButton.setManaged(true);

            openPuzzleCreator();
        } catch (InvalidInputFileException e) {
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
     * Determines whether a given integer value is a valid puzzle index for the current puzzle set.
     *
     * @param index The index of the puzzle to be checked
     * @return A boolean value of it the puzzle index is valid
     */
    private boolean puzzleIndexValid(int index) {
        return (index <= currentPuzzleSet.getPuzzles().size() && index >= 1);
    }
}
