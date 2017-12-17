package com.zentcode.parks.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.zentcode.parks.utils.LocationEvent;

import org.greenrobot.eventbus.EventBus;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOG_SERVICE = "LOCATION_SERVICE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        Log.i(LOG_SERVICE, Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_SERVICE, Thread.currentThread().getStackTrace()[1].getMethodName());

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_SERVICE, Thread.currentThread().getStackTrace()[1].getMethodName());
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_SERVICE, Thread.currentThread().getStackTrace()[1].getMethodName() + bundle);

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Log.i(LOCATION_SERVICE, "lat: " + location.getLatitude());
            Log.i(LOCATION_SERVICE, "lon: " + location.getLongitude());
            Log.i(LOCATION_SERVICE, "accu: " + location.getAccuracy());
        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_SERVICE, Thread.currentThread().getStackTrace()[1].getMethodName() + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_SERVICE, Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOCATION_SERVICE, "lat: " + location.getLatitude());
        Log.i(LOCATION_SERVICE, "lon: " + location.getLongitude());
        Log.i(LOCATION_SERVICE, "accu: " + location.getAccuracy());
        EventBus.getDefault().post(new LocationEvent(location));
    }

    private void initLocationRequest() {
        /*interval 5000, fastestinterval 2000*/
        mLocationRequest = new LocationRequest()
                .setInterval(10000)
                .setFastestInterval(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        initLocationRequest();

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }
}