package com.example.nicolas.projet_cai.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nicolas.projet_cai.R;
import com.example.nicolas.projet_cai.RunYourData;
import com.example.nicolas.projet_cai.SessionManager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nicolas on 09/04/2018.
 */

public class CameraFragment extends Fragment implements View.OnClickListener {

    ImageView mImageView;
    Button boutonSMS;
    Button boutonMail;
    SessionManager session;
    File f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View cameraView = inflater.inflate(R.layout.camera_image, container, false);
        mImageView = cameraView.findViewById(R.id.camera_image);
        boutonSMS = cameraView.findViewById(R.id.boutonSMS);
        boutonMail = cameraView.findViewById(R.id.boutonMail);
        boutonSMS.setOnClickListener(this);
        boutonMail.setOnClickListener(this);

        session = RunYourData.getSessionManager();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            showCameraDisabledAlertToUser();
        } else {
            dispatchTakePictureIntent();
        }

        return cameraView;
    }

    /*private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }*/

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            String nomPhoto = session.getLastRunName();
            String imageFileName = "JPEG_" + nomPhoto + "_.jpeg";
            //create a file to write bitmap data
            f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), imageFileName);
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void showCameraDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Autorisation pour appareil photo désactivée sur votre téléphone. Veuillez l'activer pour accéder à cette fonctionnalité.")
                .setCancelable(false)
                .setPositiveButton("Activer l'appareil photo pour cette application",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callCameraSettingIntent = new Intent(
                                        Settings.ACTION_APPLICATION_SETTINGS);
                                startActivity(callCameraSettingIntent);
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
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.boutonSMS :

                Intent sms = new Intent(Intent.ACTION_SEND);
                sms.setType("image/jpeg");
                Uri pathsms = Uri.fromFile(f);
                sms.putExtra(Intent.EXTRA_SUBJECT, "Photo RunYourData");
                sms.putExtra(Intent.EXTRA_TEXT,"Bonjour, vous pouvez trouver ci-jointe une photo prise avec l'application RunYourData !");
                //sms.putExtra("address","0000000000");
                sms.putExtra(Intent.EXTRA_STREAM,pathsms);
                startActivity(sms);

                break;

            case R.id.boutonMail :

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");

                Uri path = Uri.fromFile(f);

                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Photo RunYourData");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App");
                if (path != null) {
                    System.out.println("PATH DIFF NULL OK\n");
                    intent.putExtra(Intent.EXTRA_STREAM, path);
                }

                intent.putExtra(Intent.EXTRA_TEXT, "Bonjour, vous pouvez trouver ci-jointe une photo prise avec l'application RunYourData !");

                startActivity(Intent.createChooser(intent, "Envoyer Email"));

                break;

        }

    }
}
