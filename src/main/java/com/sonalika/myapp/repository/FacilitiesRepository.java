package com.sonalika.myapp.repository;

import com.sonalika.myapp.domain.Facilities;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Facilities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {}
