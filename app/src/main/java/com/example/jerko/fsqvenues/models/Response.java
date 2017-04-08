package com.example.jerko.fsqvenues.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Jerko on 6.4.2017..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private String warning;
    private String headerLocation;
    private List<Group> groups;

    public List<Group> getGroups(){
        return groups;
    }

    public String getHeaderLocation() {
        return headerLocation;
    }

    public String getWarning() {
        return warning;
    }
}
