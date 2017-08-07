package com.example.luthfi.popularmovies.rest.model;

import java.util.ArrayList;

/**
 * Created by luthfi on 8/4/17.
 */

public class Review {
    String id;
    int page;
    ArrayList<Reviews> results;
    int total_pages;
    int total_results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Reviews> getResults() {
        return results;
    }

    public void setResults(ArrayList<Reviews> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
