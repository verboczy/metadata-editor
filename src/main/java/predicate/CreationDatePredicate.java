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

public class CreationDatePredicate {

    private static final Logger log = LoggerFactory.getLogger(CreationDatePredicate.class);

    private final boolean isEnabled;
    private final LocalDate afterCreationDate;
    private final LocalDate beforeCreationDate;

    public CreationDatePredicate(final boolean isEnabled, final LocalDate afterCreationDate, final LocalDate beforeCreationDate) {
        this.isEnabled = isEnabled;
        this.afterCreationDate = afterCreationDate;
        this.beforeCreationDate = beforeCreationDate;
    }

    public Predicate<File> getPredicate() {
        return file -> {
            if (!isEnabled || (afterCreationDate == null && beforeCreationDate == null)) {
                return true;
            }

            final Path path = Paths.get(file.getPath());
            try {
                final BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                final LocalDate creationDate = LocalDate.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());

                if (afterCreationDate != null && beforeCreationDate != null) {
                    return !creationDate.isBefore(afterCreationDate) && !creationDate.isAfter(beforeCreationDate);
                } else if (afterCreationDate != null) {
                    return !creationDate.isBefore(afterCreationDate);
                } else {
                    // beforeCreationDate is surely not null here
                    return !creationDate.isAfter(beforeCreationDate);
                }
            } catch (final IOException e) {
                log.warn("Cannot determine creation time of file [{}].", file.getName());
                return false;
            }
        };
    }
}
