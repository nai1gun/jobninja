package com.nailgun.jhtest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nailgun.jhtest.domain.util.CustomDateTimeDeserializer;
import com.nailgun.jhtest.domain.util.CustomDateTimeSerializer;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;


/**
 * A Position.
 */
@Document(collection = "POSITION")
public class Position implements Serializable {

    @Id
    private String id;

    @DBRef(lazy = true)
    @Indexed
    @JsonIgnore
    private User user;

    @Field("name")
    private String name;

    @Field("company")
    private String company;

    @Field("location")
    private String location;

    @Field("link")
    private String link;

    @Field("state")
    private String state;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Field("created")
    private DateTime created;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Field("edited")
    private DateTime edited;

    @Field("notes")
    private String notes;

    @Field("coverLetter")
    private String coverLetter;

    private Cv cv;

    @Field("companyLogoUrl")
    private String companyLogoUrl;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getEdited() {
        return edited;
    }

    public void setEdited(DateTime edited) {
        this.edited = edited;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Cv getCv() {
        return cv;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Position position = (Position) o;

        if ( ! Objects.equals(id, position.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", company='" + company + "'" +
                ", location='" + location + "'" +
                ", link='" + link + "'" +
                ", state='" + state + "'" +
                ", created='" + created + "'" +
                ", edited='" + edited + "'" +
                ", notes='" + notes + "'" +
                ", coverLeter='" + coverLetter + "'" +
                ", companyLogoUrl='" + companyLogoUrl + "'" +
                '}';
    }
}
