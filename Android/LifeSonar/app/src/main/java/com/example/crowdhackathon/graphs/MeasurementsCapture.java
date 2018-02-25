package com.example.crowdhackathon.graphs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Context;

import com.example.crowdhackathon.lifesonar.R;


public class MeasurementsCapture extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context context;

    private static WifiManager wifiManager;
    private static WifiConfiguration netConfig;
    private static DatagramSocket socket;
    private String[] arrayOfString = {0.0 + "", 0.0 + "", 0.0 + "", 0.0 + "",
            0.0 + "", 0.0 + "", 0.0 + "", 0.0 + "", 0.0 + "", 0.0 + "",
            0.0 + "", 0.0 + ""};
    private static boolean bindSocket = true, flag = false;

    private DatagramPacket localDatagramPacket;
    int systolicarray[] = {125, 127, 123, 127, 125, 127, 130, 119, 119, 114,
            131, 116, 121, 140, 123};
    int diastolicarray[] = {70, 79, 61, 78, 71, 104, 79, 83, 71, 103, 72, 87,
            87, 82, 85};
    String username;
    ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        netConfig = new WifiConfiguration();
        netConfig.SSID = "\"" + "ANDROID" + "\"";
        netConfig.SSID = netConfig.SSID.replace("\"", "");

        context = getApplicationContext();

        if (bindSocket == true) {
            try {
                socket = new DatagramSocket(12345);
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //mainThread();

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GeneralBiometricsTab(), "Biometrics");
        adapter.addFragment(new EmotionalStateTab(), "Emotional State");
        adapter.addFragment(new PatientSummatyTab(), "Patient Summary");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void mainThread() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                flag = true;
                int randomvalue = 0;
                try {
                    while (flag == true) {
                        try {
                            localDatagramPacket = new DatagramPacket(
                                    new byte[1024], 1024);
                            if (socket.isConnected()) {
                                socket.receive(localDatagramPacket);
                                Log.d("get data", localDatagramPacket.getData().toString());
                                arrayOfString = new String(
                                        localDatagramPacket.getData()).split("#");
                            }

                            Random pick = new Random();
                            randomvalue = pick.nextInt(15);

                            if (arrayOfString.length < 10) {
                                Log.d("error streaming", arrayOfString.length
                                        + "");

                                arrayOfString = new String[]{"20", "0",
                                        "127", "70", "0", "37", "75", "98",
                                        "2", "0", "20", "0", "50", "0"};
                            } else {
                                arrayOfString[2] = systolicarray[randomvalue]
                                        + "";
                                arrayOfString[3] = diastolicarray[randomvalue]
                                        + "";

                            }

                            for (int i = 0; i < arrayOfString.length; i++) {
                                if (arrayOfString[i].equals("0.0")) {
                                    arrayOfString = new String[]{"20", "0",
                                            "127", "70", "0", "37", "75", "98",
                                            "2", "0", "20", "0", "50", "0"};
                                    break;

                                }

                            }

                        } catch (final NullPointerException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            e.getMessage() + " :(",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                } catch (final SocketException localSocketException) {
                    localSocketException.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    localSocketException.getMessage() + " :(",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;

                } catch (final IOException localIOException) {
                    localIOException.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    localIOException.getMessage() + " :(",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;

                }

            }
        }, "UDP conn");

        t.start();
    }

    public String SensorData() {
        if (arrayOfString.length < 2) {
            return "37" + "#" + "75" + "#" + "98" + "#" + "20" + "#" + "127"
                    + "#" + "70";

        } else
            return arrayOfString[5] + "#" + arrayOfString[6] + "#"
                    + arrayOfString[7] + "#" + arrayOfString[10] + "#"
                    + arrayOfString[2] + "#" + arrayOfString[3];
    }

    public String returnMuscle() {
        if (arrayOfString.length < 12) {
            return "50";

        } else
            return arrayOfString[12];

    }

    public String returnUsername() {

        return username;
    }

    public String returnCondcuctance() {
        if (arrayOfString.length < 2) {
            return "2";

        } else
            return arrayOfString[8];

    }

    public String returnECG() {

        if (arrayOfString.length < 2) {
            return "0";

        } else
            return arrayOfString[1];

    }

    @Override
    public void onBackPressed() {
        MeasurementsCapture.this.finish();
        super.onBackPressed();

    }

    public void closeWifiHotSpot() {
        try {
            Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();

            for (Method method : wmMethods) {
                if (method.getName().equals("setWifiApEnabled")) {
                    try {
                        method.invoke(wifiManager, null, false);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openWifiHotSpot() {
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : wmMethods) {
            if (method.getName().equals("setWifiApEnabled")) {
                netConfig.SSID = "\"" + "ANDROID" + "\"";
                netConfig.SSID = netConfig.SSID.replace("\"", "");
                netConfig.allowedAuthAlgorithms
                        .set(WifiConfiguration.AuthAlgorithm.OPEN);

                WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
                try {
                    method.setAccessible(true);
                    //return (Boolean) method.invoke(wifimanager);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        MeasurementsCapture.this.finish();
        onBackPressed();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass method first
        //socket.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // Always call the superclass method first
        MeasurementsCapture.this.finish();
        socket.close();
    }

}
