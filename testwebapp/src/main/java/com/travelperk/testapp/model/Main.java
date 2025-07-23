package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Main {

   @JsonProperty("latitude")
   double latitude;

   @JsonProperty("longitude")
   double longitude;

}