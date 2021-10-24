package com.sonalika.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sonalika.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TouristTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tourist.class);
        Tourist tourist1 = new Tourist();
        tourist1.setId(1L);
        Tourist tourist2 = new Tourist();
        tourist2.setId(tourist1.getId());
        assertThat(tourist1).isEqualTo(tourist2);
        tourist2.setId(2L);
        assertThat(tourist1).isNotEqualTo(tourist2);
        tourist1.setId(null);
        assertThat(tourist1).isNotEqualTo(tourist2);
    }
}
