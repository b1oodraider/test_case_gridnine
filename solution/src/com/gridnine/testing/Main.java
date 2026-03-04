package com.gridnine.testing;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Get the test set from the factory
        List<Flight> allFlights = FlightBuilder.createFlights();

        System.out.println("=== All flights (initial list) ===");
        printFlights(allFlights);

        // Rule 1: exclude flights where departure is before current time
        FlightFilter rule1 = new DeparturePastFilter();
        List<Flight> result1 = rule1.filter(allFlights);
        System.out.println("\n=== Rule 1: Excluding flights that depart before current time ===");
        printFlights(result1);

        // Rule 2: exclude flights where any segment has arrival before departure
        FlightFilter rule2 = new ArrivalBeforeDepartureFilter();
        List<Flight> result2 = rule2.filter(allFlights);
        System.out.println("\n=== Rule 2: Excluding flights with arrival date before departure date ===");
        printFlights(result2);

        // Rule 3: exclude flights where total ground time exceeds 2 hours
        FlightFilter rule3 = new GroundTimeExceededFilter();
        List<Flight> result3 = rule3.filter(allFlights);
        System.out.println("\n=== Rule 3: Excluding flights with more than 2 hours of ground time ===");
        printFlights(result3);
    }

    /**
     * Prints each flight in the list with a number prefix.
     * Prints a message if the list is empty.
     */
    private static void printFlights(List<Flight> flights) {
        if (flights.isEmpty()) {
            System.out.println("  (no flights)");
            return;
        }
        for (int i = 0; i < flights.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + flights.get(i));
        }
    }
}
