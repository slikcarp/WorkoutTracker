package com.mobileappscompany.training.workouttracker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/16/2017.
 */
public class WorkoutAdapter extends RecyclerView.Adapter <WorkoutAdapter.WorkoutViewHolder> {

    private final List<Workout> workouts;
    private ViewGroup parent;

    public WorkoutAdapter(List<Workout> workouts) {
        this.workouts = workouts;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        if(workout != null) {
            holder.workoutDate.setText(Workout.parseDate(workout.getDate()));
            holder.muscles.clear();
            holder.exerciseLayout.removeAllViews();
            for (Muscle muscle :
                    workout.getMuscles()) {
                MuscleView muscleView = new MuscleView(parent.getContext(), muscle);
                addMuscleViewOnClickListener(parent.getContext(), muscleView, holder.workoutDate.getText().toString(), position);
                holder.muscles.add(muscleView);
                if(holder.exerciseLayout.indexOfChild(muscleView) < 0) {
                    holder.exerciseLayout.addView(muscleView);
                }
            }
        }
    }

    private void addMuscleViewOnClickListener(final Context context, final MuscleView muscleView, final String workoutDate, final int position) {
        muscleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ExercisesActivity.class);
                i.putParcelableArrayListExtra(Constants.PARAMETER_SELECTED_MUSCLES, workouts.get(position).getMuscles());
                i.putExtra(Constants.PARAMETER_SELECTED_MUSCLE, muscleView.getMuscle());
                i.putExtra(Constants.PARAMETER_WORKOUT_DATE, workoutDate);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {

        private TextView workoutDate;
        private List<MuscleView> muscles = new ArrayList<>();
        private LinearLayout exerciseLayout;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutDate = (TextView) itemView.findViewById(R.id.workoutDate);
            exerciseLayout = ((LinearLayout)itemView.findViewById(R.id.exerciseLayout));
            for (MuscleView muscleView :
                    muscles) {
                exerciseLayout.addView(muscleView);
            }
        }
    }
}
