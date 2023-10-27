package com.example.myapplication;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationListener locationListener;
    private WifiManager wifiManager;
    private final long intervalMillis = 2*1000;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        TextView textGps = findViewById(R.id.text_gps);
        TextView textWifi = findViewById(R.id.text_wifi);

        locationRequest = new LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, intervalMillis).setDurationMillis(Long.MAX_VALUE).build();

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double altitude = location.getAltitude();
                Log.d(TAG, latitude + " , " + longitude + " , " + altitude);
                Resources res = getResources();
                String text1 = String.format(res.getString(R.string.text_gps_latitude), latitude);
                String text2 = String.format(res.getString(R.string.text_gps_longitude), longitude);
                String text3 = String.format(res.getString(R.string.text_gps_altitude), altitude);
                String finalText = text1 + "\n" + text2 + "\n" + text3;
                textGps.setText(finalText);
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationListener, null);

        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        final NetworkRequest request = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build();
        final ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Log.d(TAG, "onAvailable");
            }
            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                Log.d(TAG, "onCapabilitiesChanged");
                WifiInfo wifiInfo = (WifiInfo) networkCapabilities.getTransportInfo();
                int level = wifiManager.calculateSignalLevel(wifiInfo.getRssi());
                Log.d(TAG, "wifi signal level = " + level);
                Resources res = getResources();
                String text = String.format(res.getString(R.string.text_wifi_signal_strength), level);
                textWifi.setText(text);

            }
        };
        connectivityManager.requestNetwork(request, networkCallback); // For request
        connectivityManager.registerNetworkCallback(request, networkCallback); // For listen

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }
}
