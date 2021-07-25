package com.glc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import java.util.*

class BdMap : AppCompatActivity() {
    var mLocationClient: LocationClient? = null
    private var positionText: TextView? = null
    private var mapView: MapView? = null
    private lateinit var baiduMap: BaiduMap
    private var isFirstLocate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLocationClient = LocationClient(applicationContext)
        mLocationClient!!.registerLocationListener(MyLocationListener())
        SDKInitializer.initialize(applicationContext)
        setContentView(R.layout.activity_bd_map)
        mapView = findViewById<View>(R.id.bmapView) as MapView
        baiduMap = mapView!!.map
        baiduMap.setMyLocationEnabled(true)
        positionText = findViewById<View>(R.id.position_text_view) as TextView
        val permissionList: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                this@BdMap,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                this@BdMap,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (ContextCompat.checkSelfPermission(
                this@BdMap,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!permissionList.isEmpty()) {
            val permissions = permissionList.toTypedArray()
            ActivityCompat.requestPermissions(this@BdMap, permissions, 1)
        } else {
            requestLocation()
        }
    }

    private fun navigateTo(location: BDLocation) {
        if (isFirstLocate) {
            Toast.makeText(this, "nav to " + location.addrStr, Toast.LENGTH_SHORT).show()
            val ll = LatLng(location.latitude, location.longitude)
            var update = MapStatusUpdateFactory.newLatLng(ll)
            baiduMap!!.animateMapStatus(update)
            update = MapStatusUpdateFactory.zoomTo(16f)
            baiduMap!!.animateMapStatus(update)
            isFirstLocate = false
        }
        val locationBuilder = MyLocationData.Builder()
        locationBuilder.latitude(location.latitude)
        locationBuilder.longitude(location.longitude)
        val locationData = locationBuilder.build()
        baiduMap!!.setMyLocationData(locationData)
    }

    private fun requestLocation() {
        initLocation()
        mLocationClient!!.start()
    }

    private fun initLocation() {
        val option = LocationClientOption()
        option.setScanSpan(5000)
        option.setIsNeedAddress(true)
        mLocationClient!!.locOption = option
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient!!.stop()
        mapView!!.onDestroy()
        baiduMap!!.isMyLocationEnabled = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults.size > 0) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show()
                        finish()
                        return
                    }
                }
                requestLocation()
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
            }
        }
    }

    inner class MyLocationListener : BDLocationListener {
        override fun onReceiveLocation(location: BDLocation) {
            if (location.locType == BDLocation.TypeGpsLocation
                || location.locType == BDLocation.TypeNetWorkLocation
            ) {
                navigateTo(location)
            }
        }
    }
}