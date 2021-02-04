package metadata;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class MetadataWriter {

    public void write(Path path, String category, String value) {
        UserDefinedFileAttributeView userView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            userView.write(category, Charset.defaultCharset().encode(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO delete category

    // TODO rename category
}
