package predicate;

import domain.FileExtension;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static domain.FileExtension.*;

public class ExtensionPredicate {

    private static final Logger log = LoggerFactory.getLogger(ExtensionPredicate.class);

    private final boolean isEnabled;
    private final boolean png;
    private final boolean jpg;
    private final boolean mp3;
    private final boolean mp4;
    private final boolean avi;
    private final boolean mkv;
    private final boolean txt;
    private final boolean doc;
    private final boolean docx;
    private final boolean pdf;
    private final String other;

    private final List<FileExtension> extensionList;
    private final List<String> otherExtensions;

    public ExtensionPredicate(
            final boolean isEnabled,
            final boolean png,
            final boolean jpg,
            final boolean mp3,
            final boolean mp4,
            final boolean avi,
            final boolean mkv,
            final boolean txt,
            final boolean doc,
            final boolean docx,
            final boolean pdf,
            final String other) {
        this.isEnabled = isEnabled;
        this.png = png;
        this.jpg = jpg;
        this.mp3 = mp3;
        this.mp4 = mp4;
        this.avi = avi;
        this.mkv = mkv;
        this.txt = txt;
        this.doc = doc;
        this.docx = docx;
        this.pdf = pdf;
        this.other = other;
        this.extensionList = getExtensionList();
        this.otherExtensions = getOtherExtensions();
    }

    public Predicate<File> getPredicate() {
        return file -> {
            if (!isEnabled) {
                return true;
            }

            final String extension = FilenameUtils.getExtension(file.getName());
            if (otherExtensions.contains(extension)) {
                return true;
            } else {
                try {
                    final FileExtension fileExtension = valueOf(extension.toUpperCase());
                    return extensionList.contains(fileExtension);
                } catch (final IllegalArgumentException e) {
                    log.trace("Cannot parse [{}] as a file extension", extension);
                    return false;
                }
            }
        };
    }

    private List<FileExtension> getExtensionList() {
        final List<FileExtension> extensionList = new ArrayList<>();

        if (png) {
            extensionList.add(PNG);
        }
        if (jpg) {
            extensionList.add(JPG);
        }
        if (mp3) {
            extensionList.add(MP3);
        }
        if (mp4) {
            extensionList.add(MP4);
        }
        if (avi) {
            extensionList.add(AVI);
        }
        if (mkv) {
            extensionList.add(MKV);
        }
        if (txt) {
            extensionList.add(TXT);
        }
        if (doc) {
            extensionList.add(DOC);
        }
        if (docx) {
            extensionList.add(DOCX);
        }
        if (pdf) {
            extensionList.add(PDF);
        }

        return extensionList;
    }

    private List<String> getOtherExtensions() {
        return Arrays.stream(other.split(",")).map(extension -> {
            extension = extension.trim();
            if (extension.startsWith(".")) {
                extension = extension.substring(1);
            }
            return extension;
        }).collect(Collectors.toList());
    }
}
