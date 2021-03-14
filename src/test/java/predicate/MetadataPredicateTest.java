package predicate;

import domain.MatchType;
import domain.MetadataType;
import metadata.MetadataSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.Collections;
import java.util.stream.Stream;

import static domain.MatchType.*;
import static domain.MetadataType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetadataPredicateTest {

    private static final File txtFile = new File("src/test/resources/predicate/example.avi");

    //region Arguments
    private static Stream<Arguments> metadataArguments() {
        // @formatter:off
        return Stream.of(
                Arguments.of("actor1",                          "actor",    "Natalie Portman",  STRING,     EXACT,              true),
                Arguments.of("actor2",                          "actor",    "Mila Kunis",       STRING,     EXACT,              true),
                Arguments.of("imdb exact",                      "imdb",     "8.0",              DOUBLE,     EXACT,              true),
                Arguments.of("imdb higher or equals",           "imdb",     "8.0",              DOUBLE,     HIGHER_OR_EQUALS,   true),
                Arguments.of("imdb lower or equals",            "imdb",     "8.0",              DOUBLE,     LOWER_OR_EQUALS,    true),
                Arguments.of("imdb higher",                     "imdb",     "7.0",              DOUBLE,     HIGHER,             true),
                Arguments.of("imdb lower",                      "imdb",     "9.0",              DOUBLE,     LOWER,              true),
                Arguments.of("length exact",                    "length",   "108",              INTEGER,    EXACT,              true),
                Arguments.of("year exact",                      "year",     "2010",             INTEGER,    EXACT,              true),
                Arguments.of("year higher or equals",           "year",     "2010",             INTEGER,    HIGHER_OR_EQUALS,   true),
                Arguments.of("year lower or equals",            "year",     "2010",             INTEGER,    LOWER_OR_EQUALS,    true),
                Arguments.of("year higher",                     "year",     "2009",             INTEGER,    HIGHER,             true),
                Arguments.of("year lower",                      "year",     "2011",             INTEGER,    LOWER,              true),
                Arguments.of("actor not",                       "actor",    "Jennifer Aniston", STRING,     EXACT,              false),
                Arguments.of("imdb exact - not",                "imdb",     "8.1",              DOUBLE,     EXACT,              false),
                Arguments.of("imdb higher or equals - not",     "imdb",     "8.1",              DOUBLE,     HIGHER_OR_EQUALS,   false),
                Arguments.of("imdb lower or equals - not",      "imdb",     "7.9",              DOUBLE,     LOWER_OR_EQUALS,    false),
                Arguments.of("imdb higher - not",               "imdb",     "9.0",              DOUBLE,     HIGHER,             false),
                Arguments.of("imdb lower - not",                "imdb",     "7.0",              DOUBLE,     LOWER,              false),
                Arguments.of("length exact - not",              "length",   "109",              INTEGER,    EXACT,              false),
                Arguments.of("year exact - not",                "year",     "2011",             INTEGER,    EXACT,              false),
                Arguments.of("year higher or equals - not",     "year",     "2011",             INTEGER,    HIGHER_OR_EQUALS,   false),
                Arguments.of("year lower or equals - not",      "year",     "2009",             INTEGER,    LOWER_OR_EQUALS,    false),
                Arguments.of("year higher - not",               "year",     "2011",             INTEGER,    HIGHER,             false),
                Arguments.of("year lower - not",                "year",     "2009",             INTEGER,    LOWER,              false)
                );
        // @formatter:on
    }
    //endregion

    //region Tests
    @ParameterizedTest(name = "{index} - {0}, expected: {5}")
    @MethodSource("metadataArguments")
    void metadataPredicateTest(@SuppressWarnings("unused") final String name, final String category, final String value, final MetadataType metadataType, final MatchType matchType, final boolean expected) {
        // Given
        MetadataPredicate metadataPredicate = new MetadataPredicate(true, Collections.singletonList(new MetadataSearch(category, value, metadataType.toString(), matchType.toString())));

        // When
        boolean actual = metadataPredicate.getPredicate().test(txtFile);

        // Then
        assertEquals(expected, actual);
    }

    @DisplayName("Metadata searching disabled test")
    @Test
    void disabledMetadataPredicateTest() {
        // Given
        MetadataPredicate metadataPredicate = new MetadataPredicate(false, Collections.singletonList(new MetadataSearch("asd", "asd", STRING.toString(), EXACT.toString())));

        // When
        boolean actual = metadataPredicate.getPredicate().test(txtFile);

        // Then
        assertTrue(actual);
    }
    //endregion
}
