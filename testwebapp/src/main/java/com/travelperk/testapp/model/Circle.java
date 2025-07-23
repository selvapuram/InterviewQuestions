package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Circle {

   @JsonProperty("center")
   Center center;

   @JsonProperty("radius")
   int radius;

}