package com.metis.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.metis.product.api.ProductsService
import com.metis.product.model.Product
import com.metis.product.viewmodel.MainViewModel
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

class MainViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var productsService: ProductsService

    @InjectMocks
    var mainViewModel = MainViewModel()

    private var testSingle: Single<List<Product>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getProductListSuccess() {
        val productList = arrayListOf(
            Product(1, "Title 1", "", "", true, 55.00),
            Product(2, "Title 2", "", "", false, 155.00),
            Product(3, "Title 3", "", "", true, 0.00)
        )
        testSingle = Single.just(productList)
        `when`(productsService.getProductList()).thenReturn(testSingle)
        mainViewModel.getProductList()
        Assert.assertEquals(3, mainViewModel.products.value?.size)
        Assert.assertEquals(false, mainViewModel.isLoading.value)
        Assert.assertNull(mainViewModel.isError.value)
    }

    @Test
    fun getProductListEmpty() {
        val productList = emptyList<Product>()
        testSingle = Single.just(productList)
        `when`(productsService.getProductList()).thenReturn(testSingle)
        mainViewModel.getProductList()
        Assert.assertEquals(true, mainViewModel.isEmpty.value)
        Assert.assertEquals(false, mainViewModel.isLoading.value)
        Assert.assertNull(mainViewModel.isError.value)
    }

    @Test
    fun getProductListFail() {
        testSingle = Single.error(Throwable())
        `when`(productsService.getProductList()).thenReturn(testSingle)
        mainViewModel.getProductList()
        Assert.assertEquals(false, mainViewModel.isLoading.value)
        Assert.assertNotNull(mainViewModel.isError.value)
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