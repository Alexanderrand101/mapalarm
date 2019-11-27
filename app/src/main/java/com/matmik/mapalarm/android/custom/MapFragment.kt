package com.matmik.mapalarm.android.custom

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.matmik.mapalarm.android.EditNoteActivity
//import com.matmik.mapalarm.android.GlobalVariables
import com.matmik.mapalarm.android.R
import com.matmik.mapalarm.android.db.DbHelper
import com.matmik.mapalarm.android.model.Alarm
import com.matmik.mapalarm.android.model.Options
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MapFragment : Fragment() {

    var map: MapView? = null
    var mLocationOverlay: MyLocationNewOverlay? = null
    var counter: Int = 0;
    val handler = Handler()
    var lastLoc: GeoPoint? = null
    val runnableCenter = object : Runnable {
        override fun run() {
            synchronized(counter) {
                counter++
                if (mLocationOverlay!!.myLocation != null) {
                    lastLoc = mLocationOverlay!!.myLocation
                    map!!.controller!!.setCenter(mLocationOverlay!!.myLocation)
                } else {
                    if (counter < 5) {
                        handler.postDelayed(this, 1000)
                    } else {
                        if (lastLoc != null) {
                            map!!.controller!!.setCenter(lastLoc)
                        } else {

                        }
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val ctx = activity!!.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState!!.containsKey("lastlat")) {
            lastLoc = GeoPoint(
                savedInstanceState.getDouble("lastlat"),
                savedInstanceState.getDouble("lastlong")
            )
        }
        map = view!!.findViewById<MapView>(R.id.map)
        map!!.setTileSource(TileSourceFactory.MAPNIK)
        map!!.setBuiltInZoomControls(false)
        map!!.setMultiTouchControls(true)
        val mapController = map!!.controller
        mapController.setZoom(16)

        view!!.findViewById<ImageButton>(R.id.plusButton).setOnClickListener(View.OnClickListener { mapController.zoomIn() })
        view!!.findViewById<ImageButton>(R.id.minusButton).setOnClickListener(View.OnClickListener { mapController.zoomOut() })
        view!!.findViewById<ImageButton>(R.id.gpsButton).setOnClickListener(View.OnClickListener { center() })

        val startPoint = GeoPoint(53.198627, 50.113987)//mLocationOverlay!!.myLocation
        mapController.setCenter(startPoint)
        val mRecive = object : MapEventsReceiver {
            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                val editIntent = Intent(view!!.context, EditNoteActivity::class.java)
                editIntent.putExtra(
                    "EditableNote", Alarm(
                        name = "new Alarm",
                        time = Date(0),
                        options = mutableListOf(Options.Monday),
                        location = p!!.toString()
                    )
                )
                startActivity(editIntent)
                return false
            }

        }
        map!!.overlays.add(MapEventsOverlay(mRecive))
        //val obj = object: ItemizedIconOverlay.OnItemGestureListener<OverlayItem>{

            //override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                //return true
            //}

            //override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                //return false
            //}

        //}

        DbHelper(this.activity!!.applicationContext).getAllAlarms().filter { it.locationBound }.forEach {
            val latid = it.location.split(",")[0].toDouble()
            val longt = it.location.split(",")[1].toDouble()
            val marker = Marker(map)
            marker.position = GeoPoint(latid, longt)
            marker.infoWindow = AlarmInfoWindow(R.layout.alarm_info_window,
                this.activity!!.supportFragmentManager.findFragmentById(R.id.alarm_list_fragment) as RefreshableContainer? ,map!!, it)
            map!!.overlays.add(marker)
        }

        //val mOverlay = ItemizedOverlayWithFocus<OverlayItem>(GlobalVariables.items, obj, context)
        //mOverlay.setFocusItemsOnTap(true);
        //map!!.overlays.add(mOverlay)

        if (ContextCompat.checkSelfPermission(
                this.activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            centerOnMe()
        }
    }

    override fun onResume() {
        super.onResume()
        map!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        map!!.onPause()
    }

    fun centerOnMe() {
        val provider = GpsMyLocationProvider(this.activity!!.applicationContext)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        mLocationOverlay = MyLocationNewOverlay(provider, map)
        mLocationOverlay!!.enableMyLocation()
        map!!.overlays.add(mLocationOverlay)
        center()
    }


    private fun center() {
        synchronized(counter) {
            counter = 0
        }
        if (mLocationOverlay!!.myLocation == null)
            handler.postDelayed(runnableCenter, 2000)
        else {
            lastLoc = mLocationOverlay!!.myLocation
            map!!.controller!!.setCenter(mLocationOverlay!!.myLocation)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (lastLoc != null) {
            outState.putDouble("lastlat", lastLoc!!.latitude)
            outState.putDouble("lastlong", lastLoc!!.longitude)
        }
    }

    fun refreshMarkers(alarm: Alarm?){
        if (map != null) {
            val marker =
                map!!.overlays.find { it is Marker && it.infoWindow is AlarmInfoWindow && (it.infoWindow as AlarmInfoWindow).alarm == alarm} as Marker
            marker.infoWindow.close()
            map!!.overlays.remove(marker)
            map!!.invalidate()//force refresh
        }
    }

    fun snapToLoc(point: GeoPoint){
        map!!.controller.animateTo(point)
    }
}