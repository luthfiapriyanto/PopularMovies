package com.example.luthfi.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.luthfi.popularmovies.rest.model.Movies;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    ArrayList<Movies> listMovies;
    Context mContext;

    public MovieAdapter(Context mContext, ArrayList<Movies> listMovies) {
        this.mContext = mContext;
        this.listMovies = listMovies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(listMovies.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }
}
