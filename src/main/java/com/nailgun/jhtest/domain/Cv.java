package com.nailgun.jhtest.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nailgun.jhtest.domain.util.CustomDateTimeDeserializer;
import com.nailgun.jhtest.domain.util.CustomDateTimeSerializer;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author nailgun
 * @since 07.11.15
 */
public class Cv implements Serializable {

    private String filePath;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Field("created")
    private DateTime created = null;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
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
