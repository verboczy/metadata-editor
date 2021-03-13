package predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LastModifiedDatePredicateTest {

    private static final File txtFile = new File("src/test/resources/predicate/example.txt"); // Last modified on 2021-03-12

    //region Arguments
    private static Stream<Arguments> lastModifiedDateArguments() {
        // @formatter:off
        return Stream.of(
                //            NAME                          ENABLED   AFTER DATE                                        BEFORE DATE                                     EXPECTED
                Arguments.of("Disabled",                    false, LocalDate.now(),                                    LocalDate.now(),                                   true),
                Arguments.of("Disabled - impossible dates", false, LocalDate.now().plusWeeks(1),                       LocalDate.now().minusWeeks(1),                     true),
                Arguments.of("Null dates",                  true,  null,                                               null,                                              true),
                Arguments.of("Null before",                 true,  LocalDate.of(2021, 3, 11),    null,                                              true),
                Arguments.of("Null after",                  true,  null,                                               LocalDate.of(2021, 3, 13),   true),
                Arguments.of("Same day after",              true,  LocalDate.of(2021, 3, 12),    null,                                              true),
                Arguments.of("Same day before",             true,  null,                                               LocalDate.of(2021, 3, 12),   true),
                Arguments.of("Same day",                    true,  LocalDate.of(2021, 3, 12),    LocalDate.of(2021, 3, 12),   true),
                Arguments.of("In between",                  true,  LocalDate.of(2021, 3, 11),    LocalDate.of(2021, 3, 13),   true),
                Arguments.of("Not after",                   true,  LocalDate.of(2021, 3, 13),    null,                                              false),
                Arguments.of("Not before",                  true,  null,                                               LocalDate.of(2021, 3, 11),   false),
                Arguments.of("In between - swapped",        true,  LocalDate.of(2021, 3, 13),    LocalDate.of(2021, 3, 11),   false)
        );
        // @formatter:on
    }
    //endregion

    //region  Test
    @DisplayName("Last modified date predicate tests")
    @ParameterizedTest(name = "{index} - {0}, expected: {4}")
    @MethodSource("lastModifiedDateArguments")
    void lastModifiedDatePredicateTest(@SuppressWarnings("unused") final String name, final boolean enabled, final LocalDate afterDate, final LocalDate beforeDate, final boolean expected) {
        // Given
        final LastModifiedDatePredicate lastModifiedDatePredicate = new LastModifiedDatePredicate(enabled, afterDate, beforeDate);

        // When
        final boolean actual = lastModifiedDatePredicate.getPredicate().test(txtFile);

        // Then
        assertEquals(expected, actual);
    }
    //endregion
}
