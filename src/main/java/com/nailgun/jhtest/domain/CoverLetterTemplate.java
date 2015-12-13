package com.nailgun.jhtest.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;


/**
 * A CoverLetterTemplate.
 */
@Document(collection = "COVER_LETTER_TEMPLATE")
public class CoverLetterTemplate implements Serializable {

    @Id
    private String id;



    @Field("name")
    private String name;



    @Field("text")
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoverLetterTemplate coverLetterTemplate = (CoverLetterTemplate) o;

        if ( ! Objects.equals(id, coverLetterTemplate.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverLetterTemplate{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", text='" + text + "'" +
                '}';
    }
}
