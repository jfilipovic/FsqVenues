package com.example.jerko.fsqvenues.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Jerko on 6.4.2017..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {
    private String type;
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public String getType() {
        return type;
    }
}
