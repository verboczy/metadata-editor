package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metadata.Metadata;
import metadata.MetadataWriter;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    // Text fields
    @FXML
    private TextField categoryTextField;
    @FXML
    private TextField metadataValueTextField;

    // Button
    @FXML
    private Button editButton;

    private Path path;
    private MetadataWriter metadataWriter;
    private BrowserController browserController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        metadataWriter = new MetadataWriter();
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setCategoryText(String category) {
        categoryTextField.setText(category);
    }

    public void setMetadataValueText(String metadataValue) {
        metadataValueTextField.setText(metadataValue);
    }

    public void setBrowserController(BrowserController browserController) {
        this.browserController = browserController;
    }

    public void editMetadata() {
        metadataWriter.write(path, categoryTextField.getText(), metadataValueTextField.getText());
        browserController.loadMetadata(path);
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}
