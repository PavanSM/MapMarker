package com.demo.boonggdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.boonggdemo.pojo.LocationPojo;
import com.demo.boonggdemo.utils.L;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    ProgressDialog pDialog;
     List<LocationPojo> model_LocationList;
    private double currentLat,currentLong;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

    }

    @Override
    public void onLocationChanged(Location location) {
                 // getting current location lat long
            currentLat=location.getLatitude();
            currentLong=location.getLongitude();
        try {
            Log.d("CURRENT","Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

            //convert lat long value to human readable address

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            getSupportActionBar().setTitle( addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
            // fetch data from api after current location change so we can get accurate distance on location change
            getLocationList();
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MapsActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    private void getLocationList(){

        if (!L.isNetworkAvailable(this)) {
            Toast.makeText(this,"Network not found",Toast.LENGTH_SHORT).show();

        } else {
            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://13.127.135.220:3100/api/stores",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject returnOtpObj;

                                JSONArray jsonarray_data = new JSONArray(response);
                                Log.d("RESP",response);

                                    pDialog.dismiss();

                                    Gson gson = new Gson();


                                   Type Location_ListType = new TypeToken<ArrayList<LocationPojo>>(){}.getType();

                                model_LocationList = gson.fromJson(jsonarray_data.toString(), Location_ListType); // store data to pojo

                                for(int i=0;i<model_LocationList.size();i++) {


                                    if (model_LocationList.get(i).getLatitude().equals("0")) {
                                    } else {

                                        double distance = distance(currentLat, currentLong, Double.parseDouble(model_LocationList.get(i).getLatitude()), Double.parseDouble(model_LocationList.get(i).getLongitude()), "K");

                                        LatLng newLocation = new LatLng(Double.valueOf(model_LocationList.get(i).getLatitude()), Double.valueOf(model_LocationList.get(i).getLongitude()));

                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 10));


                                        //adding marker,title and distance from user's current location
                                        mMap.addMarker(new MarkerOptions()
                                                .title(model_LocationList.get(i).getName())
                                                .snippet(String.valueOf(new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP).doubleValue()) + " KM")
                                                .position(new LatLng(
                                                        Double.valueOf(model_LocationList.get(i).getLatitude()),
                                                        Double.valueOf(model_LocationList.get(i).getLongitude())
                                                ))
                                        ).showInfoWindow();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pDialog.dismiss();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }

    }
//getting distance between current location and location passed
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
