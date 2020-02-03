package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    String file="a.txt";
    String file1="b.txt";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final String TAG = "MainActivity";
    TextView tt,tt1;
    Button btn5;
    String strAdd = "";
    Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn= findViewById(R.id.button);
         btn5= findViewById(R.id.button5);
        Button btn1=findViewById(R.id.button2);
        tt= (TextView) findViewById(R.id.textView3);
        tt1= (TextView) findViewById(R.id.textView2);

        File f1 = new File(file1);
        if(f1.exists())
        {
            Toast.makeText(getApplicationContext(), "File exist",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {

            FileOutputStream stream = null;
            try {
                stream = openFileOutput(file1, getApplicationContext().MODE_PRIVATE);
                stream.write("".getBytes());
                stream.close();
                Toast.makeText(getApplicationContext(), " FILE CREATED",
                        Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), " exist",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), " exist",
                        Toast.LENGTH_SHORT).show();
            }


        }

        FileInputStream fis = null;

        try {
            fis = openFileInput(file1);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

           text = br.readLine();
         //   Toast.makeText(getApplicationContext(), "Some Error"+text,
                   // Toast.LENGTH_SHORT).show();
           if(text=="YES")
           {
               tt1.setText("Click on DISABLE to disable Shake Services");
               Intent intent = new Intent(this, MyService.class);
               startService(intent);
               btn5.setText("DISABLE");

           }
           else
           {
               tt1.setText("Click on ENABLE to enable Shake Services");
               btn5.setText("ENABLE");
           }






        } catch (FileNotFoundException e) {
////            Toast.makeText(getApplicationContext(), "File Not found",
//                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Some Error",
                    Toast.LENGTH_SHORT).show();
        }



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                       1);
            }
        }



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());







    }
    public void sendMessage(View view) {
        Intent i= new Intent(this, addcontactActivity.class);
        startActivity(i);
    }

    public void onservice(View view) {

        if (tt1.getText().toString().equals("Click on DISABLE to disable Shake Services"))
        {
            FileOutputStream stream = null;
            try {
                stream = openFileOutput(file1, getApplicationContext().MODE_PRIVATE);
                stream.write("NO".getBytes());
                stream.close();

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), " exist",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), " exist",
                        Toast.LENGTH_SHORT).show();
            }

            tt1.setText("Click on ENABLE to enable Shake Services");
            btn5.setText("ENABLE");

            Intent myService = new Intent(MainActivity.this, MyService.class);
            stopService(myService);




        }
        else
        {
            FileOutputStream stream = null;
            try {
                stream = openFileOutput(file1, getApplicationContext().MODE_PRIVATE);
                stream.write("YES".getBytes());
                stream.close();

            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), " exist",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), " exist",
                        Toast.LENGTH_SHORT).show();
            }


            tt1.setText("Click on DISABLE to disable Shake Services");
            btn5.setText("DISABLE");

            startService(new Intent(this, MyService.class));


        }
    }









    public void sendMessage1(View view) {
//        Log.i(TAG, "MyClass.getView() — get item number " );
tt.setText("");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }





        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        int i=0;
                        String map="";
                        String text1="";

                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            strAdd=addresses.get(0).getLocality();
                            map ="http://www.google.com/maps/place/"+location.getLatitude()+","+location.getLongitude();




                            FileInputStream fis = null;
                            SmsManager smsManager = SmsManager.getDefault();
                            try {
                                fis = openFileInput(file);
                                InputStreamReader isr = new InputStreamReader(fis);
                                BufferedReader br = new BufferedReader(isr);

                                while ((text1 = br.readLine()) != null) {
if(i==0) {
    i++;
    continue;
}smsManager.sendTextMessage(text1, null, "Help me!! Location:"+strAdd+"  maplink:"+map, null, null);

                                    tt.append("\n");

                                    tt.append(text1);
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







                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });











    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    return;
                }
            }
        }

    }













}
