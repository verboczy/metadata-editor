package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import metadata.MetadataWriter;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField metadataValueTextField;

    private MetadataWriter metadataWriter;
    private Path path;

    public void setPath(Path path) {
        this.path = path;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        metadataWriter = new MetadataWriter();
    }

    public void setCategoryText(String category) {
        categoryTextField.setText(category);
    }

    public void setMetadataValueText(String metadataValue) {
        metadataValueTextField.setText(metadataValue);
    }


    @FXML
    private void editMetadata() {
        metadataWriter.write(path, categoryTextField.getText(), metadataValueTextField.getText());
        // TODO 1. close the window on success
        // TODO 2. Refresh file's data
    }
}
