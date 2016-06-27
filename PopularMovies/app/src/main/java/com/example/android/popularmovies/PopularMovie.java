package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manoj.attal on 4/19/2016.
 */
public class PopularMovie implements Parcelable {
    public  String Title;
    public String Overview;
    public  String ReleaseDate;
    public  String UserRating;
    public  String PosterImage;

    public  PopularMovie(String title, String overview, String releaseDate, String userRating, String posterImage)
    {
        this.Title = title;
        this.Overview = overview;
        this.ReleaseDate = releaseDate;
        this.UserRating = userRating;
        this.PosterImage = posterImage;
    }

    private  PopularMovie(Parcel parcel)
    {
        Title = parcel.readString();
        Overview = parcel.readString();
        ReleaseDate = parcel.readString();
        UserRating = parcel.readString();
        PosterImage = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Title);
        dest.writeString(this.Overview);
        dest.writeString(this.ReleaseDate);
        dest.writeString(this.UserRating);
        dest.writeString(this.PosterImage);
    }

    public static final Parcelable.Creator<PopularMovie> CREATOR = new Parcelable.Creator<PopularMovie>()
    {
        @Override
        public PopularMovie createFromParcel(Parcel source) {
            return new PopularMovie(source);
        }

        @Override
        public PopularMovie[] newArray(int size) {
            return new PopularMovie[size];
        }
    };
}
