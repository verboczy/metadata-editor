package controller;

import domain.FileSizeUnit;
import domain.MatchType;
import domain.MetadataType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import metadata.MetadataCell;
import metadata.MetadataSearch;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import predicate.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    @FXML
    CheckBox metadataEnabledCheckBox;
    @FXML
    TableView<MetadataSearch> metadataTableView;
    @FXML
    TableColumn<MetadataSearch, String> categoryTableColumn;
    @FXML
    TableColumn<MetadataSearch, String> valueTableColumn;
    @FXML
    TableColumn<MetadataSearch, String> typeTableColumn;
    @FXML
    TableColumn<MetadataSearch, String> matchTableColumn;
    @FXML
    TextField newCategoryTextField;
    @FXML
    Button addCategoryButton;

    ObservableList<MetadataSearch> metadataList = FXCollections.observableArrayList(); // Starting with empty list

    // Search elements
    @FXML
    Button searchButton;

    //region Initialize
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        initializeFileSizeElements();
        initializeExtensionElements();
        initializeCreationDateElements();
        initializeLastModificationDateElements();
        initializeMetadataElements();
        initializeSearchElements();
    }

    private void initializeFileSizeElements() {
        // If file size is not enabled, then disable all file size element.
        lowerBoundChoiceBox.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        upperBoundChoiceBox.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        fileSizeLowerBoundTextField.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());
        fileSizeUpperBoundTextField.disableProperty().bind(fileSizeEnabledCheckBox.selectedProperty().not());

        // Set file size unit choice box values.
        lowerBoundChoiceBox.getItems().addAll(FileSizeUnit.getAbbreviations());
        upperBoundChoiceBox.getItems().addAll(FileSizeUnit.getAbbreviations());
        // Make megabyte (MB) the default value.
        lowerBoundChoiceBox.setValue(FileSizeUnit.MB.getAbbreviation());
        upperBoundChoiceBox.setValue(FileSizeUnit.MB.getAbbreviation());

        // Make lower bound and upper bound file size unit choice boxes change together.
        lowerBoundChoiceBox.getSelectionModel().selectedItemProperty().addListener((a, oldValue, newValue) -> {
            if (newValue != null) {
                upperBoundChoiceBox.setValue(newValue);
            }
        });
        upperBoundChoiceBox.getSelectionModel().selectedItemProperty().addListener((a, oldValue, newValue) -> {
            if (newValue != null) {
                lowerBoundChoiceBox.setValue(newValue);
            }
        });
    }

    private void initializeExtensionElements() {
        // If extensions are not enabled, then disable all extension element.
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
        metadataTableView.setEditable(true);

        final Callback<TableColumn<MetadataSearch, String>, TableCell<MetadataSearch, String>> cellFactory = p -> new MetadataCell();

        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryTableColumn.setCellFactory(cellFactory);
        categoryTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setCategory(t.getNewValue()));

        valueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueTableColumn.setCellFactory(cellFactory);
        valueTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getNewValue()));

        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("metadataType"));
        final ObservableList<String> typeList = FXCollections.observableList(Arrays.stream(MetadataType.values()).map(MetadataType::toString).collect(Collectors.toList()));
        typeTableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(typeList));
        typeTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setMetadataType(t.getNewValue()));

        matchTableColumn.setCellValueFactory(new PropertyValueFactory<>("matchType"));
        final ObservableList<String> matchList = FXCollections.observableList(Arrays.stream(MatchType.values()).map(MatchType::toString).collect(Collectors.toList()));
        matchTableColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(matchList));
        matchTableColumn.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setMatchType(t.getNewValue()));

        metadataTableView.setItems(metadataList);

        // If metadata is not enabled, then disable all metadata element.
        metadataTableView.disableProperty().bind(metadataEnabledCheckBox.selectedProperty().not());
        newCategoryTextField.disableProperty().bind(metadataEnabledCheckBox.selectedProperty().not());
        addCategoryButton.disableProperty().bind(metadataEnabledCheckBox.selectedProperty().not());
    }

    private void initializeSearchElements() {
        searchButton.disableProperty().bind(
                fileSizeEnabledCheckBox.selectedProperty().not()
                        .and(extensionEnabledCheckBox.selectedProperty().not())
                        .and(creationDateEnabledCheckBox.selectedProperty().not())
                        .and(lastModificationDateEnabledCheckBox.selectedProperty().not())
                        .and(metadataEnabledCheckBox.selectedProperty().not())
        );
    }
    //endregion

    //region Event handlers
    public void selectRootFolder() {
        directoryChooser = new DirectoryChooser();
        final File directory = directoryChooser.showDialog(null);
        log.info(directory.getAbsolutePath());
        rootFolderTextField.setText(directory.getAbsolutePath());
    }

    public void addNewCategory() {
        metadataList.add(new MetadataSearch(newCategoryTextField.getText(), "", MetadataType.STRING.toString(), MatchType.EXACT.toString()));
        newCategoryTextField.clear();
    }

    public void searchButtonClick() {
        log.info("Searching files...");

        final FileSizePredicate fileSizePredicate = getFileSizePredicate();
        final ExtensionPredicate extensionPredicate = getExtensionPredicate();
        final CreationDatePredicate creationDatePredicate = getCreationDatePredicate();
        final LastModifiedDatePredicate lastModifiedDatePredicate = getLastModifiedDatePredicate();
        final MetadataPredicate metadataPredicate = getMetadataPredicate();

        @SuppressWarnings("unchecked") final List<File> foundFiles = ((List<File>) FileUtils.listFiles(new File(rootFolderTextField.getText()), null, true)).stream()
                .filter(fileSizePredicate.getPredicate())
                .filter(extensionPredicate.getPredicate())
                .filter(creationDatePredicate.getPredicate())
                .filter(lastModifiedDatePredicate.getPredicate())
                .filter(metadataPredicate.getPredicate())
                .collect(Collectors.toList());

        openResultWindow(foundFiles);
    }

    private void openResultWindow(final List<File> fileList) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/result.fxml"));
            final Parent root = loader.load();

            final ResultController resultController = loader.getController();
            resultController.setResults(fileList);

            final Stage searchStage = new Stage();
            searchStage.setTitle("Results");
            searchStage.initStyle(StageStyle.DECORATED);
            searchStage.initModality(Modality.APPLICATION_MODAL);
            searchStage.setScene(new Scene(root, 800, 400));
            searchStage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Get predicates
    private FileSizePredicate getFileSizePredicate() {
        int lowerBound = -1;
        int upperBound = -1;
        try {
            lowerBound = Integer.parseInt(fileSizeLowerBoundTextField.getText());
            upperBound = Integer.parseInt(fileSizeUpperBoundTextField.getText());
        } catch (final NumberFormatException e) {
            log.warn("Cannot parse file size (lower bound: [{}], upper bound: [{}]).", lowerBound, upperBound);
        }
        return new FileSizePredicate(fileSizeEnabledCheckBox.isSelected(), FileSizeUnit.valueOf(lowerBoundChoiceBox.getValue().toUpperCase()), lowerBound, upperBound);
    }

    private ExtensionPredicate getExtensionPredicate() {
        return new ExtensionPredicate(
                extensionEnabledCheckBox.isSelected(),
                pngCheckBox.isSelected(),
                jpgCheckBox.isSelected(),
                mp3CheckBox.isSelected(),
                mp4CheckBox.isSelected(),
                aviCheckBox.isSelected(),
                mkvCheckBox.isSelected(),
                txtCheckBox.isSelected(),
                docCheckBox.isSelected(),
                docxCheckBox.isSelected(),
                pdfCheckBox.isSelected(),
                otherExtensionTextField.getText()
        );
    }

    private CreationDatePredicate getCreationDatePredicate() {
        return new CreationDatePredicate(
                creationDateEnabledCheckBox.isSelected(),
                afterCreationDatePicker.getValue(),
                beforeCreationDatePicker.getValue()
        );
    }

    private LastModifiedDatePredicate getLastModifiedDatePredicate() {
        return new LastModifiedDatePredicate(
                lastModificationDateEnabledCheckBox.isSelected(),
                afterLastModificationDatePicker.getValue(),
                beforeLastModificationDatePicker.getValue()
        );
    }

    private MetadataPredicate getMetadataPredicate() {
        return new MetadataPredicate(metadataEnabledCheckBox.isSelected(), metadataList);
    }
    //endregion
}
