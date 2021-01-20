package com.bmanchi.flickrbrowser

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

private const val TAG = "BaseActivity"
internal const val FLICKR_QUERY = "FLICK_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"

open class BaseActivity: AppCompatActivity() {
    internal fun activateToolbar(enableHome: Boolean) {
        Log.d(TAG, ".activateToolbar")

        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)

    }
}