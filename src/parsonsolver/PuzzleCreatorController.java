package parsonsolver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;

/**
 * Controller class for the Puzzle Creator GUI
 * @author Travis Ridge
 */
public class PuzzleCreatorController {

    private ObservableList<String> MCFalseAnswers = FXCollections.observableArrayList();

    @FXML
    private ListView<String> MCAnswersList;

    @FXML
    public void initialize() {
        MCAnswersList.setItems(MCFalseAnswers);
        MCAnswersList.setCellFactory(TextFieldListCell.forListView());

        MCAnswersList.setOnEditCommit(event -> MCAnswersList.getItems().set(event.getIndex(), event.getNewValue()));
    }

    @FXML
    public void AddMCAnswer(ActionEvent event){
        MCAnswersList.getItems().add("New Answer");
    }

    @FXML
    public void RemoveMCAnswer(ActionEvent event){
        MCAnswersList.getItems().remove(MCAnswersList.getItems().size() - 1);
    }
}
