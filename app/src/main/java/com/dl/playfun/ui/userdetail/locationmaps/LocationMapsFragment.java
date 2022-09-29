package com.dl.playfun.ui.userdetail.locationmaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.dl.playfun.app.AppViewModelFactory;
import com.dl.playfun.ui.base.BaseToolbarFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dl.playfun.BR;
import com.dl.playfun.R;
import com.dl.playfun.databinding.FragmentLocationMapsBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 位置地图
 *
 * @author wulei
 */
@SuppressLint("MissingPermission")
public class LocationMapsFragment extends BaseToolbarFragment<FragmentLocationMapsBinding, LocationMapsViewModel> implements View.OnClickListener, OnMapReadyCallback {

    public static final String ARG_LAT = "arg_lat";
    public static final String ARG_LNG = "arg_lng";
    public static final String ARG_NAME = "arg_name";
    public static final String ARG_ADDRESS = "arg_address";


    private Double lat, lng;
    private String name, address;

    private GoogleMap mGoogleMap;
    private Boolean granted = false;

    public static Bundle getStartBundle(String name, String address, Double lat, Double lng) {
        if (name == null) {
            return null;
        }
        if (address == null) {
            return null;
        }
        if (lat == null) {
            return null;
        }
        if (lng == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString(ARG_NAME, name);
        bundle.putString(ARG_ADDRESS, address);
        bundle.putDouble(ARG_LAT, lat);
        bundle.putDouble(ARG_LNG, lng);
        return bundle;
    }

    @Override
    public void initParam() {
        super.initParam();
        name = getArguments().getString(ARG_NAME);
        address = getArguments().getString(ARG_ADDRESS);
        lat = getArguments().getDouble(ARG_LAT, 0);
        lng = getArguments().getDouble(ARG_LNG, 0);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_location_maps;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LocationMapsViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(mActivity.getApplication());
        return ViewModelProviders.of(this, factory).get(LocationMapsViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        binding.tvName.setText(this.name);
        binding.tvAddress.setText(this.address);
        binding.ivNav.setOnClickListener(this);
        try {
            new RxPermissions(this)
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        this.granted = granted;
                        if (granted) {
                            if (mGoogleMap != null) {
                                mGoogleMap.setMyLocationEnabled(true);
                                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            }
                        } else {
                            if (mGoogleMap != null) {
                                mGoogleMap.setMyLocationEnabled(false);
                                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void startGoogleMap() {
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", lat, lng));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (granted) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        LatLng sydney = new LatLng(this.lat, this.lng);
        googleMap.addMarker(new MarkerOptions().position(sydney).title(this.name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_nav) {
            startGoogleMap();
        }
    }
}
