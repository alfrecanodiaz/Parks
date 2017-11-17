package com.zentcode.parks.utils;

import android.location.Location;

public class LocationEvent {

    private final Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}