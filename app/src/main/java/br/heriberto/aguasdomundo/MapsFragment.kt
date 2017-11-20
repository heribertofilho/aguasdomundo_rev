package br.heriberto.aguasdomundo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import br.heriberto.aguasdomundo.Interface.RetrieveDataListener
import br.heriberto.aguasdomundo.Models.Media
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.*


class MapsFragment : Fragment(), AnkoLogger, OnMapReadyCallback, LocationListener, GoogleMap.InfoWindowAdapter {
    private lateinit var mMap: GoogleMap
    private var latLng: LatLng? = null
    private lateinit var retrieveDataListener: RetrieveDataListener
    private lateinit var media: Media

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            retrieveDataListener = context as RetrieveDataListener
        } catch (castException: ClassCastException) {

        }
    }

    fun updateMap(medias: ArrayList<Media>) {
        medias.forEach { m ->
            media = m
            let {
                val latlng: LatLng = LatLng(m.latitude, m.longitude)
                mMap.addMarker(MarkerOptions().position(latlng))
                mMap.setInfoWindowAdapter(this)
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        mMap.uiSettings.setAllGesturesEnabled(false)

        mMap.setMaxZoomPreference(15f)
        mMap.setMinZoomPreference(15f)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, this)
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null)
            return
        latLng = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.addMarker(MarkerOptions().position(latLng!!).title("Você"))
        retrieveDataListener.retrieveData(latLng!!)

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        return
    }

    override fun onProviderEnabled(provider: String?) {
        return
    }

    override fun onProviderDisabled(provider: String?) {
        if (provider.equals("GPS"))
            Toast.makeText(this.context, "Sem sinal de GPS", Toast.LENGTH_SHORT).show()
    }


    override fun getInfoWindow(marker: Marker): View? {
        return null
        //return prepareInfoView(marker);
    }

    override fun getInfoContents(marker: Marker): View {
        //return null;
        return prepareInfoView(marker)

    }

    @SuppressLint("SetTextI18n")
    private fun prepareInfoView(marker: Marker): View {
        //prepare InfoView programmatically
        val ctx = AnkoContext.create(context)
        val infoView = ctx.verticalLayout {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.oxigenio) + ": "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //OXIGENIO
                if (media.superficie > 5 || media.transparency > 50) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else {
                    result.text = "RECOMENDAÇÕES"
                    result.textColor = Color.RED
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.dureza) + ": "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //DUREZA
                if (media.dureza > 5) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else {
                    result.text = "RECOMENDAÇÕES"
                    result.textColor = Color.RED
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.ph_variante) + ": "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //PH
                if (media.peixe_apatico > 5) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else {
                    result.text = "RECOMENDAÇÕES"
                    result.textColor = Color.RED
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                if (media.algaeColor > 0.5)
                    title.text = context.getString(R.string.algas) + " Verde: "
                else
                    title.text = context.getString(R.string.algas) + " Marrom/Vermelha: "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //ALGAS
                if (media.algae in 50.0..70.0) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else if (media.algaeColor < 0.5 && media.rain > 0.5) {
                    result.text = "Resíduos acumulados!"
                    result.textColor = Color.RED
                } else {
                    result.text = "RECOMENDAÇÕES"
                    result.textColor = Color.RED
                }
            }
        }
        return infoView
    }

}

