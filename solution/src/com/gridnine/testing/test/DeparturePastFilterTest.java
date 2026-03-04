package com.gridnine.testing.test;

import com.gridnine.testing.DeparturePastFilter;
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

@DisplayName("DeparturePastFilter tests")
class DeparturePastFilterTest {

    private DeparturePastFilter filter;

    @BeforeEach
    void setUp() {
        filter = new DeparturePastFilter();
    }

    // Helper: create a simple one-segment flight
    private Flight createFlight(LocalDateTime departure, LocalDateTime arrival) {
        return new Flight(Collections.singletonList(new Segment(departure, arrival)));
    }

    @Test
    @DisplayName("Flight departing in the past should be excluded")
    void flightWithPastDeparture_shouldBeExcluded() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        Flight pastFlight = createFlight(yesterday, yesterday.plusHours(2));

        List<Flight> result = filter.filter(Collections.singletonList(pastFlight));

        assertTrue(result.isEmpty(), "Past flight must not be in the result");
    }

    @Test
    @DisplayName("Flight departing in the future should be included")
    void flightWithFutureDeparture_shouldBeIncluded() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        Flight futureFlight = createFlight(tomorrow, tomorrow.plusHours(2));

        List<Flight> result = filter.filter(Collections.singletonList(futureFlight));

        assertEquals(1, result.size(), "Future flight must be kept in the result");
    }

    @Test
    @DisplayName("Only future flights are kept when the list is mixed")
    void mixedList_onlyFutureFlightsAreKept() {
        LocalDateTime now = LocalDateTime.now();
        Flight past = createFlight(now.minusDays(2), now.minusDays(2).plusHours(3));
        Flight future = createFlight(now.plusDays(2), now.plusDays(2).plusHours(3));

        List<Flight> result = filter.filter(Arrays.asList(past, future));

        assertEquals(1, result.size());
        // The remaining flight must be the future one
        assertEquals(future, result.get(0));
    }

    @Test
    @DisplayName("Empty input list should return an empty list")
    void emptyList_shouldReturnEmptyList() {
        List<Flight> result = filter.filter(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Multi-segment flight whose first segment departs in the past should be excluded")
    void multiSegmentFlightWithPastFirstDeparture_shouldBeExcluded() {
        LocalDateTime past = LocalDateTime.now().minusDays(3);
        Flight flight = new Flight(Arrays.asList(
                new Segment(past, past.plusHours(2)),
                new Segment(past.plusHours(3), past.plusHours(5))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertTrue(result.isEmpty());
    }
}
