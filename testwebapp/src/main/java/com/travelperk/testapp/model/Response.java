package com.travelperk.testapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

   
public class Response {

   @JsonProperty("results")
   List<Results> results;

    @JsonProperty("context")
   Context context;


    public void setResults(List<Results> results) {
        this.results = results;
    }
    public List<Results> getResults() {
        return results;
    }
    
    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(results);
    }
    
}