package parsonsolver;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Puzzle Creator GUI
 * @author Travis Ridge
 */
public class PuzzleCreatorController {
    private PuzzlePaneController rootController; //Link to the controller of the root for passing event
    private PuzzleCreator puzzleCreator; //The current instance of the Puzzle Creator

    private ObservableList<String> MCFalseAnswers = FXCollections.observableArrayList();
    private int nextLine = 1;
    private List<Integer> codeLines = new ArrayList<>();
    private List<Integer> distractorLines = new ArrayList<>();

    //Flags
    private boolean shiftEnter = false; //Set during a shift-enter edit
    private boolean isNewPuzzle = false; //Set if the puzzle is new, and must be created before saving
    private boolean unsavedChanges = false; //Set if unsaved changes exist in the puzzle

    //PUZZLE DEFINITIONS
    @FXML
    private Label HeaderText;

    @FXML
    private TextField ProblemName;

    @FXML
    private TextArea ProblemDescription;

    //PUZZLE OPTIONS
    @FXML
    private TextField Language;

    @FXML
    private CheckBox RequireIndentation;

    @FXML
    private ComboBox<PuzzleType> ProblemType;

    @FXML
    private Button SavePuzzleButton;

    //PUZZLE CODE
    @FXML
    private TextArea SourceCodeBlocks;

    @FXML
    private TextArea SourceCodeEditor;

    @FXML
    private TextArea DistractorBlocks;

    @FXML
    private TextArea DistractorEditor;

    @FXML
    private ListView<String> MCAnswersList;

    @FXML
    private GridPane MCAnswerGrid;

    @FXML
    private Label MCAnswerHeader;

    /**
     * Initialize the Creator screen
     */
    @FXML
    public void initialize() {
        MCAnswersList.setItems(MCFalseAnswers);
        MCAnswersList.setCellFactory(TextFieldListCell.forListView());

        ProblemType.getItems().add(PuzzleType.DnD);
        ProblemType.getItems().add(PuzzleType.MC);
        ProblemType.getItems().add(PuzzleType.FiB);

        codeLines = new ArrayList<>();
        codeLines.add(nextLine++);

        distractorLines = new ArrayList<>();
        distractorLines.add(nextLine++);

        SourceCodeEditor.textProperty().addListener(this::SourceCodeChanged);
        DistractorEditor.textProperty().addListener(this::DistractorChanged);

        ProblemName.textProperty().addListener(this::PuzzlePropertyTextChanged);
        ProblemDescription.textProperty().addListener(this::PuzzlePropertyTextChanged);
        Language.textProperty().addListener(this::PuzzlePropertyTextChanged);
        SourceCodeEditor.textProperty().addListener(this::PuzzlePropertyTextChanged);
        DistractorEditor.textProperty().addListener(this::PuzzlePropertyTextChanged);
        ProblemName.textProperty().addListener(this::PuzzlePropertyTextChanged);

        SetSourceCodeLines();
        SetDistractorLines();
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
     * @param creator     The PuzzleCreator instance
     * @param isNewPuzzle Set true if it is a new puzzle, false is editing puzzle already in set
     */
    public void setPuzzleCreator(PuzzleCreator creator, boolean isNewPuzzle) {
        puzzleCreator = creator;
        this.isNewPuzzle = isNewPuzzle;

        //TODO: Add some proper validation to puzzle creation
        if (isNewPuzzle) {
            ProblemType.setValue(PuzzleType.DnD);
            PuzzleTypePropertyChanged(null);
        }

        PuzzleSet currentSet = puzzleCreator.getCurrentSet();
        if (currentSet != null) {
            HeaderText.setText("Editing PuzzleSet \"" + currentSet.getName() + "\"");

            if (!isNewPuzzle) {
                loadCurrentPuzzle();
            }
        }
        RefreshCanSave();
    }

    private void loadCurrentPuzzle() {
        if (puzzleCreator != null && puzzleCreator.getCurrentSet() != null) {
            Puzzle currentPuzzle = puzzleCreator.getCurrentPuzzle();
            if (currentPuzzle != null) {
                ProblemName.setText(currentPuzzle.getName());
                ProblemDescription.setText(currentPuzzle.getDescription());
                Language.setText(currentPuzzle.getLanguage());
                RequireIndentation.setSelected(currentPuzzle.isIndentRequired());
                ProblemType.setValue(currentPuzzle.getType());
                PuzzleTypePropertyChanged(new ActionEvent()); //Trigger the MC answer display update

                //Puzzle Lines
                if (currentPuzzle.getLines().size() > 0) {
                    List<Block> lines = currentPuzzle.getLines();
                    List<Integer> newColumns = new ArrayList<>();
                    String newText = "";

                    for (int i = 0; i < lines.size(); i++) {
                        if (i != 0)
                            newText = newText.concat("\n");

                        if (lines.get(i).getTab() > 0)
                            newText = newText.concat(new String(new char[lines.get(i).getTab()]).replace("\0", "\t")).concat(lines.get(i).getLines());
                        else
                            newText = newText.concat(lines.get(i).getLines());

                        long newlines = lines.get(i).getLines().chars().filter(ch -> ch == '\n').count();
                        int nextNum = nextLine++;
                        for (long j = 0; j <= newlines; j++) {
                            newColumns.add(nextNum);
                        }
                    }

                    SourceCodeEditor.setText(newText);
                    codeLines = newColumns;
                    SetSourceCodeLines();
                }

                //Distractors
                if (currentPuzzle.getDistractors().size() > 0) {
                    List<Block> lines = currentPuzzle.getDistractors();
                    List<Integer> newColumns = new ArrayList<>();
                    String newText = "";

                    for (int i = 0; i < lines.size(); i++) {
                        if (i != 0)
                            newText = newText.concat("\n");

                        if (lines.get(i).getTab() > 0)
                            newText = newText.concat(new String(new char[lines.get(i).getTab()]).replace("\0", "\t")).concat(lines.get(i).getLines());
                        else
                            newText = newText.concat(lines.get(i).getLines());

                        long newlines = lines.get(i).getLines().chars().filter(ch -> ch == '\n').count();
                        int nextNum = nextLine++;
                        for (long j = 0; j <= newlines; j++) {
                            newColumns.add(nextNum);
                        }
                    }

                    DistractorEditor.setText(newText);
                    distractorLines = newColumns;
                    SetDistractorLines();
                }

                //MC Answers
                if (currentPuzzle.getType() == PuzzleType.MC) {
                    MultipleChoicePuzzle mcPuzzle = (MultipleChoicePuzzle) currentPuzzle;
                    if (mcPuzzle.getChoices().size() > 0) {
                        List<List<Block>> falseAnswers = mcPuzzle.getFalseAnswers();
                        for (List<Block> answer : falseAnswers) {
                            String text = "";
                            if (answer.size() > 0) {
                                text = answer.get(0).getID();
                                for (int i = 1; i < answer.size(); i++) {
                                    text = text.concat(", ").concat(answer.get(i).getID());
                                }
                            }
                            MCAnswersList.getItems().add(text);
                        }
                    }
                }
            }
        }
        RefreshCanSave();
    }

    /**
     * Handle changes to the source code text in the editor
     *
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue   The old value
     * @param newValue   The new value
     */
    public void SourceCodeChanged(ObservableValue<? extends String> observable,
                                  String oldValue, String newValue) {
        //Shift-enter change propagates to here
        //Already handled by the method triggering this event
        if (shiftEnter) {
            shiftEnter = false;
            return;
        }

        if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
            int oldLength = oldValue.length();
            int newLength = newValue.length();
            int minLength = Math.min(oldLength, newLength); //Getter the smaller length

            int startChangeOffset;
            for (startChangeOffset = 0; startChangeOffset < minLength; startChangeOffset++) {
                if (oldValue.charAt(startChangeOffset) != newValue.charAt(startChangeOffset))
                    break;
            }

            if (startChangeOffset == oldLength) {
                long lines = newValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    codeLines.add(nextLine++);
                }
            } else if (startChangeOffset == newLength) {
                long lines = oldValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    codeLines.remove(codeLines.size() - 1);
                }
            } else {
                int endChangeOffset;
                for (endChangeOffset = 1; startChangeOffset + endChangeOffset < minLength + 1; endChangeOffset++) {
                    if (oldValue.charAt(oldLength - endChangeOffset) != newValue.charAt(newLength - endChangeOffset))
                        break;
                }
                endChangeOffset--;

                long startLine = newValue.substring(0, startChangeOffset).chars().filter(ch -> ch == '\n').count();
                long addedLines = newValue.substring(startChangeOffset, newLength - endChangeOffset).chars().filter(ch -> ch == '\n').count();
                long removedLines = oldValue.substring(startChangeOffset, oldLength - endChangeOffset).chars().filter(ch -> ch == '\n').count();

                for (int i = 0; i < removedLines; i++) {
                    codeLines.remove((int) startLine + 1);
                }

                for (int i = 0; i < addedLines; i++) {
                    codeLines.add((int) startLine + i, nextLine++);
                }
            }

            SetSourceCodeLines();
        }
    }

    /**
     * Handle changes to the distractor text in the editor
     *
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue   The old value
     * @param newValue   The new value
     */
    public void DistractorChanged(ObservableValue<? extends String> observable,
                                  String oldValue, String newValue) {
        //Shift-enter change propagates to here
        //Already handled by the method triggering this event
        if (shiftEnter) {
            shiftEnter = false;
            return;
        }

        if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
            int oldLength = oldValue.length();
            int newLength = newValue.length();
            int minLength = Math.min(oldLength, newLength); //Getter the smaller length

            int startChangeOffset;
            for (startChangeOffset = 0; startChangeOffset < minLength; startChangeOffset++) {
                if (oldValue.charAt(startChangeOffset) != newValue.charAt(startChangeOffset))
                    break;
            }

            if (startChangeOffset == oldLength) {
                long lines = newValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    distractorLines.add(nextLine++);
                }
            } else if (startChangeOffset == newLength) {
                long lines = oldValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    distractorLines.remove(distractorLines.size() - 1);
                }
            } else {
                int endChangeOffset;
                for (endChangeOffset = 1; startChangeOffset + endChangeOffset < minLength + 1; endChangeOffset++) {
                    if (oldValue.charAt(oldLength - endChangeOffset) != newValue.charAt(newLength - endChangeOffset))
                        break;
                }
                endChangeOffset--;

                long startLine = newValue.substring(0, startChangeOffset).chars().filter(ch -> ch == '\n').count();
                long addedLines = newValue.substring(startChangeOffset, newLength - endChangeOffset).chars().filter(ch -> ch == '\n').count();
                long removedLines = oldValue.substring(startChangeOffset, oldLength - endChangeOffset).chars().filter(ch -> ch == '\n').count();

                for (int i = 0; i < removedLines; i++) {
                    distractorLines.remove((int) startLine + 1);
                }

                for (int i = 0; i < addedLines; i++) {
                    distractorLines.add((int) startLine + i, nextLine++);
                }
            }

            SetDistractorLines();
        }
    }

    /**
     * Sets the unsaved changes flag to indicate and edit has occurred
     *
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue   The old value
     * @param newValue   The new value
     */
    @FXML
    public void PuzzlePropertyTextChanged(ObservableValue<? extends String> observable,
                                          String oldValue, String newValue) {
        unsavedChanges = true;
    }

    /**
     * Sets the unsaved changes flag to indicate and edit has occurred
     *
     * @param event The triggered event
     */
    @FXML
    public void PuzzlePropertyChanged(ActionEvent event) {
        unsavedChanges = true;
    }

    /**
     * Sets the unsaved changes flag to indicate and edit has occurred
     *
     * @param event The triggered event
     */
    @FXML
    public void PuzzleTypePropertyChanged(ActionEvent event) {
        if (ProblemType.getValue() != PuzzleType.MC) {
            MCAnswerGrid.setVisible(false);
            MCAnswerHeader.setVisible(false);
            MCAnswerGrid.setManaged(false);
            MCAnswerHeader.setManaged(false);
        } else {
            MCAnswerGrid.setVisible(true);
            MCAnswerHeader.setVisible(true);
            MCAnswerGrid.setManaged(true);
            MCAnswerHeader.setManaged(true);
        }
        PuzzlePropertyChanged(event);
    }

    /**
     * Get key events in the source code editor, and sets flags as needed
     *
     * @param event The Key Event
     */
    @FXML
    public void SourceCodeKeyEvent(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED && event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
            int index = SourceCodeEditor.getCaretPosition();
            shiftEnter = true;

            long startLine = SourceCodeEditor.getText(0, index).chars().filter(ch -> ch == '\n').count();
            codeLines.add((int) startLine + 1, codeLines.get((int) startLine));

            String newText = SourceCodeEditor.getText(0, index) + "\n" + SourceCodeEditor.getText().substring(index);
            SourceCodeEditor.setText(newText);
            SourceCodeEditor.positionCaret(index + 1);

            SetSourceCodeLines();
        }
    }

    /**
     * Get key events in the distractor editor, and sets flags as needed
     *
     * @param event The Key Event
     */
    @FXML
    public void DistractorKeyEvent(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_RELEASED && event.getCode() == KeyCode.ENTER && event.isShiftDown()) {
            int index = DistractorEditor.getCaretPosition();
            shiftEnter = true;

            long startLine = DistractorEditor.getText(0, index).chars().filter(ch -> ch == '\n').count();
            distractorLines.add((int) startLine + 1, distractorLines.get((int) startLine));

            String newText = DistractorEditor.getText(0, index) + "\n" + DistractorEditor.getText().substring(index);
            DistractorEditor.setText(newText);
            DistractorEditor.positionCaret(index + 1);

            SetDistractorLines();
        }
    }

    /**
     * Commit changes to the MC Answer
     *
     * @param event EditEvent from the ListView
     */
    @FXML
    public void CommitMCAnswerEdit(ListView.EditEvent<String> event) {
        //Add validation of new value here
        MCAnswersList.getItems().set(event.getIndex(), event.getNewValue());
        //Convert new text to usable output here?

        unsavedChanges = true;
    }

    /**
     * Add a new MC Answer to the list
     *
     * @param event ActionEvent of the button press
     */
    @FXML
    public void AddMCAnswer(ActionEvent event) {
        if (MCAnswersList.getSelectionModel() != null) {
            List<Integer> indices = MCAnswersList.getSelectionModel().getSelectedIndices();
            //Uses single selection mode, so we only have maximum one index
            if (indices.size() > 0)
                MCAnswersList.getItems().add(indices.get(0) + 1, "1, 2, 3");
            else
                MCAnswersList.getItems().add("1, 2, 3");
        } else {
            MCAnswersList.getItems().add("1, 2, 3");
        }
        unsavedChanges = true;
    }

    /**
     * Remove a MC answer from the list
     *
     * @param event ActionEvent of the button press
     */
    @FXML
    public void RemoveMCAnswer(ActionEvent event) {
        if (MCAnswersList.getItems().size() == 0)
            return;

        if (MCAnswersList.getSelectionModel() != null) {
            List<Integer> indices = MCAnswersList.getSelectionModel().getSelectedIndices();
            //Uses single selection mode, so we only have maximum one index
            if (indices.size() > 0)
                MCAnswersList.getItems().remove((int) indices.get(0));
            else
                MCAnswersList.getItems().remove(MCAnswersList.getItems().size() - 1);
        } else {
            MCAnswersList.getItems().remove(MCAnswersList.getItems().size() - 1);
        }
        unsavedChanges = true;
    }

    /**
     * Save the puzzle being edited
     *
     * @param event ActionEvent of the button press
     */
    @FXML
    public void SavePuzzle(ActionEvent event) {
        try {
            if (isNewPuzzle) {
                puzzleCreator.createNewPuzzle(ProblemName.getText(), ProblemType.getValue());
                isNewPuzzle = false;
            }

            Puzzle currentPuzzle = puzzleCreator.getCurrentPuzzle();
            currentPuzzle.setName(ProblemName.getText());
            currentPuzzle.setDescription(ProblemDescription.getText());
            currentPuzzle.setLanguage(Language.getText());
            currentPuzzle.setIndentRequired(RequireIndentation.isSelected());
            currentPuzzle.setType(ProblemType.getValue());

            //Create code blocks
            String currentBlock = "";
            List<String> blockStrings = new ArrayList<>();
            String[] lines = SourceCodeEditor.getText().split("\n");
            for (int i = 0; i < lines.length; i++) {
                currentBlock = currentBlock.concat(lines[i]);

                if (i != lines.length - 1 && codeLines.get(i).equals(codeLines.get(i + 1))) {
                    currentBlock = currentBlock.concat("\n");
                } else {
                    blockStrings.add(currentBlock);
                    currentBlock = "";
                }
            }
            currentPuzzle.setLines(puzzleCreator.convertLines(blockStrings, false));

            currentBlock = "";
            blockStrings = new ArrayList<>();
            lines = DistractorEditor.getText().split("\n");
            for (int i = 0; i < lines.length; i++) {
                currentBlock = currentBlock.concat(lines[i]);

                if (i != lines.length - 1 && distractorLines.get(i).equals(distractorLines.get(i + 1))) {
                    currentBlock = currentBlock.concat("\n");
                } else {
                    blockStrings.add(currentBlock);
                    currentBlock = "";
                }
            }
            currentPuzzle.setDistractors(puzzleCreator.convertLines(blockStrings, true));

            //Create false answers
            if (currentPuzzle.getType() == PuzzleType.MC) {
                List<List<Block>> answers = new ArrayList<>();
                for (String answerText : MCAnswersList.getItems()) {
                    List<Block> answer = new ArrayList<>();
                    String[] ids = answerText.split(",");
                    for (String s : ids) {
                        Block b = currentPuzzle.getBlock(s.trim());
                        if (b == null) //Block missing, fallback
                            new Block(s.trim(), "");
                        answer.add(b);
                    }
                    answers.add(answer);
                }
                ((MultipleChoicePuzzle) currentPuzzle).setFalseAnswers(answers);
            }

            unsavedChanges = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update the texts of the blocks list
     */
    private void SetSourceCodeLines() {
        int lineNum = 1;
        String codeColumns = "" + lineNum++;
        for (int i = 1; i < codeLines.size(); i++) {
            if (codeLines.get(i).equals(codeLines.get(i - 1)))
                codeColumns = codeColumns.concat("\n-");
            else
                codeColumns = codeColumns.concat("\n" + lineNum++);
        }
        SourceCodeBlocks.setText(codeColumns);
    }

    /**
     * Update the texts of the blocks list
     */
    private void SetDistractorLines() {
        int lineNum = 1;
        String codeColumns = "0X" + lineNum++;
        for (int i = 1; i < distractorLines.size(); i++) {
            if (distractorLines.get(i).equals(distractorLines.get(i - 1)))
                codeColumns = codeColumns.concat("\n-");
            else
                codeColumns = codeColumns.concat("\n0X" + lineNum++);
        }
        DistractorBlocks.setText(codeColumns);
    }

    /**
     * Refresh if save button is available
     */
    private void RefreshCanSave() {
        if (puzzleCreator != null && puzzleCreator.getCurrentSet() != null) {
            SavePuzzleButton.setDisable(false);
        } else {
            SavePuzzleButton.setDisable(true);
        }
    }
}
