package com.mobileappscompany.training.workouttracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/16/2017.
 */
public class Workout {

    private String date;
    private ArrayList<Muscle> muscles;

    public Workout() {
    }

    public Workout(String date, ArrayList<Muscle> muscles) {
        this.date = date;
        this.muscles = muscles;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Muscle> getMuscles() {
        return muscles;
    }

    public void setMuscles(ArrayList<Muscle> muscles) {
        this.muscles = muscles;
    }

    /**
     * ddMMyyyy -> dd/MM/yyyy
     * @param date
     * @return
     */
    public static String parseDate(String date) {
        return String.format("%1$2s/%2$2s/%3$4s",date.substring(0,2), date.substring(2, 4), date.substring(4, 8));
    }

    /**
     * dd/MM/yyyy -> ddMMyyyy
     * @param date
     * @return
     */
    public static String format(String date) {
        return date.replaceAll("/", "");
    }
}
