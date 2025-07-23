package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Roof {

   @JsonProperty("latitude")
   double latitude;

   @JsonProperty("longitude")
   double longitude;


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
        return latitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLongitude() {
        return longitude;
    }
    
}