package com.example.luthfi.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luthfi.popularmovies.adapter.MovieAdapter;
import com.example.luthfi.popularmovies.rest.MainService;
import com.example.luthfi.popularmovies.rest.model.Movie;
import com.example.luthfi.popularmovies.rest.model.Movies;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRVmovieList;
    ProgressBar mPBloding;
    String type;
    String apiKey;

    protected Cursor cursor;
    DataHelper dbHelper;

    ArrayList<Movies> listMovies;
    MovieAdapter adapterMovies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        dbHelper = new DataHelper(this);

        mRVmovieList = (RecyclerView) findViewById(R.id.mRVmovieList);
        mPBloding = (ProgressBar) findViewById(R.id.mPBloding);

        listMovies = new ArrayList<>();
        adapterMovies = new MovieAdapter(this, listMovies);
        Log.e("TAG", "onCreate: " );

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRVmovieList.setLayoutManager(gridLayoutManager);
        mRVmovieList.setAdapter(adapterMovies);

        apiKey = getString(R.string.apikey);
        type = "popular";

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            callNetworking(type);
        } else {
            mPBloding.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No Internet Access Available!", Toast.LENGTH_LONG).show();
        }

    }

    public void callNetworking(String type) {
        mRVmovieList.setVisibility(View.GONE);
        mPBloding.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        MainService mainService = retrofit.create(MainService.class);

        Call<Movie> moviesCall = mainService.getMovies(type, apiKey);
        moviesCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if (response.code() == 200) {
                    mRVmovieList.setVisibility(View.VISIBLE);
                    mPBloding.setVisibility(View.GONE);
                    listMovies.addAll(response.body().getResults());
                    adapterMovies.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                mPBloding.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void database() {
        mRVmovieList.setVisibility(View.GONE);
        mPBloding.setVisibility(View.VISIBLE);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM favorite",null);
        Movies movies;
        cursor.moveToFirst();
        for (int i=0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            movies = new Movies(formatter(cursor.getString(0)), formatter(cursor.getString(3)), formatter(cursor.getString(1)), 0.0,
                    formatter(cursor.getString(5)), "a", formatter(cursor.getString(4)), formatter(cursor.getString(6)), formatter(cursor.getString(2)));
            listMovies.add(movies);
        }
        adapterMovies.notifyDataSetChanged();
        mRVmovieList.setVisibility(View.VISIBLE);
        mPBloding.setVisibility(View.GONE);
    }

    public String formatter(String value){
        value = value.replace("''","'");
        return value;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_top_rated) {
            listMovies.clear();
            type = "top_rated";
            callNetworking(type);
            return true;

        } else if (itemThatWasClickedId == R.id.action_popular) {
            listMovies.clear();
            type = "popular";
            callNetworking(type);
            return true;

        } else if (itemThatWasClickedId == R.id.action_favorite) {
            listMovies.clear();
            database();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
