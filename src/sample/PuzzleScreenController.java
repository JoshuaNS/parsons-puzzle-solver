package sample;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;

import java.util.ArrayList;

/**
 * Controller class for the PuzzleScreen GUI
 * @author Travis Ridge
 */
public class PuzzleScreenController {
    private PuzzlePaneController rootController; //Link to the controller of the root for passing event
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

    /**
     * Loads the next puzzle in the PuzzleSet into the GUI, if applicable.
     * Currently only supports sequential puzzle order.
     * Feedback text bar displays an error message if no next puzzle exists.
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void NextPuzzle(ActionEvent event) {

        if(puzzleIndexValid(puzzleIndex+1)) {
            setCurrentPuzzle(puzzleIndex+1);
        }
        else{
            try {
                rootController.openPuzzleSelect();
            }
            catch (Exception e) {}
        }
    }

    /**
     * Reloads the currently selected puzzle, clearing all progress in the GUI.
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void ResetPuzzle(ActionEvent event) {
        loadPuzzleData();
    }

    /**
     * Checks the current solution answer and determines result.
     * One of three results will occur depend on answer:
     * 1. Invalid answer error message if an invalid solution supplied (i.e. multiple choice with no answer).
     * 2. Correct answer message if solution was the correct answer.
     * 3. Incorrect answer message if solution was valid but incorrect.
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void CheckAnswer(ActionEvent event) {
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

    /**
     * Initializes the puzzle screen.
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
     * Sets a new puzzle set, and loads the selected puzzle in the set to the GUI.
     * @param puzzleSet The set of the puzzles to be loaded
     * @param puzzleIndex The index of the first puzzle to be loaded
     */
    public void setPuzzleSet(PuzzleSet puzzleSet, int puzzleIndex){
        currentPuzzleSet = puzzleSet;

        setCurrentPuzzle(puzzleIndex);
    }

    /**
     * Sets a new puzzle and loads it into the GUI.
     * @param newPuzzleIndex The index of the new puzzle within the puzzle set.
     */
    private void setCurrentPuzzle(int newPuzzleIndex){
        if(!puzzleIndexValid(newPuzzleIndex)){
            throw new IllegalArgumentException("newPuzzleIndex: " + newPuzzleIndex);
        }

        puzzleIndex = newPuzzleIndex;
        currentPuzzle = currentPuzzleSet.getPuzzle(puzzleIndex);
        ProblemName.setText(currentPuzzle.getName());
        ProblemDescription.setText(currentPuzzle.getDescription());
        puzzleFragments = currentPuzzle.buildChoices();
        if(currentPuzzle.getType() == PuzzleType.MC){
            puzzleAnswers = (((MultipleChoicePuzzle)currentPuzzle).buildAnswers());
        }
        loadPuzzleData();

    }

    /**
     * Loads the data for the currently selected puzzle into the GUI.
     * Can be used to load a new puzzle or reset progress on the current puzzle.
     */
    private void loadPuzzleData(){
        //Check that a puzzle has actually been loaded first
        if(currentPuzzle == null){
            throw new IllegalStateException("No puzzle selected.");
        }

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
            PuzzleLabel newLabel = new PuzzleLabel("" + (char)('A' + i), false);
            newLabel.setAlignment(Pos.CENTER);
            CodeFragmentGrid.add(newLabel,0, i);
            GridPane.setMargin(newLabel,labelMargins);

            PuzzleLabel newFragment;
            if (currentPuzzle.getType().equals(PuzzleType.DnD)) {
                newFragment = new PuzzleFragmentLabel(puzzleFragments.get(i));
            }
            else{
                newFragment = new PuzzleLabel(puzzleFragments.get(i));
            }
            CodeFragmentGrid.add(newFragment,1, i);
            GridPane.setMargin(newFragment,fragmentMargins);
        }

        //Set the solution data
        if (currentPuzzle.getType().equals(PuzzleType.DnD)) {
            for(int i = 0; i < currentPuzzle.getSolutions().size(); i++) {
                SolutionGrid.getRowConstraints().add(rowConstraint);

                PuzzleLabel newLabel = new PuzzleLabel("" + (i + 1), false);
                newLabel.setAlignment(Pos.CENTER);
                SolutionGrid.add(newLabel, 0, i);
                GridPane.setMargin(newLabel, labelMargins);

                PuzzleFragmentLabel newFragment = new PuzzleFragmentLabel(null);
                SolutionGrid.add(newFragment, 1, i);
                GridPane.setMargin(newFragment, fragmentMargins);
                puzzleAnswerLabels.add(newFragment);
            }
        }
        else if (currentPuzzle.getType().equals(PuzzleType.MC)){
            for(int i = 0; i < puzzleAnswers.size(); i++){
                SolutionGrid.getRowConstraints().add(rowConstraint);

                PuzzleLabel newLabel = new PuzzleLabel("" + (i+1),false);
                newLabel.setAlignment(Pos.CENTER);
                SolutionGrid.add(newLabel,0, i);
                GridPane.setMargin(newLabel,labelMargins);

                RadioButton newAnswer = new RadioButton(puzzleAnswers.get(i).toString()); //TODO: replace with proper answer text
                newAnswer.setTooltip(new Tooltip(puzzleAnswers.get(i).toString()));
                newAnswer.setUserData(puzzleAnswers.get(i));
                newAnswer.setToggleGroup(puzzleAnswerToggles);
                newAnswer.setStyle("-fx-background-color: aliceblue; -fx-border-color: black;");
                newAnswer.setPadding(paddings);
                newAnswer.setMaxWidth(Double.MAX_VALUE);
                SolutionGrid.add(newAnswer,1, i);
                GridPane.setMargin(newAnswer,fragmentMargins);
            }
        }
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
