package com.mobileappscompany.training.workouttracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditingExercisesActivity extends AppCompatActivity {

    private LinearLayout exercisesLeftLayout;
    private LinearLayout exercisesRightLayout;
    private Muscle muscle;
    private List<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_exercises);

        exercisesLeftLayout = (LinearLayout) findViewById(R.id.exercises_left_layout);
        exercisesRightLayout = (LinearLayout) findViewById(R.id.exercises_right_layout);
        exercises = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        muscle = i.getParcelableExtra(Constants.PARAMETER_SELECTED_MUSCLE);

        addValueEventListener();
        Constants.getMusclesNode()
                .child(String.valueOf(Constants.DEFAULT_EXERCISE.getKey()))
                .setValue(Constants.DEFAULT_EXERCISE);
    }

    private void addValueEventListener() {
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercises.clear();

                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    Exercise exercise = ds.getValue(Exercise.class);
                    if(exercise.getKey() != 0 && exercise.getMuscleKey() == muscle.getKey()) {
                        exercises.add(exercise);
                    }
                }

                paintExercises();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Constants.dr.child(Constants.NODE_EXERCISES).addValueEventListener(vel);
    }

    private void paintExercises() {
        exercisesLeftLayout.removeAllViews();
        exercisesRightLayout.removeAllViews();

        for (Exercise exercise :
                exercises) {
            ExerciseView exerciseView = new ExerciseView(getApplicationContext(), exercise);

            exerciseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveExercise((ExerciseView) v);
                }
            });

            if(muscle.getExercises() != null) {
                boolean isTheExerciseSelected = false;
                for (Exercise previousExercise :
                        muscle.getExercises()) {
                    if(previousExercise.getKey() == exercise.getKey()) {
                        exerciseView.setExercise(previousExercise);
                        isTheExerciseSelected = true;
                        break;
                    }
                }
                if(isTheExerciseSelected) {
                    exercisesRightLayout.addView(exerciseView);
                } else {
                    exercisesLeftLayout.addView(exerciseView);
                }
            } else {
                exercisesLeftLayout.addView(exerciseView);
            }
        }
    }

    private void moveExercise(ExerciseView exerciseView) {
        if(exercisesLeftLayout.indexOfChild(exerciseView) > -1) {
            exercisesLeftLayout.removeView(exerciseView);
            exercisesRightLayout.addView(exerciseView);
        } else {
            exercisesRightLayout.removeView(exerciseView);
            exercisesLeftLayout.addView(exerciseView);
        }
    }

    @Override
    public void onBackPressed() {
        if(exercisesRightLayout.getChildCount() < 1) {
            showDialog("Confirmation!","At least you have to add an exercise.\nThe changes will be lost.\nDo you really want to continue?");
            return;
        }

        int totalOfExercises = exercisesRightLayout.getChildCount();
        ArrayList<Exercise> selectedExercises = new ArrayList<>(totalOfExercises);
        for (int index = 0; index < totalOfExercises; index++) {
            selectedExercises.add(((ExerciseView)exercisesRightLayout.getChildAt(index)).getExercise());
        }

        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(Constants.PARAMETER_SELECTED_EXERCISES, selectedExercises);

        setResult(ResultCodes.OK, intent);
        super.onBackPressed();
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        EditingExercisesActivity.super.onBackPressed();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
                .create().show();
    }
}
