package com.example.luthfi.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luthfi.popularmovies.adapter.ReviewAdapter;
import com.example.luthfi.popularmovies.rest.MainService;
import com.example.luthfi.popularmovies.rest.model.Review;
import com.example.luthfi.popularmovies.rest.model.Reviews;
import com.example.luthfi.popularmovies.rest.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;

    ImageView mIVposter, mIVplay, mIVstar;
    TextView mTVtitle, mTVrating, mTVdate, mTVdesc;
    RecyclerView mRVreview;
    String title, date, rating, backdrop, baseImageUrl, desc, apiKey, Id, trailer, poster;
    Boolean favorited;

    ArrayList<Reviews> listReviews;
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        dbHelper = new DataHelper(this);

        favorited = false;

        listReviews = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(this, listReviews);

        baseImageUrl = getResources().getString(R.string.base_image_url);

        mIVposter = (ImageView) findViewById(R.id.mIVposter);
        mTVtitle = (TextView) findViewById(R.id.mTVtitle);
        mTVrating = (TextView) findViewById(R.id.mTVrating);
        mTVdate = (TextView) findViewById(R.id.mTVdate);
        mTVdesc = (TextView) findViewById(R.id.mTVdesc);
        mRVreview = (RecyclerView) findViewById(R.id.mRVreview);
        mIVplay = (ImageView) findViewById(R.id.mIVplay);
        mIVstar = (ImageView) findViewById(R.id.mIVstar);

        mRVreview.setNestedScrollingEnabled(false);
        mRVreview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVreview.setAdapter(reviewAdapter);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        rating = intent.getStringExtra("rating");
        backdrop = intent.getStringExtra("backdrop");
        desc = intent.getStringExtra("desc");
        Id = intent.getStringExtra("Id");
        poster = intent.getStringExtra("poster");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTVtitle.setText(title);
        mTVrating.setText(rating);
        mTVdate.setText(getString(R.string.release_date, date));
        mTVdesc.setText(desc);
        mIVstar.setBackgroundResource(R.drawable.star_inactive);

        Picasso.with(this).load(baseImageUrl+"original"+backdrop)
                .placeholder(R.drawable.placeholder)
                .into(mIVposter);

        apiKey = getString(R.string.apikey);

        search(Id);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            callNetworking(Id);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Access Available!", Toast.LENGTH_LONG).show();
        }

        mIVplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trailer!=null){
                    String url = "https://www.youtube.com/watch?v="+trailer;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        mIVstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(favorited){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("delete from favorite where Id = '"+Id+"'");
                    mIVstar.setBackgroundResource(R.drawable.star_inactive);
                    favorited = false;

                }else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("insert into favorite(Id, title, date, rating, backdrop, poster, desc) values('" +
                            formatter(Id) + "','" +
                            formatter(title) + "','" +
                            formatter(date) + "','" +
                            formatter(rating) + "','" +
                            formatter(backdrop) + "','" +
                            formatter(poster) + "','" +
                            formatter(desc) + "')");
                    mIVstar.setBackgroundResource(R.drawable.star_active);
                    favorited = true;
                }

            }
        });
   }

   public String formatter(String value){
       value = value.replace("'","''");
       return value;
   }

   public void search(String query){
       SQLiteDatabase db = dbHelper.getReadableDatabase();
       cursor = db.rawQuery("SELECT * FROM favorite",null);
       cursor.moveToFirst();
       for (int i=0; i < cursor.getCount(); i++){
           cursor.moveToPosition(i);
           if(cursor.getString(0).equals(query)){
               mIVstar.setBackgroundResource(R.drawable.star_active);
               favorited = true;
               break;
           }
       }
   }

    public void callNetworking(String Id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        MainService mainService = retrofit.create(MainService.class);

        Call<Review> moviesCall = mainService.getReview(Id, apiKey);

        moviesCall.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.code() == 200) {
                    listReviews.addAll(response.body().getResults());
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Cannot get review!", Toast.LENGTH_LONG).show();
            }
        });

        final Call<Trailer> trailerCall = mainService.getTrailer(Id, apiKey);
        trailerCall.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                if (response.code() == 200) {
                    trailer = response.body().getResults().get(0).getKey();
                }
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Cannot get trailer!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
