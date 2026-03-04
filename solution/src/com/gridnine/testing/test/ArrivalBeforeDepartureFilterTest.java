package com.gridnine.testing.test;

import com.gridnine.testing.ArrivalBeforeDepartureFilter;
import com.gridnine.testing.Flight;
import com.gridnine.testing.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArrivalBeforeDepartureFilter tests")
class ArrivalBeforeDepartureFilterTest {

    private ArrivalBeforeDepartureFilter filter;
    // A fixed base time so tests don't depend on the current moment
    private final LocalDateTime base = LocalDateTime.now().plusDays(3);

    @BeforeEach
    void setUp() {
        filter = new ArrivalBeforeDepartureFilter();
    }

    @Test
    @DisplayName("Normal segment (arrival after departure) should be included")
    void normalSegment_shouldBeIncluded() {
        Flight flight = new Flight(Collections.singletonList(
                new Segment(base, base.plusHours(2))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Segment where arrival is before departure should be excluded")
    void arrivalBeforeDeparture_shouldBeExcluded() {
        Flight flight = new Flight(Collections.singletonList(
                // arrival is 6 hours BEFORE departure — makes no sense
                new Segment(base, base.minusHours(6))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertTrue(result.isEmpty(), "Flight with bad segment must be excluded");
    }

    @Test
    @DisplayName("Multi-segment flight where all segments are valid should be included")
    void multiSegment_allValid_shouldBeIncluded() {
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),
                new Segment(base.plusHours(3), base.plusHours(5))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Multi-segment flight with one invalid segment should be excluded")
    void multiSegment_oneInvalid_shouldBeExcluded() {
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),             // valid
                new Segment(base.plusHours(3), base.plusHours(1)) // arrival before departure
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertTrue(result.isEmpty(), "Flight with at least one bad segment must be excluded");
    }

    @Test
    @DisplayName("Mixed list: only the flight with valid segments is kept")
    void mixedList_onlyValidFlightsAreKept() {
        Flight validFlight = new Flight(Collections.singletonList(
                new Segment(base, base.plusHours(3))
        ));
        Flight invalidFlight = new Flight(Collections.singletonList(
                new Segment(base, base.minusHours(1))
        ));

        List<Flight> result = filter.filter(Arrays.asList(validFlight, invalidFlight));

        assertEquals(1, result.size());
        assertEquals(validFlight, result.get(0));
    }

    @Test
    @DisplayName("Empty input list should return an empty list")
    void emptyList_shouldReturnEmptyList() {
        List<Flight> result = filter.filter(Collections.emptyList());
        assertTrue(result.isEmpty());
    }
}
