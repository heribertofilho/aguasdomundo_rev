package br.heriberto.aguasdomundo

import android.Manifest
import android.content.Context
import android.content.Intent
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
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.maps.android.heatmaps.HeatmapTileProvider
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast


class MapsFragment : Fragment(), AnkoLogger, OnMapReadyCallback, LocationListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var retrieveDataListener: RetrieveDataListener
    private lateinit var medias: Array<Media>

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
        } catch (castException: ClassCastException) {   }
    }

    fun updateMap(medias: Array<Media>) {
        val list: ArrayList<LatLng> = ArrayList()
        this.medias = medias
        medias.forEach { m ->
            var media = m
            val latlng = LatLng(m.latitude, m.longitude)
            list.add(latlng)
            mMap.addMarker(MarkerOptions().position(latlng))
        }

        val mProvider = HeatmapTileProvider.Builder()
                .data(list)
                .build()
        // Add a tile overlay to the map, using the heat map tile provider.
        val mOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }

    override fun onInfoWindowClick(marker: Marker) {
        var media: Media? = null
        medias.forEach { m ->
            if (m.latitude == marker.position.latitude && m.longitude == marker.position.longitude) {
                media = m
                return@forEach
            }
        }
        var oxi = false
        var dur = false
        var ph = false
        var tur = false
        var algas = false
        if (media!!.superficie > 5)
            oxi = true
        if(media!!.dureza > 5)
            dur = true
        if (media!!.peixe_apatico > 5)
            ph = true
        if(media!!.turbidez > 80)
            tur = true
        if(media!!.algae !in 50.0..70.0)
            algas = true
        recomendacoes(oxi, dur, ph, tur, algas)
    }

    private fun prepareInfoView(marker: Marker): View {
        //prepare InfoView programmatically
        var media: Media? = null
        medias.forEach { m ->
            if (m.latitude == marker.position.latitude && m.longitude == marker.position.longitude) {
                media = m
                return@forEach
            }
        }
        val ctx = AnkoContext.create(context)
        val infoView = ctx.verticalLayout {
            var oxi = false
            var dur = true
            var ph = false
            var tur = false
            var algas = false
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.oxigenio) + ": "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //OXIGENIO
                if (media!!.superficie < 5) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else {
                    result.text = context.getString(R.string.recomendacoes)
                    result.textColor = Color.RED
                    oxi = true
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.dureza) + ": "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //DUREZA
                if (media!!.dureza > 5) {
                    result.text = context.getString(R.string.camaroes)
                    result.textColor = Color.BLUE
                } else {
                    result.text = context.getString(R.string.peixes)
                    result.textColor = Color.BLUE
                }
                dur = true
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.ph_variante) + ": "
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //PH
                if (media!!.peixe_apatico < 5) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else {
                    result.text = context.getString(R.string.recomendacoes)
                    result.textColor = Color.RED
                    ph = true
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                var cor: String = context.getString(R.string.algas) + " "
                if (media!!.algaeColor > 0.5) {
                    cor += context.getString(R.string.verde)
                    title.text = cor
                } else {
                    cor += context.getString(R.string.marrom) + " "
                    title.text = cor
                }
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //ALGAS
                if (media!!.algae in 50.0..70.0) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else if (media!!.algaeColor < 0.5 && media!!.rain > 0.5) {
                    tur = true
                    algas = true
                    result.text = context.getString(R.string.residuos)
                    result.textColor = Color.RED
                } else {
                    algas = true
                    result.text = context.getString(R.string.recomendacoes)
                    result.textColor = Color.RED
                }
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                val title = textView()
                title.text = context.getString(R.string.turbidez)
                val result = textView()
                result.typeface = Typeface.DEFAULT_BOLD
                //OXIGENIO
                if (media!!.turbidez > 80) {
                    result.text = context.getString(R.string.condicoes_ideais)
                    result.textColor = Color.GREEN
                } else {
                    result.text = context.getString(R.string.recomendacoes)
                    result.textColor = Color.RED
                    tur = true
                }
            }
            if (oxi || dur || ph || tur || algas) {
                longToast("Aquífero não está em condições ideais, clique no balão acima dele para ver recomendações")
            }
        }
        return infoView
    }

    fun recomendacoes(oxi: Boolean, dur: Boolean, ph: Boolean, tur: Boolean, algas: Boolean) {
        val i = Intent(context, RecomendacoesActivity::class.java)
        i.putExtra("oxigenio", oxi)
        i.putExtra("dureza", dur)
        i.putExtra("ph", ph)
        i.putExtra("turbidez", tur)
        i.putExtra("algas", algas)
        startActivity(i)
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        mMap.uiSettings.setAllGesturesEnabled(false)

        mMap.setMaxZoomPreference(15f)
        mMap.setMinZoomPreference(15f)

        mMap.setInfoWindowAdapter(this)
        mMap.setOnInfoWindowClickListener(this)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, this)
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null)
            return
        val latLng = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        retrieveDataListener.retrieveData(latLng)

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
}

