package com.sonalika.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Places.
 */
@Entity
@Table(name = "places")
public class Places implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "places_name", nullable = false)
    private String placesName;

    @NotNull
    @Column(name = "places_id", nullable = false)
    private Long placesId;

    @Column(name = "places_desc")
    private String placesDesc;

    @NotNull
    @Column(name = "places_current_status", nullable = false)
    private String placesCurrentStatus;

    @Column(name = "place_special_attraction")
    private String placeSpecialAttraction;

    @NotNull
    @Column(name = "place_visit_price", nullable = false)
    private Long placeVisitPrice;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    /**
     * Another side of the same relationship
     */
    @ApiModelProperty(value = "Another side of the same relationship")
    @OneToMany(mappedBy = "places")
    @JsonIgnoreProperties(value = { "activities", "location", "facilities", "places" }, allowSetters = true)
    private Set<Tourist> tourists = new HashSet<>();

    @OneToMany(mappedBy = "places")
    @JsonIgnoreProperties(value = { "tourists", "places" }, allowSetters = true)
    private Set<Facilities> facilities = new HashSet<>();

    @OneToMany(mappedBy = "places")
    @JsonIgnoreProperties(value = { "tourist", "places" }, allowSetters = true)
    private Set<Activities> activities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Places id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlacesName() {
        return this.placesName;
    }

    public Places placesName(String placesName) {
        this.setPlacesName(placesName);
        return this;
    }

    public void setPlacesName(String placesName) {
        this.placesName = placesName;
    }

    public Long getPlacesId() {
        return this.placesId;
    }

    public Places placesId(Long placesId) {
        this.setPlacesId(placesId);
        return this;
    }

    public void setPlacesId(Long placesId) {
        this.placesId = placesId;
    }

    public String getPlacesDesc() {
        return this.placesDesc;
    }

    public Places placesDesc(String placesDesc) {
        this.setPlacesDesc(placesDesc);
        return this;
    }

    public void setPlacesDesc(String placesDesc) {
        this.placesDesc = placesDesc;
    }

    public String getPlacesCurrentStatus() {
        return this.placesCurrentStatus;
    }

    public Places placesCurrentStatus(String placesCurrentStatus) {
        this.setPlacesCurrentStatus(placesCurrentStatus);
        return this;
    }

    public void setPlacesCurrentStatus(String placesCurrentStatus) {
        this.placesCurrentStatus = placesCurrentStatus;
    }

    public String getPlaceSpecialAttraction() {
        return this.placeSpecialAttraction;
    }

    public Places placeSpecialAttraction(String placeSpecialAttraction) {
        this.setPlaceSpecialAttraction(placeSpecialAttraction);
        return this;
    }

    public void setPlaceSpecialAttraction(String placeSpecialAttraction) {
        this.placeSpecialAttraction = placeSpecialAttraction;
    }

    public Long getPlaceVisitPrice() {
        return this.placeVisitPrice;
    }

    public Places placeVisitPrice(Long placeVisitPrice) {
        this.setPlaceVisitPrice(placeVisitPrice);
        return this;
    }

    public void setPlaceVisitPrice(Long placeVisitPrice) {
        this.placeVisitPrice = placeVisitPrice;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Places location(Location location) {
        this.setLocation(location);
        return this;
    }

    public Set<Tourist> getTourists() {
        return this.tourists;
    }

    public void setTourists(Set<Tourist> tourists) {
        if (this.tourists != null) {
            this.tourists.forEach(i -> i.setPlaces(null));
        }
        if (tourists != null) {
            tourists.forEach(i -> i.setPlaces(this));
        }
        this.tourists = tourists;
    }

    public Places tourists(Set<Tourist> tourists) {
        this.setTourists(tourists);
        return this;
    }

    public Places addTourist(Tourist tourist) {
        this.tourists.add(tourist);
        tourist.setPlaces(this);
        return this;
    }

    public Places removeTourist(Tourist tourist) {
        this.tourists.remove(tourist);
        tourist.setPlaces(null);
        return this;
    }

    public Set<Facilities> getFacilities() {
        return this.facilities;
    }

    public void setFacilities(Set<Facilities> facilities) {
        if (this.facilities != null) {
            this.facilities.forEach(i -> i.setPlaces(null));
        }
        if (facilities != null) {
            facilities.forEach(i -> i.setPlaces(this));
        }
        this.facilities = facilities;
    }

    public Places facilities(Set<Facilities> facilities) {
        this.setFacilities(facilities);
        return this;
    }

    public Places addFacilities(Facilities facilities) {
        this.facilities.add(facilities);
        facilities.setPlaces(this);
        return this;
    }

    public Places removeFacilities(Facilities facilities) {
        this.facilities.remove(facilities);
        facilities.setPlaces(null);
        return this;
    }

    public Set<Activities> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activities> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.setPlaces(null));
        }
        if (activities != null) {
            activities.forEach(i -> i.setPlaces(this));
        }
        this.activities = activities;
    }

    public Places activities(Set<Activities> activities) {
        this.setActivities(activities);
        return this;
    }

    public Places addActivities(Activities activities) {
        this.activities.add(activities);
        activities.setPlaces(this);
        return this;
    }

    public Places removeActivities(Activities activities) {
        this.activities.remove(activities);
        activities.setPlaces(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Places)) {
            return false;
        }
        return id != null && id.equals(((Places) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Places{" +
            "id=" + getId() +
            ", placesName='" + getPlacesName() + "'" +
            ", placesId=" + getPlacesId() +
            ", placesDesc='" + getPlacesDesc() + "'" +
            ", placesCurrentStatus='" + getPlacesCurrentStatus() + "'" +
            ", placeSpecialAttraction='" + getPlaceSpecialAttraction() + "'" +
            ", placeVisitPrice=" + getPlaceVisitPrice() +
            "}";
    }
}
