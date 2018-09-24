package com.antiagression.Classes;

import android.location.Location;

public class Position {

    private Location location;
    private String addressAsText;

    public Position(Location location, String addressAsText) {
        this.location = location;
        this.addressAsText = addressAsText;
    }

    public Location getLocation(){
        return this.location;
    }


    public String getAddressAsText(){
        return this.addressAsText;
    }


}
