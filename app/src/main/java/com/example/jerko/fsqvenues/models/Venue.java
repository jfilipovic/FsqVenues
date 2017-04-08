package com.example.jerko.fsqvenues.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Jerko on 6.4.2017..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue {
    private String name;
    private Location location;
    private List<Category> categories;
    private Price price;
    private Hours hours;
    private String rating;

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Price getPrice() {
        return price;
    }

    public Hours getHours() {
        return hours;
    }

    public String getRating() {
        return rating;
    }


}
