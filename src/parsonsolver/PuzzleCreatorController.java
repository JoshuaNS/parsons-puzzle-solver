package parsonsolver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.InputMethodEvent;
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
    private int MCAnswerSelectedIndex = -1;
    private int nextLine = 1;
    private List<Integer> codeLines = new ArrayList<>();
    private boolean shiftEnter = false;

    @FXML
    private ListView<String> MCAnswersList;

    //@FXML
    //private TableColumn<TableView, TextArea> SourceCodeColumn;

    @FXML
    private TextArea SourceCodeEditor;

    @FXML
    public void initialize() {
        MCAnswersList.setItems(MCFalseAnswers);
        MCAnswersList.setCellFactory(TextFieldListCell.forListView());

        codeLines = new ArrayList<>();
        codeLines.add(nextLine++);

        SourceCodeEditor.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if(shiftEnter){
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

                    //System.out.println("OLD LENGTH:            " + oldLength);
                    //System.out.println("NEW LENGTH:            " + newLength);
                    //System.out.println("START OFFSET:          " + startChangeOffset);
                    if(startChangeOffset == oldLength){
                        //System.out.println("TEXT APPENDED TO END:  \"" + newValue.substring(startChangeOffset) + "\"");
                        if(shiftEnter){
                            System.out.println("MULTI-LINE BLOCK");
                            codeLines.add(codeLines.get(codeLines.size()-1));
                            shiftEnter = false;
                        }
                        else {
                            long lines = newValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                            for(int i = 0; i < lines; i++){
                                codeLines.add(nextLine++);
                            }
                        }
                    }
                    else if(startChangeOffset == newLength){
                        //System.out.println("TEXT DELETED FROM END: " + oldValue.substring(startChangeOffset));
                        long lines = oldValue.substring(startChangeOffset).chars().filter(ch -> ch == '\n').count();
                        for(int i = 0; i < lines; i++){
                            codeLines.remove(codeLines.size()-1);
                        }
                    }
                    else {
                        int endChangeOffset;
                        for (endChangeOffset = 1; startChangeOffset + endChangeOffset < minLength + 1; endChangeOffset++) {
                            if (oldValue.charAt(oldLength - endChangeOffset) != newValue.charAt(newLength - endChangeOffset))
                                break;
                        }
                        endChangeOffset--;
                        //System.out.println("TEXT CHANGED FROM:     \"" + oldValue.substring(startChangeOffset, oldLength - endChangeOffset) + "\"");
                        //System.out.println("               TO:     \"" + newValue.substring(startChangeOffset, newLength - endChangeOffset) + "\"");

                        long startLine = newValue.substring(0, startChangeOffset).chars().filter(ch -> ch == '\n').count();
                        long addedLines = newValue.substring(startChangeOffset, newLength - endChangeOffset).chars().filter(ch -> ch == '\n').count();
                        long removedLines = oldValue.substring(startChangeOffset, oldLength - endChangeOffset).chars().filter(ch -> ch == '\n').count();

                        for (int i = 0; i < removedLines; i++) {
                            codeLines.remove((int)startLine + 1);
                        }

                        if (shiftEnter) {
                            System.out.println("MULTI-LINE BLOCK");
                            codeLines.add((int) startLine + 1, codeLines.get((int)startLine));
                            shiftEnter = false;
                        } else {
                            for (int i = 0; i < addedLines; i++) {
                                codeLines.add((int) startLine + i, nextLine++);
                            }
                        }
                    }

                    /*System.out.println("Old Text:       " + oldValue);
                    System.out.println("New Text:       " + newValue);
                    System.out.println("Start index:    " + startChangeOffset);
                    System.out.println("End offset:     " + endChangeOffset);
                    System.out.println("Old end index:  " + (oldLength - endChangeOffset));
                    System.out.println("New end index:  " + (newLength - endChangeOffset));
                    System.out.println("Caret Position: " + SourceCodeEditor.getCaretPosition());
                    System.out.println("Old value:      \"" + oldValue.substring(startChangeOffset, oldLength - endChangeOffset) + "\"");
                    System.out.println("New value:      \"" + newValue.substring(startChangeOffset, newLength - endChangeOffset) + "\"");
                    System.out.println();*/

                    printLineNums();
                }
            }
        });
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
     * Get key events in the source code editor, and sets flags as needed
     * @param event
     */
    @FXML
    public void SourceCodeKeyEvent(KeyEvent event) {
        //System.out.println(event.getEventType() + "\t" + event.getCode() + "\t" + event.getCharacter() + "\t" + event.isControlDown() + "\t" + event.isShiftDown());
        if(event.getEventType() == KeyEvent.KEY_RELEASED && event.getCode() == KeyCode.ENTER && event.isShiftDown()){
            int index = SourceCodeEditor.getCaretPosition();
            shiftEnter = true;

            long startLine = SourceCodeEditor.getText(0,index).chars().filter(ch -> ch == '\n').count();
            //System.out.println("MULTI-LINE BLOCK");
            codeLines.add((int) startLine + 1, codeLines.get((int)startLine));

            String newText = SourceCodeEditor.getText(0,index) + "\n" + SourceCodeEditor.getText().substring(index);
            SourceCodeEditor.setText(newText);
            SourceCodeEditor.positionCaret(index+1);

            printLineNums();
        }
    }

    @FXML
    public void CommitMCAnswerEdit(ListView.EditEvent<String> event){
        //Add validation of new value here
        MCAnswersList.getItems().set(event.getIndex(), event.getNewValue());
        //Convert new text to usable output here?

        MCAnswerEdit(event);
    }

    /**
     * Debugging event for editing of the MC Answers list
     * @param event
     */
    @FXML
    public void MCAnswerEdit(ListView.EditEvent<String> event){
        System.out.println(event.getEventType() + "\t" + event.getIndex() + "\t" + event.getNewValue());
    }

    @FXML
    public void AddMCAnswer(ActionEvent event) {
        MCAnswersList.getItems().add("New Answer");
    }

    @FXML
    public void RemoveMCAnswer(ActionEvent event) {
        MCAnswersList.getItems().remove(MCAnswersList.getItems().size() - 1);
    }

    @FXML
    public void KeyReleased(KeyEvent event) {
        //System.out.println(event.getEventType() + "\t" + event.getCode() + "\t" + event.getCharacter() + "\t" + event.isControlDown() + "\t" + event.isShiftDown());
        if(event.getCode() == KeyCode.ENTER && MCAnswerSelectedIndex != -1){
            MCAnswersList.getItems().add(MCAnswerSelectedIndex + 1,"New Answer");
        }
    }

    @FXML
    public void InputMethodChanged(InputMethodEvent event){
        System.out.println(event.getEventType());
    }

    private void printLineNums(){
        int lineNum = 1;
        String[] lines = SourceCodeEditor.getText().split("\n");
        System.out.println(lineNum++ + "\t" + lines[0]);
        for(int i = 1; i < codeLines.size(); i++) {
            if (codeLines.get(i) == codeLines.get(i - 1)) {
                System.out.println("-\t" + lines[i]);
            }
            else if(i >= lines.length){ //Some weirdness with trailing newlines
                System.out.println(lineNum++);
            }
            else {
                System.out.println(lineNum++ + "\t" + lines[i]);
            }
        }
        System.out.println();
    }
}
