package com.example.vitmap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
    /* access modifiers changed from: private */
    public MarkerOptions currLocation;
    /* access modifiers changed from: private */
    public Marker currMarkLocation;
    private Polyline currentPolyline;
    Button getDirection;
    /* access modifiers changed from: private */
    public Marker gotoMarkPlace;
    /* access modifiers changed from: private */
    public MarkerOptions gotoPlace;
    LocationListener locationListener;
    LocationManager locationManager;
    /* access modifiers changed from: private */
    public GoogleMap mMap;
    String place;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            this.locationManager.requestLocationUpdates("gps", 0, 0.0f, this.locationListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.getDirection = (Button) findViewById(R.id.btnGetDirection);
        this.getDirection.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FetchURL fetchURL = new FetchURL(MapActivity.this);
                MapActivity mapActivity = MapActivity.this;
                String str = "driving";
                fetchURL.execute(new String[]{mapActivity.getUrl(mapActivity.currLocation.getPosition(), MapActivity.this.gotoPlace.getPosition(), str), str});
            }
        });
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @SuppressLint("WrongConstant")
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.locationManager = (LocationManager) getSystemService(Param.LOCATION);
        this.locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                MapActivity.this.currLocation = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()));
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (MapActivity.this.currMarkLocation != null) {
                    MapActivity.this.currMarkLocation.remove();
                }
                MapActivity mapActivity = MapActivity.this;
                mapActivity.currMarkLocation = mapActivity.mMap.addMarker(MapActivity.this.currLocation.title("Current Location"));
                Intent it = MapActivity.this.getIntent();
                String place = it.getStringExtra("block");
                String str = "SJT";
                if (place.equals(str)) {
                    MapActivity.this.gotoPlace = new MarkerOptions().position(new LatLng(12.9709d, 79.1638d));
                    MapActivity mapActivity2 = MapActivity.this;
                    mapActivity2.gotoMarkPlace = mapActivity2.mMap.addMarker(MapActivity.this.gotoPlace.title(str));
                } else {
                    MapActivity.this.gotoPlace = new MarkerOptions().position(new LatLng(12.9696d, 79.1577d));
                    MapActivity mapActivity3 = MapActivity.this;
                    mapActivity3.gotoMarkPlace = mapActivity3.mMap.addMarker(MapActivity.this.gotoPlace.title("SMV"));
                }
                FetchURL fetchURL = new FetchURL(MapActivity.this);
                MapActivity mapActivity4 = MapActivity.this;
                String str2 = "driving";
                fetchURL.execute(new String[]{mapActivity4.getUrl(mapActivity4.currLocation.getPosition(), MapActivity.this.gotoPlace.getPosition(), str2), str2});
                MapActivity.this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f));
                MapActivity.this.mMap.animateCamera(CameraUpdateFactory.zoomIn());
                MapActivity.this.mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 2000, null);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        String str = "android.permission.ACCESS_FINE_LOCATION";
        if (ContextCompat.checkSelfPermission(this, str) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{str}, 1);
            return;
        }
        this.locationManager.requestLocationUpdates("gps", 0, 10.0f, this.locationListener);
    }

    /* access modifiers changed from: private */
    public String getUrl(LatLng origin, LatLng dest, String directionMode) {
        StringBuilder sb = new StringBuilder();
        sb.append("origin=");
        sb.append(origin.latitude);
        String str = ",";
        sb.append(str);
        sb.append(origin.longitude);
        String str_origin = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("destination=");
        sb2.append(dest.latitude);
        sb2.append(str);
        sb2.append(dest.longitude);
        String str_dest = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mode=");
        sb3.append(directionMode);
        String mode = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str_origin);
        String str2 = "&";
        sb4.append(str2);
        sb4.append(str_dest);
        sb4.append(str2);
        sb4.append(mode);
        String parameters = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("https://maps.googleapis.com/maps/api/directions/");
        sb5.append("json");
        sb5.append("?");
        sb5.append(parameters);
        sb5.append("&key=");
        sb5.append(getString(R.string.map_key));
        return sb5.toString();
    }

    public void onTaskDone(Object... values) {
        Polyline polyline = this.currentPolyline;
        if (polyline != null) {
            polyline.remove();
        }
        this.currentPolyline = this.mMap.addPolyline((PolylineOptions) values[0]);
    }
}
