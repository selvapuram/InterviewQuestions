package com.travelperk.testapp.service;

import com.travelperk.testapp.http.PlacesHttpClient;
import com.travelperk.testapp.model.Coordinate;
import com.travelperk.testapp.model.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class SearchPlaces {

    PlacesHttpClient placesHttpClient;

    public ResponseEntity<Response> search(Coordinate nearby) {
        if (nearby != null &&
                StringUtils.hasLength(nearby.latitude()) &&
                StringUtils.hasLength(nearby.longitude())) {
            return placesHttpClient.get(nearby);
        } else {
            // log some error some message
            return ResponseEntity.badRequest().build();
        }
    }
}
