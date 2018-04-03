package com.shubu.kmitlbike.data.adapter;


import android.location.Location;

public class LocationAdapter {
    public static com.shubu.kmitlbike.data.model.bike.Location makeLocationForm(Location loc){
        com.shubu.kmitlbike.data.model.bike.Location form = new com.shubu.kmitlbike.data.model.bike.Location();
        form.setLatitude(loc.getLatitude());
        form.setLongitude(loc.getLongitude());
        return form;
    }
}
