package com.metis.product.api

import com.metis.product.model.Product
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("/products")
    fun getProductList(): Single<List<Product>>

    @GET("/products/{id}")
    fun getProduct(@Path(value = "id") id:Int) : Single<Product>
}