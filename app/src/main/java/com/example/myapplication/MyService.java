package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class MyService extends Service implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    String strAdd;
    String map;
    Geocoder geocoder;
    private static final String LOGSERVICE = "#######";
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), " Shake services has been started",
                Toast.LENGTH_SHORT).show();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 11) {
            Toast.makeText(getApplicationContext(), " sms"+strAdd,
                    Toast.LENGTH_SHORT).show();
            FileInputStream fis = null;
            int i=0;
            SmsManager smsManager = SmsManager.getDefault();
            try {
                fis = openFileInput("a.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);

                String text1;
                while ((text1 = br.readLine()) != null) {
                    if(i==0) {
                        i++;
                        continue;
                    }smsManager.sendTextMessage(text1, null, "Help me!! Location:"+strAdd+"  maplink:"+map, null, null);


                    i++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(i==0)
                Toast.makeText(getApplicationContext(), "Please Atleast Add a number to send sms",
                        Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Message Send to "+i+" Successfully",
                        Toast.LENGTH_SHORT).show();



        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

       geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        strAdd = addresses.get(0).getLocality();

        map = "http://www.google.com/maps/place/" + l.getLatitude() + "," + l.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), " Shake services has been stoped",
                Toast.LENGTH_SHORT).show();

    }
}
