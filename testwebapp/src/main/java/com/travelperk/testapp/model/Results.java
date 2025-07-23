package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Results {

   @JsonProperty("fsq_id")
   String fsqId;

   @JsonProperty("categories")
   List<Categories> categories;

   @JsonProperty("chains")
   List<String> chains;

   @JsonProperty("closed_bucket")
   String closedBucket;

   @JsonProperty("distance")
   int distance;

   @JsonProperty("geocodes")
   Geocodes geocodes;

   @JsonProperty("link")
   String link;

   @JsonProperty("location")
   Location location;

   @JsonProperty("name")
   String name;

   @JsonProperty("related_places")
   RelatedPlaces relatedPlaces;

   @JsonProperty("timezone")
   String timezone;

}