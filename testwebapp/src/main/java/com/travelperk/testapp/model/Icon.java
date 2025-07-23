package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Icon {

   @JsonProperty("prefix")
   String prefix;

   @JsonProperty("suffix")
   String suffix;

}