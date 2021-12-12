package com.swarajyadev.covid_19coronavirusstatistics.Activities

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.swarajyadev.covid_19coronavirusstatistics.Data.Transporter
import com.swarajyadev.covid_19coronavirusstatistics.R
import kotlinx.android.synthetic.main.normal_toolbar.*


class Locator_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_container)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        var item = Transporter.country
        val marker = LatLng(item!!.lat.toDouble(),item!!.lon.toDouble())
        mMap.addMarker(MarkerOptions().position(marker).title(item!!.province+" ,${item!!.country_name}")).showInfoWindow()
        val zoomLevel = 9.0f //This goes up to 21

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, zoomLevel))

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night_mode));
        iv_back.setOnClickListener { finish() }
        mMap.setPadding(10,80,35,120);
    }

}
