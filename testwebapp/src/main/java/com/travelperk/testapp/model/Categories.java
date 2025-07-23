package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Categories {

   @JsonProperty("id")
   int id;

   @JsonProperty("name")
   String name;

   @JsonProperty("short_name")
   String shortName;

   @JsonProperty("plural_name")
   String pluralName;

   @JsonProperty("icon")
   Icon icon;
}