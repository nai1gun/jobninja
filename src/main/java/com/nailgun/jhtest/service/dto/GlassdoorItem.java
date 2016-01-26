package com.nailgun.jhtest.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author nailgun
 * @since 25.01.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlassdoorItem {

    private List<GlassdoorEmployer> employers;

    public List<GlassdoorEmployer> getEmployers() {
        return employers;
    }

    public void setEmployers(List<GlassdoorEmployer> employers) {
        this.employers = employers;
    }
}
