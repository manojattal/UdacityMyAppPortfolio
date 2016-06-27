package com.example.android.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity implements MovieFragment.OnFragmentInteractionListener {

    private MovieFragment childFragment = new MovieFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, childFragment)
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.moviessortorder);
        item.setActionView(R.layout.switchlayout);
        SwitchCompat toggleMoviesSwitch = (SwitchCompat)item.getActionView().findViewById(R.id.switchForActionBar);
        toggleMoviesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(childFragment != null)
                {
                    Log.v("TODO", "Hello");
                    childFragment.ToggleMovieSortOrder();
                    childFragment.ReloadListData();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private boolean isChecked = false;
}
