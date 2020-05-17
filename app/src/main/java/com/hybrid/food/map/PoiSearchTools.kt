package com.hybrid.food.map

import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*

/**
 * @author YangJ
 */
class PoiSearchTools {

    private var isExistPoi = false
    private var mPoiSearch: PoiSearch
    private var mListener: OnGetPoiResultListener? = null

    constructor() {
        val poiSearch = PoiSearch.newInstance()
        poiSearch.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
            override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {}

            override fun onGetPoiResult(poiResult: PoiResult) {
                if (SearchResult.ERRORNO.NO_ERROR == poiResult.error) {
                    // 需要两个覆盖物分别展示商家和骑手
                    if (poiResult.allPoi.size >= 2 && !isExistPoi) {
                        isExistPoi = true
                        mListener?.onGetPoiResult(poiResult)
                    }
                }
            }

            override fun onGetPoiDetailResult(p0: PoiDetailResult?) {}

            override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {}

        })
        this.mPoiSearch = poiSearch
    }

    fun searchNearby(lat: Double, lon: Double) {
        searchNearby(LatLng(lat, lon))
    }

    fun searchNearby(latLng: LatLng) {
        if (isExistPoi) return
        // 开始执行poi检索
        val options =
            PoiNearbySearchOption().location(latLng).radius(5000).keyword("商店").pageNum(1)
        this.mPoiSearch.searchNearby(options)
    }

    fun destroy() {
        this.mPoiSearch.destroy()
    }

    fun setOnGetPoiResultListener(listener: OnGetPoiResultListener?) {
        this.mListener = listener
    }

    interface OnGetPoiResultListener {
        fun onGetPoiResult(poiResult: PoiResult)
    }
}