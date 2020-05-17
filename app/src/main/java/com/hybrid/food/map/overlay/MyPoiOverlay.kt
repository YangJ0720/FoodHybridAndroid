package com.hybrid.food.map.overlay

import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.poi.PoiResult
import com.hybrid.food.map.utils.OverlayManager
import com.hybrid.food.map.utils.PoiOverlay
import java.util.*

private const val MAX_POI_SIZE = 2

/**
 * @author YangJ
 */
class MyPoiOverlay : PoiOverlay {

    constructor(baiduMap: BaiduMap?) : super(baiduMap)

}