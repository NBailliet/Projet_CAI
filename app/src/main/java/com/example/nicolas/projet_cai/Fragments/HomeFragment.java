package com.example.nicolas.projet_cai.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.example.nicolas.projet_cai.R;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class HomeFragment extends Fragment {

        static Chronometer chronometer;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            int min = 0;
            int hour = 0;
            int sec = 0;

            View homeView = inflater.inflate(R.layout.home, container, false);
            chronometer = homeView.findViewById(R.id.chronometre);

            if (isMyServiceRunning(com.example.nicolas.projet_cai.Services.LocalService.class)) {
                chronometer.setVisibility(0);
                System.out.println(com.example.nicolas.projet_cai.Services.LocalService.getSeconds());
                sec= com.example.nicolas.projet_cai.Services.LocalService.getSeconds();
                if (sec>60) {
                    min = (sec%3600)/60;
                    if (min>60) {
                        hour = sec / 3600;
                    }
                    sec = sec - (hour*3600 + min*60);
                }
                System.out.println(sec);
                System.out.println(min);
                System.out.println(hour);
                chronometer.setBase(SystemClock.elapsedRealtime() - (hour * 60000 + min * 60000 + sec * 1000));
                //chronometer.setText(hh+":"+mm+":"+ss);
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
}

