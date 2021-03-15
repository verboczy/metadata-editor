package metadata;

import domain.MatchType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static domain.MatchType.*;
import static domain.MatchType.LOWER_OR_EQUALS;
import static domain.MetadataType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MetadataSearchTest {

    //region String tests
    private static final String actor = "actor";
    private static final String actor1 = "Brad Pitt";
    private static final String actor2 = "Vinnie Jones";
    private static final String actor3 = "Jason Statham";

    private static Stream<Arguments> stringMetadataArguments() {
        // @formatter:off
        return Stream.of(
                Arguments.of(String.join(",", actor1, actor2, actor3),    actor,      actor3,                                             true),
                Arguments.of(actor3,                                              actor,      String.join(",", actor1, actor2, actor3),   true),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join(",", actor1, actor3),           true),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join(" , ", actor1, actor3),         true),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join("\t,\t", actor1, actor3),       true),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join("\n,", actor1, actor3),         true),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join("\r,", actor1, actor3),         true),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join("\r\n,", actor1, actor3),       true),
                Arguments.of(String.join(",", actor2, actor3),            "category", String.join(";", actor1, actor3),           false),
                Arguments.of(String.join(",", actor2, actor3),            actor,      String.join(";", actor1, actor3),           false),
                Arguments.of(String.join(",", actor2),                    actor,      String.join(",", actor3),                   false)
        );
        // @formatter:on
    }

    @DisplayName("String metadata tests")
    @ParameterizedTest(name = "{index} - [{0}] matches [{2}], expected: {3}")
    @MethodSource("stringMetadataArguments")
    void stringMetadataTest(final String value, final String otherCategory, final String otherValue, final boolean expected) {
        // Given
        final MetadataSearch metadata = new MetadataSearch(actor, value, STRING.toString(), "");
        final Metadata otherMetadata = new Metadata(otherCategory, otherValue);

        // When
        final boolean actual = metadata.matchesMetadata(otherMetadata);

        // Then
        assertEquals(expected, actual);
    }
    //endregion

    //region Double tests
    private static final String imdb = "imdb";
    private static final String exactScore = "8.3";
    private static final String lowerScore = "8.2";
    private static final String higherScore = "8.4";

    private static Stream<Arguments> doubleMetadataArguments() {
        // @formatter:off
        return Stream.of(
                Arguments.of("Wrong category",                              "dbmi",     exactScore,     EXACT,              false),
                Arguments.of("Exact, and file is equal",                    imdb,       exactScore,     EXACT,              true),
                Arguments.of("Exact, but file is lower",                    imdb,       higherScore,    EXACT,              false),
                Arguments.of("Exact, but file is higher",                   imdb,       lowerScore,     EXACT,              false),
                Arguments.of("Lower match, and file is lower",              imdb,       higherScore,    LOWER,              true),
                Arguments.of("Lower match, but file is equal",              imdb,       exactScore,     LOWER,              false),
                Arguments.of("Lower match, but file is higher",             imdb,       lowerScore,     LOWER,              false),
                Arguments.of("Lower or equals match, and file is lower",    imdb,       higherScore,    LOWER_OR_EQUALS,    true),
                Arguments.of("Lower or equals match, and file is equal",    imdb,       exactScore,     LOWER_OR_EQUALS,    true),
                Arguments.of("Lower or equals match, but file is higher",   imdb,       lowerScore,     LOWER_OR_EQUALS,    false),
                Arguments.of("Higher match, but file is lower",             imdb,       higherScore,    HIGHER,             false),
                Arguments.of("Higher match, but file is equal",             imdb,       exactScore,     HIGHER,             false),
                Arguments.of("Higher match, and file is higher",            imdb,       lowerScore,     HIGHER,             true),
                Arguments.of("Higher or equals match, but file is lower",   imdb,       higherScore,    HIGHER_OR_EQUALS,   false),
                Arguments.of("Higher or equals match, and file is equal",   imdb,       exactScore,     HIGHER_OR_EQUALS,   true),
                Arguments.of("Higher or equals match, and file is higher",  imdb,       lowerScore,     HIGHER_OR_EQUALS,   true),
                Arguments.of("Not valid value",                             imdb,       "number",       EXACT,              false)
        );
        // @formatter:on
    }

    @DisplayName("Double metadata tests")
    @ParameterizedTest(name = "{index} - {0}, expected: {4}")
    @MethodSource("doubleMetadataArguments")
    void doubleMetadataTest(@SuppressWarnings("unused") final String name, final String category, final String value, final MatchType matchType, final boolean expected) {
        // Given
        final MetadataSearch metadata = new MetadataSearch(category, value, DOUBLE.toString(), matchType.toString());
        final Metadata otherMetadata = new Metadata(imdb, exactScore);

        // When
        final boolean actual = metadata.matchesMetadata(otherMetadata);

        // Then
        assertEquals(expected, actual);
    }
    //endregion

    //region Integer tests
    private static final String yearCategory = "year";
    private static final String year = "2000";
    private static final String before = "1999";
    private static final String after = "2001";

    private static Stream<Arguments> integerMetadataArguments() {
        // @formatter:off
        return Stream.of(
                Arguments.of("Wrong category",                              "reay",         year,       EXACT,              false),
                Arguments.of("Exact, and file is equal",                    yearCategory,   year,       EXACT,              true),
                Arguments.of("Exact, but file is lower",                    yearCategory,   after,      EXACT,              false),
                Arguments.of("Exact, but file is higher",                   yearCategory,   before,     EXACT,              false),
                Arguments.of("Lower match, and file is lower",              yearCategory,   after,      LOWER,              true),
                Arguments.of("Lower match, but file is equal",              yearCategory,   year,       LOWER,              false),
                Arguments.of("Lower match, but file is higher",             yearCategory,   before,     LOWER,              false),
                Arguments.of("Lower or equals match, and file is lower",    yearCategory,   after,      LOWER_OR_EQUALS,    true),
                Arguments.of("Lower or equals match, and file is equal",    yearCategory,   year,       LOWER_OR_EQUALS,    true),
                Arguments.of("Lower or equals match, but file is higher",   yearCategory,   before,     LOWER_OR_EQUALS,    false),
                Arguments.of("Higher match, but file is lower",             yearCategory,   after,      HIGHER,             false),
                Arguments.of("Higher match, but file is equal",             yearCategory,   year,       HIGHER,             false),
                Arguments.of("Higher match, and file is higher",            yearCategory,   before,     HIGHER,             true),
                Arguments.of("Higher or equals match, but file is lower",   yearCategory,   after,      HIGHER_OR_EQUALS,   false),
                Arguments.of("Higher or equals match, and file is equal",   yearCategory,   year,       HIGHER_OR_EQUALS,   true),
                Arguments.of("Higher or equals match, and file is higher",  yearCategory,   before,     HIGHER_OR_EQUALS,   true),
                Arguments.of("Not valid value",                             yearCategory,   "number",   EXACT,              false)
        );
        // @formatter:on
    }

    @DisplayName("Integer metadata tests")
    @ParameterizedTest(name = "{index} - {0}, expected: {4}")
    @MethodSource("integerMetadataArguments")
    void integerMetadataTest(@SuppressWarnings("unused") final String name, final String category, final String value, final MatchType matchType, final boolean expected) {
        // Given
        final MetadataSearch metadata = new MetadataSearch(category, value, INTEGER.toString(), matchType.toString());
        final Metadata otherMetadata = new Metadata(yearCategory, year);

        // When
        final boolean actual = metadata.matchesMetadata(otherMetadata);

        // Then
        assertEquals(expected, actual);
    }
    //endregion
}
