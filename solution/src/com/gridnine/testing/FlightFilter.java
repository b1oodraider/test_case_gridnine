package com.gridnine.testing;

import java.util.List;

/**
 * Interface that defines a single filtering rule for flights.
 * Each implementation represents one specific rule.
 */
public interface FlightFilter {

    /**
     * Takes a list of flights and returns only those that pass this rule.
     *
     * @param flights the list of flights to filter
     * @return a new list with only the flights that satisfy the rule
     */
    List<Flight> filter(List<Flight> flights);
}
