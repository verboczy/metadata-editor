package metadata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

public class MetadataReader {

    public String read(Path path) {
        StringBuilder result = new StringBuilder();
        UserDefinedFileAttributeView fileAttributeView = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        try {
            List<String> metadataList = fileAttributeView.list();
            for (String metadataCategory : metadataList) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(fileAttributeView.size(metadataCategory));
                fileAttributeView.read(metadataCategory, byteBuffer);
                byteBuffer.flip();
                String metadataValue = Charset.defaultCharset().decode(byteBuffer).toString();
                result.append(metadataCategory).append(" - ").append(metadataValue).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
