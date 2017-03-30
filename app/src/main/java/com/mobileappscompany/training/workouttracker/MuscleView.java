package com.mobileappscompany.training.workouttracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by User on 2/16/2017.
 */

public class MuscleView extends LinearLayout {

    private Muscle muscle;
    private TextView keyView;
    private TextView nameView;

    public MuscleView(Context context, Muscle muscle) {
        super(context);

        keyView = new TextView(context);
        keyView.setText(String.valueOf(muscle.getKey()));
        keyView.setVisibility(GONE);

        nameView = new TextView(context);
        nameView.setText(muscle.getName());
        Constants.applyStyle(context, getResources(), nameView);

        this.addView(keyView);
        this.addView(nameView);

        this.muscle = muscle;
    }

    public Muscle getMuscle() {
        return muscle;
    }

    public void setMuscle(Muscle muscle) {
        this.muscle = muscle;
    }
}
