package metadata;

import javafx.beans.property.SimpleStringProperty;

public class MetadataSearch {
    private final SimpleStringProperty category;
    private final SimpleStringProperty value;
    private final SimpleStringProperty metadataType;
    private final SimpleStringProperty matchType;

    public MetadataSearch(String category, String value, String metadataType, String matchType) {
        this.category = new SimpleStringProperty(category);
        this.value = new SimpleStringProperty(value);
        this.metadataType = new SimpleStringProperty(metadataType);
        this.matchType = new SimpleStringProperty(matchType);
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

    public String getMetadataType() {
        return metadataType.get();
    }

    public void setMetadataType(String metadataType) {
        this.metadataType.set(metadataType);
    }

    public String getMatchType() {
        return matchType.get();
    }

    public void setMatchType(String matchType) {
        this.matchType.set(matchType);
    }
}
