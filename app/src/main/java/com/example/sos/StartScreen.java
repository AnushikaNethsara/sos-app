package com.example.sos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

public class StartScreen extends AppCompatActivity {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME=1000; //minimum time interval between location updates, in milliseconds
    private final long MIN_DISTANCE=5; //minimum distance between location updates, in meters
    private LatLng latLng;


    String currentLong;
    String currentLat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        getLocation();

        Button buttonStop = findViewById(R.id.stopBtn);
        buttonStop.setEnabled(false);


    }


    public void stopAlert(View view){
        Button buttonSos = findViewById(R.id.sosBtn);
        buttonSos.setEnabled(true);

        Button buttonStop = findViewById(R.id.stopBtn);
        buttonStop.setEnabled(false);
    }

    public void onPressSos(View view){
        Button buttonSos = findViewById(R.id.sosBtn);
        buttonSos.setEnabled(false);

        Button buttonStop = findViewById(R.id.stopBtn);
        buttonStop.setEnabled(true);

        try{
            getLocation();
            Log.d("long-sms",currentLong);
            Log.d("lat-sms",currentLat);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Location Failed: "+e,Toast.LENGTH_LONG).show();
        }

        try{
            String mobileNo="0711768517";
            String message="Anushika-IM/2017/065 " +
                    "http://maps.google.com/?q="+currentLong+","+currentLat;

            if(!currentLong.equals("") && !currentLat.equals("") && !mobileNo.equals("")){
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(mobileNo,null,message,null,null);
                Toast.makeText(getApplicationContext(),"SMS Sent to "+mobileNo,Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"SMS Sending Failed: "+e,Toast.LENGTH_LONG).show();
        }

    }


    public void getLocation() {

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    currentLong=Double.toString(location.getLatitude());
                    currentLat=Double.toString(location.getLongitude());
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };


        try {
            locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,locationListener);
        }catch (SecurityException e){e.printStackTrace();
        }
    }




}
