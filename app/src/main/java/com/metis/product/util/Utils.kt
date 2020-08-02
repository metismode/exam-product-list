package com.metis.product.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.metis.product.R

object Utils {

    fun alert(message:String,context: Context){
        val builder = AlertDialog.Builder(context)
        with(builder)
        {
            setTitle("Opp..")
            setMessage(message)
            setNegativeButton("close") { _: DialogInterface, _: Int ->

            }
            show()
        }
    }

}

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_baseline_image_24)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}