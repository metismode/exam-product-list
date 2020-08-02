package com.metis.product.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metis.product.api.ProductsService
import com.metis.product.di.DaggerApiComponent
import com.metis.product.model.Product
import com.metis.product.view.ProductActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainViewModel : ViewModel() {

    @Inject
    lateinit var productsService: ProductsService

    init {
        DaggerApiComponent.create().inject(this)
    }

    private val disposable = CompositeDisposable()

    val products = MutableLiveData<List<Product>>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Any>()
    val isEmpty = MutableLiveData<Boolean>()
    var status = MutableLiveData<String>()

    fun getProductList() {
        fetchProductList()
    }

    private fun fetchProductList() {

        isLoading.value = true
        disposable.add(
            productsService.getProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<List<Product>>() {
                    override fun onSuccess(value: List<Product>) {
                        isLoading.value = false
                        if (value.isEmpty()) {
                            isEmpty.value = true
                        } else {
                            products.value = value
                        }
                    }
                    override fun onError(e: Throwable) {
                        isLoading.value = false
                        isError.value = e.message.toString()

                    }
                })
        )
    }

    fun setStatus(msg:String){
        status.value = msg
    }

    fun clickProduct(product: Product, context: Context) {
        val intent = Intent(context, ProductActivity::class.java)
        intent.putExtra("id", product.id)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}