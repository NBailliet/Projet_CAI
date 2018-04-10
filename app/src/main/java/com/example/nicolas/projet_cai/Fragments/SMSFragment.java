package com.example.nicolas.projet_cai.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolas.projet_cai.R;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class SMSFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View smsView = inflater.inflate(R.layout.sms, container, false);
        return smsView;
    }
}
