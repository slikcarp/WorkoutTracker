package com.mobileappscompany.training.workouttracker;

import java.util.List;

/**
 * Created by User on 2/16/2017.
 */
public class User {
    private String accountKey;

    private List<Workout> workouts;

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}
