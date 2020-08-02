package com.metis.product

import android.app.Application
import android.content.Context
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class ProductApplication : Application(){

    override fun onCreate() {
        super.onCreate()
//        initFont()

    }

    private fun initFont() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Kanit-Regular.ttf")
                            .build()
                    )
                )
                .build()
        )
    }


}