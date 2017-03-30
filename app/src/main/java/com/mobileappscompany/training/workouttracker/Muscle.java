package com.mobileappscompany.training.workouttracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/16/2017.
 */
public class Muscle implements Parcelable {

    public static final Parcelable.Creator<Muscle> CREATOR
            = new Parcelable.Creator<Muscle>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Muscle createFromParcel(Parcel in) {
            return new Muscle(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Muscle[] newArray(int size) {
            return new Muscle[size];
        }
    };

    private int key;
    private String name;
    private ArrayList<Exercise> exercises;

    public Muscle() {
    }

    public Muscle(Parcel in) {
        this(in.readInt(), in.readString(), in.readArrayList(Exercise.class.getClassLoader()));
    }

    public Muscle(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public Muscle(int key, String name, ArrayList<Exercise> exercises) {
        this(key, name);
        this.exercises = exercises;
    }

    public int getKey() { return key; }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getExercises() { return exercises; }

    public void setExercises(ArrayList<Exercise> exercises) { this.exercises = exercises; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(key);
        dest.writeString(name);
        dest.writeList(exercises);
    }
}
