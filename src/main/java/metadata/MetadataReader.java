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

    public List<Metadata> read(Path path) {
        log.info("Reading metadata of file [{}]...", path);
        List<Metadata> result = new ArrayList<>();
        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            List<String> metadataList = fileAttributeView.list();
            for (String metadataCategory : metadataList) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(fileAttributeView.size(metadataCategory));
                fileAttributeView.read(metadataCategory, byteBuffer);
                byteBuffer.flip();
                String metadataValue = Charset.defaultCharset().decode(byteBuffer).toString();
                result.add(new Metadata(metadataCategory, metadataValue));
            }
            log.info("Reading was successful.");
        } catch (IOException e) {
            log.error("Reading was not successful.");
            log.error(e.getMessage());
            // TODO throw custom exception
        }

        return result;
    }

    public boolean isCategoryUnique(Path path, String category) {
        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            List<String> metadataList = fileAttributeView.list();
            return !metadataList.contains(category);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
