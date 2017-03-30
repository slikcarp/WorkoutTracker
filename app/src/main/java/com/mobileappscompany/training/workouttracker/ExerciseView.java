package com.mobileappscompany.training.workouttracker;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by User on 2/20/2017.
 */

public class ExerciseView extends TextView {
    private Exercise exercise;

    public ExerciseView(Context context, Exercise exercise) {
        super(context);
        this.exercise = exercise;
        setText(exercise.getName());
        Constants.applyStyle(getContext(), getResources(), this);
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
