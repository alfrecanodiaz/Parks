package com.zentcode.parks.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.zentcode.parks.services.LocationService;

import java.util.ArrayList;

public class LocationProvider {

    private Context mContext;
    private boolean isRunning = false;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    public LocationProvider(Context context) {
        mContext = context;
        this.mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);;
    }

    public void runLocationProvider() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (hasGpsPermissions())
                startService();
            else
                restartLoop();
        } else {
            startService();
        }
    }

    private void startService() {
        mContext.startService(new Intent(mContext, LocationService.class));
    }

    private void restartLoop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runLocationProvider();
            }
        }, 500);
    }

    public boolean hasGpsPermissions() {
        return ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public Location getLocation()
    {
        return this.mLocation;
    }

    public boolean canGetLocation() {
        return this.mLocation != null && isAccurate(mLocation) && hasLatLon(mLocation);
    }

    private boolean isAccurate(Location location) {
        return location.hasAccuracy() && location.getAccuracy() < 100;
    }

    private boolean hasLatLon(Location location) {
        return location.getLatitude() != 0.0 && location.getLongitude() != 0.0;
    }

    public void showPermissionsAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("Atención!");
        alertBuilder.setMessage("Ha rechazado el permiso requerido del GPS. Debe habilitarlo en la configuración de permisos de la aplicación.");
        alertBuilder.setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermission();
            }
        });
        alertBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("Atención!");
        alertBuilder.setMessage("El GPS no esta activado. Active el GPS.");
        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public void showGPSError() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        alertBuilder.setTitle("Atención!");
        alertBuilder.setMessage("Hubo un error al procesar la ubicación del GPS. Intente de nuevo.");
        alertBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void requestPermission() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + mContext.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        mContext.startActivity(i);
    }

    public String getGpsAsArray(Location location) {
        ArrayList<String> gpsLocation = new ArrayList<>();

        if (location != null) {
            gpsLocation.add(String.valueOf(location.getLatitude()));
            gpsLocation.add(String.valueOf(location.getLongitude()));
        }

        return String.valueOf(gpsLocation);
    }
}