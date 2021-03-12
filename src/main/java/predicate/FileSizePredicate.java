package predicate;

import domain.FileSizeUnit;

import java.io.File;
import java.util.function.Predicate;

public class FileSizePredicate {
    private final boolean isEnabled;
    private final FileSizeUnit unit;
    private final Integer largerThan;
    private final Integer smallerThan;


    public FileSizePredicate(boolean isEnabled, FileSizeUnit unit, Integer largerThan, Integer smallerThan) {
        this.isEnabled = isEnabled;
        this.unit = unit;
        this.largerThan = largerThan;
        this.smallerThan = smallerThan;
    }

    public Predicate<File> getPredicate() {
        return file -> {
            if (!isEnabled) {
                return true;
            }

            if (largerThan != null) {
                int largerThanBytes = unit.getByteMultiplier() * largerThan;
                if (file.length() < largerThanBytes) {
                    return false;
                }
            }

            if (smallerThan != null) {
                int smallerThanBytes = unit.getByteMultiplier() * smallerThan;
                return file.length() <= smallerThanBytes;
            }

            return true;
        };
    }
}
