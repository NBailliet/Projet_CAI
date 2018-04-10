package com.example.nicolas.projet_cai.Services;

/**
 * Created by Nicolas on 03/04/2018.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.Localisation;
import com.example.nicolas.projet_cai.BDD.Run;
import com.example.nicolas.projet_cai.BDD.Time;
import com.example.nicolas.projet_cai.BDD.User;
import com.example.nicolas.projet_cai.RunYourData;
import com.example.nicolas.projet_cai.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOError;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RideLocationGetter extends Service {
    public RideLocationGetter() {
    }


    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private List<Localisation> listDataLoc = new ArrayList<>();
    //private List<String> listDataRunName = new ArrayList<>();
    private String currentDataName;
    //private List<Run> listDataRunTest = new ArrayList<>();
    int nbRun;
   // private ArrayList<Time> listDataTime = new ArrayList<>();
    private Run run;
    private static final String TABLE_LOC = "TABLE_LOC";
    private BDD bdd;
    private double distanceG;
    private double vitesseM;
    private List<Double> vitesseI;
    SessionManager session;
    private static boolean testFirstRun=false;


    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point
     * @returns Distance in km
     */
    public double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final double R = 6371.01; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // convert to km

        return distance;
    }

    public double getVitesseMoy(List<Double> vitesses) {

        if (!vitesses.isEmpty()) {
            for (int i = 0; i < vitesses.size(); i++) {
                vitesseM += vitesses.get(i);
            }
        }
        vitesseM = vitesseM/vitesses.size();
        return vitesseM;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "Localisation créée");
        locationListener = new LocationListenerTest();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        bdd = new BDD(getApplicationContext());
        session = RunYourData.getSessionManager();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        Vibrator vib;
        vib= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(100);
        Toast.makeText(getBaseContext(), "Localisation du parcours lancée", Toast.LENGTH_SHORT).show();


        if (!testFirstRun) {
            //First run
            //Toast.makeText(this, "FIRST RUN", Toast.LENGTH_SHORT).show();
            //run = new Run ("Run0", session.getLoginPref(), 0);
            nbRun=1;
            currentDataName="Run0";
            //System.out.println("RUN LOGIN = "+run.getLogin());
            //System.out.println("RUN DISTANCE = "+run.getDistance());
        }
        else {
            //bdd.open();
            //listDataRun = bdd.getAllRun();
            //bdd.close();
            nbRun = session.getNbRun();
            currentDataName = "Run" + nbRun;
            nbRun = nbRun+1;
            //System.out.println("Run" + listDataRun.size());
            //run = new Run("Run" + listDataRun.size(), session.getLoginPref(), 0);
        }
        //System.out.println("RUN LOGIN = "+run.getLogin());
        //System.out.println("RUN DISTANCE = "+run.getDistance());
        //session.setLastRunName(currentDataName);
        session.createRunSession(currentDataName,0,nbRun);
        /*bdd.open();
        bdd.insertRun(run);
        listDataRunTest = bdd.getRunsWithLogin(run.getLogin());
        bdd.close();
        System.out.println("RUN NAME" + listDataRunTest.get(0).getName() + "RUN LOGIN" + listDataRunTest.get(0).getLogin() + "RUN DISTANCE" + listDataRunTest.get(0).getDistance());*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Pas encore implémenté");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "Nouvelle localisation");
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocationListenerTest implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DATE);
            int hours = c.get(Calendar.HOUR);
            int mins = c.get(Calendar.MINUTE);
            int seconds = c.get(Calendar.SECOND);
            int milliseconds = c.get(Calendar.MILLISECOND);
            Log.v(TAG, String.valueOf("y=" + year + " mo=" + month + " day=" + day + " h=" + hours + " m=" + mins + " s=" + seconds + " ms=" + milliseconds));
            Time time = new Time(year, month, day, hours, mins, seconds, milliseconds);
            Localisation localisation = new Localisation("FIRST", new LatLng(loc.getLatitude(),loc.getLongitude()), time , loc.getAltitude());
            bdd.open();
            bdd.insertLoc(localisation);
            bdd.close();
            sendMessage();

        }

        // Send an Intent with an action named "custom-event-name". The Intent sent should
        // be received by the ReceiverActivity.
        private void sendMessage() {
            Intent intent = new Intent("Nouvelle Localisation");
            // You can also include some extra data.
            //intent.putExtra("clear map", true);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        Vibrator vib;
        vib= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(100);

        //Calcul distance
        bdd.open();
        listDataLoc = bdd.getAllLoc();
        bdd.close();
        for (int i=0;i<listDataLoc.size();i++) {
            if (i<listDataLoc.size()-1) {
                distanceG += distance(listDataLoc.get(i).getLocation().latitude, listDataLoc.get(i+1).getLocation().latitude, listDataLoc.get(i).getLocation().longitude, listDataLoc.get(i + 1).getLocation().longitude);
                //vitesseI.add(distance(listDataLoc.get(i).getLocation().latitude, listDataLoc.get(i).getLocation().longitude, listDataLoc.get(i + 1).getLocation().latitude, listDataLoc.get(i + 1).getLocation().longitude)/(((listDataLoc.get(i+1).getTime())-(listDataLoc.get(i).getTime())));
                //Toast.makeText(getBaseContext(), "Distance intermédiaire parcourue : " + Double.toString(distanceG) + " m", Toast.LENGTH_SHORT).show();
            }
        }
        //TODO GERER ACCELERO ET GYRO POUR VITESSE
        //run.setDistance(distanceG);
        String distString = String.format("%.11f", distanceG);
        Toast.makeText(getBaseContext(), "Distance totale parcourue : " + distString + " km", Toast.LENGTH_SHORT).show();
        Toast.makeText(getBaseContext(), "Localisation du parcours arrêtée", Toast.LENGTH_SHORT).show();
        session.setLastDistance((float)distanceG);
        testFirstRun=true;



        //Log.w(TAG, "serviceLoc destroy");
    }
}
