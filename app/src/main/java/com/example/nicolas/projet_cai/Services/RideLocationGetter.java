package com.example.nicolas.projet_cai.Services;

/**
 * Created by Nicolas on 03/04/2018.
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.Localisation;
import com.example.nicolas.projet_cai.BDD.Time;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class RideLocationGetter extends Service {
    public RideLocationGetter() {
    }


    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private ArrayList<Location> listDataLoc = new ArrayList<>();
    private ArrayList<Time> listDataTime = new ArrayList<>();
    private BDD bdd;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "serviceLoc created");
        locationListener = new LocationListenerTest();
        locationMangaer = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        bdd = new BDD(getApplicationContext());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        Toast.makeText(getBaseContext(), "Location service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "serviceLoc started");
        return super.onStartCommand(intent, flags, startId);
    }

    public class LocationListenerTest implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            //Toast.makeText(getBaseContext(), "Location changed : Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);
            //listDataLoc.add(loc);
            //Log.v(TAG, String.valueOf(listDataLoc));
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DATE);
            int hours = c.get(Calendar.HOUR);
            int mins = c.get(Calendar.MINUTE);
            int seconds = c.get(Calendar.SECOND);
            int milliseconds = c.get(Calendar.MILLISECOND);
            Log.v(TAG, String.valueOf("y=" + year + " mo=" + month + " day=" + day + " h=" + hours + " m=" + mins + " s=" + seconds + " ms=" + milliseconds));
            //listDataTime.add(new Time(year,month,day,hours,mins,seconds,milliseconds));
            Time time = new Time(year, month, day, hours, mins, seconds, milliseconds);
            Localisation localisation = new Localisation("FIRST", new LatLng(loc.getLatitude(),loc.getLongitude()), time , loc.getAltitude());
            bdd.open();
            bdd.insertLoc(localisation);
            bdd.close();
            Log.v(TAG, "insert");
            sendMessage();

        }

        // Send an Intent with an action named "custom-event-name". The Intent sent should
// be received by the ReceiverActivity.
        private void sendMessage() {
            Log.d("sender", "Broadcasting message");
            Intent intent = new Intent("new location");
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
        //ArrayList<Localisation> listlocs = new ArrayList<>(bdd.getAllLoc());
        //Log.v(TAG, listlocs.toString());
        //stopSelf();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationMangaer.removeUpdates(locationListener);
        Toast.makeText(getBaseContext(), "Location service stoped", Toast.LENGTH_SHORT).show();
        Log.w(TAG, "serviceLoc destroy");
    }
}
