package metadata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.List;

public class MetadataReader {

    public List<Metadata> read(Path path) {
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
        } catch (IOException e) {
            e.printStackTrace();
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
