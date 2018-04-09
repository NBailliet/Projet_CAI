package com.example.nicolas.projet_cai.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nicolas.projet_cai.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nicolas on 09/04/2018.
 */

public class CameraFragment extends Fragment implements View.OnClickListener {

    ImageView mImageView;
    Button boutonSMS;
    //Button boutonMail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View cameraView = inflater.inflate(R.layout.camera_image, container, false);
        mImageView = cameraView.findViewById(R.id.camera_image);
        boutonSMS = cameraView.findViewById(R.id.boutonSMS);
        //boutonMail = cameraView.findViewById(R.id.boutonMail);
        boutonSMS.setOnClickListener(this);
        //boutonMail.setOnClickListener(this);
        dispatchTakePictureIntent();


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
        }
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.boutonSMS :

                break;

            /*case R.id.boutonMail :

                break;*/

        }

    }
}
