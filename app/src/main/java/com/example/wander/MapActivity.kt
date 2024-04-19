package com.example.wander

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions

class MapActivity : ComponentActivity() , AMap.OnMapLoadedListener{

    private lateinit var mapview: MapView
    private var amap: AMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
        super.onCreate(savedInstanceState)
        mapview = MapView(this)
        mapview?.onCreate(savedInstanceState)
        setContentView(mapview)
        amap = mapview!!.getMap()
        amap!!.setOnMapLoadedListener(this)
        val location = intent.getParcelableExtra<LatLng>("LOCATION")
        val name = intent.getStringExtra("NAME")
        if (location != null) {
            // 使用传递过来的地址信息
            setMapLocation(location,name)
        }
        }

    private fun setMapLocation(location: LatLng, name: String?) {
        amap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(BitmapDescriptorFactory.defaultMarker())
            .title(name) // 设置标记的标题
        val marker = amap?.addMarker(markerOptions)
        marker?.showInfoWindow() // 立即显示信息窗口
    }

    override fun onResume() {
        super.onResume()
        mapview?.onResume();
    }


    override fun onPause() {
        super.onPause()
        mapview?.onPause();
    }


    override fun onDestroy() {
        super.onDestroy()
        mapview?.onDestroy();


    }
    override fun onMapLoaded() {
    }


}