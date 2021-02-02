package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditorController {

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField metadataValueTextField;

    public void setCategoryText(String category) {
        categoryTextField.setText(category);
    }

    public void setMetadataValueText(String metadataValue) {
        metadataValueTextField.setText(metadataValue);
    }
}
