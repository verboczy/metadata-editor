package predicate;

import domain.FileSizeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static domain.FileSizeUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileSizePredicateTest {

    final File testFile = new File("src/test/resources/predicate/example.txt");

    //region Arguments
    private static Stream<Arguments> fileSizeArguments() {
        // @formatter:off
        return Stream.of(
                //            TEST CASE NAME                    ENABLED    <= x    x <=     UNIT       EXPECTED
                Arguments.of("Disabled, 0 <= x <= 0 KB",        false,      0,      0,      KB,   true),
                Arguments.of("Disabled, null <= x <= null KB",  false,      null,   null,   KB,   true),
                Arguments.of("Disabled, 0 <= x <= -6 KB",       false,      0,      -6,     KB,   true),
                Arguments.of("Disabled, 0 <= x <= -6 GB",       false,      0,      -6,     GB,   true),
                Arguments.of("Disabled, 100 <= x <= null GB",   false,      100,    null,   GB,   true),
                Arguments.of("Enabled, 100 <= x <= 99 Byte",    true,       100,    99,     BYTE,       false),
                Arguments.of("Enabled, 0 <= x <= -6 Byte",      true,       0,      -6,     BYTE,       false),
                Arguments.of("Enabled, 1201 <= x <= null Byte", true,       1201,   null,   BYTE,       false),
                Arguments.of("Enabled, null <= x <= 1199 Byte", true,       null,   1199,   BYTE,       false),
                Arguments.of("Enabled, 1 <= x <= 10 MB",        true,       1,      10,     MB,   false),
                Arguments.of("Enabled, 1 <= x <= 10 GB",        true,       1,      10,     GB,   false),
                Arguments.of("Enabled, 1200 <= x <= 1200 Byte", true,       1200,   1200,   BYTE,       true),
                Arguments.of("Enabled, 1200 <= x <= null Byte", true,       1200,   null,   BYTE,       true),
                Arguments.of("Enabled, null <= x <= 1200 Byte", true,       null,   1200,   BYTE,       true),
                Arguments.of("Enabled, 1 <= x <= null KB",      true,       1,      null,   KB,   true),
                Arguments.of("Enabled, null <= x <= 10 KB",     true,       null,   10,     KB,   true),
                Arguments.of("Enabled, 1 <= x <= 10 KB",        true,       1,      10,     KB,   true),
                Arguments.of("Enabled, 0 <= x <= null MB",      true,       0,      null,   MB,   true),
                Arguments.of("Enabled, null <= x <= 1 MB",      true,       null,   1,      MB,   true),
                Arguments.of("Enabled, 0 <= x <= 1 MB",         true,       0,      1,      MB,   true),
                Arguments.of("Enabled, 0 <= x <= null GB",      true,       0,      null,   GB,   true),
                Arguments.of("Enabled, null <= x <= 1 GB",      true,       null,   1,      GB,   true),
                Arguments.of("Enabled, 0 <= x <= 1 GB",         true,       0,      1,      GB,   true)
        );
        // @formatter:on
    }
    //endregion

    //region Tests
    @DisplayName("File size predicate tests")
    @ParameterizedTest(name = "{index} - {0}, expected: {5}")
    @MethodSource("fileSizeArguments")
    void fileSizePredicateTest(@SuppressWarnings("unused") final String name, final boolean enabled, final Integer largerThan, final Integer smallerThan, final FileSizeUnit unit, final boolean expected) {
        // Given
        final FileSizePredicate fileSizePredicate = new FileSizePredicate(enabled, unit, largerThan, smallerThan);

        // When
        final boolean actual = fileSizePredicate.getPredicate().test(testFile);

        // Then
        assertEquals(expected, actual);
    }
    //endregion
}
