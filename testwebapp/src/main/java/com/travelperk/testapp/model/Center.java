package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Center {

   @JsonProperty("latitude")
   double latitude;

   @JsonProperty("longitude")
   double longitude;
}