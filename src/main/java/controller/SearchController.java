package controller;

import domain.FileSizeUnit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @FXML
    TextField rootFolderTextField;

    @FXML
    ChoiceBox<String> lowerBoundChoiceBox;
    @FXML
    ChoiceBox<String> upperBoundChoiceBox;

    @FXML
    CheckBox fileSizeEnabledCheckBox;
    @FXML
    TextField fileSizeLowerBoundTextField;
    @FXML
    TextField fileSizeUpperBoundTextField;

    DirectoryChooser directoryChooser;


    public void selectRootFolder() {
        directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        log.info(directory.getAbsolutePath());
        rootFolderTextField.setText(directory.getAbsolutePath());
    }

    public void searchButtonClick() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFileSizeElements();
    }

    private void initializeFileSizeElements() {
        lowerBoundChoiceBox.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        upperBoundChoiceBox.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        fileSizeLowerBoundTextField.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        fileSizeUpperBoundTextField.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());

        // Initialize file size choice boxes
        lowerBoundChoiceBox.getItems().addAll(FileSizeUnit.getAbbreviations());
        lowerBoundChoiceBox.setValue(FileSizeUnit.MEGABYTE.getAbbreviation());
        lowerBoundChoiceBox.getSelectionModel().selectedItemProperty().addListener((a, oldValue, newValue) -> {
            if (newValue != null) {
                log.info("lower");
                upperBoundChoiceBox.setValue(newValue);
            }
        });

        upperBoundChoiceBox.getItems().addAll(FileSizeUnit.getAbbreviations());
        upperBoundChoiceBox.setValue(FileSizeUnit.MEGABYTE.getAbbreviation());
        upperBoundChoiceBox.getSelectionModel().selectedItemProperty().addListener((a, oldValue, newValue) -> {
            if (newValue != null) {
                log.info("upper");
                lowerBoundChoiceBox.setValue(newValue);
            }
        });
    }
}
