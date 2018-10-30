package sample;

import com.sun.org.apache.bcel.internal.classfile.Code;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

public class PuzzleScreenController {

    private PuzzleSet currentPuzzleSet;
    private int puzzleIndex;
    private Puzzle currentPuzzle;
    private ArrayList<String> puzzleFragments;
    private ArrayList<ArrayList<String>> puzzleAnswers; //currently only used by MC
    private ToggleGroup puzzleAnswerToggles; //holds MC selection
    private ArrayList<Label> puzzleAnswerLabels; //holds DnD answer

    @FXML
    private GridPane CodeFragmentGrid;

    @FXML
    private GridPane SolutionGrid;

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
        Object answer = null;
        if (currentPuzzle.getType().equals(PuzzleType.DnD)) {
            ArrayList<String> answerList = new ArrayList<>();
            for (Label solution : puzzleAnswerLabels) {
                answerList.add(solution.getText());
            }
            answer = answerList;
        }
        else if (currentPuzzle.getType().equals(PuzzleType.MC)){
            Toggle selectedAnswer = puzzleAnswerToggles.getSelectedToggle();
            if(selectedAnswer != null){
                answer = selectedAnswer.getUserData();
            }
            else{
                FeedbackText.setText("Error: No answer selected");
                return;
            }
        }

        Object result = currentPuzzle.checkSolution(answer);
        if(result == null)
            FeedbackText.setText("Error: Invalid answer");
        else if(result.equals(true))
            FeedbackText.setText("Solution is Correct!");
        else
            FeedbackText.setText("Incorrect Answer!");
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
        currentPuzzle = currentPuzzleSet.getPuzzle(puzzleIndex);
        ProblemName.setText(currentPuzzle.getName());
        ProblemDescription.setText(currentPuzzle.getDescription());
        puzzleFragments = currentPuzzle.buildChoices();
        if(currentPuzzle.getType() == PuzzleType.MC){
            puzzleAnswers = (((MultipleChoicePuzzle)currentPuzzle).buildAnswers());
        }
    }

    //Loads the currently selected puzzle into the UI
    private void loadPuzzleData(){
        //Reset the code fragment grid to use the current puzzle
        CodeFragmentGrid.getChildren().clear();
        CodeFragmentGrid.getRowConstraints().clear();
        SolutionGrid.getChildren().clear();
        SolutionGrid.getRowConstraints().clear();
        puzzleAnswerToggles = new ToggleGroup();
        puzzleAnswerLabels = new ArrayList<>();

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
            newFragment.setTooltip(new Tooltip(puzzleFragments.get(i)));
            newFragment.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
            newFragment.setPadding(paddings);
            newFragment.setMaxWidth(Double.MAX_VALUE);
            CodeFragmentGrid.add(newFragment,1, i);
            CodeFragmentGrid.setMargin(newFragment,fragmentMargins);

            newFragment.setOnDragDetected(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    /* drag was detected, start a drag-and-drop gesture*/
                    /* allow any transfer mode */
                    Dragboard db = newFragment.startDragAndDrop(TransferMode.ANY);

                    /* Put a string on a dragboard */
                    ClipboardContent content = new ClipboardContent();
                    content.putString(newFragment.getText());
                    db.setContent(content);

                    event.consume();
                }
            });
        }

        //Set the solution data
        if (currentPuzzle.getType().equals(PuzzleType.DnD)) {
            for(int i = 0; i < currentPuzzle.getSolutions().size(); i++){
                SolutionGrid.getRowConstraints().add(rowConstraint);

                Label newLabel = new Label("" + (i+1));
                newLabel.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
                newLabel.setPadding(paddings);
                newLabel.setMaxWidth(Double.MAX_VALUE);
                newLabel.setAlignment(Pos.CENTER);
                SolutionGrid.add(newLabel,0, i);
                SolutionGrid.setMargin(newLabel,labelMargins);

                Label newFragment = new Label();
                newFragment.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
                newFragment.setPadding(paddings);
                newFragment.setMaxWidth(Double.MAX_VALUE);
                SolutionGrid.add(newFragment,1, i);
                SolutionGrid.setMargin(newFragment,fragmentMargins);

                puzzleAnswerLabels.add(newFragment);

                newFragment.setOnDragOver(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data is dragged over the target */
                        /* accept it only if it is not dragged from the same node
                         * and if it has a string data */
                        if (event.getGestureSource() != newFragment &&
                                event.getDragboard().hasString()) {
                            /* allow for both copying and moving, whatever user chooses */
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }

                        event.consume();
                    }
                });
                newFragment.setOnDragDropped(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data dropped */
                        /* if there is a string data on dragboard, read it and use it */
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if (db.hasString()) {
                            newFragment.setText(db.getString());
                            newFragment.setTooltip(new Tooltip(db.getString()));
                            success = true;
                        }
                        /* let the source know whether the string was successfully
                         * transferred and used */
                        event.setDropCompleted(success);

                        event.consume();
                    }
                });
            }
        }
        else if (currentPuzzle.getType().equals(PuzzleType.MC)){
            for(int i = 0; i < puzzleAnswers.size(); i++){
                SolutionGrid.getRowConstraints().add(rowConstraint);

                Label newLabel = new Label("" + (i+1));
                newLabel.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
                newLabel.setPadding(paddings);
                newLabel.setMaxWidth(Double.MAX_VALUE);
                newLabel.setAlignment(Pos.CENTER);
                SolutionGrid.add(newLabel,0, i);
                SolutionGrid.setMargin(newLabel,labelMargins);

                RadioButton newAnswer = new RadioButton(puzzleAnswers.get(i).toString()); //TODO: replace with proper answer text
                newAnswer.setTooltip(new Tooltip(puzzleAnswers.get(i).toString()));
                newAnswer.setUserData(puzzleAnswers.get(i));
                newAnswer.setToggleGroup(puzzleAnswerToggles);
                newAnswer.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
                newAnswer.setPadding(paddings);
                newAnswer.setMaxWidth(Double.MAX_VALUE);
                SolutionGrid.add(newAnswer,1, i);
                SolutionGrid.setMargin(newAnswer,fragmentMargins);
            }
        }
    }
}
