package com.example.herib.guasdomundo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class MapsFragment() : Fragment(), OnMapReadyCallback, LocationListener {
    private var mMap: GoogleMap? = null

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

    override fun onMapReady(map: GoogleMap?) {
        mMap = map

        mMap!!.setMaxZoomPreference(1f)
        mMap!!.setMinZoomPreference(10f)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, this)
        }
    }

    private fun locationUpdate(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null)
            return
        locationUpdate(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        return
    }

    override fun onProviderEnabled(provider: String?) {
        return
    }

    override fun onProviderDisabled(provider: String?) {
        if(provider.equals("GPS"))
            Toast.makeText(this.context, "Sem sinal de GPS", Toast.LENGTH_SHORT).show()
    }
}

