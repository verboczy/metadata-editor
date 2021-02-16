package metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class MetadataWriter {

    private static final Logger log = LoggerFactory.getLogger(MetadataWriter.class);

    public void write(Path path, String category, String value) {
        log.info("Writing category [{}] - value [{}], to file [{}]...", category, value, path);
        UserDefinedFileAttributeView userView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            userView.write(category, Charset.defaultCharset().encode(value));
            log.info("Writing was successful.");
        } catch (IOException e) {
            log.error("Writing was not successful.");
            log.error(e.getMessage());
            // TODO throw custom exception
        }
    }

    public void delete(Path path, String category) {
        log.info("Deleting category [{}] from file [{}]...", category, path);
        UserDefinedFileAttributeView userView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            userView.delete(category);
            log.info("Successfully deleted category.");
        } catch (IOException e) {
            log.error("Could not delete category.");
            log.error(e.getMessage());
            // TODO throw custom exception
        }
    }

    public void rename(Path path, String oldCategory, String newCategory, String value) {
        log.info("Renaming category from [{}] to [{}] for file [{}]...", oldCategory, newCategory, path);
        delete(path, oldCategory);
        write(path, newCategory, value);
    }
}
