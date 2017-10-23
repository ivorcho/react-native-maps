package com.airbnb.android.react.maps;

import android.content.Context;
import android.graphics.Color;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

public class AirMapHeatMap extends AirMapFeature {

  private TileOverlay tileOverlay;
  private TileOverlayOptions tileOverlayOptions;
  private HeatmapTileProvider tileProvider;

  private double opacity;
  private int radius;
  private float zIndex;
  private Gradient gradient = HeatmapTileProvider.DEFAULT_GRADIENT;
  private float[] gradientSteps = {
          0.2f, 1f
  };
  private int[] gradientColors = {
          Color.rgb(102, 225, 0),
          Color.rgb(255, 0, 0)
  };
  private int maxIntensity = 0;
  private int gradientInitialColor = Color.TRANSPARENT;
  private List<WeightedLatLng> coordinates;

  public AirMapHeatMap(Context context) {
    super(context);
  }


  public void setCoordinates(ReadableArray coordinates) {
    // it's kind of a bummer that we can't run map() or anything on the ReadableArray
    this.coordinates = new ArrayList<>(coordinates.size());
    for (int i = 0; i < coordinates.size(); i++) {
      ReadableMap coordinate = coordinates.getMap(i);
      this.coordinates.add(i,
          new WeightedLatLng(new LatLng(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")), coordinate.getDouble("intensity")));
    }
    if (tileProvider != null) {
      tileProvider.setWeightedData(this.coordinates);
    }
    if (tileOverlay != null) {
      tileOverlay.clearTileCache();
    }
  }

  public void setOpacity(double opacity) {
    this.opacity = opacity;
    if (tileProvider != null) {
      tileProvider.setOpacity(opacity);
    }
    if (tileOverlay != null) {
      tileOverlay.clearTileCache();
    }
  }

  public void setRadius(int radius) {
    this.radius = radius;
    if (tileProvider != null) {
      tileProvider.setRadius(radius);
    }
    if (tileOverlay != null) {
      tileOverlay.clearTileCache();
    }
  }

  public void setZIndex(float zIndex) {
    this.zIndex = zIndex;
    if (tileOverlay != null) {
      tileOverlay.setZIndex(zIndex);
    }
  }

  public void setGradientSteps(ReadableArray steps) {
    maxIntensity = steps.getInt(steps.size() - 1);
    gradientSteps = new float[steps.size()];
    for (int i = 0; i < steps.size(); i++) {
      gradientSteps[i] = (float)steps.getInt(i) / (float)maxIntensity;
    }
    if (tileProvider != null) {
      tileProvider.setMaxIntensity(maxIntensity);
    }
    updateTiles();
  }

  public void setGradientColors(ReadableArray colors) {
    gradientColors = new int[colors.size()];
    for (int i = 0; i < colors.size(); i++) {
      gradientColors[i] = colors.getInt(i);
    }
    updateTiles();
  }

  public void setDefaultColor(int color) {
    gradientInitialColor = color;
    updateTiles();
  }

  private void updateTiles() {
    gradient = new Gradient(gradientColors, gradientSteps, 1000, gradientInitialColor);
    if (tileProvider != null) {
      tileProvider.setGradient(gradient);
    }
    if (tileOverlay != null) {
      tileOverlay.clearTileCache();
    }
  }

  public TileOverlayOptions getTileOverlayOptions() {
    if (tileOverlayOptions == null) {
      tileOverlayOptions = createTileOverlayOptions();
    }
    return tileOverlayOptions;
  }

  private TileOverlayOptions createTileOverlayOptions() {
    TileOverlayOptions options = new TileOverlayOptions();
    options.zIndex(zIndex);
    this.tileProvider = new HeatmapTileProvider.Builder()
        .weightedData(this.coordinates)
        .opacity(this.opacity)
        .radius(this.radius)
        .gradient(gradient)
        .maxIntensity(maxIntensity)
        .build();
    options.tileProvider(this.tileProvider);
    return options;
  }

  @Override
  public Object getFeature() {
    return tileOverlay;
  }

  @Override
  public void addToMap(GoogleMap map) {
    this.tileOverlay = map.addTileOverlay(getTileOverlayOptions());
  }

  @Override
  public void removeFromMap(GoogleMap map) {
    tileOverlay.remove();
  }

}
