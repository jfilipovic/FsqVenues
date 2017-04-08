package com.example.jerko.fsqvenues.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jerko on 6.4.2017..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    private String name;
    private Icon icon;

    public Icon getIcon() {
        return icon;
    }

    public String getName(){
        return name;
    }

}
