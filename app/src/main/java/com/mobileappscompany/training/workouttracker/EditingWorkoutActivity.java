package com.mobileappscompany.training.workouttracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

public class EditingWorkoutActivity extends AppCompatActivity implements TimePickerFragment.TimePickerFragmentResponse {

    private LinearLayout musclesLeftLayout;
    private LinearLayout musclesRightLayout;
    private EditText workoutDateText;
    private FloatingActionButton workoutDatePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_workout);

        musclesLeftLayout = (LinearLayout) findViewById(R.id.musclesLeftLayout);
        musclesRightLayout = (LinearLayout) findViewById(R.id.musclesRightLayout);
        workoutDateText = (EditText) findViewById(R.id.workoutDateText);
        workoutDatePickerButton = (FloatingActionButton) findViewById(R.id.workoutDatePickerButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String date = intent.getStringExtra(Constants.PARAMETER_WORKOUT_DATE);
        if(date != null) {
            workoutDateText.setText(date);
            workoutDateText.setEnabled(false);
            workoutDatePickerButton.setEnabled(false);
        }

        addValueEventListener();
        Constants.getMusclesNode()
                .child(String.valueOf(Constants.CHEST_MUSCLE.getKey()))
                .setValue(Constants.CHEST_MUSCLE);
    }

    private void addValueEventListener() {
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Constants.muscles.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        Muscle muscle = ds.getValue(Muscle.class);
                        if(muscle.getKey() != 0) {
                            Constants.muscles.add(muscle);
                        }
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }

                paintMuscles();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Constants.dr.child(Constants.NODE_MUSCLES).addValueEventListener(vel);
    }

    private void paintMuscles() {
        musclesLeftLayout.removeAllViews();
        musclesRightLayout.removeAllViews();
        Intent intent = getIntent();
        ArrayList<Muscle> muscles = intent.getParcelableArrayListExtra(Constants.PARAMETER_SELECTED_MUSCLES);

        for (Muscle muscle :
                Constants.muscles) {

            MuscleView muscleView = new MuscleView(getBaseContext(), muscle);

            muscleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveMuscle((MuscleView) v);
                }
            });

            if(muscleView.getMuscle().getExercises() != null) {
                muscleView.getMuscle().getExercises().clear();
            }

            if(muscles != null) {
                boolean isTheMuscleSelected = false;
                for (Muscle previousMuscle :
                        muscles) {
                    if(previousMuscle.getKey() == muscle.getKey()) {
                        muscleView.setMuscle(previousMuscle);
                        isTheMuscleSelected = true;
                        break;
                    }
                }
                if(isTheMuscleSelected) {
                    musclesRightLayout.addView(muscleView);
                } else {
                    musclesLeftLayout.addView(muscleView);
                }
            } else {
                musclesLeftLayout.addView(muscleView);
            }
        }
    }

    private void moveMuscle(MuscleView muscleView) {
        if(musclesLeftLayout.indexOfChild(muscleView) > -1) {
            musclesLeftLayout.removeView(muscleView);
            musclesRightLayout.addView(muscleView);
        } else {
            musclesRightLayout.removeView(muscleView);
            musclesLeftLayout.addView(muscleView);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isDateValid()) {
            showDialog("Confirmation!","The date is incorrect.\nThe changes will be lost.\nDo you really want to continue?");
            return;
        }

        if(musclesRightLayout.getChildCount() < 1) {
            showDialog("Confirmation!","At least you have to add a muscle.\nThe changes will be lost.\nDo you really want to exit?");
            return;
        }

        int totalOfMuscles = musclesRightLayout.getChildCount();
        ArrayList<Muscle> selectedMuscles = new ArrayList<>(totalOfMuscles);
        for (int index = 0; index < totalOfMuscles; index++) {
            selectedMuscles.add(((MuscleView)musclesRightLayout.getChildAt(index)).getMuscle());
        }

        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(Constants.PARAMETER_SELECTED_MUSCLES, selectedMuscles);
        intent.putExtra(Constants.PARAMETER_WORKOUT_DATE, workoutDateText.getText().toString());

        setResult(ResultCodes.OK, intent);
        super.onBackPressed();
    }

    private boolean isDateValid() {
        String workoutDateString = workoutDateText.getText().toString();

        boolean isValidDate = true;
        try {
            Constants.sdf.parse(workoutDateString);
        } catch (ParseException e) {
            isValidDate = false;
        }

        return isValidDate;
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        EditingWorkoutActivity.super.onBackPressed();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    public void showCalendar(View view) {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), Constants.WORKOUT_DATE_PICKER);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if(requestCode == Constants.REQUEST_CODE_WORKOUT_DATE_PICKER) {
//            if(resultCode == ResultCodes.OK) {
//                workoutDateText.setText(intent.getStringExtra(Constants.PARAMETER_WORKOUT_DATE));
//            }
//        }
//    }

    @Override
    public void setDate(String date) {
        workoutDateText.setText(date);
    }
}
