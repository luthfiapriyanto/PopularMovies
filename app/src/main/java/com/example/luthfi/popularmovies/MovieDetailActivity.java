package com.example.luthfi.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    ImageView mIVposter;
    TextView mTVtitle, mTVrating, mTVdate, mTVdesc;
    String title, date, rating, backdrop, baseImageUrl, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        baseImageUrl = getResources().getString(R.string.base_image_url);

        mIVposter = (ImageView) findViewById(R.id.mIVposter);
        mTVtitle = (TextView) findViewById(R.id.mTVtitle);
        mTVrating = (TextView) findViewById(R.id.mTVrating);
        mTVdate = (TextView) findViewById(R.id.mTVdate);
        mTVdesc = (TextView) findViewById(R.id.mTVdesc);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        rating = intent.getStringExtra("rating");
        backdrop = intent.getStringExtra("backdrop");
        desc = intent.getStringExtra("desc");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTVtitle.setText(title);
        mTVrating.setText(rating);
        mTVdate.setText(getString(R.string.release_date, date));
        mTVdesc.setText(desc);

        Picasso.with(this).load(backdrop)
                .placeholder(R.drawable.placeholder)
                .into(mIVposter);
    }
}
