package com.mobileappscompany.training.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ResultCodes;

import java.util.ArrayList;

public class ExercisesActivity extends AppCompatActivity {

    private LinearLayout exercisesLayout;
    private Muscle muscle;
    private String workoutDate;
    private ArrayList<Muscle> muscles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        exercisesLayout = (LinearLayout) findViewById(R.id.exercises_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        muscles = i.getParcelableArrayListExtra(Constants.PARAMETER_SELECTED_MUSCLES);
        muscle = i.getParcelableExtra(Constants.PARAMETER_SELECTED_MUSCLE);
        workoutDate = i.getStringExtra(Constants.PARAMETER_WORKOUT_DATE);

        paintMuscles();
    }

    private void paintMuscles() {
        if(muscle.getExercises() != null) {
            exercisesLayout.removeAllViews();
            for (Exercise exercise :
                    muscle.getExercises()) {
                ExerciseView exerciseView = new ExerciseView(getApplicationContext(), exercise);
                addMuscleClickListener(exerciseView);
                exercisesLayout.addView(exerciseView);
            }
        }
    }

    private void addMuscleClickListener(TextView muscleView) {
        muscleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not implemented jet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == Constants.REQUEST_CODE_EDIT_EXERCISES_ACTIVITY) {
            if (resultCode == ResultCodes.OK) {
                for (Muscle muscle :
                        muscles) {
                    if(muscle.getKey() == this.muscle.getKey()) {
                        muscles.remove(muscle);
                        muscles.add(this.muscle);
                        break;
                    }
                }
                ArrayList<Exercise> exercises = intent.getParcelableArrayListExtra(Constants.PARAMETER_SELECTED_EXERCISES);
                muscle.setExercises(exercises);
                Constants.getWorkoutNodeByDate(Workout.format(workoutDate)).child(Constants.NODE_MUSCLES).setValue(muscles);
                paintMuscles();
                return;
            }
        }

        Toast.makeText(this, getResources().getString(R.string.unknown_sign_in_response), Toast.LENGTH_SHORT).show();
    }

    public void editExercise(View view) {
        Intent i = new Intent(getApplicationContext(), EditingExercisesActivity.class);
        i.putExtra(Constants.PARAMETER_SELECTED_MUSCLE, muscle);
        startActivityForResult(i, Constants.REQUEST_CODE_EDIT_EXERCISES_ACTIVITY);
    }
}
