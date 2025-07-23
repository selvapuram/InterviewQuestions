package com.travelperk.testapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelperk.testapp.model.Coordinate;
import com.travelperk.testapp.model.Response;
import com.travelperk.testapp.service.SearchPlaces;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchPlacesITest {

    private static final Logger LOG = LoggerFactory.getLogger(SearchPlacesITest.class);

	@Autowired
	private SearchPlaces searchPlaces;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldReturnPlacesGivenLatLong() throws Exception {
        String lat = "41.399150";
        String lang = "2.193260";
        Coordinate nearBy = new Coordinate(lat, lang);
        ResponseEntity<Response> responseEntity = searchPlaces.search(nearBy);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        LOG.info(mapper.writeValueAsString(responseEntity.getBody()));
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().isEmpty()).isEqualTo(false);
    }
}