package domain;

public enum FileExtension {
    ALL("All files", "*"),
    // Image formats
    PNG("PNG files", "png"),
    JPG("JPG files", "jpg"),
    // Music formats
    MP3("MP3 files", "mp3"),
    // Video formats
    MP4("MP4 files", "mp4"),
    AVI("AVI files", "avi"),
    MKV("MKV files", "mkv"),
    // Document formats
    TXT("TXT files", "txt"),
    DOC("DOC files", "doc"),
    DOCX("DOCX files", "docx"),
    PDF("PDF files", "pdf");

    private final String extensionName;
    private final String extension;

    FileExtension(final String extensionName, final String extension) {
        this.extensionName = extensionName;
        this.extension = extension;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public String getExtension() {
        return extension;
    }

    public String getExtensionWithStarDot() {
        return String.format("*.%s", extension);
    }
}
