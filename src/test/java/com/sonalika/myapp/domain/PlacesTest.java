package com.sonalika.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonalika.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlacesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Places.class);
        Places places1 = new Places();
        places1.setId(1L);
        Places places2 = new Places();
        places2.setId(places1.getId());
        assertThat(places1).isEqualTo(places2);
        places2.setId(2L);
        assertThat(places1).isNotEqualTo(places2);
        places1.setId(null);
        assertThat(places1).isNotEqualTo(places2);
    }
}
