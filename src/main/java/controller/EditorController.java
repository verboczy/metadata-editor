package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import metadata.MetadataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class EditorController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(EditorController.class);

    @FXML
    private TextField categoryTextField;
    @FXML
    private TextArea metadataValueTextArea;
    @FXML
    private Button editButton;

    private boolean isRename;
    private String oldCategory;

    private Path path;
    private MetadataWriter metadataWriter;
    private BrowserController browserController;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        metadataWriter = new MetadataWriter();
    }

    public void setRename(final boolean isRename) {
        this.isRename = isRename;
        categoryTextField.setEditable(isRename);
    }

    public void setOldCategory(final String category) {
        this.oldCategory = category;
    }

    public void setPath(final Path path) {
        this.path = path;
    }

    public void setCategoryText(final String category) {
        categoryTextField.setText(category);
    }

    public void setMetadataValueText(final String metadataValue) {
        metadataValueTextArea.setText(metadataValue);
    }

    public void setBrowserController(final BrowserController browserController) {
        this.browserController = browserController;
    }

    public void editMetadata() {
        if (isRename) {
            log.trace("Renaming category [{}] to [{}] in file [{}].", oldCategory, categoryTextField.getText(), path);
            metadataWriter.rename(path, oldCategory, categoryTextField.getText(), metadataValueTextArea.getText());
        } else {
            log.trace("Writing [{}] category with [{}] value to file [{}].", categoryTextField.getText(), metadataValueTextArea.getText(), path);
            metadataWriter.write(path, categoryTextField.getText(), metadataValueTextArea.getText());
        }
        browserController.loadMetadata(path);
        final Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }
}
