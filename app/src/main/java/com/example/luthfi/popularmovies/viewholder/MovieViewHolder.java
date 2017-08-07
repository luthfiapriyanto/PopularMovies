package com.example.luthfi.popularmovies.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.luthfi.popularmovies.MovieDetailActivity;
import com.example.luthfi.popularmovies.R;
import com.example.luthfi.popularmovies.rest.model.Movies;
import com.squareup.picasso.Picasso;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mIVposter;
    Movies movies;
    Context mContext;
    String baseImageUrl, title, date, rating, backdrop, desc, Id, poster;

    public MovieViewHolder(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        mIVposter = (ImageView) itemView.findViewById(R.id.mIVposter);

        baseImageUrl = mContext.getString(R.string.base_image_url);
        itemView.setOnClickListener(this);
    }

    public void bind(Movies movies) {
        Picasso.with(mContext).load(baseImageUrl+"w185"+movies.getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(mIVposter);

        title = movies.getTitle();
        date = movies.getRelease_date();
        rating = movies.getVote_average();
        backdrop = movies.getBackdrop_path();
        desc = movies.getOverview();
        Id = movies.getId();
        poster = movies.getPoster_path();

        this.movies = movies;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("date", date);
        intent.putExtra("rating", rating);
        intent.putExtra("backdrop", backdrop);
        intent.putExtra("desc", desc);
        intent.putExtra("Id", Id);
        intent.putExtra("poster", poster);

        v.getContext().startActivity(intent);
    }
}