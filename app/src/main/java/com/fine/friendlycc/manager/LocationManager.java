package com.fine.friendlycc.manager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.fine.friendlycc.app.AppContext;
import com.fine.friendlycc.event.LocationChangeEvent;
import com.fine.friendlycc.utils.ExceptionReportUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * @author litchi
 */
public class LocationManager {
    public static final String TAG = "LocationManager";
    private static LocationManager instance;
    private final FusedLocationProviderClient mFusedLocationClient;
    private Double lat;
    private Double lng;

    private LocationManager() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AppContext.instance().getApplicationContext());
    }

    public static LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

//    public void setLatLng(Double lat, Double lng) {
//        this.lat = lat;
//        this.lng = lng;
//    }

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

    public void getLastLocation(LocationListener locationListener) {
        try {
            int i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AppContext.instance().getApplicationContext());
            if (i == ConnectionResult.SERVICE_INVALID) {
                if (locationListener != null) {
                    locationListener.onLocationFailed();
                }
                return;
            }
            if (ActivityCompat.checkSelfPermission(AppContext.instance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(AppContext.instance().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    try {
                        Location location = task.getResult();
                        if (locationListener != null) {
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                locationListener.onLocationSuccess(lat, lng);
                                startLocation(null);
                            } else {
                                if (locationListener != null) {
                                    startLocation(locationListener);
                                }
                            }
                        } else {
                            startLocation(null);
                        }
                    }catch (Exception e){//兼容部分手机不支持谷歌api
                        startLocation(null);
                    }

                });
            }
        } catch (Exception e) {
            Log.e("异常当前定位获取：", e.getMessage());
            if (locationListener != null) {
                locationListener.onLocationFailed();
            }
        }
    }

    public void startLocation(LocationListener locationListener) {
        try {
            if (ActivityCompat.checkSelfPermission(AppContext.instance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(AppContext.instance().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setNumUpdates(1);
                //mLocationRequest.setInterval(10*1000); // two minute interval
//              mLocationRequest.setFastestInterval(120000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                RxBus.getDefault().post(new LocationChangeEvent(LocationChangeEvent.LOCATION_STATUS_START));
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        List<Location> locationList = locationResult.getLocations();
                        if (locationList.size() > 0) {
                            Location location = locationList.get(locationList.size() - 1);
                            Log.i(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            if (locationListener != null) {
                                if (locationListener != null) {
                                    locationListener.onLocationSuccess(lat, lng);
                                }
                            }

                        } else {
                        }
                    }

                    @Override
                    public void onLocationAvailability(LocationAvailability locationAvailability) {
                        if (!locationAvailability.isLocationAvailable()) {
                            if (locationListener != null) {
                                locationListener.onLocationFailed();
                            }
                        }
                    }
                }, Looper.myLooper());
            }
        } catch (Exception e) {
            Log.e("获取定位抛出异常", e.getMessage());
            if (locationListener != null) {
                locationListener.onLocationFailed();
            }
        }
    }

    public void getFromLocation(double lat, double lng, GeocoderListener listener) {
        if (!Geocoder.isPresent()) {
            ToastUtils.showShort("Geocoder not present!");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(AppContext.instance().getApplicationContext(), Locale.TAIWAN);
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
//                        ToastUtils.showShort(String.format("%s-%s-%s", address.getAdminArea(), address.getSubAdminArea(), address.getLocality()));
                        if (listener != null) {
                            listener.onGeocoderSuccess(address.getAdminArea(), address.getSubAdminArea(), address.getLocality());
                        }
                    } else {
                        if (listener != null) {
                            listener.onGeocoderFailed();
                        }
                    }
                } catch (Exception e) {
                    ExceptionReportUtils.report(e);
                    if (listener != null) {
                        listener.onGeocoderFailed();
                    }
                }
            }
        }.start();
    }

    public void initloadLocation(LocationListener locationListener) {
        try {
            int i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AppContext.instance().getApplicationContext());
            if (i == ConnectionResult.SERVICE_INVALID) {
                return;
            }
            if (ActivityCompat.checkSelfPermission(AppContext.instance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(AppContext.instance().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null) {
                        locationListener.onLocationSuccess(location.getLatitude(), location.getLongitude());
                        lat = location.getLatitude();
                        lng = location.getLongitude();
//                            getFromLocation(lat, lng, new GeocoderListener() {
//                                @Override
//                                public void onGeocoderSuccess(String province, String city, String country) {
//                                    Log.e("获取详细地址",province+"======"+city+"======"+country);
//                                }
//
//                                @Override
//                                public void onGeocoderFailed() {
//
//                                }
//                            });
                    } else {
                        locationListener.onLocationFailed();
                    }
                });
            }
        } catch (Exception e) {
            locationListener.onLocationFailed();
        }
    }

    public interface initLocationListener {
        void onGeocoderSuccess(String province, String city, String country);
    }

    public interface GeocoderListener {
        void onGeocoderSuccess(String province, String city, String country);

        void onGeocoderFailed();
    }

    public interface LocationListener {
        /**
         * 定位成功
         *
         * @param lat
         * @param lng
         */
        void onLocationSuccess(double lat, double lng);

        /**
         * 定位失败
         */
        void onLocationFailed();
    }

}
