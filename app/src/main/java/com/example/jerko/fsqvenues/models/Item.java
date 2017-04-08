package com.example.jerko.fsqvenues.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jerko on 6.4.2017..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private Venue venue;

    public Venue getVenue() {
        return venue;
    }
}
