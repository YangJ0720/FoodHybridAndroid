package com.hybrid.food.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hybrid.food.R
import com.hybrid.food.boost.PageRouter
import com.idlefish.flutterboost.containers.BoostFlutterActivity

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            val map = HashMap<String, Any>()
            PageRouter.openPageByUrl(this, PageRouter.URL_HOME, map)
            finish()
        }, 1000)
    }

    override fun onBackPressed() {

    }
}
