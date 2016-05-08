package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {

    private static final String SAVED_DATA="popularMovies";
    private MovieImageAdapter m_moiveImageAdapter;
    private PopularMovie[] m_movies;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onStart() {
        super.onStart();
        GetMovies();
    }

    public MovieFragment() {
        // Required empty public constructor
    }


    public static MovieFragment newInstance(String param1, String param2) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey(SAVED_DATA)) {
            ReloadListData();
        }
        else
        {
            ArrayList<PopularMovie> list = savedInstanceState.getParcelableArrayList(SAVED_DATA);
        }
    }

    private void  ReloadListData()
    {
        new FetchMoviesTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        m_moiveImageAdapter = new MovieImageAdapter(getActivity(), new ArrayList<PopularMovie>());
        gridView.setAdapter(m_moiveImageAdapter);
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(SAVED_DATA, m_movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private  void  GetMovies()
    {
        FetchMoviesTask task = new FetchMoviesTask();
        task.execute();
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, PopularMovie[]> {
        private  final  String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(PopularMovie[] movies) {
            if(movies != null){
                m_moiveImageAdapter.clear();
                m_movies = movies;
                for (PopularMovie movie:  movies)
                {
                    m_moiveImageAdapter.add(movie);
                }
            }
        }

        @Override
        protected PopularMovie[] doInBackground(String... params) {

            BufferedReader reader = null;
            String moviesJsonStr = null;
            HttpURLConnection urlConnection = null;

            try {
                String BASE_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=a8b4f7b811047c61ba8aa8724e939d58";
                URL url = new URL(BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //forecastJsonStr = null;
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movies JSON String: " + moviesJsonStr);

            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                moviesJsonStr = null;
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                   return GetMoviesDateFromJson(moviesJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private PopularMovie[] GetMoviesDateFromJson(String JsonData) throws JSONException
        {
            final String OWM_List = "results";
            final String TITLE = "title";
            final String OVERVIEW = "overview";
            final String RELEASEDATE = "release_date";
            final String USERRATING = "vote_average";
            final String IMAGE_BASEPATH = "http://image.tmdb.org/t/p/w185/";
            final String POSTER = "poster_path";
            JSONObject moviesJson = new JSONObject(JsonData);
            JSONArray movieArray = moviesJson.getJSONArray(OWM_List);
            PopularMovie[] movies = new PopularMovie[movieArray.length()];
            //String[] movieTitles = new String[movieArray.length()];
            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                //Log.e(LOG_TAG, movie.getString(TITLE));
                Log.e(LOG_TAG, movie.getString(OVERVIEW));
                Log.e(LOG_TAG, movie.getString(RELEASEDATE));
                Log.e(LOG_TAG, Double.toString(movie.getDouble(USERRATING)));
                Log.e(LOG_TAG, IMAGE_BASEPATH.concat(movie.getString(POSTER)));
                movies[i] = new PopularMovie(movie.getString(TITLE), movie.getString(OVERVIEW), movie.getString(RELEASEDATE), Double.toString(movie.getDouble(USERRATING)),IMAGE_BASEPATH.concat(movie.getString(POSTER)));
            }
            return movies;
        }
    }
}
