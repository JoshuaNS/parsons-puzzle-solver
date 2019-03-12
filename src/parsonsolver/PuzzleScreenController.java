package parsonsolver;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for the PuzzleScreen GUI
 * @author Travis Ridge
 */
public class PuzzleScreenController {
    private PuzzlePaneController rootController; //Link to the controller of the root for passing event
    private PuzzleSet currentPuzzleSet;
    private int puzzleIndex;
    private Puzzle currentPuzzle;
    private List<Block> puzzleFragments;
    private List<List<Block>> puzzleAnswers; //not used by DnD
    private List<Block> puzzleLines; //only used by FiB
    private ToggleGroup puzzleAnswerToggles; //holds MC and FiB selection
    private List<Label> puzzleAnswerLabels; //holds DnD answer
    private Map<String, String> puzzleIdMapping; //maps block id value to a GUI label

    //Default UI parameters for puzzle grids
    private static Insets labelMargins = new Insets(5, -1, 5, 5);
    private static Insets fragmentMargins = new Insets(5, 5, 5, 0);
    private static Insets labelPadding = new Insets(5, 5, 5, 5);
    private static RowConstraints rowConstraint = new RowConstraints(-1, -1, -1, Priority.NEVER, null, true);

    @FXML
    private GridPane PuzzleGrid;

    @FXML
    private GridPane CodeFragmentGrid;

    @FXML
    private GridPane SolutionGrid;

    @FXML
    private GridPane FillBlanksGrid;

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
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void NextPuzzle(ActionEvent event) {

        if (puzzleIndexValid(puzzleIndex + 1)) {
            setCurrentPuzzle(puzzleIndex + 1);
        } else {
            rootController.openPuzzleSelect();
        }
    }

    /**
     * Reloads the currently selected puzzle, clearing all progress in the GUI.
     *
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
     *
     * @param event The ActionEvent sent by PuzzleScreen
     */
    @FXML
    public void CheckAnswer(ActionEvent event) {
        List<Block> answer;
        if (currentPuzzle.getType() == PuzzleType.DnD) {
            answer = new ArrayList<>();
            for (Label solution : puzzleAnswerLabels) {
                answer.add((Block) solution.getUserData());
            }
        } else {
            Toggle selectedAnswer = puzzleAnswerToggles.getSelectedToggle();
            if (selectedAnswer != null) {
                answer = (List<Block>) selectedAnswer.getUserData();
            } else {
                answer = new ArrayList<>();
            }
        }

        Object result = currentPuzzle.checkSolution(answer);
        if (result == null) {
            FeedbackText.setText("Error: Invalid answer");
        } else if (result.equals(true)) {
            FeedbackText.setText("Solution is Correct!");
            for (Label label : puzzleAnswerLabels) {
                ((PuzzleLabel) label).setCurrentSyle(PuzzleLabel.DEFAULT_STYLE);
            }
            currentPuzzle.endPuzzle();
            currentPuzzle.startPuzzle();
        } else if (result.equals(false))
            FeedbackText.setText("Incorrect Answer!");
        else if (result instanceof boolean[]) { //should only trigger on DnD
            boolean[] feedback = (boolean[]) result;
            int correct = 0;
            for (int i = 0; i < feedback.length; i++) {
                if (feedback[i]) {
                    ((PuzzleLabel) puzzleAnswerLabels.get(i)).setCurrentSyle(PuzzleLabel.DEFAULT_STYLE);
                    correct++;
                } else
                    ((PuzzleLabel) puzzleAnswerLabels.get(i)).setCurrentSyle(PuzzleLabel.INCORRECT_STYLE);
            }
            FeedbackText.setText("Incorrect Answer! (" + correct + "/" + feedback.length + ")");
        } else
            FeedbackText.setText("Error: Invalid answer");
    }

    /**
     * Initializes the puzzle screen.
     */
    @FXML
    public void initialize() {

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
     * Sets a new puzzle set, and loads the selected puzzle in the set to the GUI.
     *
     * @param puzzleSet   The set of the puzzles to be loaded
     * @param puzzleIndex The index of the first puzzle to be loaded
     */
    public void setPuzzleSet(PuzzleSet puzzleSet, int puzzleIndex) {
        currentPuzzleSet = puzzleSet;

        setCurrentPuzzle(puzzleIndex);
    }

    /**
     * Stops the timer on the current puzzle
     */
    public void endPuzzle() {
        currentPuzzle.endPuzzle();
    }

    /**
     * Sets a new puzzle and loads it into the GUI.
     *
     * @param newPuzzleIndex The index of the new puzzle within the puzzle set.
     */
    private void setCurrentPuzzle(int newPuzzleIndex) {
        if (!puzzleIndexValid(newPuzzleIndex)) {
            throw new IllegalArgumentException("newPuzzleIndex: " + newPuzzleIndex);
        }
        if (currentPuzzle != null) {
            currentPuzzle.endPuzzle();
        }

        puzzleIndex = newPuzzleIndex;
        currentPuzzle = currentPuzzleSet.getPuzzle(puzzleIndex);
        currentPuzzle.startPuzzle();
        ProblemName.setText(currentPuzzle.getName());
        ProblemDescription.setText(currentPuzzle.getDescription());
        puzzleFragments = currentPuzzle.getBlocksSet();
        puzzleAnswers = currentPuzzle.getChoices();
        puzzleLines = currentPuzzle.getLines();
        loadPuzzleData();
    }

    /**
     * Loads the data for the currently selected puzzle into the GUI.
     * Can be used to load a new puzzle or reset progress on the current puzzle.
     */
    private void loadPuzzleData() {
        //Check that a puzzle has actually been loaded first
        if (currentPuzzle == null) {
            throw new IllegalStateException("No puzzle selected.");
        }

        //Reset the code fragment grid to use the current puzzle
        CodeFragmentGrid.getChildren().clear();
        CodeFragmentGrid.getRowConstraints().clear();
        SolutionGrid.getChildren().clear();
        SolutionGrid.getRowConstraints().clear();
        FillBlanksGrid.getChildren().clear();
        FillBlanksGrid.getRowConstraints().clear();
        puzzleAnswerToggles = new ToggleGroup();
        puzzleAnswerLabels = new ArrayList<>();
        puzzleIdMapping = new HashMap<>();
        FeedbackText.setText(null);

        //Set the code fragments
        loadFragmentGrid();

        //Set the solution data
        loadSolutionGrid();

        //Set the partial solution data
        loadFillBlanksGrid();
    }

    /**
     * Loads Code Fragment grid
     * Default: Contains all blocks in labels marked A-Z
     * DnD: Contains all blocks in draggable labels
     * FiB: Contains all blocks that do not have an associated block
     */
    private void loadFragmentGrid() {
        int j = 0;
        for (Block fragment : puzzleFragments) {
            if (currentPuzzle.getType() != PuzzleType.FiB || fragment.hasAssociatedBlocks()) {
                CodeFragmentGrid.getRowConstraints().add(rowConstraint);

                //Recursive function to build label string
                String labelText = getFragmentLabelText(j);
                puzzleIdMapping.put(fragment.getID(), labelText);

                PuzzleLabel newLabel = new PuzzleLabel(labelText, false);
                newLabel.setAlignment(Pos.CENTER);
                CodeFragmentGrid.add(newLabel, 0, j);
                GridPane.setMargin(newLabel, labelMargins);

                PuzzleLabel newFragment;
                if (currentPuzzle.getType() == PuzzleType.DnD) {
                    newFragment = new PuzzleFragmentLabel(fragment.getLines());
                } else {
                    newFragment = new PuzzleLabel(fragment.getLines());
                }
                newFragment.setUserData(fragment);
                CodeFragmentGrid.add(newFragment, 1, j);
                GridPane.setMargin(newFragment, fragmentMargins);
                j++;
            }
        }
    }

    /**
     * Generate the puzzle solution grid
     * Default: Contains radio button list of answers
     * DnD: Contains blank list to drag solution into
     */
    private void loadSolutionGrid() {
        if (currentPuzzle.getType() == PuzzleType.DnD) {
            for (int i = 0; i < currentPuzzle.getSolutionSet().size(); i++) {
                SolutionGrid.getRowConstraints().add(rowConstraint);

                PuzzleLabel newLabel = new PuzzleLabel("" + (i + 1), false);
                newLabel.setAlignment(Pos.CENTER);
                SolutionGrid.add(newLabel, 0, i);
                GridPane.setMargin(newLabel, labelMargins);

                PuzzleFragmentLabel newFragment = new PuzzleFragmentLabel(null);
                newFragment.setUserData(new Block("", ""));
                SolutionGrid.add(newFragment, 1, i);
                GridPane.setMargin(newFragment, fragmentMargins);
                puzzleAnswerLabels.add(newFragment);
            }
        } else {
            for (int i = 0; i < puzzleAnswers.size(); i++) {
                SolutionGrid.getRowConstraints().add(rowConstraint);

                PuzzleLabel newLabel = new PuzzleLabel("" + (i + 1), false);
                newLabel.setAlignment(Pos.CENTER);
                SolutionGrid.add(newLabel, 0, i);
                GridPane.setMargin(newLabel, labelMargins);

                RadioButton newAnswer = new RadioButton(generateCondensedMCAnswerText(i));
                ToolTipDefaultsFixer.setTooltipTimers(1000, 50000, 200);
                newAnswer.setTooltip(new Tooltip(generateMCAnswerText(i)));
                newAnswer.setUserData(puzzleAnswers.get(i));
                newAnswer.setToggleGroup(puzzleAnswerToggles);
                newAnswer.setStyle(PuzzleLabel.DEFAULT_STYLE);
                newAnswer.setPadding(labelPadding);
                newAnswer.setMaxWidth(Double.MAX_VALUE);
                SolutionGrid.add(newAnswer, 1, i);
                GridPane.setMargin(newAnswer, fragmentMargins);
            }
        }
    }

    /**
     * Loads Fill Blanks grid
     * Default: Hidden
     * FiB: Contains partial solution for puzzle
     */
    private void loadFillBlanksGrid() {
        if (currentPuzzle.getType() == PuzzleType.FiB) {
            PuzzleGrid.getColumnConstraints().get(2).setPercentWidth(50);
            int solutionLine = 1;
            for (int i = 0; i < puzzleLines.size(); i++) {
                FillBlanksGrid.getRowConstraints().add(rowConstraint);

                PuzzleLabel newLabel;
                PuzzleLabel newFragment;
                if (puzzleLines.get(i).hasAssociatedBlocks()) {
                    newLabel = new PuzzleLabel("" + solutionLine++, false);
                    newFragment = new PuzzleLabel(null);
                } else {
                    newLabel = new PuzzleLabel(null, false);
                    newFragment = new PuzzleLabel(puzzleLines.get(i).getLines());
                }

                newLabel.setAlignment(Pos.CENTER);
                FillBlanksGrid.add(newLabel, 0, i);
                GridPane.setMargin(newLabel, labelMargins);

                FillBlanksGrid.add(newFragment, 1, i);
                GridPane.setMargin(newFragment, fragmentMargins);
            }
        } else {
            PuzzleGrid.getColumnConstraints().get(2).setPercentWidth(0);
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

    /**
     * Generate the condensed answer text for an MC answer
     *
     * @param index The index of the MC Answer
     * @return The MC answer as a atring
     */
    private String generateCondensedMCAnswerText(int index) {
        if (index >= puzzleAnswers.size()) {
            return null;
        }

        List<Block> answerList = puzzleAnswers.get(index);

        String toReturn = "[ ";
        if (answerList != null && !answerList.isEmpty()) {
            toReturn = toReturn.concat(getFragmentIdLabel(answerList.get(0)));
            for (int i = 1; i < answerList.size(); i++) {
                toReturn = toReturn.concat(", ").concat(getFragmentIdLabel(answerList.get(i)));
            }
        }
        toReturn = toReturn.concat(" ]");


        return toReturn;
    }

    /**
     * Generate the answer text for an MC answer
     *
     * @param index The index of the MC Answer
     * @return The MC answer code as a string
     */
    private String generateMCAnswerText(int index) {
        if (index >= puzzleAnswers.size()) {
            return null;
        }

        List<Block> answerList = puzzleAnswers.get(index);

        String toReturn = "";
        if (answerList != null && !answerList.isEmpty()) {
            for (int i = 0; i < answerList.size(); i++) {
                toReturn = toReturn.concat(answerList.get(i).getLines()).concat("\n");
            }
        }


        return toReturn;
    }

    /**
     * Get the displayed label text associated with a block
     *
     * @param b A code block
     * @return The id label associated with that block
     */
    private String getFragmentIdLabel(Block b) {
        return puzzleIdMapping.get(b.getID());
    }

    /**
     * Get the letter label text (A-Z) for an integer
     * Called recursively if multiple characters required
     *
     * @param k The number to be encoded
     * @return The number encoded in alphabetical characters
     */
    private String getFragmentLabelText(int k) {
        if (k < 26) {
            return Character.toString((char) ('A' + k));
        } else {
            return getFragmentLabelText(k / 26 - 1).concat(Character.toString((char) ('A' + (k % 26))));
        }
    }
}
