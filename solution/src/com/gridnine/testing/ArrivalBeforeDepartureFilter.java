package com.gridnine.testing;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule 2: Excludes flights that have at least one segment where
 * the arrival date is earlier than the departure date.
 * That means the segment data is logically incorrect.
 */
public class ArrivalBeforeDepartureFilter implements FlightFilter {

    @Override
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            boolean allSegmentsValid = true;

            for (Segment segment : flight.getSegments()) {
                // If arrival is before departure - this segment is invalid
                if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) {
                    allSegmentsValid = false;
                    break;
                }
            }

            if (allSegmentsValid) {
                result.add(flight);
            }
        }

        return result;
    }
}
