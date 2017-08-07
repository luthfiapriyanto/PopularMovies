package com.example.luthfi.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.luthfi.popularmovies.R;
import com.example.luthfi.popularmovies.rest.model.Reviews;
import com.example.luthfi.popularmovies.viewholder.MovieViewHolder;
import com.example.luthfi.popularmovies.viewholder.ReviewViewHolder;

import java.util.ArrayList;

/**
 * Created by luthfi on 8/5/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    ArrayList<Reviews> listReviews;
    Context mContext;

    public ReviewAdapter(Context mContext, ArrayList<Reviews> listReviews) {
        this.mContext = mContext;
        this.listReviews = listReviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(listReviews.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listReviews.size();
    }
}
