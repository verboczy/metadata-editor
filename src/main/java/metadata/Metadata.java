package metadata;

import javafx.beans.property.SimpleStringProperty;

public class Metadata {
    private final SimpleStringProperty  category;
    private final SimpleStringProperty value;

    public Metadata(String category, String value) {
        this.category = new SimpleStringProperty(category);
        this.value = new SimpleStringProperty(value);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }
}
