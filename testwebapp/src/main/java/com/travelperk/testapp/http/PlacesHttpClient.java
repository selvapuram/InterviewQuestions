package com.travelperk.testapp.http;

import com.travelperk.testapp.model.Coordinate;
import com.travelperk.testapp.model.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PlacesHttpClient {

    public static final String QUERY_KEY_LAT_LONG = "ll";
    @Value("${foursquare.places.url.search}")
    @Getter
    String apiUrl;

    @Value("${foursquare.api.key}")
    @Getter//to be read from vault
    String apiKey;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<Response> get(Coordinate coordinate) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Accept", "application/json");
        headers.add("Authorization", apiKey);

        float latitude = Float.parseFloat(coordinate.latitude());
        float longitude = Float.parseFloat(coordinate.longitude());
        String url = apiUrl + "?" + QUERY_KEY_LAT_LONG + "=" + String.format("%.2f", latitude) + "," + String.format("%.2f", longitude);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Response.class);
    }
}
