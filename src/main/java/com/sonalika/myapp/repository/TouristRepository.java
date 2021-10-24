package com.sonalika.myapp.repository;

import com.sonalika.myapp.domain.Tourist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tourist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {}
