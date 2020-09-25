package in.ateesinfomedia.remedio.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.managers.MyPreferenceManager;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.TrackingModel;

public class MapsTrackingActivity extends FragmentActivity implements OnMapReadyCallback, NetworkCallback {

    private GoogleMap mMap;
    private String lat,longi,orderId,from,userId;
    private MyPreferenceManager manager;
    private int REQUEST_OTC_ORDER_DETAILS_ID = 9800;
    private int REQUEST_PRESCRIPTION_ORDER_DETAILS_ID = 1008;
    private ArrayList<TrackingModel> locationArray = new ArrayList<>();
    private Marker vehicleMarker;
    private MapView mMapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Handler handler;
    private LatLng newPlace;
    boolean first = true;
    private LatLng mainPosition;
    private ValueAnimator vAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_tracking);

        handler = new Handler();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);



//        mMapView.onResume();

//        try {
//            MapsInitializer.initialize(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                mMap = mMap;
//
//                // For showing a move to my location button
////                googleMap.setMyLocationEnabled(true);
//                // For dropping a marker at a point on the Map
//                LatLng qibla = new LatLng(21.422487,39.826206);
//                mMap.addMarker(new MarkerOptions().position(qibla).title("Kaaba").snippet("Mecca, Saudi Arabia"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(qibla).zoom(0).build();
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mMapView = (MapView)findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        manager = new MyPreferenceManager(this);
        lat = getIntent().getStringExtra("lat");
        longi = getIntent().getStringExtra("long");
        orderId = getIntent().getStringExtra("orderId");
        from = getIntent().getStringExtra("from");
        userId = manager.getdelicioId();

        getTrackingDetails();
    }

    private void getTrackingDetails() {
        Map<String,String> map = new HashMap<>();
        if (from.equals("otc")){
            map.put("order_id",orderId);
            map.put("user_id",userId);

            new NetworkManager(this).doPost(map, Apis.API_POST_OTC_TRACKING_DETAILS,"REQUEST_OTC_ORDER_DETAILS",REQUEST_OTC_ORDER_DETAILS_ID,this);
        } else {
            map.put("prescription_id",orderId);
//            map.put("user_id",userId);
            new NetworkManager(this).doPost(map, Apis.API_POST_PRESCRIPTION_TRACKING_DETAILS,"REQUEST_PRESCRIPTION_ORDER_DETAILS",REQUEST_PRESCRIPTION_ORDER_DETAILS_ID,this);
        }
//        LoadingDialog.showLoadingDialog(this,"Loading...");

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
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
////        LatLng destination = new LatLng(Double.valueOf(lat), Double.valueOf(longi));
////        mMap.addMarker(new MarkerOptions().position(destination).title("Delivery Location").draggable(false));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//
//    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_OTC_ORDER_DETAILS_ID){
                processOtcTracking(response);
            } else if (requestId == REQUEST_PRESCRIPTION_ORDER_DETAILS_ID){
//                processPrescriptionTracking(response);
                processOtcTracking(response);
            }
        }
    }

    private void processOtcTracking(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){

                getTrackingDetails();
            } else {

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                locationArray.clear();
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    TrackingModel trackingModel = new TrackingModel();
                    trackingModel.setLati(jsonObject1.optDouble("latitude"));
                    trackingModel.setLongi(jsonObject1.optDouble("longitude"));
                    LatLng latLng = new LatLng(jsonObject1.optDouble("latitude"),jsonObject1.optDouble("longitude"));
                    trackingModel.setLatLong(latLng);

                    locationArray.add(trackingModel);
                }

                Bitmap markerIcon = BitmapFactory.decodeResource(getResources(), R.drawable.map_icon_blue);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        final GroundOverlay groundOverlay1=mMap.addGroundOverlay(new GroundOverlayOptions()
//                                .position(new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi()), 10)
//                                .transparency(0.5f)
//                                .image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_blue)));
//                        OverLay(groundOverlay1);
//                    }
//                }, 2000);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        final GroundOverlay groundOverlay2=mMap.addGroundOverlay(new GroundOverlayOptions()
//                                .position(new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi()), 10)
//                                .transparency(0.5f)
//                                .image(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_blue)));
//                        OverLay(groundOverlay2);
//                    }
//                }, 4000);
                if (vehicleMarker == null) {
                    vehicleMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi()))
                            .anchor(0.5f, 0.5f)
                            .draggable(false)
                            .visible(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_blue)));
                    pulseMarker(markerIcon, vehicleMarker, 1000);
//                    changePositionSmoothly(vehicleMarker,new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi()));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi())));
                } else {
                    vehicleMarker.setPosition(new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi()));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi())));
//                    changePositionSmoothly(vehicleMarker,new LatLng(locationArray.get(0).getLati(), locationArray.get(0).getLongi()));
                }
//                if (vehicleMarker == null) {
////                    createMarker(locationArray.get(0).getLati(), locationArray.get(0).getLongi(), "", "", R.drawable.ic_edit);
//
//                    vehicleMarker = createMarker(locationArray.get(0).getLati(), locationArray.get(0).getLongi(), "", "",R.drawable.bikedanew);
//                }
//                if (first){
//                    animateMarkerNew(locationArray.get(0).getLatLong(),locationArray.get(0).getLatLong(),vehicleMarker);
//                    first = false;
//                } else {
//                    animateMarkerNew(newPlace,locationArray.get(0).getLatLong(),vehicleMarker);
//                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getTrackingDetails();
                    }
                },0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void OverLay(final GroundOverlay groundOverlay){
        vAnimator = ValueAnimator.ofInt(0, 2000);
        int r = 99999;
        vAnimator.setRepeatCount(r);
        //vAnimator.setIntValues(0, 500);
        vAnimator.setDuration(12000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new LinearInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                Integer i = (Integer) valueAnimator.getAnimatedValue();
                groundOverlay.setDimensions(i);
            }
        });
        vAnimator.start();
    }

    public void changePositionSmoothly(Marker marker, LatLng newLatLng) {
        if (marker == null) {
            return;
        }
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 100f);
        final float[] previousStep = {0f};
        double deltaLatitude = newLatLng.latitude - marker.getPosition().latitude;
        double deltaLongitude = newLatLng.longitude - marker.getPosition().latitude;
        animation.setDuration(1500);
        animation.addUpdateListener(animation1 -> {
            float deltaStep = (Float) animation1.getAnimatedValue() - previousStep[0];
            previousStep[0] = (Float) animation1.getAnimatedValue();
            marker.setPosition(new LatLng(marker.getPosition().latitude + deltaLatitude * deltaStep * 1 / 100, marker.getPosition().latitude + deltaStep * deltaLongitude * 1 / 100));
        });
        animation.start();
    }

    private void processPrescriptionTracking(String response) {
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {
    }

    private void pulseMarker(final Bitmap markerIcon, final Marker marker, final long onePulseDuration) {
        final Handler handler = new Handler();
        final long startTime = System.currentTimeMillis();

        final Interpolator interpolator = new CycleInterpolator(1f);
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = interpolator.getInterpolation((float) elapsed / onePulseDuration);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(scaleBitmap(markerIcon, 1f + 0.05f * t)));
                handler.postDelayed(this, 16);
            }
        });
    }

    public Bitmap scaleBitmap(Bitmap bitmap, float scaleFactor) {
        final int sizeX = Math.round(bitmap.getWidth() * scaleFactor);
        final int sizeY = Math.round(bitmap.getHeight() * scaleFactor);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);
        return bitmapResized;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(15);
//        LatLng ny = new LatLng(40.7143528, -74.0059731);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        LatLng destination = new LatLng(Double.valueOf(lat), Double.valueOf(longi));
        mMap.addMarker(new MarkerOptions().position(destination).title("Delivery Location").draggable(false));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);

        return -1;
    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .draggable(false)
                .visible(true)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }

    private void animateMarkerNew(final LatLng startPosition, final LatLng destination, final Marker marker) {
        if (marker != null) {
            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        newPlace = endPosition;
                        mainPosition= newPosition;
                        marker.setPosition(newPosition);
                        marker.setVisible(true);
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(15f)
                                .build()));

                        marker.setRotation(getBearing(startPosition, new LatLng(destination.latitude, destination.longitude)));
                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                     if (vehicleMarker != null) {
                         vehicleMarker.remove();
                     }
                    vehicleMarker = mMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.bikedanew)).draggable(false)
                            .visible(true).anchor(0.5f, 0.5f));
//                    if (vehicleMarker == null) {
//                        vehicleMarker = mMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.bikedanew)).draggable(false)
//                                .visible(true).anchor(0.5f, 0.5f));
//                    } else {
//                        vehicleMarker.setPosition(mainPosition);
//                    }
                }
            });
            valueAnimator.start();
        }
    }
}