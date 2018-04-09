package com.example.nicolas.projet_cai.FloatingButtonsPlus;

import android.view.View;

import com.example.nicolas.projet_cai.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class FloatingButtonsRem implements View.OnClickListener {


    @Override
    public void onClick(View v) {


        FloatingActionButton myFabStart = (FloatingActionButton)  v.findViewById(R.id.floatingActionButtonStart);
        FloatingActionButton myFabEnd = (FloatingActionButton)  v.findViewById(R.id.floatingActionButtonEnd);
        FloatingActionButton myFabClean = (FloatingActionButton)  v.findViewById(R.id.floatingActionButtonCleantable);


        myFabStart.setVisibility(View.INVISIBLE);
        myFabEnd.setVisibility(View.INVISIBLE);
        myFabClean.setVisibility(View.INVISIBLE);
    }

}
