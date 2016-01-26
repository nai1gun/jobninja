package com.nailgun.jhtest.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author nailgun
 * @since 25.01.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlassdoorEmployer {

    private String id;

    private String name;

    private double overallRating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }
}
