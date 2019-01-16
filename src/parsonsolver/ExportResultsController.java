package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ExportResultsController {
    @FXML
    private TextArea ExportResultsText;

    @FXML
    public void initialize() {

    }

    public void setExportResultsText(String text) {
        ExportResultsText.setText(text);
    }
}
