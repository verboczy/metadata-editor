package metadata;

import domain.MatchType;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static domain.MetadataType.valueOf;

public class MetadataSearch {

    private static final Logger log = LoggerFactory.getLogger(MetadataSearch.class);

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

    public boolean matchesMetadata(final Metadata fileMetadata) {
        if (!this.getCategory().equals(fileMetadata.getCategory())) {
            return false;
        }

        switch (valueOf(metadataType.getValue())) {
            case STRING:
                return matchString(fileMetadata);
            case DOUBLE:
                return matchDouble(fileMetadata);
            case INTEGER:
                return matchInteger(fileMetadata);
            default:
                return false;
        }
    }

    private boolean matchString(final Metadata fileMetadata) {
        final List<String> thisValues = getValueList(this.getValue());
        final List<String> otherValues = getValueList(fileMetadata.getValue());

        return thisValues.stream().anyMatch(otherValues::contains);
    }

    private List<String> getValueList(final String value) {
        return Arrays.stream(value.split(",")).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());
    }

    private boolean matchDouble(final Metadata fileMetadata) {
        try {
            final double searchValue = Double.parseDouble(this.getValue());
            final double fileValue = Double.parseDouble(fileMetadata.getValue());

            switch (MatchType.valueOf(matchType.getValue())) {
                case EXACT:
                    return fileValue == searchValue;
                case LOWER_OR_EQUALS:
                    return fileValue <= searchValue;
                case LOWER:
                    return fileValue < searchValue;
                case HIGHER_OR_EQUALS:
                    return fileValue >= searchValue;
                case HIGHER:
                    return fileValue > searchValue;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            log.warn("Cannot parse the given values (search: [{}], file: [{}]).", this.getValue(), fileMetadata.getValue());
            return false;
        }
    }

    private boolean matchInteger(final Metadata fileMetadata) {
        try {
            final double searchValue = Double.parseDouble(this.getValue());
            final double fileValue = Double.parseDouble(fileMetadata.getValue());

            switch (MatchType.valueOf(matchType.getValue())) {
                case EXACT:
                    return fileValue == searchValue;
                case LOWER_OR_EQUALS:
                    return fileValue <= searchValue;
                case LOWER:
                    return fileValue < searchValue;
                case HIGHER_OR_EQUALS:
                    return fileValue >= searchValue;
                case HIGHER:
                    return fileValue > searchValue;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            log.warn("Cannot parse the given values (search: [{}], file: [{}]).", this.getValue(), fileMetadata.getValue());
            return false;
        }
    }
}
