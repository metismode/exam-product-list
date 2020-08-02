package com.metis.product.util

import android.os.SystemClock
import android.view.View

abstract class OnSingleClickListener : View.OnClickListener {

    private val MIN_CLICK_INTERVAL: Long = 1000

    private var mLastClickTime: Long = 0

    abstract fun onSingleClick(v: View?)

    final override fun onClick(v: View?) {

        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime

        mLastClickTime = currentClickTime

        if (elapsedTime <= MIN_CLICK_INTERVAL) return

        onSingleClick(v)

    }


}