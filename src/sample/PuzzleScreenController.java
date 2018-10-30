package sample;

import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.util.ArrayList;

public class PuzzleScreenController {

    private PuzzleSet currentPuzzleSet;
    private int puzzleIndex;

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

        if(puzzleIndex+1 < currentPuzzleSet.getPuzzles().size()) {
            puzzleIndex++;

            loadPuzzle();
        }
        else{
            FeedbackText.setText("Error: No puzzles remaining");
        }
    }

    @FXML
    void ResetPuzzle(ActionEvent event) {
        loadPuzzle();
    }

    @FXML
    void CheckAnswer(ActionEvent event) {

    }

    @FXML
    public void initialize(){
        File f = new File("testfiles/puzzlesamp.xml");
        currentPuzzleSet = new PuzzleSet(f);
        puzzleIndex = 1;

        loadPuzzle();
    }

    //Loads the currently selected puzzle into the UI
    private void loadPuzzle(){
        Puzzle currentPuzzle = currentPuzzleSet.getPuzzle(puzzleIndex);
        ProblemName.setText(currentPuzzle.getName());
        ProblemDescription.setText(currentPuzzle.getDescription());

        ArrayList<String> choices = currentPuzzle.buildChoices();

        //Reset the code fragment grid to use the current puzzle
        CodeFragmentGrid.getChildren().clear();
        CodeFragmentGrid.getRowConstraints().clear();

        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.NEVER);

        Insets labelMargins = new Insets(5,-1,5,5);
        Insets fragmentMargins = new Insets(5,5,5,0);
        Insets paddings = new Insets(5,5,5,5);


        for(int i = 0; i < choices.size(); i++){
            CodeFragmentGrid.getRowConstraints().add(rowConstraint);

            //Creates button with A-Z label. Bit messy and won't work past 26 lines
            Label newLabel = new Label("" + (char)('A' + i));
            newLabel.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
            newLabel.setPadding(paddings);
            newLabel.setMaxWidth(Double.MAX_VALUE);
            newLabel.setAlignment(Pos.CENTER);
            CodeFragmentGrid.add(newLabel,0, i);
            CodeFragmentGrid.setMargin(newLabel,labelMargins);

            //Creates button with A-Z label. Bit messy and won't work past 26 lines
            Label newFragment = new Label(choices.get(i));
            newFragment.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
            newFragment.setPadding(paddings);
            newFragment.setMaxWidth(Double.MAX_VALUE);
            CodeFragmentGrid.add(newFragment,1, i);
            CodeFragmentGrid.setMargin(newFragment,fragmentMargins);
        }
    }
}
