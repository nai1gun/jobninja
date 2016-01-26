package com.nailgun.jhtest.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author nailgun
 * @since 25.01.16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlassdoorResponse {

    private boolean success;

    private String status;

    private GlassdoorItem response;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GlassdoorItem getResponse() {
        return response;
    }

    public void setResponse(GlassdoorItem response) {
        this.response = response;
    }
}
