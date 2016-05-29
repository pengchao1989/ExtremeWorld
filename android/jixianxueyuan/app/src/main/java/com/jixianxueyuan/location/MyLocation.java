package com.jixianxueyuan.location;

import java.io.Serializable;

/**
 * Created by pengchao on 3/26/16.
 */
public class MyLocation implements Serializable{

    private double latitude;
    private double longitude;
    private String address;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
