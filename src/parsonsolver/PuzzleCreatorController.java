package parsonsolver;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Puzzle Creator GUI
 * @author Travis Ridge
 */
public class PuzzleCreatorController {
    private PuzzlePaneController rootController; //Link to the controller of the root for passing event
    private ObservableList<String> MCFalseAnswers = FXCollections.observableArrayList();
    private int nextLine = 1;
    private List<Integer> codeLines = new ArrayList<>();
    private List<Integer> distractorLines = new ArrayList<>();
    private boolean shiftEnter = false;

    @FXML
    private ListView<String> MCAnswersList;

    @FXML
    private TextArea SourceCodeBlocks;

    @FXML
    private TextArea DistractorBlocks;

    @FXML
    private TextArea SourceCodeEditor;

    @FXML
    private TextArea DistractorEditor;

    @FXML
    public void initialize() {
        MCAnswersList.setItems(MCFalseAnswers);
        MCAnswersList.setCellFactory(TextFieldListCell.forListView());

        codeLines = new ArrayList<>();
        codeLines.add(nextLine++);

        distractorLines = new ArrayList<>();
        distractorLines.add(nextLine++);

        SourceCodeEditor.textProperty().addListener(this::SourceCodeChanged);
        DistractorEditor.textProperty().addListener(this::DistractorChanged);

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
     * Handle changes to the source code text in the editor
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue The old value
     * @param newValue The new value
     */
    public void SourceCodeChanged(ObservableValue<? extends String> observable,
                                  String oldValue, String newValue){
        if(shiftEnter){ //Shift-enter change propagates to here
            shiftEnter = false;
            return;
        }

        if(oldValue != null && newValue != null && !oldValue.equals(newValue)) {
            int oldLength = oldValue.length();
            int newLength = newValue.length();
            int minLength = Math.min(oldLength, newLength); //Getter the smaller length

            int startChangeOffset;
            for (startChangeOffset = 0; startChangeOffset < minLength; startChangeOffset++) {
                if (oldValue.charAt(startChangeOffset) != newValue.charAt(startChangeOffset))
                    break;
            }

            if(startChangeOffset == oldLength) {
                long lines = newValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    codeLines.add(nextLine++);
                }
            }
            else if(startChangeOffset == newLength) {
                long lines = oldValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    codeLines.remove(codeLines.size() - 1);
                }
            }
            else {
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
     * @param observable The {@code ObservableValue} which value changed
     * @param oldValue The old value
     * @param newValue The new value
     */
    public void DistractorChanged(ObservableValue<? extends String> observable,
                                  String oldValue, String newValue){
        if(shiftEnter){ //Shift-enter change propagates to here
            shiftEnter = false;
            return;
        }

        if(oldValue != null && newValue != null && !oldValue.equals(newValue)) {
            int oldLength = oldValue.length();
            int newLength = newValue.length();
            int minLength = Math.min(oldLength, newLength); //Getter the smaller length

            int startChangeOffset;
            for (startChangeOffset = 0; startChangeOffset < minLength; startChangeOffset++) {
                if (oldValue.charAt(startChangeOffset) != newValue.charAt(startChangeOffset))
                    break;
            }

            if(startChangeOffset == oldLength) {
                long lines = newValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    distractorLines.add(nextLine++);
                }
            }
            else if(startChangeOffset == newLength) {
                long lines = oldValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                for (int i = 0; i < lines; i++) {
                    distractorLines.remove(distractorLines.size() - 1);
                }
            }
            else {
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
     * Get key events in the source code editor, and sets flags as needed
     * @param event
     */
    @FXML
    public void SourceCodeKeyEvent(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_RELEASED && event.getCode() == KeyCode.ENTER && event.isShiftDown()){
            int index = SourceCodeEditor.getCaretPosition();
            shiftEnter = true;

            long startLine = SourceCodeEditor.getText(0,index).chars().filter(ch -> ch == '\n').count();
            codeLines.add((int) startLine + 1, codeLines.get((int)startLine));

            String newText = SourceCodeEditor.getText(0,index) + "\n" + SourceCodeEditor.getText().substring(index);
            SourceCodeEditor.setText(newText);
            SourceCodeEditor.positionCaret(index+1);

            SetSourceCodeLines();
        }
    }

    /**
     * Get key events in the distractor editor, and sets flags as needed
     * @param event
     */
    @FXML
    public void DistractorKeyEvent(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_RELEASED && event.getCode() == KeyCode.ENTER && event.isShiftDown()){
            int index = DistractorEditor.getCaretPosition();
            shiftEnter = true;

            long startLine = DistractorEditor.getText(0,index).chars().filter(ch -> ch == '\n').count();
            distractorLines.add((int) startLine + 1, distractorLines.get((int)startLine));

            String newText = DistractorEditor.getText(0,index) + "\n" + DistractorEditor.getText().substring(index);
            DistractorEditor.setText(newText);
            DistractorEditor.positionCaret(index+1);

            SetDistractorLines();
        }
    }

    /**
     * Commit changes to the MC Answer
     * @param event EditEvent from the ListView
     */
    @FXML
    public void CommitMCAnswerEdit(ListView.EditEvent<String> event){
        //Add validation of new value here
        MCAnswersList.getItems().set(event.getIndex(), event.getNewValue());
        //Convert new text to usable output here?

        MCAnswerEdit(event);
    }

    /**
     * Debugging event for editing of the MC Answers list
     * @param event EditEvent from the ListView
     */
    @FXML
    public void MCAnswerEdit(ListView.EditEvent<String> event){
        System.out.println(event.getEventType() + "\t" + event.getIndex() + "\t" + event.getNewValue());
    }

    /**
     * Add a new MC Answer to the list
     * @param event ActionEvent of button press
     */
    @FXML
    public void AddMCAnswer(ActionEvent event) {
        MCAnswersList.getItems().add("New Answer");
    }

    /**
     * Remove a MC answer from the list
     * @param event ActionEvent of button press
     */
    @FXML
    public void RemoveMCAnswer(ActionEvent event) {
        MCAnswersList.getItems().remove(MCAnswersList.getItems().size() - 1);
    }

    /**
     * Update the texts of the blocks list
     */
    private void SetSourceCodeLines(){
        int lineNum = 1;
        String codeColumns = "" + lineNum++;
        for(int i = 1; i < codeLines.size(); i++) {
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
    private void SetDistractorLines(){
        int lineNum = 1;
        String codeColumns = "X" + lineNum++;
        for(int i = 1; i < distractorLines.size(); i++) {
            if (distractorLines.get(i).equals(distractorLines.get(i - 1)))
                codeColumns = codeColumns.concat("\n-");
            else
                codeColumns = codeColumns.concat("\nX" + lineNum++);
        }
        DistractorBlocks.setText(codeColumns);
    }
}
