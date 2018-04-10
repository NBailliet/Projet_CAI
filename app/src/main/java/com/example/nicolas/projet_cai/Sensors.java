package com.example.nicolas.projet_cai;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Sensors extends AppCompatActivity implements SensorEventListener {
    final String TAG="sensor";
    public SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;

    //private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
    private TextView currentX, currentY, currentZ;
    private TextView currentXG, currentYG, currentZG;

    //private float lastX, lastY, lastZ;

    //private float deltaXMax = 0;
    //private float deltaYMax = 0;
    //private float deltaZMax = 0;

    /*
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors_layout);
        initializeViews();

        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSensorManager != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event){
        //Sensor mSensor = event.sensor;
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float Ax = event.values[0];
            float Ay = event.values[1];
            float Az = event.values[2];
            Log.v(TAG, "TimeAcc = " + event.timestamp + "Ax = "+ Ax + "Ay = " + Ay + "Az = " + Az);
            displayCurrentValues(Ax, Ay, Az);
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float Gx = event.values[0];
            float Gy = event.values[1];
            float Gz = event.values[2];
            Log.v(TAG, "TimeGyro = " + event.timestamp + "Gx = " + Gx + "Gy = " + Gy + "Gz = " + Gz);
            displayCurrentValues2(Gx, Gy, Gz);
        }


        // display the current x,y,z accelerometer values
        //displayCurrentValues();
        // display the max x,y,z accelerometer values
        //displayMaxValues();

        /*
        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);
        */

    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        currentXG = (TextView) findViewById(R.id.currentXG);
        currentYG = (TextView) findViewById(R.id.currentYG);
        currentZG = (TextView) findViewById(R.id.currentZG);

        /*
        maxX = (TextView) findViewById(R.id.maxX);
        maxY = (TextView) findViewById(R.id.maxY);
        maxZ = (TextView) findViewById(R.id.maxZ);
        */

        // clean current values
        displayCleanValues();
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
        currentXG.setText("0.0");
        currentYG.setText("0.0");
        currentZG.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues(float x, float y, float z) {
        /*
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
        */
        currentX.setText(Float.toString(x));
        currentY.setText(Float.toString(y));
        currentZ.setText(Float.toString(z));
    }

    public void displayCurrentValues2(float x, float y, float z) {

        currentXG.setText(Float.toString(x));
        currentYG.setText(Float.toString(y));
        currentZG.setText(Float.toString(z));

    }
    /*
    // display the max x,y,z accelerometer values
    public void displayMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
            maxZ.setText(Float.toString(deltaZMax));
        }
    }
    */
}