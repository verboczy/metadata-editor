package controller;

import domain.TextTreeItem;
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
import java.util.List;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {

    @FXML
    private TreeView<TextTreeItem> fileTree;

    @FXML
    private  Label fileNameLabel;
    @FXML
    private  Label fileSizeLabel;
    @FXML
    private  Label fileCreationLabel;
    @FXML
    private  Label fileLastModifiedLabel;

    @FXML
    private TableView<Metadata> metadataTableView;
    @FXML
    private TableColumn<Metadata, String> categoryTableColumn;
    @FXML
    private TableColumn<Metadata, String> valueTableColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFileLabels();
        initializeTreeView();
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

        ObservableList<Metadata> metadata = FXCollections.observableArrayList(
                Arrays.asList(new Metadata("actor", "Natalie Portman"),
                        new Metadata("genre", "drama"),
                        new Metadata("genre2", "drama"),
                        new Metadata("genre3", "drama"),
                        new Metadata("genre4", "drama"),
                        new Metadata("genre5", "drama"),
                        new Metadata("genre6", "drama"),
                        new Metadata("genre7", "drama"),
                        new Metadata("genre8", "drama"),
                        new Metadata("genre9", "drama"),
                        new Metadata("genre10", "drama")
                ));
        metadataTableView.setItems(metadata);
    }

    private void initializeTreeView() {
        TreeItem<TextTreeItem> root = new TreeItem<>();

        Arrays.asList(File.listRoots()).forEach(drive -> {
            Label driveLabel = createDirectoryLabel(drive.getAbsolutePath());
            makeBranch(driveLabel, root);
        });

        fileTree.setRoot(root);

        fileTree.getSelectionModel().selectedItemProperty().addListener((selectedItem, oldValue, newValue) -> {
            if (newValue != null) {
                addBranch(newValue);
            }
        });
    }

    private String getPath(TreeItem<TextTreeItem> branch) {
        StringBuilder path = new StringBuilder();
        while (branch.getParent() != null) {
            path.insert(0, "\\").insert(0, branch.getValue().getText());
            branch = branch.getParent();
        }
        return path.toString();
    }

    private void addBranch(TreeItem<TextTreeItem> parent) {
        try {
            // Tree item does not store the full path in itself. Get it from the hierarchy.
            File file = new File(getPath(parent));
            // If it is a directory, then list its contents.
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    Arrays.asList(files).forEach(f -> {
                        Label label;
                        // Use different color for directory, and file.
                        if (f.isDirectory()) {
                            label = createDirectoryLabel(f.getName());
                        } else {
                            label = createFileLabel(f.getName());
                        }
                        // Add content as new tree item.
                        makeBranch(label, parent);
                    });
                }
            }
            if (file.isFile()) {
                handleFileDetails(file);
            }
            // TODO: normal catch
        } catch (Exception e) {
            System.out.println("not found");
        }
    }

    // TODO: add test for this
    private void makeBranch(Label name, TreeItem<TextTreeItem> parent) {
        // Add label to new tree item.
        TreeItem<TextTreeItem> item = new TreeItem<>(new TextTreeItem(name));
        item.setExpanded(false);
        // Add the new tree item to the given parent tree item.
        parent.getChildren().add(item);
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

    private Label createFileLabel(String fileName) {
        Label label = new Label(fileName);
        label.setStyle("-fx-text-fill: #FF00FF;");
        return label;
    }

    private Label createDirectoryLabel(String dirName) {
        Label label = new Label(dirName);
        label.setStyle("-fx-text-fill: #0000FF;");
        return label;
    }

    private void openMetadataEditor(String category, String value) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
            Parent root = loader.load();

            EditorController editorController = loader.getController();
            editorController.setCategoryText(category);
            editorController.setMetadataValueText(value);

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
}
