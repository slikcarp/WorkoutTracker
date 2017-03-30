package com.mobileappscompany.training.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final List<Workout> workouts = new ArrayList<>();
    private WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            signIn();
        } else {
            loadApp();
        }
    }

    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                Constants.REQUEST_CODE_LOGIN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == Constants.REQUEST_CODE_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(intent);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
//                finish();
                loadApp();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToastMessage(this.getResources().getString(R.string.login_canceled));
                    onLogout(null);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToastMessage(this.getResources().getString(R.string.no_internet_connection));
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showToastMessage(this.getResources().getString(R.string.unknown_error));
                    return;
                }
            }
        } else if(requestCode == Constants.REQUEST_CODE_CREATE_WORKOUT_ACTIVITY ||
                requestCode == Constants.REQUEST_CODE_EDIT_WORKOUT_ACTIVITY) {
            if (resultCode == ResultCodes.OK) {
                ArrayList<Muscle> muscles = intent.getParcelableArrayListExtra(Constants.PARAMETER_SELECTED_MUSCLES);
                String date = intent.getStringExtra(Constants.PARAMETER_WORKOUT_DATE);
                Workout workout = new Workout(Workout.format(date), muscles);
                Constants.getWorkoutNodeByDate(workout).setValue(workout);
                return;
            } else {
                return;
            }
        }

        Toast.makeText(this, getResources().getString(R.string.unknown_sign_in_response), Toast.LENGTH_SHORT).show();
    }

    private void loadApp() {
        String userUID = getUserUID();

        if(userUID == null) {
            signIn();
            return;
        }

        Constants.user.setAccountKey(userUID);

        Constants.initBroadcastReceiver(getApplicationContext());

        addValueEventListener();

        initRecyclerView();

        Constants.dr.child(Constants.NODE_USERS).child(Constants.user.getAccountKey()).child(Constants.NODE_USER).setValue(Constants.user);
    }

    public void onLogout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

    public String getUserUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            return user.getUid();
        }
        return null;
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addValueEventListener() {
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workouts.clear();

                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    Workout workout = ds.getValue(Workout.class);
                    if(workout != null) {
                        workouts.add(workout);
                    }
                }

                workoutAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Constants.getWorkoutNode().addValueEventListener(vel);
    }

    private void initRecyclerView() {
        RecyclerView recyclerWorkout = (RecyclerView) findViewById(R.id.recyclerWorkout);
        recyclerWorkout.hasFixedSize();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerWorkout.setLayoutManager(layoutManager);

        workoutAdapter = new WorkoutAdapter(workouts);
        recyclerWorkout.setAdapter(workoutAdapter);
    }

    public void addWorkout(View view) {
        Intent intent = new Intent(getApplicationContext(), EditingWorkoutActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_CREATE_WORKOUT_ACTIVITY);
    }

    public void editWorkout(View view) {
        Intent intent = new Intent(getApplicationContext(), EditingWorkoutActivity.class);

        LinearLayout linearLayout = (LinearLayout) view.getParent();
        linearLayout = (LinearLayout) linearLayout.findViewById(R.id.exerciseLayout);

        int totalOfMuscles = linearLayout.getChildCount();
        ArrayList<Muscle> muscles = new ArrayList<>(totalOfMuscles);
        for (int index = 0; index < totalOfMuscles; index++) {
            muscles.add(((MuscleView)linearLayout.getChildAt(index)).getMuscle());
        }

        intent.putExtra(Constants.PARAMETER_WORKOUT_DATE, ((TextView)view).getText().toString());
        intent.putExtra(Constants.PARAMETER_SELECTED_MUSCLES, muscles);
        startActivityForResult(intent, Constants.REQUEST_CODE_EDIT_WORKOUT_ACTIVITY);
    }
}
