package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Geocodes {

   @JsonProperty("main")
   Main main;

   @JsonProperty("roof")
   Roof roof;


    public void setMain(Main main) {
        this.main = main;
    }
    public Main getMain() {
        return main;
    }
    
    public void setRoof(Roof roof) {
        this.roof = roof;
    }
    public Roof getRoof() {
        return roof;
    }
    
}