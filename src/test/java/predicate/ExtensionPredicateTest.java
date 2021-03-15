package predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionPredicateTest {
    //region Files
    private static final File pngFile = new File("src/test/resources/predicate/example.png");
    private static final File jpgFile = new File("src/test/resources/predicate/example.jpg");
    private static final File mp3File = new File("src/test/resources/predicate/example.mp3");
    private static final File mp4File = new File("src/test/resources/predicate/example.mp4");
    private static final File aviFile = new File("src/test/resources/predicate/example.avi");
    private static final File mkvFile = new File("src/test/resources/predicate/example.mkv");
    private static final File txtFile = new File("src/test/resources/predicate/example.txt");
    private static final File docFile = new File("src/test/resources/predicate/example.doc");
    private static final File docxFile = new File("src/test/resources/predicate/example.docx");
    private static final File pdfFile = new File("src/test/resources/predicate/example.pdf");
    private static final File asdFile = new File("src/test/resources/predicate/example.asd");
    private static final File bFile = new File("src/test/resources/predicate/example.b");
    //endregion

    //region Arguments
    private static Stream<Arguments> extensionArguments() {
        // @formatter:off
        return Stream.of(
                //           NAME            ENABLED     PNG     JPG     MP3     MP4     AVI     MKV     TXT     DOC     DOCX    PDF     OTHER    FILE        EXPECTED
                Arguments.of("Disabled",     false,      false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  "",      txtFile,    true),
                Arguments.of("PNG",          true,       true,   false,  false,  false,  false,  false,  false,  false,  false,  false,  "",      pngFile,    true),
                Arguments.of("JPG",          true,       false,  true,   false,  false,  false,  false,  false,  false,  false,  false,  "",      jpgFile,    true),
                Arguments.of("MP3",          true,       false,  false,  true,   false,  false,  false,  false,  false,  false,  false,  "",      mp3File,    true),
                Arguments.of("MP4",          true,       false,  false,  false,  true,   false,  false,  false,  false,  false,  false,  "",      mp4File,    true),
                Arguments.of("AVI",          true,       false,  false,  false,  false,  true,   false,  false,  false,  false,  false,  "",      aviFile,    true),
                Arguments.of("MKV",          true,       false,  false,  false,  false,  false,  true,   false,  false,  false,  false,  "",      mkvFile,    true),
                Arguments.of("TXT",          true,       false,  false,  false,  false,  false,  false,  true,   false,  false,  false,  "",      txtFile,    true),
                Arguments.of("DOC",          true,       false,  false,  false,  false,  false,  false,  false,  true,   false,  false,  "",      docFile,    true),
                Arguments.of("DOCX",         true,       false,  false,  false,  false,  false,  false,  false,  false,  true,   false,  "",      docxFile,   true),
                Arguments.of("PDF",          true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  true,   "",      pdfFile,    true),
                Arguments.of("ASD",          true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  "asd",   asdFile,    true),
                Arguments.of(".ASD",         true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  ".asd",  asdFile,    true),
                Arguments.of("B",            true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  "b",     bFile,      true),
                Arguments.of(".B",           true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  ".b",    bFile,      true),
                Arguments.of("More other",   true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  "asd,b", asdFile,    true),
                Arguments.of("More other b", true,       false,  false,  false,  false,  false,  false,  false,  false,  false,  false,  "asd,b", bFile,      true),
                Arguments.of("PNG all true", true,       true,   true,   true,   true,   true,   true,   true,   true,   true,   true,   "asd",   pngFile,    true),

                Arguments.of("PNG false",    true,       false,  true,   true,   true,   true,   true,   true,   true,   true,   true,   "asd",   pngFile,    false),
                Arguments.of("JPG false",    true,       true,   false,  true,   true,   true,   true,   true,   true,   true,   true,   "asd",   jpgFile,    false),
                Arguments.of("MP3 false",    true,       true,   true,   false,  true,   true,   true,   true,   true,   true,   true,   "asd",   mp3File,    false),
                Arguments.of("MP4 false",    true,       true,   true,   true,   false,  true,   true,   true,   true,   true,   true,   "asd",   mp4File,    false),
                Arguments.of("AVI false",    true,       true,   true,   true,   true,   false,  true,   true,   true,   true,   true,   "asd",   aviFile,    false),
                Arguments.of("MKV false",    true,       true,   true,   true,   true,   true,   false,  true,   true,   true,   true,   "asd",   mkvFile,    false),
                Arguments.of("TXT false",    true,       true,   true,   true,   true,   true,   true,   false,  true,   true,   true,   "asd",   txtFile,    false),
                Arguments.of("DOC false",    true,       true,   true,   true,   true,   true,   true,   true,   false,  true,   true,   "asd",   docFile,    false),
                Arguments.of("DOCX false",   true,       true,   true,   true,   true,   true,   true,   true,   true,   false,  true,   "asd",   docxFile,   false),
                Arguments.of("PDF false",    true,       true,   true,   true,   true,   true,   true,   true,   true,   true,   false,  "asd",   pdfFile,    false),
                Arguments.of("ASD false",    true,       true,   true,   true,   true,   true,   true,   true,   true,   true,   true,   "",      asdFile,    false),
                Arguments.of("B false",      true,       true,   true,   true,   true,   true,   true,   true,   true,   true,   true,   "",      bFile,      false),
                Arguments.of("AS false",     true,       true,   true,   true,   true,   true,   true,   true,   true,   true,   true,   "as",    asdFile,    false)
        );
        // @formatter:on
    }
    //endregion

    //region Test
    @DisplayName("Extension predicate tests")
    @ParameterizedTest(name = "{index} - {0}, expected: {14}")
    @MethodSource("extensionArguments")
    void extensionPredicateTest(
            @SuppressWarnings("unused") final String name,
            final boolean enabled,
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
            final String other,
            final File file,
            final boolean expected) {

        // Given
        final ExtensionPredicate extensionPredicate = new ExtensionPredicate(enabled, png, jpg, mp3, mp4, avi, mkv, txt, doc, docx, pdf, other);

        // When
        final boolean actual = extensionPredicate.getPredicate().test(file);

        // Then
        assertEquals(expected, actual);
    }
    //endregion
}
