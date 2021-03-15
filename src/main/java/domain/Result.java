package domain;

import javafx.beans.property.SimpleStringProperty;

public class Result {
    private final SimpleStringProperty fileName;
    private final SimpleStringProperty absolutePath;

    public Result(String fileName, String absolutePath) {
        this.fileName = new SimpleStringProperty(fileName);
        this.absolutePath = new SimpleStringProperty(absolutePath);
    }

    public String getFileName() {
        return fileName.get();
    }

    public SimpleStringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getAbsolutePath() {
        return absolutePath.get();
    }

    public SimpleStringProperty absolutePathProperty() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath.set(absolutePath);
    }
}
