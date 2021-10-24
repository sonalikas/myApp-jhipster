package com.sonalika.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonalika.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacilitiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Facilities.class);
        Facilities facilities1 = new Facilities();
        facilities1.setId(1L);
        Facilities facilities2 = new Facilities();
        facilities2.setId(facilities1.getId());
        assertThat(facilities1).isEqualTo(facilities2);
        facilities2.setId(2L);
        assertThat(facilities1).isNotEqualTo(facilities2);
        facilities1.setId(null);
        assertThat(facilities1).isNotEqualTo(facilities2);
    }
}
