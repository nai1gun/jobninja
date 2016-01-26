package com.nailgun.jhtest.web.rest.dto;

/**
 * @author nailgun
 * @since 25.01.16
 */
public class GlassdoorEmployerDTO {

    private String url;

    private double rating;

    public GlassdoorEmployerDTO(String url, double rating) {
        this.url = url;
        this.rating = rating;
    }

    public GlassdoorEmployerDTO() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
