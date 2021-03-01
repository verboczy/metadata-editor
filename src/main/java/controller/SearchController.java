package controller;

import domain.FileSizeUnit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    // Root folder elements
    @FXML
    TextField rootFolderTextField;
    DirectoryChooser directoryChooser;

    // File size elements
    @FXML
    CheckBox fileSizeEnabledCheckBox;
    @FXML
    ChoiceBox<String> lowerBoundChoiceBox;
    @FXML
    ChoiceBox<String> upperBoundChoiceBox;
    @FXML
    TextField fileSizeLowerBoundTextField;
    @FXML
    TextField fileSizeUpperBoundTextField;

    // Extension elements
    @FXML
    CheckBox extensionEnabledCheckBox;

    @FXML
    CheckBox pngCheckBox;
    @FXML
    CheckBox jpgCheckBox;

    @FXML
    CheckBox mp3CheckBox;

    @FXML
    CheckBox mp4CheckBox;
    @FXML
    CheckBox aviCheckBox;
    @FXML
    CheckBox mkvCheckBox;

    @FXML
    CheckBox txtCheckBox;
    @FXML
    CheckBox docCheckBox;
    @FXML
    CheckBox docxCheckBox;
    @FXML
    CheckBox pdfCheckBox;

    @FXML
    TextField otherExtensionTextField;

    // Creation date elements
    @FXML
    CheckBox creationDateEnabledCheckBox;
    @FXML
    DatePicker afterCreationDatePicker;
    @FXML
    DatePicker beforeCreationDatePicker;

    // Last modification elements
    @FXML
    CheckBox lastModificationDateEnabledCheckBox;
    @FXML
    DatePicker afterLastModificationDatePicker;
    @FXML
    DatePicker beforeLastModificationDatePicker;

    // Metadata elements

    // Search elements


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFileSizeElements();
        initializeExtensionElements();
        initializeCreationDateElements();
        initializeLastModificationDateElements();
        initializeMetadataElements();
    }

    private void initializeFileSizeElements() {
        // If not enabled, then disable all element.
        lowerBoundChoiceBox.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        upperBoundChoiceBox.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        fileSizeLowerBoundTextField.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        fileSizeUpperBoundTextField.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());

        // Set file size unit choice box values.
        lowerBoundChoiceBox.getItems().addAll(FileSizeUnit.getAbbreviations());
        upperBoundChoiceBox.getItems().addAll(FileSizeUnit.getAbbreviations());
        // Make megabyte (MB) the default one.
        lowerBoundChoiceBox.setValue(FileSizeUnit.MEGABYTE.getAbbreviation());
        upperBoundChoiceBox.setValue(FileSizeUnit.MEGABYTE.getAbbreviation());

        // Make lower bound and upper bound file size unit choice boxes change together.
        lowerBoundChoiceBox.getSelectionModel().selectedItemProperty().addListener((a, oldValue, newValue) -> {
            if (newValue != null) {
                log.info("lower");
                upperBoundChoiceBox.setValue(newValue);
            }
        });
        upperBoundChoiceBox.getSelectionModel().selectedItemProperty().addListener((a, oldValue, newValue) -> {
            if (newValue != null) {
                log.info("upper");
                lowerBoundChoiceBox.setValue(newValue);
            }
        });
    }

    private void initializeExtensionElements() {
        // If not enabled, then disable all element.
        pngCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        jpgCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        mp3CheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        mp4CheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        aviCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        mkvCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        txtCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        docCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        docxCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        pdfCheckBox.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
        otherExtensionTextField.disableProperty().bind(extensionEnabledCheckBox.selectedProperty().not());
    }

    private void initializeCreationDateElements() {
        afterCreationDatePicker.disableProperty().bind(creationDateEnabledCheckBox.selectedProperty().not());
        beforeCreationDatePicker.disableProperty().bind(creationDateEnabledCheckBox.selectedProperty().not());
    }

    private void initializeLastModificationDateElements() {
        afterLastModificationDatePicker.disableProperty().bind(lastModificationDateEnabledCheckBox.selectedProperty().not());
        beforeLastModificationDatePicker.disableProperty().bind(lastModificationDateEnabledCheckBox.selectedProperty().not());
    }

    private void initializeMetadataElements() {

    }

    public void selectRootFolder() {
        directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        log.info(directory.getAbsolutePath());
        rootFolderTextField.setText(directory.getAbsolutePath());
    }

    public void searchButtonClick() {
        log.info("unimplemented yet");
    }
}
