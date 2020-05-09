package com.hybrid.food.map.location

import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation

/**
 * GPS定位成功
 */
private const val LOC_TYPE_GPS_SUCCESS = 61

/**
 * 网络定位成功
 */
private const val LOC_TYPE_NETWORK_SUCCESS = 161

/**
 * @author YangJ
 */
class SimpleBDAbstractLocationListener : BDAbstractLocationListener {

    private var mListener: OnLocationListener

    constructor(listener: OnLocationListener) : super() {
        this.mListener = listener
    }

    override fun onReceiveLocation(location: BDLocation) {
        val locType = location.locType
        if (locType == LOC_TYPE_GPS_SUCCESS || locType == LOC_TYPE_NETWORK_SUCCESS) { // GPS定位成功 or 网络定位成功
            val city = location.city
            val district = location.district
            val street = location.street
            val town = location.town
            val streetNumber = location.streetNumber
            val address = "$city$district$town$street$streetNumber"
            mListener.onLocation(location, address)
        } else { // 网络定位失败
            mListener.onLocation(location, null)
        }
    }

    interface OnLocationListener {
        fun onLocation(location: BDLocation, address: String?)
    }
}