package predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Predicate;

public class LastModifiedDatePredicate {

    private static final Logger log = LoggerFactory.getLogger(LastModifiedDatePredicate.class);

    private final boolean isEnabled;
    private final LocalDate afterLastModificationDate;
    private final LocalDate beforeLastModificationDate;

    public LastModifiedDatePredicate(boolean isEnabled, LocalDate afterLastModificationDate, LocalDate beforeLastModificationDate) {
        this.isEnabled = isEnabled;
        this.afterLastModificationDate = afterLastModificationDate;
        this.beforeLastModificationDate = beforeLastModificationDate;
    }

    public Predicate<File> getPredicate() {
        return file -> {
            if (!isEnabled || (afterLastModificationDate == null && beforeLastModificationDate == null)) {
                return true;
            }

            Path path = Paths.get(file.getPath());
            try {
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                LocalDate lastModificationDate = LocalDate.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                if (afterLastModificationDate != null && beforeLastModificationDate != null) {
                    return !lastModificationDate.isBefore(afterLastModificationDate) && !lastModificationDate.isAfter(beforeLastModificationDate);
                } else if (afterLastModificationDate != null) {
                    return !lastModificationDate.isBefore(afterLastModificationDate);
                } else {
                    // beforeLastModificationDate is surely not null here
                    return !lastModificationDate.isAfter(beforeLastModificationDate);
                }
            } catch (IOException e) {
                log.warn("Cannot determine last modification time of file [{}].", file.getName());
                return false;
            }
        };
    }
}
