package com.example.android.popularmovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by manoj.attal on 4/22/2016.
 */
public class MovieImageAdapter extends ArrayAdapter<PopularMovie> {

    private  final  String LOG_TAG = MovieImageAdapter.class.getSimpleName();
    private Activity m_context;

    public MovieImageAdapter(Activity context, List<PopularMovie> movieTitles){
            super(context, 0, movieTitles);
            m_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PopularMovie popularMovie =  getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movietitle, parent, false);
        }

        ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.list_item_movie_thumbnail);
        Glide.with(m_context).load(popularMovie.PosterImage)
                .into(thumbnailImage);

        return  convertView;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting bitmap", e);
        }
        return bm;
    }
}
