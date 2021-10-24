package com.sonalika.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Facilities.
 */
@Entity
@Table(name = "facilities")
public class Facilities implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "facilities_id", nullable = false)
    private Long facilitiesId;

    @NotNull
    @Column(name = "facilities_name", nullable = false)
    private String facilitiesName;

    @Column(name = "facilities_desc")
    private String facilitiesDesc;

    @Column(name = "facilities_charges")
    private Float facilitiesCharges;

    /**
     * A relationship
     */
    @ApiModelProperty(value = "A relationship")
    @OneToMany(mappedBy = "facilities")
    @JsonIgnoreProperties(value = { "activities", "location", "facilities", "places" }, allowSetters = true)
    private Set<Tourist> tourists = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "location", "tourists", "facilities", "activities" }, allowSetters = true)
    private Places places;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Facilities id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacilitiesId() {
        return this.facilitiesId;
    }

    public Facilities facilitiesId(Long facilitiesId) {
        this.setFacilitiesId(facilitiesId);
        return this;
    }

    public void setFacilitiesId(Long facilitiesId) {
        this.facilitiesId = facilitiesId;
    }

    public String getFacilitiesName() {
        return this.facilitiesName;
    }

    public Facilities facilitiesName(String facilitiesName) {
        this.setFacilitiesName(facilitiesName);
        return this;
    }

    public void setFacilitiesName(String facilitiesName) {
        this.facilitiesName = facilitiesName;
    }

    public String getFacilitiesDesc() {
        return this.facilitiesDesc;
    }

    public Facilities facilitiesDesc(String facilitiesDesc) {
        this.setFacilitiesDesc(facilitiesDesc);
        return this;
    }

    public void setFacilitiesDesc(String facilitiesDesc) {
        this.facilitiesDesc = facilitiesDesc;
    }

    public Float getFacilitiesCharges() {
        return this.facilitiesCharges;
    }

    public Facilities facilitiesCharges(Float facilitiesCharges) {
        this.setFacilitiesCharges(facilitiesCharges);
        return this;
    }

    public void setFacilitiesCharges(Float facilitiesCharges) {
        this.facilitiesCharges = facilitiesCharges;
    }

    public Set<Tourist> getTourists() {
        return this.tourists;
    }

    public void setTourists(Set<Tourist> tourists) {
        if (this.tourists != null) {
            this.tourists.forEach(i -> i.setFacilities(null));
        }
        if (tourists != null) {
            tourists.forEach(i -> i.setFacilities(this));
        }
        this.tourists = tourists;
    }

    public Facilities tourists(Set<Tourist> tourists) {
        this.setTourists(tourists);
        return this;
    }

    public Facilities addTourist(Tourist tourist) {
        this.tourists.add(tourist);
        tourist.setFacilities(this);
        return this;
    }

    public Facilities removeTourist(Tourist tourist) {
        this.tourists.remove(tourist);
        tourist.setFacilities(null);
        return this;
    }

    public Places getPlaces() {
        return this.places;
    }

    public void setPlaces(Places places) {
        this.places = places;
    }

    public Facilities places(Places places) {
        this.setPlaces(places);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facilities)) {
            return false;
        }
        return id != null && id.equals(((Facilities) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facilities{" +
            "id=" + getId() +
            ", facilitiesId=" + getFacilitiesId() +
            ", facilitiesName='" + getFacilitiesName() + "'" +
            ", facilitiesDesc='" + getFacilitiesDesc() + "'" +
            ", facilitiesCharges=" + getFacilitiesCharges() +
            "}";
    }
}
