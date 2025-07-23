package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoBounds {

   @JsonProperty("circle")
   Circle circle;

}