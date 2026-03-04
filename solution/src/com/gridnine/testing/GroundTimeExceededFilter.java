package com.gridnine.testing;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule 3: Excludes flights where the total time spent on the ground
 * between segments exceeds two hours.
 *
 * Ground time = the gap between the arrival of one segment
 * and the departure of the next segment.
 */
public class GroundTimeExceededFilter implements FlightFilter {

    // Maximum allowed ground time in minutes (2 hours = 120 minutes)
    private static final long MAX_GROUND_TIME_MINUTES = 120;

    @Override
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            List<Segment> segments = flight.getSegments();
            long totalGroundMinutes = 0;

            // For each pair of consecutive segments, calculate the gap between them
            for (int i = 0; i < segments.size() - 1; i++) {
                Segment current = segments.get(i);
                Segment next = segments.get(i + 1);

                Duration gap = Duration.between(current.getArrivalDate(), next.getDepartureDate());
                totalGroundMinutes += gap.toMinutes();
            }

            // Only keep the flight if the total ground time is within the limit
            if (totalGroundMinutes <= MAX_GROUND_TIME_MINUTES) {
                result.add(flight);
            }
        }

        return result;
    }
}
