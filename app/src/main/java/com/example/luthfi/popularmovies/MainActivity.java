package com.example.luthfi.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.luthfi.popularmovies.rest.MainService;
import com.example.luthfi.popularmovies.rest.model.Movie;
import com.example.luthfi.popularmovies.rest.model.Movies;

import java.util.ArrayList;

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

    ArrayList<Movies> listMovies;
    MovieAdapter adapterMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRVmovieList = (RecyclerView) findViewById(R.id.mRVmovieList);
        mPBloding = (ProgressBar) findViewById(R.id.mPBloding);

        listMovies = new ArrayList<>();
        adapterMovies = new MovieAdapter(this, listMovies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRVmovieList.setLayoutManager(gridLayoutManager);
        mRVmovieList.setAdapter(adapterMovies);

        apiKey = "API_KEY";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        }
        return super.onOptionsItemSelected(item);
    }
}
