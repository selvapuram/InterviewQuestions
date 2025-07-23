package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Location {

   @JsonProperty("address")
   String address;

   @JsonProperty("census_block")
   String censusBlock;

   @JsonProperty("country")
   String country;

   @JsonProperty("dma")
   String dma;

   @JsonProperty("formatted_address")
   String formattedAddress;

   @JsonProperty("locality")
   String locality;

   @JsonProperty("po_box")
   String poBox;

   @JsonProperty("postcode")
   String postcode;

   @JsonProperty("region")
   String region;
}