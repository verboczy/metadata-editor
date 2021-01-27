package controller;

import domain.TextTreeItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BrowserController implements Initializable {

    @FXML
    private TreeView<TextTreeItem> fileTree;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}
