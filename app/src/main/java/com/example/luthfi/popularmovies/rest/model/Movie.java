package com.example.luthfi.popularmovies.rest.model;

import java.util.ArrayList;

/**
 * Created by luthfi on 6/27/17.
 */

public class Movie {
    int page;
    int total_results;
    int total_pages;
    ArrayList<Movies> results;

    public ArrayList<Movies> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movies> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
