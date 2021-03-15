package predicate;

import metadata.Metadata;
import metadata.MetadataReader;
import metadata.MetadataSearch;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class MetadataPredicate {
    private final boolean isEnabled;
    private final List<MetadataSearch> metadataList;
    private final MetadataReader metadataReader;

    public MetadataPredicate(final boolean isEnabled, final List<MetadataSearch> metadataList) {
        this.isEnabled = isEnabled;
        this.metadataList = metadataList;
        this.metadataReader = new MetadataReader();
    }

    public Predicate<File> getPredicate() {
        return file -> {
            if (!isEnabled || metadataList.isEmpty()) {
                return true;
            }

            final Path path = Paths.get(file.getPath());
            final List<Metadata> fileMetadataList = metadataReader.read(path);

            return metadataList.stream().allMatch(metadata -> fileMetadataList.stream().anyMatch(metadata::matchesMetadata));
        };
    }
}
