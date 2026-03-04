package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule 1: Excludes flights where the departure time is before the current moment.
 * A flight is considered "in the past" if its first segment departs before now.
 */
public class DeparturePastFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            // The departure of the flight is the departure of its first segment
            LocalDateTime departureTime = flight.getSegments().get(0).getDepartureDate();
            if (!departureTime.isBefore(now)) {
                result.add(flight);
            }
        }

        return result;
    }
}
