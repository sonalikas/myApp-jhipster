package com.sonalika.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Activities.
 */
@Entity
@Table(name = "activities")
public class Activities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "activities_name", nullable = false)
    private String activitiesName;

    @NotNull
    @Column(name = "activities_condition", nullable = false)
    private String activitiesCondition;

    @NotNull
    @Column(name = "activites_id", nullable = false)
    private Long activitesId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "activities", "location", "facilities", "places" }, allowSetters = true)
    private Tourist tourist;

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "tourists", "facilities", "activities" }, allowSetters = true)
    private Places places;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activities id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivitiesName() {
        return this.activitiesName;
    }

    public Activities activitiesName(String activitiesName) {
        this.setActivitiesName(activitiesName);
        return this;
    }

    public void setActivitiesName(String activitiesName) {
        this.activitiesName = activitiesName;
    }

    public String getActivitiesCondition() {
        return this.activitiesCondition;
    }

    public Activities activitiesCondition(String activitiesCondition) {
        this.setActivitiesCondition(activitiesCondition);
        return this;
    }

    public void setActivitiesCondition(String activitiesCondition) {
        this.activitiesCondition = activitiesCondition;
    }

    public Long getActivitesId() {
        return this.activitesId;
    }

    public Activities activitesId(Long activitesId) {
        this.setActivitesId(activitesId);
        return this;
    }

    public void setActivitesId(Long activitesId) {
        this.activitesId = activitesId;
    }

    public Tourist getTourist() {
        return this.tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public Activities tourist(Tourist tourist) {
        this.setTourist(tourist);
        return this;
    }

    public Places getPlaces() {
        return this.places;
    }

    public void setPlaces(Places places) {
        this.places = places;
    }

    public Activities places(Places places) {
        this.setPlaces(places);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activities)) {
            return false;
        }
        return id != null && id.equals(((Activities) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activities{" +
            "id=" + getId() +
            ", activitiesName='" + getActivitiesName() + "'" +
            ", activitiesCondition='" + getActivitiesCondition() + "'" +
            ", activitesId=" + getActivitesId() +
            "}";
    }
}
