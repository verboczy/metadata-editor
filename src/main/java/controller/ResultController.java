package controller;

import domain.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ResultController implements Initializable {

    @FXML
    TableView<Result> resultTableView;
    @FXML
    TableColumn<Result, String> fileNameColumn;
    @FXML
    TableColumn<Result, String> absolutePathColumn;

    final ObservableList<Result> results = FXCollections.observableArrayList(); // Starting with empty list

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        absolutePathColumn.setCellValueFactory(new PropertyValueFactory<>("absolutePath"));
        resultTableView.setItems(results);
    }

    public void setResults(final List<File> fileList) {
        results.addAll(fileList.stream().map(file -> new Result(file.getName(), file.getAbsolutePath())).collect(Collectors.toList()));
    }
}
