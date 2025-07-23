package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Context {

   @JsonProperty("geo_bounds")
   GeoBounds geoBounds;

}