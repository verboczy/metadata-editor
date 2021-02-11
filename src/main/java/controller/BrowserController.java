package controller;

import domain.FileExtension;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import metadata.Metadata;
import metadata.MetadataReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {

    // Labels
    @FXML
    private Label fileNameLabel;
    @FXML
    private Label fileSizeLabel;
    @FXML
    private Label fileCreationLabel;
    @FXML
    private Label fileLastModifiedLabel;

    // List views
    @FXML
    private ListView<File> fileListView;

    // Table elements
    @FXML
    private TableView<Metadata> metadataTableView;
    @FXML
    private TableColumn<Metadata, String> categoryTableColumn;
    @FXML
    private TableColumn<Metadata, String> valueTableColumn;

    private File selectedFile;
    private FileChooser fileChooser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFileLabels();
        initializeSelectedFiles();
    }

    private void initializeSelectedFiles() {
        selectedFile = null;

        fileListView.getSelectionModel().selectedItemProperty().addListener((selectedItem, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFile = newValue;

                MenuItem menuItem = new MenuItem("Clear");
                menuItem.setOnAction((ActionEvent event) -> {
                    clearFileDetails();
                    fileListView.getItems().remove(newValue);
                });
                fileListView.setContextMenu(getContextMenu(menuItem));

                handleFileDetails();
            }
        });

        fileChooser = new FileChooser();
        for (FileExtension fileExtension : FileExtension.values()) {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(fileExtension.extensionName, fileExtension.extension));
        }
    }

    public void openFile() {
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File selectedFile : selectedFiles) {
                if (selectedFile != null) {
                    fileListView.getItems().add(selectedFile);
                }
            }
        }
    }

    private void initializeFileLabels() {
        fileNameLabel.setText("");
        fileSizeLabel.setText("");
        fileCreationLabel.setText("");
        fileLastModifiedLabel.setText("");

        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        metadataTableView.getSelectionModel().selectedItemProperty().addListener((a, oldSelection, newSelection) -> {
            if (newSelection != null) {
                MenuItem menuItem = new MenuItem("Edit");
                menuItem.setOnAction((ActionEvent event) -> openMetadataEditor(newSelection.getCategory(), newSelection.getValue()));
                metadataTableView.setContextMenu(getContextMenu(menuItem));
            }
        });
    }

    private void handleFileDetails() {
        initializeFileLabels();
        fileNameLabel.setText(selectedFile.getName());
        fileSizeLabel.setText(String.format("%d bytes", selectedFile.length()));
        try {
            Path path = Paths.get(selectedFile.getPath());
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            fileCreationLabel.setText(attr.creationTime().toString());
            fileLastModifiedLabel.setText(attr.lastModifiedTime().toString());
            MetadataReader metadataReader = new MetadataReader();
            ObservableList<Metadata> metadataList = FXCollections.observableArrayList(metadataReader.read(path));
            metadataTableView.setItems(metadataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMetadataEditor(String category, String value) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
            Parent root = loader.load();

            EditorController editorController = loader.getController();
            editorController.setCategoryText(category);
            editorController.setMetadataValueText(value);
            editorController.setPath(Paths.get(selectedFile.getPath()));

            Stage editorStage = new Stage();
            editorStage.initStyle(StageStyle.DECORATED);
            editorStage.setScene(new Scene(root, 600, 400));
            editorStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ContextMenu getContextMenu(MenuItem menuItem) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(menuItem);
        return contextMenu;
    }

    public void clearFiles() {
        fileListView.getItems().removeAll(fileListView.getItems());
        clearFileDetails();
    }

    private void clearFileDetails() {
        metadataTableView.getItems().removeAll(metadataTableView.getItems());
        initializeFileLabels();
    }
}
