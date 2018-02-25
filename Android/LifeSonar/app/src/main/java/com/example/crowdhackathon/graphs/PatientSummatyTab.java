package com.example.crowdhackathon.graphs;


import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.crowdhackathon.lifesonar.R;

public class PatientSummatyTab extends Fragment {

    private View rootView;
    private static double countAirflow = 0, countMeanAirflow = 0;
    private static double countECG = 0, countMeanECG = 0;
    private static double countTemperature = 0, countMeanTemperature = 0;
    private static double countPulse = 0, countMeanPulse = 0;
    private static double countOxygen = 0, countMeanOxygen = 0;
    private static double countConductivity = 0, countMeanConductivity = 0;
    private static int counter = 0;
    private TextView temperature, airflow, ecg, pulse, oxygen, conductivity, progresstext;
    private Button speak;
    private String[] temp, summary;
    private TextToSpeech myTTS;
    private static boolean flag = false;
    private ProgressBar progressBar;
    private static Context context;
    private TextView title, labeltemperature, labeloxygen, labelairflow, labelconductivity,
            labelpulse, labelecg;
    private static Handler handler = new Handler();
    private Random rand;
    private MeasurementsCapture measurementsCapture;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_patientsummary, container,
                false);

        temperature = (TextView) rootView
                .findViewById(R.id.diagnosisValueTemperature);
        airflow = (TextView) rootView.findViewById(R.id.diagnosisValueAirflow);
        ecg = (TextView) rootView.findViewById(R.id.diagnosisValueECG);
        pulse = (TextView) rootView.findViewById(R.id.diagnosisValuePulse);
        oxygen = (TextView) rootView
                .findViewById(R.id.diagnosisValueBloodOxygen);
        conductivity = (TextView) rootView
                .findViewById(R.id.diagnosisValueConductivity);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progresstext = (TextView) rootView.findViewById(R.id.textprogress);

        title = (TextView) rootView.findViewById(R.id.titleDiagnosis);
        labelairflow = (TextView) rootView.findViewById(R.id.diagnosisAirflow);
        labeltemperature = (TextView) rootView
                .findViewById(R.id.diagnosisTemperature);
        labeloxygen = (TextView) rootView
                .findViewById(R.id.diagnosisBloodOxygen);
        labelconductivity = (TextView) rootView
                .findViewById(R.id.diagnosisConductivity);
        labelpulse = (TextView) rootView.findViewById(R.id.diagnosisPulse);
        labelecg = (TextView) rootView.findViewById(R.id.diagnosisECG);

        final Vibrator vibe = (Vibrator) getActivity().getSystemService(
                Context.VIBRATOR_SERVICE);

        PatientSummatyTab.context = getActivity();
        rand = new Random();
        measurementsCapture = (MeasurementsCapture) getActivity();
        username = measurementsCapture.returnUsername();

        speak = (Button) rootView.findViewById(R.id.buttonTalk);

        myTTS = new TextToSpeech(getActivity(), new OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    int result = myTTS.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }

            }
        });

        speak.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                vibe.vibrate(20);
                if (speak.getText().equals("Speak + Save") == true) {
                    speak.setText("Stop");


                    if (flag == false) {
                        speakWords("No data are stored right now");
                        speak.setText("Speak + Save");

                    } else if (flag == true) {


                        speak.setText("Stop");
                        new MeanValuesTask().onCancelled();

                        if (username.equals(""))
                            speakWords("Welcome dear guest");
                        else
                            speakWords("Welcome dear " + username);

                        speakWords("Here are your summary results");
                        speakWords("Your temperature is " + summary[0]);
                        speakWords("Your blood oxygen is " + summary[1]);
                        speakWords("Your airflow is " + summary[2]);
                        speakWords("Your conductivity is " + summary[3]);
                        speakWords("That's all, Have a nice day");

                        AlertDialog.Builder alert = new AlertDialog.Builder(
                                getActivity(), AlertDialog.THEME_HOLO_DARK);
                        alert.setTitle("Summary Results");

                        String guestOrNot = null;
                        if (username.equals(""))
                            guestOrNot = "guest";
                        else guestOrNot = username;

                        alert.setMessage(Html
                                .fromHtml("<font color='#00ddff'><b>Welcome dear " + guestOrNot
                                        + "</b></font>"
                                        + "<br><br>"
                                        + "<font color='#00ddff'><b>Here are your summary results:</b></font>"
                                        + "<br>" + "Your temperature is "
                                        + "<font color='#CC66FF'><b>"
                                        + summary[0] + "</b></font>"
                                        + "<br>Your blood oxygen is "
                                        + "<font color='#64ff64'><b>"
                                        + summary[1] + "</b></font>"
                                        + "<br>Your airflow is "
                                        + "<font color='#2196F3'><b>"
                                        + summary[2] + "</b></font>"
                                        + "<br>Your conductivity is "
                                        + "<font color='#CC9900'><b>"
                                        + summary[3] + "</b></font>"));

                        alert.setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        arg0.dismiss();
                                    }
                                }).create();

                        AlertDialog dialog = alert.create();
                        dialog.show();

                    }

                } else if (speak.getText().equals("Stop") == true) {

                    speak.setText("Speak + Save");

                    counter = 0;
                    countAirflow = 0;
                    countConductivity = 0;
                    countECG = 0;
                    countOxygen = 0;
                    countPulse = 0;
                    countTemperature = 0;

                    // new MeanValuesTask()
                    //  .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    myTTS.stop();
                }

            }

        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new MeanValuesTask().execute();
            }
        }, 0, 10);//put here time 500 milliseconds=1 second


        return rootView;
    }

    private class MeanValuesTask extends AsyncTask<Void, String, Void> {

        // Generates dummy data in a non-ui thread
        @Override
        protected Void doInBackground(Void... params) {
            try {
                MeasurementsCapture activity = (MeasurementsCapture) getActivity();

                try {

                    temp = new String[9];

                    temp[0] = (rand.nextInt((38 - 35) + 1) + 35) + "";
                    temp[1] = (rand.nextInt((120 - 70) + 1) + 70) + "";
                    temp[2] = (rand.nextInt((99 - 88) + 1) + 88) + "";
                    temp[3] = (rand.nextInt((50 - 20) + 1) + 20) + "";
                    temp[4] = 0 + "";
                    temp[5] = 0 + "";
                    temp[6] = (rand.nextInt((8 - 1) + 1) + 1) + "";
                    temp[7] = 0 + "";
                    temp[8] = (rand.nextInt((150 - 80) + 1) + 80) + "";

                    countAirflow = countAirflow
                            + Double.parseDouble(temp[3]);
                    countECG = countECG + Double.parseDouble(temp[8]);
                    countTemperature = countTemperature
                            + Double.parseDouble(temp[0]);
                    countPulse = countPulse + Double.parseDouble(temp[1]);
                    countOxygen = countOxygen + Double.parseDouble(temp[2]);
                    countConductivity = countConductivity
                            + ((Double.parseDouble(temp[6])));


                } catch (NumberFormatException e) {
                    countECG = countECG + Double.parseDouble(temp[8]);
                    countTemperature = countTemperature
                            + Double.parseDouble(temp[0]);
                    countPulse = countPulse + Double.parseDouble(temp[1]);
                    countOxygen = countOxygen + Double.parseDouble(temp[2]);
                    countConductivity = countConductivity
                            + ((Double.parseDouble(temp[6])));

                    countAirflow = countAirflow + 20;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    public void run() {
                        progressBar.setProgress(counter);
                        progresstext.setText("Loading Sensor Data: "
                                + counter + "/" + progressBar.getMax());
                    }
                });

                if (counter == 100) {
                    flag = false;

                    countMeanAirflow = countAirflow / (double) counter;
                    countMeanConductivity = countConductivity
                            / (double) counter;
                    countMeanECG = countECG / (double) counter;
                    countMeanOxygen = countOxygen / (double) counter;
                    countMeanPulse = countPulse / (double) counter;
                    countMeanTemperature = countTemperature
                            / (double) counter;

                    final DecimalFormat df = new DecimalFormat("0.00");

                    summary = new String[5];

                    if (countMeanTemperature < 35)
                        summary[0] = "Hypothermia";
                    else if (countMeanTemperature >= 35
                            && countMeanTemperature < 37.5)
                        summary[0] = "Normal";
                    else if (countMeanTemperature >= 37.5
                            && countMeanTemperature < 40)
                        summary[0] = "Fever or Hyperthermia";
                    else if (countMeanTemperature >= 40)
                        summary[0] = "Hyperpyrexia";

                    if (countMeanOxygen < 88)
                        summary[1] = "Low";
                    else if (countMeanOxygen >= 88
                            && countMeanOxygen < 94.9)
                        summary[1] = "Hypoxic Drive Problem";
                    else if (countMeanOxygen >= 95 && countMeanOxygen <= 99)
                        summary[1] = "Normal";
                    else if (countMeanOxygen >= 99.1)
                        summary[1] = "Carbon Monoxide Poissoning";

                    if (countMeanAirflow < 15)
                        summary[2] = "Low";
                    else if (countMeanAirflow >= 15
                            && countMeanAirflow <= 30)
                        summary[2] = "Normal";
                    else if (countMeanAirflow >= 31)
                        summary[2] = "High";


                    if (countMeanConductivity < 5)
                        summary[3] = "Low";
                    else if (countMeanConductivity >= 5
                            && countMeanConductivity < 7.5)
                        summary[3] = "Normal";
                    else if (countMeanConductivity >= 7.5
                            && countMeanConductivity < 8.5)
                        summary[3] = "High";
                    else if (countMeanConductivity >= 8.5)
                        summary[3] = "Very High";

                    flag = true;
                    counter = -1;

                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub

                                temperature.setText(df
                                        .format(countMeanTemperature) + "");
                                pulse.setText(df.format(countMeanPulse) + "");
                                oxygen.setText(df.format(countMeanOxygen) + "");
                                airflow.setText(df.format(countMeanAirflow)
                                        + "");
                                ecg.setText(df.format(countMeanECG) + "");
                                conductivity.setText(df
                                        .format(countMeanConductivity) + "");

                                countAirflow = 0;
                                countConductivity = 0;
                                countECG = 0;
                                countOxygen = 0;
                                countPulse = 0;
                                countTemperature = 0;

                            }
                        });
                    }

                }

                counter = counter + 1;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {

        }
    }

//    @Override
//    public void onInit(int status) {
//
//        if (status == TextToSpeech.SUCCESS) {
//
//            int result = myTTS.setLanguage(Locale.US);
//
//            if (result == TextToSpeech.LANG_MISSING_DATA
//                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TTS", "This Language is not supported");
//            }
//
//        } else {
//            Log.e("TTS", "Initilization Failed!");
//        }
//
//    }

    @SuppressWarnings("deprecation")
    public void speakWords(String speech) {

        // voice pitch
        myTTS.setPitch(1.2f);

        // Set Speech Rate, 1.0 is Normal
        // Lower values slow down the speech, greater values accelerate it

        myTTS.setSpeechRate(0.8f);

        myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);

    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass method first
        new MeanValuesTask().cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first
        counter = 0;
        countAirflow = 0;
        countConductivity = 0;
        countECG = 0;
        countOxygen = 0;
        countPulse = 0;
        countTemperature = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        myTTS.stop();
        myTTS.shutdown();
    }

    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        super.onDestroy();
    }
}
