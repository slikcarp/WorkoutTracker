package com.mobileappscompany.training.workouttracker;

import android.app.usage.ConfigurationStats;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/17/2017.
 */

public abstract class Constants {

    private Constants(){}

    public static final DatabaseReference dr = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_NAME);
    public static final User user = new User();

    public static final String DATABASE_NAME = "workout_tracker";
    public static final String NODE_USER = "user";
    public static final String NODE_USERS = "users";
    public static final String NODE_WORKOUTS = "workouts";
    public static final String NODE_MUSCLES = "muscles";
    public static final String NODE_EXERCISES = "exercises";

    public static final List<Muscle> muscles = new ArrayList<>();
    public static final Muscle CHEST_MUSCLE = new Muscle(1, "Chest");
    public static final Exercise DEFAULT_EXERCISE = new Exercise(1, "Push Ups", 1);

    //Result codes
    public static final int REQUEST_CODE_LOGIN = 1;
    public static final int REQUEST_CODE_EDIT_WORKOUT_ACTIVITY = 2;
    public static final int REQUEST_CODE_CREATE_WORKOUT_ACTIVITY = 3;
    public static final int REQUEST_CODE_EDIT_EXERCISES_ACTIVITY = 4;

    //Parameters
    public static final String PARAMETER_SELECTED_MUSCLES = "SELECTED_MUSCLES";
    public static final String PARAMETER_SELECTED_MUSCLE = "SELECTED_MUSCLE";
    public static final String PARAMETER_SELECTED_EXERCISES = "SELECTED_EXERCISES";
    public static final String PARAMETER_WORKOUT_DATE = "WORKOUT_DATE";
    public static final String PARAMETER_WORKOUT_NOTIFICATION = "WORKOUT_NOTIFICATION";
    public static final String PARAMETER_WORKOUT_NOTIFICATION_ID = "WORKOUT_NOTIFICATION_ID";

    //Random
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final String WORKOUT_DATE_PICKER = "WORKOUT_DATE_PICKER";

    public static final String BROADCAST_RECEIVER_NAME = "WORKOUT_TRACKER_PUSH_NOTIFICATION_BROADCAST_RECEIVER";
    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final BroadcastReceiver BROADCAST_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getStringExtra(PARAMETER_WORKOUT_NOTIFICATION), Toast.LENGTH_SHORT).show();
        }
    };

    private static DatabaseReference getAccountNode() {
        return dr.child(NODE_USERS).child(user.getAccountKey());
    }

    public static DatabaseReference getUserNode() {
        return getAccountNode().child(NODE_USER);
    }

    public static DatabaseReference getWorkoutNode() {
        return getAccountNode().child(NODE_WORKOUTS);
    }

    public static DatabaseReference getWorkoutNodeByDate(Workout workout) {
        return getWorkoutNodeByDate(workout.getDate());
    }

    public static DatabaseReference getWorkoutNodeByDate(String workoutDate) {
        return getWorkoutNode().child(workoutDate);
    }

    public static DatabaseReference getMusclesNode() {
        return dr.child(NODE_MUSCLES);
    }

    public static DatabaseReference getExercisesNode() {
        return dr.child(NODE_EXERCISES);
    }

    public static void applyStyle(Context context, Resources resources, TextView textView) {
        textView.setTextColor(resources.getColor(R.color.authui_colorPrimary));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        float d = context.getResources().getDisplayMetrics().density;
        llp.setMargins((int)(10 * d), (int)(20 * d), (int)(10 * d), (int)(20 * d)); // llp.setMargins(left, top, right, boeView.setLayoutParams(llp);
        textView.setLayoutParams(llp);
    }

    public static void initBroadcastReceiver(Context applicationContext) {
        INTENT_FILTER.addAction(BROADCAST_RECEIVER_NAME);

        applicationContext.registerReceiver(BROADCAST_RECEIVER, INTENT_FILTER);
    }
}
