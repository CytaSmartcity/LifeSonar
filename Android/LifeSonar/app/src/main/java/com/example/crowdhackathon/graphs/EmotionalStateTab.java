package com.example.crowdhackathon.graphs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crowdhackathon.lifesonar.R;

public class EmotionalStateTab extends Fragment {

    private GraphicalView mChart;
    private XYSeries visitsSeries;
    private XYMultipleSeriesDataset dataset;
    private XYSeriesRenderer visitsRenderer;
    private XYMultipleSeriesRenderer multiRenderer;
    private LinearLayout chartContainer;
    private View rootView;
    public static int increaseValue = 0;
    private TextView conductivity;
    private int temp = 0;
    private TextView title, labelconductivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.emotionalstatetab, container, false);
        conductivity = (TextView) rootView
                .findViewById(R.id.sensorvalueConductivity);
        title = (TextView) rootView.findViewById(R.id.titleEmotional);
        labelconductivity = (TextView) rootView
                .findViewById(R.id.LabelSensorConductivity);

        setupChart();
        increaseValue = 0;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new ChartTask().execute();
            }
        }, 0, 200);//put here time 500 milliseconds=1 second

        return rootView;
    }

    private void setupChart() {

        // Creating an XYSeries for Visits
        visitsSeries = new XYSeries(null);

        // Creating a dataset to hold each series
        dataset = new XYMultipleSeriesDataset();
        // Adding Visits Series to the dataset
        dataset.addSeries(visitsSeries);

        // Creating XYSeriesRenderer to customize visitsSeries
        visitsRenderer = new XYSeriesRenderer();
        visitsRenderer.setColor(Color.parseColor("#009933"));
        visitsRenderer.setPointStyle(PointStyle.DIAMOND);
        visitsRenderer.setFillPoints(true);
        visitsRenderer.setLineWidth(10);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();

        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(true, false);
        multiRenderer.setPanLimits(new double[]{0.0, 100, 0.0,
                Double.MAX_VALUE});
        multiRenderer.setLabelsTextSize(20);
        multiRenderer.setXAxisMin(0);
        multiRenderer.setXAxisMax(100);
        multiRenderer.setXLabelsAlign(Align.CENTER);

        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(10);

        multiRenderer.setYLabelsAlign(Align.RIGHT);

        multiRenderer.setLabelsColor(Color.WHITE);
        multiRenderer.setYLabelsColor(0, Color.WHITE);
        multiRenderer.setAxesColor(Color.WHITE);
        multiRenderer.setXLabels(10);
        multiRenderer.setYLabels(15);
        multiRenderer.setShowLegend(false);

        multiRenderer.setShowGridX(true);
        multiRenderer.setGridColor(Color.BLACK);
        multiRenderer.setShowGridY(true);

        multiRenderer.addSeriesRenderer(visitsRenderer);

        // Getting a reference to LinearLayout of the MainActivity Layout
        chartContainer = (LinearLayout) rootView
                .findViewById(R.id.chart_containEmotional);

        mChart = (GraphicalView) ChartFactory.getLineChartView(
                rootView.getContext(), dataset, multiRenderer);

        chartContainer.addView(mChart);

    }

    private class ChartTask extends AsyncTask<Void, String, Void> {

        // Generates dummy data in a non-ui thread
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String[] values = new String[2];

                final float value = (float) Math.random() * 8;
                float x = Float.parseFloat(value + "");
                temp = Math.round(x);
                values[1] = Double.toString(temp);
                values[0] = Double.toString(increaseValue);

                increaseValue++;
                publishProgress(values);
                Thread.sleep(150);
                mChart.repaint();

                if (increaseValue % 100 == 0) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chartContainer.removeView(mChart);

                            visitsSeries = new XYSeries(null);

                            dataset = new XYMultipleSeriesDataset();

                            dataset.addSeries(visitsSeries);

                            mChart = (GraphicalView) ChartFactory
                                    .getLineChartView(
                                            rootView.getContext(), dataset,
                                            multiRenderer);
                            chartContainer.addView(mChart);

                        }
                    });
                    increaseValue = 0;
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        // Plotting generated data in the graph
        @Override
        protected void onProgressUpdate(String... values) {
            visitsSeries.add(Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]));
            conductivity.setText(temp + "");

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
        increaseValue = 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView(); // Always call the superclass method first
        new ChartTask().cancel(true);
    }

    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        super.onDestroy();
    }
}
