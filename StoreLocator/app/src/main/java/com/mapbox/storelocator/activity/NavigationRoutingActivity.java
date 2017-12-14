package com.mapbox.storelocator.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewListener;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationUnitType;
import com.mapbox.storelocator.R;

import static com.mapbox.storelocator.util.StringConstants.MOCK_DEVICE_LOCATION_LAT_KEY;
import static com.mapbox.storelocator.util.StringConstants.MOCK_DEVICE_LOCATION_LONG_KEY;
import static com.mapbox.storelocator.util.StringConstants.DESTINATION_LOCATION_LAT_KEY;
import static com.mapbox.storelocator.util.StringConstants.DESTINATION_LOCATION_LONG_KEY;
import static com.mapbox.storelocator.util.StringConstants.SIMULATE_NAV_ROUTE_KEY;

public class NavigationRoutingActivity extends AppCompatActivity implements NavigationViewListener {

  private NavigationView navigationView;
  private double navOriginLat;
  private double navOriginLong;
  private double navDestinationLat;
  private double navDestinationLong;
  private boolean simulateRoute;

  private String TAG = "NavigationRoutingActivity";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getIntent().getExtras() != null) {
      navOriginLat = getIntent().getExtras().getDouble(MOCK_DEVICE_LOCATION_LAT_KEY);
      navOriginLong = getIntent().getExtras().getDouble(MOCK_DEVICE_LOCATION_LONG_KEY);
      navDestinationLat = getIntent().getExtras().getDouble(DESTINATION_LOCATION_LAT_KEY);
      navDestinationLong = getIntent().getExtras().getDouble(DESTINATION_LOCATION_LONG_KEY);
      simulateRoute = getIntent().getExtras().getBoolean(SIMULATE_NAV_ROUTE_KEY);

      Log.d(TAG, "onCreate: navOriginLat = " + navOriginLat);
      Log.d(TAG, "onCreate: navOriginLong = " + navOriginLong);
      Log.d(TAG, "onCreate: navDestinationLat = " + navDestinationLat);
      Log.d(TAG, "onCreate: navDestinationLong = " + navDestinationLong);
    }
    // Hide the status bar for the map to fill the entire screen
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    // Inflate the layout with the the NavigationView.
    setContentView(R.layout.activity_navigation_ui);
    navigationView = findViewById(R.id.navigationView);
    navigationView.onCreate(savedInstanceState);
    navigationView.getNavigationAsync(this);
  }

  @Override
  public void onNavigationReady() {
    Log.d(TAG, "onNavigationReady:");
    NavigationViewOptions options = NavigationViewOptions.builder()
      .origin(Point.fromLngLat(navOriginLong, navOriginLat))
      .destination(Point.fromLngLat(navDestinationLong, navDestinationLat))
      .awsPoolId(null)
      .unitType(NavigationUnitType.TYPE_IMPERIAL)
      .shouldSimulateRoute(simulateRoute)
      .build();
    navigationView.startNavigation(options);
    Log.d(TAG, "onNavigationReady: starting navigation");
  }

  @Override
  public void onNavigationFinished() {
    finish();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    navigationView.onLowMemory();
  }

  @Override
  public void onBackPressed() {
    // If the navigation view didn't need to do anything, call super
    if (!navigationView.onBackPressed()) {
      super.onBackPressed();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    navigationView.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    navigationView.onRestoreInstanceState(savedInstanceState);
  }

}
