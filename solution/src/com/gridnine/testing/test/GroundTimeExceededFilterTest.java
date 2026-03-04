package com.gridnine.testing.test;

import com.gridnine.testing.Flight;
import com.gridnine.testing.GroundTimeExceededFilter;
import com.gridnine.testing.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GroundTimeExceededFilter tests")
class GroundTimeExceededFilterTest {

    private GroundTimeExceededFilter filter;
    private final LocalDateTime base = LocalDateTime.now().plusDays(3);

    @BeforeEach
    void setUp() {
        filter = new GroundTimeExceededFilter();
    }

    @Test
    @DisplayName("Single-segment flight has no ground time and should be included")
    void singleSegment_noGroundTime_shouldBeIncluded() {
        Flight flight = new Flight(Collections.singletonList(
                new Segment(base, base.plusHours(2))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Two segments with exactly 2 hours gap should be included (boundary)")
    void twoSegments_exactlyTwoHoursGap_shouldBeIncluded() {
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),
                // departs exactly 2 hours after previous arrival
                new Segment(base.plusHours(4), base.plusHours(6))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertEquals(1, result.size(), "Exactly 2h ground time is allowed");
    }

    @Test
    @DisplayName("Two segments with less than 2 hours gap should be included")
    void twoSegments_lessThanTwoHoursGap_shouldBeIncluded() {
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),
                new Segment(base.plusHours(3), base.plusHours(5)) // 1h gap
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Two segments with more than 2 hours gap should be excluded")
    void twoSegments_moreThanTwoHoursGap_shouldBeExcluded() {
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),
                new Segment(base.plusHours(5), base.plusHours(6)) // 3h gap
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertTrue(result.isEmpty(), "3h ground time must be excluded");
    }

    @Test
    @DisplayName("Three segments where total ground time exceeds 2 hours should be excluded")
    void threeSegments_totalGroundTimeExceedsLimit_shouldBeExcluded() {
        // Gap between seg1 and seg2: 1h, gap between seg2 and seg3: 2h → total 3h
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),
                new Segment(base.plusHours(3), base.plusHours(4)),
                new Segment(base.plusHours(6), base.plusHours(7))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertTrue(result.isEmpty(), "Total ground time of 3h must be excluded");
    }

    @Test
    @DisplayName("Three segments where total ground time is within the limit should be included")
    void threeSegments_totalGroundTimeWithinLimit_shouldBeIncluded() {
        // Gap between seg1 and seg2: 30min, gap between seg2 and seg3: 30min → total 1h
        Flight flight = new Flight(Arrays.asList(
                new Segment(base, base.plusHours(2)),
                new Segment(base.plusHours(2).plusMinutes(30), base.plusHours(4)),
                new Segment(base.plusHours(4).plusMinutes(30), base.plusHours(6))
        ));

        List<Flight> result = filter.filter(Collections.singletonList(flight));

        assertEquals(1, result.size(), "1h total ground time must be kept");
    }

    @Test
    @DisplayName("Empty input list should return an empty list")
    void emptyList_shouldReturnEmptyList() {
        List<Flight> result = filter.filter(Collections.emptyList());
        assertTrue(result.isEmpty());
    }
}
