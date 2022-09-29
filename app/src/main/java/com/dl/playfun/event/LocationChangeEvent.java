package com.dl.playfun.event;

/**
 * @author wulei
 */
public class LocationChangeEvent {
    public static final int LOCATION_STATUS_START = 1;
    public static final int LOCATION_STATUS_SUCCESS = 2;
    public static final int LOCATION_STATUS_FAILED = 3;

    private Double lat, lng;
    private int status;

    public LocationChangeEvent() {
    }

    public LocationChangeEvent(int status) {
        this.status = status;
    }

    public LocationChangeEvent(int status, Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
        this.status = status;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
