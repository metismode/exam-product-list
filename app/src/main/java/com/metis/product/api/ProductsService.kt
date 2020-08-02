package com.metis.product.api

import com.metis.product.di.DaggerApiComponent
import com.metis.product.model.Product
import io.reactivex.Single
import javax.inject.Inject

class ProductsService {

    @Inject
    lateinit var api: ProductApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getProductList(): Single<List<Product>> {
        return api.getProductList()
    }

    fun getProduct(id:Int): Single<Product> {
        return api.getProduct(id)
    }

}