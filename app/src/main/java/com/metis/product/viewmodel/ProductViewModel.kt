package com.metis.product.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.metis.product.R
import com.metis.product.api.ProductsService
import com.metis.product.databinding.ActivityProductBinding
import com.metis.product.di.DaggerApiComponent
import com.metis.product.model.Product
import com.metis.product.util.getProgressDrawable
import com.metis.product.util.loadImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductViewModel : ViewModel() {

    @Inject
    lateinit var productsService: ProductsService

    init {
        DaggerApiComponent.create().inject(this)
    }

    private val disposable = CompositeDisposable()

    val product = MutableLiveData<Product>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Any>()
    val isEmpty = MutableLiveData<Boolean>()

    var title = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var content = MutableLiveData<String>()
    var new = MutableLiveData<String>()
    var status = MutableLiveData<String>()

    fun getProduct(id: Int) {
        fetchProduct(id)
    }

    private fun fetchProduct(id: Int) {

        isLoading.value = true
        disposable.add(
            productsService.getProduct(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                    DisposableSingleObserver<Product>() {
                    override fun onSuccess(value: Product) {
                        isLoading.value = false
                        product.value = value
                    }
                    override fun onError(e: Throwable) {
                        isLoading.value = false
                        isError.value = e.message.toString()
                    }
                })
        )
    }


    fun setDetail(
        product: Product,
        view: ActivityProductBinding,
        context: Context
    ) {

        title.value = product.title
        price.value = product.price.toString()
        content.value = product.content

        if (product.isNewProduct) {
            new.value = context.getString(R.string.product_new)
        } else {
            new.value = ""
        }

        if (!(context as Activity).isFinishing) {
            view.image.loadImage(product.image, getProgressDrawable(context))
        }
    }

    fun setStatus(msg:String){
        status.value = msg
    }
}
