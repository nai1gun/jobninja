package com.nailgun.jhtest.domain;

import java.io.Serializable;

/**
 * @author nailgun
 * @since 07.11.15
 */
public class Cv implements Serializable {

    private String filePath;

    public Cv(String filePath) {
        this.filePath = filePath;
    }

    public Cv() {}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cv cv = (Cv) o;

        return filePath.equals(cv.filePath);

    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }
}
