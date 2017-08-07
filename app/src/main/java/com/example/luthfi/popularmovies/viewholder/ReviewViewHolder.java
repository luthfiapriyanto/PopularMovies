package com.example.luthfi.popularmovies.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.luthfi.popularmovies.R;
import com.example.luthfi.popularmovies.rest.model.Reviews;

/**
 * Created by luthfi on 8/5/17.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTVcontent, mTVauthor;
    Reviews reviews;
    Context mContext;

    public ReviewViewHolder(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        mTVcontent = (TextView) itemView.findViewById(R.id.mTVcontent);
        mTVauthor = (TextView) itemView.findViewById(R.id.mTVauthor);
    }

    public void bind(Reviews reviews) {
        mTVcontent.setText(reviews.getContent());
        mTVauthor.setText(reviews.getAuthor());
        this.reviews = reviews;
    }

    @Override
    public void onClick(View v) {
    }
}