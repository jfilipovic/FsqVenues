package com.example.jerko.fsqvenues.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jerko on 6.4.2017..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Icon {
    private String prefix;
    private String suffix;

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
