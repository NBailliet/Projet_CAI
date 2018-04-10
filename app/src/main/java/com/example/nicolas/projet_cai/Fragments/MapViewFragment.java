package com.example.nicolas.projet_cai.Fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import com.example.nicolas.projet_cai.FloatingButtonsPlus.FloatingButtonsAdd;
import com.example.nicolas.projet_cai.RunYourData;
import com.example.nicolas.projet_cai.Services.LocalService;
import com.example.nicolas.projet_cai.SettingsManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nicolas.projet_cai.BDD.BDD;
import com.example.nicolas.projet_cai.BDD.Localisation;
import com.example.nicolas.projet_cai.R;
import com.example.nicolas.projet_cai.Services.RideLocationGetter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Nicolas on 03/04/2018.
 */

public class MapViewFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    View rootView;
    SettingsManager settings;

    private BDD bdd;



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.map, container, false);

        bdd = new BDD(getContext());
        setUserVisibleHint(false);

        settings = RunYourData.getSettingsManager();



        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("Nouvelle localisation"));

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                googleMap.setMyLocationEnabled(true);

                PolylineOptions polylineOptions = new PolylineOptions()
                        .width(5)
                        .color(Color.RED)
                        .width(2);

                bdd.open();
                List<Localisation> locs = new ArrayList<Localisation>(bdd.getAllLoc());
                bdd.close();
                if(locs.size()!=0) {
                    for (int i = 0; i < locs.size(); i++) {
                        polylineOptions.add(locs.get(i).getLocation());
                    }
                    Polyline line = googleMap.addPolyline(polylineOptions);

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(locs.get(0).getLocation()).zoom(14).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(48.3606531,-4.568874)).zoom(14).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(getActivity(), "GPS activé sur votre téléphone", Toast.LENGTH_SHORT).show();
        } else {
           showGPSDisabledAlertToUser();
        }

        final FloatingActionButton myFabStart = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButtonStart);
        final FloatingActionButton myFabEnd = (FloatingActionButton)  rootView.findViewById(R.id.floatingActionButtonEnd);

        //System.out.println("SETTINGS START RUN PREF = " + settings.getStartRunPref());

        if (!settings.getStartRunPref())
        {
            //Toast.makeText(getActivity(), "CORRECT START PREF", Toast.LENGTH_SHORT).show();
            myFabEnd.setColorDisabled(0);
            myFabEnd.setColorNormal(Color.GRAY);
        }
        else
        {
            //Toast.makeText(getActivity(), "ERREUR START PREF", Toast.LENGTH_SHORT).show();
            myFabStart.setColorDisabled(0);
            myFabStart.setColorNormal(Color.GRAY);
        }

        myFabStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              Intent myIntentServiceIntent = new Intent(getActivity(), RideLocationGetter.class);
              getActivity().startService(myIntentServiceIntent);
              myFabStart.setColorDisabled(0);
              myFabStart.setColorNormal(Color.GRAY);
              myFabEnd.setColorDisabled(1);
              myFabEnd.setColorNormalResId(R.color.pink);
              settings.setStartRunPref(true);
              settings.setStartPref(true);
              settings.setStopPref(false);
              Intent intentChrono = new Intent(getActivity(), LocalService.class);
              getActivity().startService(intentChrono);


            }
        });

        myFabEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              Intent myIntentServiceIntent = new Intent(getActivity(), RideLocationGetter.class);
              getActivity().stopService(myIntentServiceIntent);
              myFabEnd.setColorDisabled(0);
              myFabEnd.setColorNormal(Color.GRAY);
              myFabStart.setColorDisabled(1);
              myFabStart.setColorNormalResId(R.color.pink);
              Intent intentChrono2 = new Intent(getActivity(), LocalService.class);
              getActivity().stopService(intentChrono2);
              settings.setStartRunPref(false);
              settings.setStartPref(false);
              settings.setStopPref(true);
            }
        });

        FloatingActionButton myFabClean = (FloatingActionButton)  rootView.findViewById(R.id.floatingActionButtonCleantable);
        myFabClean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bdd.clearTable("TABLE_LOC");
                Toast.makeText(getActivity(), "Parcours supprimé.", Toast.LENGTH_SHORT).show();
                if (googleMap!=null) {
                    googleMap.clear();
                }
            }
        });

        FloatingActionsMenu myFabMenu = (FloatingActionsMenu) rootView.findViewById(R.id.menu_plus);
        myFabMenu.setOnClickListener(new FloatingButtonsAdd());


        return rootView;
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (googleMap!=null) {
                googleMap.clear();


                PolylineOptions polylineOptions = new PolylineOptions()
                        .width(5)
                        .color(Color.RED);

                bdd.open();
                List<Localisation> locs = new ArrayList<Localisation>(bdd.getAllLoc());
                bdd.close();

                if (locs.size() != 0) {
                    for (int i = 0; i < locs.size(); i++) {
                        polylineOptions.add(locs.get(i).getLocation());
                    }
                    Polyline line = googleMap.addPolyline(polylineOptions);
                    //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                }
            }
            // For zooming automatically to the location of the marker
           /* CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(locs.get(locs.size()-1).getLocation().getLatitude(),locs.get(locs.size()-1).getLocation().getLongitude())).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
        }
    };

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS désactivé sur votre téléphone. Veuillez l'activer pour accéder à cette fonctionnalité.")
                .setCancelable(false)
                .setPositiveButton("Activer la fonctionnalité GPS sur votre téléphone",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
