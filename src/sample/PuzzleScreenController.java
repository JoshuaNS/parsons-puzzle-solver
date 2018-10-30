package sample;

import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

public class PuzzleScreenController {

    private PuzzleSet currentPuzzleSet;
    private int puzzleIndex;
    private ArrayList<String> puzzleFragments;

    @FXML
    private GridPane CodeFragmentGrid;

    @FXML
    private Label FeedbackText;

    @FXML
    private Label ProblemName;

    @FXML
    private Label ProblemDescription;

    @FXML
    void NextPuzzle(ActionEvent event) {

        if(puzzleIndex+1 <= currentPuzzleSet.getPuzzles().size()) {
            puzzleIndex++;
            setCurrentPuzzle();
            loadPuzzleData();
        }
        else{
            FeedbackText.setText("Error: No puzzles remaining");
        }
    }

    @FXML
    void ResetPuzzle(ActionEvent event) {
        loadPuzzleData();
    }

    @FXML
    void CheckAnswer(ActionEvent event) {

    }

    @FXML
    void LoadPuzzleSet(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File f = fileChooser.showOpenDialog(null);

        if(f != null && f.exists()){ //if null no file was selected
            setPuzzleSet(f);
        }
    }

    @FXML
    void Exit(ActionEvent event){
        System.exit(0);
    }

    @FXML
    public void initialize(){
        File f = new File("testfiles/puzzlesamp.xml");
        setPuzzleSet(f);
    }

    //Changes the currently selected puzzle set
    private void setPuzzleSet(File f){
        currentPuzzleSet = new PuzzleSet(f);
        puzzleIndex = 1;

        setCurrentPuzzle();
        loadPuzzleData();
    }

    //Changes the currently selected puzzle
    private void setCurrentPuzzle(){
        Puzzle currentPuzzle = currentPuzzleSet.getPuzzle(puzzleIndex);
        ProblemName.setText(currentPuzzle.getName());
        ProblemDescription.setText(currentPuzzle.getDescription());
        puzzleFragments = currentPuzzle.buildChoices();
    }

    //Loads the currently selected puzzle into the UI
    private void loadPuzzleData(){
        //Reset the code fragment grid to use the current puzzle
        CodeFragmentGrid.getChildren().clear();
        CodeFragmentGrid.getRowConstraints().clear();

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.NEVER);

        Insets labelMargins = new Insets(5,-1,5,5);
        Insets fragmentMargins = new Insets(5,5,5,0);
        Insets paddings = new Insets(5,5,5,5);

        //Set the code fragments
        for(int i = 0; i < puzzleFragments.size(); i++){
            CodeFragmentGrid.getRowConstraints().add(rowConstraint);

            //Creates button with A-Z label. Bit messy and won't work past 26 lines
            Label newLabel = new Label("" + (char)('A' + i));
            newLabel.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
            newLabel.setPadding(paddings);
            newLabel.setMaxWidth(Double.MAX_VALUE);
            newLabel.setAlignment(Pos.CENTER);
            CodeFragmentGrid.add(newLabel,0, i);
            CodeFragmentGrid.setMargin(newLabel,labelMargins);

            Label newFragment = new Label(puzzleFragments.get(i));
            newFragment.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
            newFragment.setPadding(paddings);
            newFragment.setMaxWidth(Double.MAX_VALUE);
            CodeFragmentGrid.add(newFragment,1, i);
            CodeFragmentGrid.setMargin(newFragment,fragmentMargins);
        }
    }
}
