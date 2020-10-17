// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.isoftzone.yoappstore.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.isoftzone.yoappstore.R;
import com.isoftzone.yoappstore.adapter.PlaceAdapter;
import com.isoftzone.yoappstore.baseactivity.BaseActivity;
import com.isoftzone.yoappstore.bean.SelectedProduct;
import com.isoftzone.yoappstore.bean.googleresp.GoogleResponseBean;
import com.isoftzone.yoappstore.network.CommonInterfaces;
import com.isoftzone.yoappstore.network.MakeParamsHandler;
import com.isoftzone.yoappstore.network.RestApiManager;
import com.isoftzone.yoappstore.util.GpsUtils;
import com.isoftzone.yoappstore.util.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        PlaceAdapter.Listner,
        CommonInterfaces.getShippingRate,
        CommonInterfaces.add_address,
        CommonInterfaces.getLocationNameFromGoolge, CommonInterfaces.checkVerifyAddress {

    private GoogleMap mMap;
    // New variables for Current Place Picker
    private static final String TAG = "MapsActivity";

    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.


    private final LatLng mDefaultLocation = new LatLng(22.7196, 75.8577);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;

    ArrayList<Place> placeArrayList;
    RecyclerView listPlacesRecyclerView;
    boolean isGPS = false;
    TextView doneTextView, currentlocatTextView;
    /*   private String[] mLikelyPlaceNames;
       private String[] mLikelyPlaceAddresses;
       private String[] mLikelyPlaceAttributions;
       private LatLng[] mLikelyPlaceLatLngs;*/
    LatLng latLngMain;
    ProgressBar addressProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Set up the action toolbar
        // setCustomToolBar(CompanyDetails.getInstance().getCompanyName(), false, true);
        //  cartRelativeLayout.setVisibility(View.GONE);

        doneTextView = findViewById(R.id.doneTextView);
        currentlocatTextView = findViewById(R.id.currentlocatTextView);
        addressProgressBar = findViewById(R.id.addressProgressBar);
        listPlacesRecyclerView = findViewById(R.id.listPlacesRecyclerView);
        listPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
                Log.e("isGPSEnable", "==" + isGPSEnable);
                pickCurrentPlace();
            }
        });

        if (isGPS) {
            pickCurrentPlace();
        }

        // Initialize the Places client
        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getApplicationContext(), apiKey);
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e(TAG, "Place: " + place.getName() + ", " + place.getId());

                mMap.clear();

                mMap.addMarker(new MarkerOptions()
                        .title(place.getName())
                        .position(place.getLatLng())
                        .snippet(place.getAddress() + "\n" + place.getAttributions()));
                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                saveLocationData(place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });

        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latLngMain != null && !currentlocatTextView.getText().toString().trim().equalsIgnoreCase("")) {
                    checkVerifyAddress();
                } else {
                    Toast.makeText(MapsActivity.this, "Please Locate your address first in Map", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void checkVerifyAddress() {
        JSONObject object = new JSONObject();
        try {
            object.put("lat", latLngMain.latitude);
            object.put("long", latLngMain.longitude);
            Log.e("checkVerifyAddress", "==" + object.toString());
            showDialog(MapsActivity.this);
            RestApiManager.checkVerifyAddress(MakeParamsHandler.getRequestBody(object.toString()), MapsActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void successisAddressVerify(String msg) {
        dismissDialog();
        if (msg.equalsIgnoreCase("1")) {
            if (latLngMain != null && !currentlocatTextView.getText().toString().trim().equalsIgnoreCase("")) {
                showDialogAddress();
            } else {
                Toast.makeText(MapsActivity.this, "Please Locate your address first in Map", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MapsActivity.this, "We are not deliver the products in this Location", Toast.LENGTH_LONG).show();
        }
    }

    String addressType = "Home";

    private void showDialogAddress() {

        final View dialogView = View.inflate(this, R.layout.address_dialog, null);

        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((1 * width) / 1, (1 * height) / 1);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText houseNoEditText = dialog.findViewById(R.id.houseNoEditText);
        final EditText floorEditText = dialog.findViewById(R.id.floorEditText);
        final TextView cityNameTextView = dialog.findViewById(R.id.cityNameTextView);

        final TextView stateNameTextView = dialog.findViewById(R.id.stateNameTextView);
        final TextView countryNameTextView = dialog.findViewById(R.id.countryNameTextView);


        final TextView areaNameTextView = dialog.findViewById(R.id.areaNameTextView);
        final EditText completeAddressEditText = dialog.findViewById(R.id.completeAddressEditText);
        final EditText landMarkEditText = dialog.findViewById(R.id.landMarkEditText);
        ImageView cancelImageView = dialog.findViewById(R.id.cancelImageView);
        final TextView saveAddress = dialog.findViewById(R.id.saveAddress);
        Spinner addressTypeSpinner = dialog.findViewById(R.id.addressTypeSpinner);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


        areaNameTextView.setText(currentlocatTextView.getText().toString().trim());

        cityNameTextView.setText(cityName);
        stateNameTextView.setText(stateName);
        countryNameTextView.setText(countryName);


     /*   final String address = SharedPref.getPrefsHelper().getPref("address").toString().trim();

        if (address != null) {
            try {
                JSONObject object1 = new JSONObject(address);
                areaNameEditText.setText(object1.getString("name"));
                floorEditText.setText(object1.getString("floor"));
                houseNoEditText.setText(object1.getString("house"));
                completeAddressEditText.setText(object1.getString("completeAddress"));
                landMarkEditText.setText(object1.getString("landmark"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        ArrayList<String> stringsList = new ArrayList<>();
        stringsList.add("Home");
        stringsList.add("Work");
        stringsList.add("Office");
        stringsList.add("Hotel");
        stringsList.add("Other");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, R.layout.size_spinner_items, stringsList);
        addressTypeSpinner.setAdapter(genderAdapter);
        addressTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addressType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String houseFlatNo = houseNoEditText.getText().toString().trim();
                String floorString = floorEditText.getText().toString().trim();
                String areaName = areaNameTextView.getText().toString().trim();
                String completeAddress = completeAddressEditText.getText().toString().trim();
                String landMark = landMarkEditText.getText().toString().trim();

                if (houseFlatNo.equalsIgnoreCase("") || areaName.equalsIgnoreCase("") || completeAddress.equalsIgnoreCase("")) {
                    Toast.makeText(MapsActivity.this, "* Fields are mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("user_id", userBean.getId());
                    object.put("area_name", areaName);
                    object.put("lat", latLngMain.latitude);
                    object.put("long", latLngMain.longitude);
                    object.put("address", completeAddress);
                    object.put("house_no", houseFlatNo);
                    object.put("landmark", landMark);
                    object.put("addr_type", addressType);
                    object.put("floor", floorString);
                    object.put("area", areaName);
                    object.put("completeAddress", completeAddress);
                    object.put("city", cityName);
                    object.put("state", stateName);
                    object.put("country", countryName);
                    Log.e("AddressJson", "==" + object.toString());
                    showDialog(MapsActivity.this);
                    RestApiManager.add_address(MakeParamsHandler.getRequestBody(object.toString()), MapsActivity.this);
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GpsUtils.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
                Toast.makeText(this, "Please wait we are trying to find your location", Toast.LENGTH_LONG).show();
                pickCurrentPlace();
            }

        } else {
            Log.e(TAG, "Else");
            Toast.makeText(this, "Please enable GPS for Search location", Toast.LENGTH_LONG).show();
            finish();
        }
    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_geolocate:

                // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                // Present the current place picker
                pickCurrentPlace();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
*/


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    mMap.setMyLocationEnabled(true);
                    pickCurrentPlace();
                }
            }
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
        mMap = googleMap;
        //  mMap.setBuildingsEnabled(true);
        // Add a marker in Sydney and move the camera
  /*      LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
*/
        //
        // PASTE THE LINES BELOW THIS COMMENT
        //

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                //  buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                pickCurrentPlace();
            } else {
                //Request Location Permission
                getLocationPermission();
            }
        } else {
            //  buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            pickCurrentPlace();
        }

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Log.e("onCameraMoveStarted", "=" + i);
            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                Log.e("onCameraIdle", "onCameraIdle=");

             /*   mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
*/
                //  getAddressByLatLong(latLng);
               /* Log.e("onCameraIdle", "latitude=" + latLng.latitude);
                Log.e("onCameraIdle", "longitude=" + latLng.longitude);*/

                getLocationNameFromGoogle();
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //  Log.e("onCameraMove", "=");
                latLngMain = mMap.getCameraPosition().target;

             /*   mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
*/
                Log.e("onCameraMove", "latitude=" + latLngMain.latitude);
                Log.e("onCameraMove", "longitude=" + latLngMain.longitude);
                //  getLocationNameFromGoogle();

            }
        });

        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {
                Log.e("onCameraMove", "=");
            }
        });


        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
             /*   mMap.clear();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
*/

                IndoorBuilding building = mMap.getFocusedBuilding();
                if (building == null) {
                    return;
                }
                IndoorLevel indoorLevel = building.getLevels().get(building.getActiveLevelIndex());

                Log.e("indoorLevel", "getName=" + indoorLevel.getName());
                Log.e("indoorLevel", "getShortName=" + indoorLevel.getShortName());
                getLocationNameFromGoogle();
            }
        });


     /*   mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng latLng = marker.getPosition();
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);

                    Log.e("onCameraIdle", "latitude=" + latLng.latitude);
                    Log.e("onCameraIdle", "longitude=" + latLng.longitude);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/


    /*    mapTouchLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
             *//*   Utils.logDebug(TAG, "Map touched!");
                timeLastTouched = System.currentTimeMillis();*//*
                return false; // Pass on the touch to the map or shadow layer.
            }
        });*/

    }

    public void getLocationNameFromGoogle() {
        // https://maps.googleapis.com/maps/api/geocode/json?latlng=22.7227535,75.884472&key=AIzaSyAyJKXE5ViV8h0J_-kvuYfbiJFCLEKd05w
        if (latLngMain != null) {
            addressProgressBar.setVisibility(View.VISIBLE);
            currentlocatTextView.setText("");
            currentlocatTextView.setVisibility(View.GONE);
            String mainUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLngMain.latitude + "," + latLngMain.longitude + "&key=" + getResources().getString(R.string.google_maps_key);
            RestApiManager.getLocationNameFromGoolge(mainUrl, this);
        }
    }


    public void getAddressFromLocation(final LatLng latLng, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)); //.append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e("Location Address Loader", "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = " Unable to get address for this location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.e("location Address=", locationAddress);
        }
    }

    public void getAddressByLatLong() {


        final double latitude = 22.715340536089766;

        final double longitude = 75.89673612266779;


        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    Log.e("getAddressByLatLong", "address=" + address);
                    Log.e("getAddressByLatLong", "city=" + city);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    private void getCurrentPlaceLikelihoods() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        // Get the likely places - that is, the businesses and other points of interest that
        // are the best match for the device's current location.
        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(this,
                new OnCompleteListener<FindCurrentPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                        if (task.isSuccessful()) {

                            FindCurrentPlaceResponse response = task.getResult();


                            // Set the count, handling cases where less than 5 entries are returned.
                            int count;
                            if (response.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                                count = response.getPlaceLikelihoods().size();
                            } else {
                                count = M_MAX_ENTRIES;
                            }

                            int i = 0;

                            placeArrayList = new ArrayList<>();

                        /*    mLikelyPlaceNames = new String[count];
                            mLikelyPlaceAddresses = new String[count];
                            mLikelyPlaceAttributions = new String[count];
                            mLikelyPlaceLatLngs = new LatLng[count];*/

                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                Place currPlace = placeLikelihood.getPlace();
                                placeArrayList.add(currPlace);

                         /*       mLikelyPlaceNames[i] = currPlace.getName();
                                mLikelyPlaceAddresses[i] = currPlace.getAddress();
                                mLikelyPlaceAttributions[i] = (currPlace.getAttributions() == null) ? null : String.join(" ", currPlace.getAttributions());
                                mLikelyPlaceLatLngs[i] = currPlace.getLatLng();
                                String currLatLng = (mLikelyPlaceLatLngs[i] == null) ? "" : mLikelyPlaceLatLngs[i].toString();*/
                                Log.e(TAG, String.format("Place " + currPlace.getName() + " has likelihood: " + placeLikelihood.getLikelihood() + " at " + currPlace.getLatLng()));

                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }
                            }


                            // COMMENTED OUT UNTIL WE DEFINE THE METHOD
                            // Populate the ListView
                            if (placeArrayList.size() > 0) {
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions()
                                        .title(placeArrayList.get(0).getName())
                                        .position(placeArrayList.get(0).getLatLng())
                                        .snippet(placeArrayList.get(0).getAddress() + "\n" + placeArrayList.get(0).getAttributions()));
                                // Position the map's camera at the location of the marker.
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(placeArrayList.get(0).getLatLng()));
                                saveLocationData(placeArrayList.get(0));


                                fillPlacesList();
                            }
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                            }
                        }
                    }
                });
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = location;
                            Log.e(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                            Log.e(TAG, "Longitude: " + mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            latLngMain = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            getLocationNameFromGoogle();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        }
                        getCurrentPlaceLikelihoods();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }
        if (mLocationPermissionGranted) {
            getDeviceLocation();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }


    /**
     * When user taps an item in the Places list, add a marker to the map with the place details
     */
 /*   private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // position will give us the index of which place was selected in the array
            LatLng markerLatLng = mLikelyPlaceLatLngs[position];
            String markerSnippet = mLikelyPlaceAddresses[position];
            if (mLikelyPlaceAttributions[position] != null) {
                markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[position];
            }

            mMap.clear();

            mMap.addMarker(new MarkerOptions()
                    .title(mLikelyPlaceNames[position])
                    .position(markerLatLng)
                    .snippet(markerSnippet));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
        }
    };*/

    /**
     * Display a list allowing the user to select a place from a list of likely places.
     */
    private void fillPlacesList() {
        // Set up an ArrayAdapter to convert likely places into TextViews to populate the ListView
        //   ArrayAdapter<String> placesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLikelyPlaceNames);
        PlaceAdapter placeAdapter = new PlaceAdapter(this, placeArrayList, this);
        listPlacesRecyclerView.setAdapter(placeAdapter);
       /* lstPlaces.setAdapter(placesAdapter);
        lstPlaces.setOnItemClickListener(listClickedHandler);*/
    }


    @Override
    public void onClickPlace(Place place) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .title(place.getName())
                .position(place.getLatLng()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .snippet(place.getAddress() + "\n" + place.getAttributions()));
        // Position the map's camera at the location of the marker.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
        saveLocationData(place);
    }

    public void saveLocationData(Place place) {
        JSONObject object = new JSONObject();
        try {
            object.put("name", place.getName());
            object.put("address", place.getAddress());
            object.put("lat", place.getLatLng().latitude);
            object.put("lng", place.getLatLng().longitude);
            object.put("house", "");
            object.put("floor", "");
            object.put("area", place.getName());
            object.put("completeAddress", "");
            object.put("landmark", "");
            object.put("attributions", place.getAttributions());
            SharedPref.getPrefsHelper().savePref("address", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getShippingRate(Place place) {
        // {"lat":"22.7244","long":"75.8839"}
        try {
            JSONObject object = new JSONObject();
            object.put("lat", place.getLatLng().latitude);
            object.put("long", place.getLatLng().longitude);

            showDialog(this);
            RestApiManager.getShippingRate(MakeParamsHandler.getRequestBody(object.toString()), this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successGetShippingRate(int shipping_rate) {
        dismissDialog();
        SelectedProduct.getInstance().setShippingRate(shipping_rate);
    }

    @Override
    public void successAddAddress(String msg) {
        dismissDialog();
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        finish();
    }

    String cityName = "";
    String stateName = "";
    String countryName = "";

    @Override
    public void successGoogleAddress(String cityStatCountry, ArrayList<GoogleResponseBean> beanArrayListGoogle) {
        cityName = "";
        stateName = "";
        countryName = "";
        for (int i = 0; i < beanArrayListGoogle.get(0).getAddressComponents().size(); i++) {
            if (beanArrayListGoogle.get(0).getAddressComponents().get(i).getTypes().contains("sublocality_level_2")) {
                currentlocatTextView.setText("" + beanArrayListGoogle.get(0).getAddressComponents().get(i).getLongName());
                break;
            } else if (beanArrayListGoogle.get(0).getAddressComponents().get(i).getTypes().contains("sublocality_level_1")) {
                currentlocatTextView.setText("" + beanArrayListGoogle.get(0).getAddressComponents().get(i).getLongName());
                break;
            }
        }

        for (int i = 0; i < beanArrayListGoogle.get(0).getAddressComponents().size(); i++) {
            if (beanArrayListGoogle.get(0).getAddressComponents().get(i).getTypes().contains("locality")) {
                cityName = beanArrayListGoogle.get(0).getAddressComponents().get(i).getLongName();
                break;
            }
        }

        //  String[] titleBody = cityStatCountry.split("\\s+");
        Log.e("cityStatCountry", "=" + cityStatCountry);

        String unused = cityStatCountry.substring(0, cityStatCountry.indexOf(' '));
        String content = cityStatCountry.substring(cityStatCountry.indexOf(' ') + 1);

        Log.e("content", "=" + content);
        String[] titleBody = content.split(",");

        cityName = titleBody[0];
        stateName = titleBody[1];
        countryName = titleBody[2];

        addressProgressBar.setVisibility(View.GONE);
        currentlocatTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public void failure(String error) {
        dismissDialog();
    }

}
