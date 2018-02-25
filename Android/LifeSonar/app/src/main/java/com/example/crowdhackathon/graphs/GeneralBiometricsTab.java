package com.example.crowdhackathon.graphs;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.example.crowdhackathon.lifesonar.R;


public class GeneralBiometricsTab extends Fragment {
    private TextView temperature, pulse, bloodoxygen, airflow, systolic,
            diastolic, labeltemperature, labelpulse, labeloxygen, labelairflow,
            labelsystolic, labeldiastolic;
    private View rootView;
    Random rand;
    private String[] temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.generalbiometricstab, container,
                false);

        temperature = (TextView) rootView
                .findViewById(R.id.sensorvalueTemperature);
        pulse = (TextView) rootView.findViewById(R.id.sensorvaluePulse);
        bloodoxygen = (TextView) rootView.findViewById(R.id.sensorvalueOxygen);
        airflow = (TextView) rootView.findViewById(R.id.sensorvalueAirflow);
        systolic = (TextView) rootView.findViewById(R.id.sensorvalueSystolic);
        diastolic = (TextView) rootView.findViewById(R.id.sensorvalueDiastolic);
        labeltemperature = (TextView) rootView
                .findViewById(R.id.LabelSensorTemperature);
        labelpulse = (TextView) rootView.findViewById(R.id.LabelSensorPulse);
        labeloxygen = (TextView) rootView.findViewById(R.id.LabelSensorOxygen);
        labelairflow = (TextView) rootView
                .findViewById(R.id.LabelSensorAirflow);
        labelsystolic = (TextView) rootView
                .findViewById(R.id.LabelSensorSystolic);
        labeldiastolic = (TextView) rootView
                .findViewById(R.id.LabelSensorDiastolic);

        rand = new Random();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new ChartTask().execute();
            }
        }, 0, 2000);//put here time 500 milliseconds=1 second

        return rootView;
    }

    private class ChartTask extends AsyncTask<Void, String, Void> {

        // Generates dummy data in a non-ui thread
        @Override
        protected Void doInBackground(Void... params) {
            try {

                temp = new String[6];

                temp[0] = (rand.nextInt((38 - 35) + 1) + 35) + "";
                temp[1] = (rand.nextInt((120 - 70) + 1) + 70) + "";
                temp[2] = (rand.nextInt((99 - 88) + 1) + 88) + "";
                temp[3] = (rand.nextInt((50 - 20) + 1) + 20) + "";
                temp[4] = 0 + "";
                temp[5] = 0 + "";

                publishProgress();
                Thread.sleep(300);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        // Plotting generated data in the graph
        @Override
        protected void onProgressUpdate(String... values) {
            temperature.setText(temp[0]);
            pulse.setText(temp[1]);
            bloodoxygen.setText(temp[2]);
            airflow.setText(temp[3]);
            systolic.setText(temp[4]);
            diastolic.setText(temp[5]);
        }

        @Override
        protected void onCancelled() {

        }
    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass method first
        new ChartTask().cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first
    }

    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        super.onDestroy();
    }
}