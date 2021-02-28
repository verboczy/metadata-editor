package domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FileSizeUnit {
    BYTE("byte", 1),
    KILOBYTE("KB", 1000),
    MEGABYTE("MB", 1000 * 1000),
    GIGABYTE("GB", 1000 * 1000 * 100);

    private final String abbreviation;
    private final int byteMultiplier;

    FileSizeUnit(final String abbreviation, final int byteMultiplier) {
        this.abbreviation = abbreviation;
        this.byteMultiplier = byteMultiplier;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getByteMultiplier() {
        return byteMultiplier;
    }

    public static List<String> getAbbreviations() {
        return Arrays.stream(FileSizeUnit.values()).map(FileSizeUnit::getAbbreviation).collect(Collectors.toList());
    }
}
