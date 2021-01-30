package metadata;

public class Metadata {
    private String category;
    private String value;

    public Metadata(String category, String value) {
        this.category = category;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
