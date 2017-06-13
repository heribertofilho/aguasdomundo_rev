package com.example.herib.guasdomundo

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class MapsFragment : Fragment(), OnMapReadyCallback, OnCompleteListener<Location> {
    private var mMap: GoogleMap? = null
    private val mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.activity_maps_fragment, container, false)

        val manager = fragmentManager
        val transaction = manager.beginTransaction()
        val fragment = SupportMapFragment()
        transaction.add(R.id.mapView, fragment as Fragment)
        transaction.commit()

        fragment.getMapAsync(this)

        return view
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
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mFusedLocationClient.lastLocation
                .addOnCompleteListener(this)
        mMap = googleMap
    }

    override fun onComplete(location: Task<Location>) {
        val l = location.result
        val latLng = LatLng(l.latitude, l.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
}
