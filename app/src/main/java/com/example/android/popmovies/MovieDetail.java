package com.example.android.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class MovieDetail extends AppCompatActivity {
    @BindView(R.id.tv_overview)
    TextView tv_overview;
    @BindView(R.id.tv_original_title)
    TextView tv_title;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.iv_poster_detail)
    ImageView iv_image;
    @BindView(R.id.tv_rate)
    TextView tv_rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent i = getIntent();
        Movie movie = (Movie) i.getParcelableExtra("Movie");

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + movie.getPoster()).into(iv_image);
        tv_date.setText(movie.getDate());
        tv_rate.setText(movie.getRate());
        tv_title.setText(movie.getTitle());
        tv_overview.setText(movie.getOverview());


    }
}
