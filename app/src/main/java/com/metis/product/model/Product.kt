package com.metis.product.model

import java.io.Serializable

data class Product(
    val id: Int,
    val title: String,
    val image: String,
    val content: String,
    val isNewProduct: Boolean,
    val price: Double
): Serializable
