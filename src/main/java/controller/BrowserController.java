package controller;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {

    // Labels
    @FXML
    private  Label fileNameLabel;
    @FXML
    private  Label fileSizeLabel;
    @FXML
    private  Label fileCreationLabel;
    @FXML
    private  Label fileLastModifiedLabel;

    // Menu items
    @FXML
    private MenuItem openFileMenuItem;
    @FXML
    private MenuItem openFilesMenuItem;
    @FXML
    private MenuItem clearFilesMenuItem;
    @FXML
    private MenuItem closeMenuItem;

    // List views
    @FXML
    private ListView<String> fileListView;

    // Table elements
    @FXML
    private TableView<Metadata> metadataTableView;
    @FXML
    private TableColumn<Metadata, String> categoryTableColumn;
    @FXML
    private TableColumn<Metadata, String> valueTableColumn;

    private File selectedFile;

    private Map<String, String> fileNameToPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileNameToPath = new HashMap<>();
        initializeFileLabels();
        selectedFile = null;

        fileListView.getSelectionModel().selectedItemProperty().addListener((selectedItem, oldValue, newValue) -> {
            if (newValue != null) {
                String path = fileNameToPath.get(newValue);
                if (path != null) {
                    handleFileDetails(new File(path));
                }
            }
        });
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            fileListView.getItems().add(selectedFile.getName());
            fileNameToPath.put(selectedFile.getName(), selectedFile.getPath());
        } else {
            System.out.println("asd");
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
                metadataTableView.setContextMenu(getContextMenu(newSelection.getCategory(), newSelection.getValue()));
            }
        });
    }

    private void handleFileDetails(File file) {
        initializeFileLabels();
        fileNameLabel.setText(file.getName());
        fileSizeLabel.setText(String.format("%d bytes", file.length()));
        try {
            Path path = Paths.get(file.getPath());
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

    private ContextMenu getContextMenu(String category, String value) {
        MenuItem menuItem = new MenuItem("Edit");
        menuItem.setOnAction((ActionEvent event) -> {
            openMetadataEditor(category, value);
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(menuItem);
        return contextMenu;
    }

    public void clearFiles() {
        System.out.println("asd");
        fileNameToPath.clear();
        fileListView.getItems().removeAll(fileListView.getItems()); // this is so ugly...
        initializeFileLabels();
        metadataTableView.getItems().removeAll(metadataTableView.getItems()); // this is so ugly...
    }
}
