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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import metadata.Metadata;
import metadata.MetadataReader;
import metadata.MetadataWriter;

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

    // Text fields
    @FXML
    private TextField newCategoryTextField;

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
    private MetadataWriter metadataWriter;
    private MetadataReader metadataReader;

    // Initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFileDetails();
        initializeSelectedFiles();
        metadataWriter = new MetadataWriter();
        metadataReader = new MetadataReader();
    }

    private void initializeFileDetails() {
        fileNameLabel.setText("");
        fileSizeLabel.setText("");
        fileCreationLabel.setText("");
        fileLastModifiedLabel.setText("");

        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        metadataTableView.getSelectionModel().selectedItemProperty().addListener((a, oldSelection, newSelection) -> {
            if (newSelection != null) {
                MenuItem editMenuItem = new MenuItem("Edit");
                editMenuItem.setOnAction((ActionEvent event) -> openMetadataEditor(newSelection.getCategory(), newSelection.getValue()));

                MenuItem deleteMenuItem = new MenuItem("Delete");
                deleteMenuItem.setOnAction((ActionEvent event) -> deleteMetadata(newSelection.getCategory()));
                metadataTableView.setContextMenu(getContextMenu(editMenuItem, deleteMenuItem));
            }
        });
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

    // Action handler methods
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

    public void clearFiles() {
        fileListView.getItems().removeAll(fileListView.getItems());
        clearFileDetails();
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void search() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/search.fxml"));
            Parent root = loader.load();

            SearchController searchController = loader.getController();

            Stage searchStage = new Stage();
            searchStage.setTitle("Search by metadata");
            searchStage.initStyle(StageStyle.DECORATED);
            searchStage.initModality(Modality.APPLICATION_MODAL);
            searchStage.setScene(new Scene(root, 600, 400));
            searchStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewCategory() {
        String newCategory = newCategoryTextField.getText();
        Path path = Paths.get(selectedFile.getPath());
        if (selectedFile != null && metadataReader.isCategoryUnique(path, newCategory)) {
            openMetadataEditor(newCategory, "");
        }
    }

    // Helper methods
    private void handleFileDetails() {
        initializeFileDetails();
        fileNameLabel.setText(selectedFile.getName());
        fileSizeLabel.setText(String.format("%d bytes", selectedFile.length()));
        try {
            Path path = Paths.get(selectedFile.getPath());
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            fileCreationLabel.setText(attr.creationTime().toString());
            fileLastModifiedLabel.setText(attr.lastModifiedTime().toString());
            loadMetadata(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ContextMenu getContextMenu(MenuItem... menuItems) {
        ContextMenu contextMenu = new ContextMenu();
        for (MenuItem menuItem : menuItems) {
            contextMenu.getItems().add(menuItem);
        }
        return contextMenu;
    }

    private void clearFileDetails() {
        metadataTableView.getItems().removeAll(metadataTableView.getItems());
        initializeFileDetails();
    }

    // Metadata
    public void loadMetadata(Path path) {
        ObservableList<Metadata> metadataList = FXCollections.observableArrayList(metadataReader.read(path));
        metadataTableView.setItems(metadataList);
    }

    private void openMetadataEditor(String category, String value) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
            Parent root = loader.load();

            EditorController editorController = loader.getController();
            editorController.setCategoryText(category);
            editorController.setMetadataValueText(value);
            editorController.setPath(Paths.get(selectedFile.getPath()));
            editorController.setBrowserController(this);

            Stage editorStage = new Stage();
            editorStage.initStyle(StageStyle.DECORATED);
            editorStage.initModality(Modality.APPLICATION_MODAL);
            editorStage.setScene(new Scene(root, 600, 400));
            editorStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMetadata(String category) {
        Path path = Paths.get(selectedFile.getPath());
        metadataWriter.delete(path, category);
        loadMetadata(path);
    }
}
