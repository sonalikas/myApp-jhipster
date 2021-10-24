package com.sonalika.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Tourist.
 */
@Entity
@Table(name = "tourist")
public class Tourist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tourist_name", nullable = false)
    private String touristName;

    @NotNull
    @Column(name = "tourist_id", nullable = false)
    private Long touristId;

    @Column(name = "tourist_from")
    private String touristFrom;

    @NotNull
    @Column(name = "tourist_image", nullable = false)
    private String touristImage;

    @NotNull
    @Column(name = "tourist_pan", nullable = false)
    private String touristPan;

    @NotNull
    @Column(name = "tourist_age", nullable = false)
    private String touristAge;

    @OneToMany(mappedBy = "tourist")
    @JsonIgnoreProperties(value = { "tourist", "places" }, allowSetters = true)
    private Set<Activities> activities = new HashSet<>();

    @ManyToOne
    private Location location;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tourists", "places" }, allowSetters = true)
    private Facilities facilities;

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "tourists", "facilities", "activities" }, allowSetters = true)
    private Places places;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tourist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTouristName() {
        return this.touristName;
    }

    public Tourist touristName(String touristName) {
        this.setTouristName(touristName);
        return this;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public Long getTouristId() {
        return this.touristId;
    }

    public Tourist touristId(Long touristId) {
        this.setTouristId(touristId);
        return this;
    }

    public void setTouristId(Long touristId) {
        this.touristId = touristId;
    }

    public String getTouristFrom() {
        return this.touristFrom;
    }

    public Tourist touristFrom(String touristFrom) {
        this.setTouristFrom(touristFrom);
        return this;
    }

    public void setTouristFrom(String touristFrom) {
        this.touristFrom = touristFrom;
    }

    public String getTouristImage() {
        return this.touristImage;
    }

    public Tourist touristImage(String touristImage) {
        this.setTouristImage(touristImage);
        return this;
    }

    public void setTouristImage(String touristImage) {
        this.touristImage = touristImage;
    }

    public String getTouristPan() {
        return this.touristPan;
    }

    public Tourist touristPan(String touristPan) {
        this.setTouristPan(touristPan);
        return this;
    }

    public void setTouristPan(String touristPan) {
        this.touristPan = touristPan;
    }

    public String getTouristAge() {
        return this.touristAge;
    }

    public Tourist touristAge(String touristAge) {
        this.setTouristAge(touristAge);
        return this;
    }

    public void setTouristAge(String touristAge) {
        this.touristAge = touristAge;
    }

    public Set<Activities> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activities> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.setTourist(null));
        }
        if (activities != null) {
            activities.forEach(i -> i.setTourist(this));
        }
        this.activities = activities;
    }

    public Tourist activities(Set<Activities> activities) {
        this.setActivities(activities);
        return this;
    }

    public Tourist addActivities(Activities activities) {
        this.activities.add(activities);
        activities.setTourist(this);
        return this;
    }

    public Tourist removeActivities(Activities activities) {
        this.activities.remove(activities);
        activities.setTourist(null);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Tourist location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Facilities getFacilities() {
        return this.facilities;
    }

    public void setFacilities(Facilities facilities) {
        this.facilities = facilities;
    }

    public Tourist facilities(Facilities facilities) {
        this.setFacilities(facilities);
        return this;
    }

    public Places getPlaces() {
        return this.places;
    }

    public void setPlaces(Places places) {
        this.places = places;
    }

    public Tourist places(Places places) {
        this.setPlaces(places);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tourist)) {
            return false;
        }
        return id != null && id.equals(((Tourist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tourist{" +
            "id=" + getId() +
            ", touristName='" + getTouristName() + "'" +
            ", touristId=" + getTouristId() +
            ", touristFrom='" + getTouristFrom() + "'" +
            ", touristImage='" + getTouristImage() + "'" +
            ", touristPan='" + getTouristPan() + "'" +
            ", touristAge='" + getTouristAge() + "'" +
            "}";
    }
}
