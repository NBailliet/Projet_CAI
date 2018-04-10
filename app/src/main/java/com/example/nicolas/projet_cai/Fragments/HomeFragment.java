package com.example.nicolas.projet_cai.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.nicolas.projet_cai.R;
import com.example.nicolas.projet_cai.Services.LocalService;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

        static Chronometer chronometer;
        Button getBackRun;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int min = 0;
            int hour = 0;
            int sec = 0;

            View homeView = inflater.inflate(R.layout.home, container, false);
            chronometer = homeView.findViewById(R.id.chronometre);
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
                @Override
                public void onChronometerTick(Chronometer cArg) {
                    long time = SystemClock.elapsedRealtime() - cArg.getBase();
                    int h= (int)(time /3600000);
                    int m= (int)(time - h*3600000)/60000;
                    int s= (int)(time - h*3600000- m*60000)/1000 ;
                    String hh = h < 10 ? "0"+h: h+"";
                    String mm = m < 10 ? "0"+m: m+"";
                    String ss = s < 10 ? "0"+s: s+"";
                    cArg.setText(hh+":"+mm+":"+ss);

                }
            });
            getBackRun = homeView.findViewById(R.id.buttonRun);
            getBackRun.setOnClickListener(this);

            if (isMyServiceRunning(LocalService.class)) {
                chronometer.setVisibility(View.VISIBLE);
                getBackRun.setVisibility(View.VISIBLE);
                System.out.println("SECONDS SERVICE" + LocalService.getSeconds());
                sec= LocalService.getSeconds();
                if (sec>60) {
                    min = (sec%3600)/60;
                    if (min>60) {
                        hour = sec / 3600;
                    }
                    sec = sec - (hour*3600 + min*60);
                }
                //System.out.println(sec);
                //System.out.println(min);
                //System.out.println(hour);
                chronometer.setBase(SystemClock.elapsedRealtime() - (hour * 60000 + min * 60000 + sec * 1000));
                chronometer.start();
            }
            return homeView;
        }

    public Chronometer getChronometer() {
        return chronometer;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {

            int id = v.getId();

            switch (id) {

                case R.id.buttonRun :

                    MapViewFragment mapFrag = new MapViewFragment();
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.frame, mapFrag, "MapView Fragment OK")
                            .addToBackStack(null)
                            .commit();

                    break;

            }

    }
}

