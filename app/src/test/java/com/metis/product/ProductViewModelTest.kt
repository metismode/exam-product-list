package com.metis.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.metis.product.api.ProductsService
import com.metis.product.model.Product
import com.metis.product.viewmodel.ProductViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ProductViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var productsService: ProductsService

    @InjectMocks
    var productViewModel = ProductViewModel()

    private var testSingle: Single<Product>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getProductSuccess() {
        val product = Product(1, "Title 1", "", "", true, 55.00)
        testSingle = Single.just(product)
        `when`(productsService.getProduct(1)).thenReturn(testSingle)
        productViewModel.getProduct(1)
        Assert.assertNotNull(productViewModel.product.value)
        Assert.assertEquals(false, productViewModel.isLoading.value)
        Assert.assertNull(productViewModel.isError.value)
    }

    @Test
    fun getProductFail() {
        testSingle = Single.error(Throwable())
        `when`(productsService.getProduct(1)).thenReturn(testSingle)
        productViewModel.getProduct(1)
        Assert.assertEquals(false, productViewModel.isLoading.value)
        Assert.assertNotNull(productViewModel.isError.value)
    }

    @Before
    fun setUp() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, delay, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, false)
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

}