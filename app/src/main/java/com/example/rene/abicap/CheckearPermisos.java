package com.example.rene.abicap;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by CamiloDesarrollo on 27/09/2016.
 */

public class CheckearPermisos {


    public void checkear(Activity a){
        if (ContextCompat.checkSelfPermission(a,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {


                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
                );
            }
        }


        if (ContextCompat.checkSelfPermission(a,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {


                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},23
                );
            }
        }
    }
}
