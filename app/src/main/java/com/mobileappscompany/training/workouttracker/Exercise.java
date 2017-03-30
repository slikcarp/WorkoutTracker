package com.mobileappscompany.training.workouttracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 2/19/2017.
 */

public class Exercise implements Parcelable {

    public static final Parcelable.Creator<Exercise> CREATOR
            = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    private int key;
    private String name;
    private int muscleKey;

    public Exercise() {
    }

    public Exercise(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public Exercise(int key, String name, int muscleKey) {
        this(key, name);
        this.muscleKey = muscleKey;
    }

    public Exercise(Parcel source) {
        this(source.readInt(), source.readString());
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMuscleKey() {
        return muscleKey;
    }

    public void setMuscleKey(int muscleKey) {
        this.muscleKey = muscleKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(key);
        dest.writeString(name);
    }
}
