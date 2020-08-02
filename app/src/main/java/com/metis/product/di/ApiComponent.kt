package com.metis.product.di

import com.metis.product.api.ProductsService
import com.metis.product.viewmodel.MainViewModel
import com.metis.product.viewmodel.ProductViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: ProductsService)

    fun inject(viewModel: MainViewModel)

    fun inject(viewModel: ProductViewModel)
}