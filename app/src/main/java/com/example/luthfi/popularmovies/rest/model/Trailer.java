package com.example.luthfi.popularmovies.rest.model;

import java.util.ArrayList;

/**
 * Created by luthfi on 8/5/17.
 */

public class Trailer {
    String id;
    ArrayList<Trailers> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Trailers> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailers> results) {
        this.results = results;
    }
}
