package com.sonalika.myapp.repository;

import com.sonalika.myapp.domain.Places;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Places entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlacesRepository extends JpaRepository<Places, Long> {}
