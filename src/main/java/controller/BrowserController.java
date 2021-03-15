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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(BrowserController.class);

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
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
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
                final MenuItem editMenuItem = new MenuItem("Edit");
                editMenuItem.setOnAction((ActionEvent event) -> openMetadataEditor(newSelection.getCategory(), newSelection.getValue(), false));

                final MenuItem renameMenuItem = new MenuItem("Rename");
                renameMenuItem.setOnAction((ActionEvent event) -> openMetadataEditor(newSelection.getCategory(), newSelection.getValue(), true));

                final MenuItem deleteMenuItem = new MenuItem("Delete");
                deleteMenuItem.setOnAction((ActionEvent event) -> deleteMetadata(newSelection.getCategory()));

                metadataTableView.setContextMenu(getContextMenu(editMenuItem, renameMenuItem, deleteMenuItem));
            }
        });
    }

    private void initializeSelectedFiles() {
        selectedFile = null;

        fileListView.getSelectionModel().selectedItemProperty().addListener((selectedItem, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFile = newValue;

                final MenuItem menuItem = new MenuItem("Clear");
                menuItem.setOnAction((ActionEvent event) -> {
                    clearFileDetails();
                    fileListView.getItems().remove(newValue);
                });
                fileListView.setContextMenu(getContextMenu(menuItem));

                handleFileDetails();
            }
        });

        fileChooser = new FileChooser();
        for (final FileExtension fileExtension : FileExtension.values()) {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(fileExtension.getExtensionName(), fileExtension.getExtensionWithStarDot()));
        }
    }

    // Action handler methods
    public void openFile() {
        log.info("Opening files...");
        final List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null) {
            for (final File selectedFile : selectedFiles) {
                if (selectedFile != null) {
                    log.debug("Selected file: [{}].", selectedFile.getPath());
                    fileListView.getItems().add(selectedFile);
                }
            }
        }
    }

    public void clearFiles() {
        log.info("Clearing files...");
        fileListView.getItems().removeAll(fileListView.getItems());
        clearFileDetails();
    }

    public void exitProgram() {
        log.info("Exiting program...");
        System.exit(0);
    }

    public void search() {
        log.info("Searching...");
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/search.fxml"));
            final Parent root = loader.load();

            final Stage searchStage = new Stage();
            searchStage.setTitle("Search by metadata");
            searchStage.initStyle(StageStyle.DECORATED);
            searchStage.initModality(Modality.APPLICATION_MODAL);
            searchStage.setScene(new Scene(root, 600, 860));
            searchStage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewCategory() {
        log.info("Adding new category...");
        if (selectedFile != null) {
            final String newCategory = newCategoryTextField.getText();
            final Path path = Paths.get(selectedFile.getPath());
            if (newCategory.isBlank()) {
                log.debug("Cannot add category [{}] to file [{}], because the category is not valid.", newCategory, path);
            } else if (!metadataReader.isCategoryUnique(path, newCategory)) {
                log.debug("Cannot add category [{}] to file [{}], because the category already exists.", newCategory, path);
            } else {
                openMetadataEditor(newCategory, "", false);
                newCategoryTextField.setText("");
                log.debug("Added new category [{}] to file [{}].", newCategory, path);
            }
        }
    }

    // Helper methods
    private void handleFileDetails() {
        log.trace("Collecting file details...");
        initializeFileDetails();
        fileNameLabel.setText(selectedFile.getName());
        fileSizeLabel.setText(String.format("%d bytes", selectedFile.length()));
        try {
            final Path path = Paths.get(selectedFile.getPath());
            final BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            fileCreationLabel.setText(attr.creationTime().toString());
            fileLastModifiedLabel.setText(attr.lastModifiedTime().toString());
            loadMetadata(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private ContextMenu getContextMenu(final MenuItem... menuItems) {
        log.trace("Creating context menu...");
        final ContextMenu contextMenu = new ContextMenu();
        for (final MenuItem menuItem : menuItems) {
            contextMenu.getItems().add(menuItem);
        }
        return contextMenu;
    }

    private void clearFileDetails() {
        log.trace("Clearing file details...");
        metadataTableView.getItems().removeAll(metadataTableView.getItems());
        initializeFileDetails();
    }

    // Metadata
    public void loadMetadata(final Path path) {
        log.trace("Loading metadata...");
        final ObservableList<Metadata> metadataList = FXCollections.observableArrayList(metadataReader.read(path));
        metadataTableView.setItems(metadataList);
    }

    private void openMetadataEditor(final String category, final String value, final boolean isRename) {
        log.trace("Opening metadata editor...");
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
            final Parent root = loader.load();

            final EditorController editorController = loader.getController();
            editorController.setRename(isRename);
            editorController.setOldCategory(category);
            editorController.setCategoryText(category);
            editorController.setMetadataValueText(value);
            editorController.setPath(Paths.get(selectedFile.getPath()));
            editorController.setBrowserController(this);

            final Stage editorStage = new Stage();
            editorStage.initStyle(StageStyle.DECORATED);
            editorStage.initModality(Modality.APPLICATION_MODAL);
            editorStage.setResizable(false);
            editorStage.setTitle("Editor");
            editorStage.setScene(new Scene(root, 600, 250));
            editorStage.show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMetadata(final String category) {
        log.info("Deleting metadata...");
        final Path path = Paths.get(selectedFile.getPath());
        metadataWriter.delete(path, category);
        loadMetadata(path);
    }
}
