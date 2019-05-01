package com.example.android.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.popmovies.Utils.JsonParsing;
import com.example.android.popmovies.Utils.Networking;
import com.example.android.popmovies.model.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickHandler {
    GridLayoutManager gridLayoutManager;
    static MovieAdapter adapter;
    static RecyclerView recyclerView;
    static Movie[] movies;
    static String type;
    SharedPreferences sharedPreferences;
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    static TextView tv_error ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_error = (TextView)findViewById(R.id.error);
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new MovieAdapter(this);

        recyclerView.setAdapter(adapter);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        type = sharedPreferences.getString("popular", POPULAR);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("DataSaved")) {
                movies = (Movie[]) savedInstanceState.getParcelableArray("DataSaved");
            }
        }


        loadMovieData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArray("DataSaved", movies);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = (Movie[]) savedInstanceState.getParcelableArray("DataSaved");
    }

    public String getType() {
        return this.type;
    }

    public static void setType(String type) {
        MainActivity.type = type;
    }

    public static void loadMovieData() {

        tv_error.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        process p = new process();
        p.execute(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();
        switch (menu_id) {
            case R.id.m_mostPopular:
                Toast.makeText(this, "popular", Toast.LENGTH_SHORT).show();
                if (type.equals(POPULAR) != true) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("popular", POPULAR);
                    editor.commit();
                    type = POPULAR;
                    loadMovieData();
                }
                type = POPULAR;
                loadMovieData();
                return true ;

            case R.id.m_topRated:
                Toast.makeText(this, "top Rated", Toast.LENGTH_SHORT).show();
                if (type.equals(TOP_RATED) != true) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("top Rated", TOP_RATED);
                    editor.commit();
                    type = TOP_RATED;
                    loadMovieData();
                }
                type = TOP_RATED;
                loadMovieData();
                return true ;

        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void OnClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetail.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);

    }

    public static class process extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String mType = params[0];

            URL url = Networking.buildUri(mType);
            try {
                String response = Networking.ReadData(url);
                Movie[] movies = JsonParsing.DataParsed(response);
                return movies;


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            if (movieData !=null){
                movies = movieData;
                adapter.setMovieData(movies);
            }else {
                recyclerView.setVisibility(View.INVISIBLE);
                tv_error.setVisibility(View.VISIBLE);
            }

        }
    }

}
