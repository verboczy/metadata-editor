package metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.List;

public class MetadataReader {

    private static final Logger log = LoggerFactory.getLogger(MetadataReader.class);

    public List<Metadata> read(final Path path) {
        log.info("Reading metadata of file [{}]...", path);
        final List<Metadata> result = new ArrayList<>();
        final UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            final List<String> metadataList = fileAttributeView.list();
            for (final String metadataCategory : metadataList) {
                final ByteBuffer byteBuffer = ByteBuffer.allocate(fileAttributeView.size(metadataCategory));
                fileAttributeView.read(metadataCategory, byteBuffer);
                byteBuffer.flip();
                final String metadataValue = Charset.defaultCharset().decode(byteBuffer).toString();
                result.add(new Metadata(metadataCategory, metadataValue));
            }
            log.info("Reading was successful.");
        } catch (final IOException e) {
            log.error("Reading was not successful.");
            log.error(e.getMessage());
        }

        return result;
    }

    public boolean isCategoryUnique(final Path path, final String category) {
        final UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            final List<String> metadataList = fileAttributeView.list();
            return !metadataList.contains(category);
        } catch (final IOException e) {
            log.error("Could not retrieve metadata from file.");
            log.error(e.getMessage());
            return false;
        }
    }
}
