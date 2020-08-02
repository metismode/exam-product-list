package com.metis.product.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.metis.product.R
import com.metis.product.adapter.ProductAdapter
import com.metis.product.databinding.ActivityMainBinding
import com.metis.product.model.Product
import com.metis.product.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupUI()
        setupViewModel()
        getProductList()

    }

    private fun getProductList() {
        viewModel.getProductList()
    }

    private fun setupUI() {
        adapter = ProductAdapter(viewModel.products.value ?: emptyList())
        { product: Product -> viewModel.clickProduct(product, this) }
        binding.recyclerViewProduct.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewProduct.adapter = adapter
        binding.reload.setOnRefreshListener {
            adapter.clear()
            getProductList()
        }
    }

    private fun setupViewModel() {

        viewModel.products.observe(this, Observer<List<Product>> {
            binding.status.visibility = View.GONE
            binding.loading.visibility = View.GONE
            it.let {
                adapter.update(it)
                binding.reload.isRefreshing = false
            }
        })

        viewModel.isLoading.observe(this, Observer<Boolean> {
            val visibility = if (it) View.VISIBLE else View.GONE
            binding.loading.visibility = visibility
            binding.status.visibility = View.GONE
        })

        viewModel.isError.observe(this, Observer<Any> {
            binding.status.visibility = View.VISIBLE
            binding.reload.isRefreshing = false
            viewModel.setStatus(resources.getString(R.string.error))
        })

        viewModel.isEmpty.observe(this, Observer<Boolean> {
            binding.status.visibility = View.VISIBLE
            binding.reload.isRefreshing = false
            viewModel.setStatus(resources.getString(R.string.product_list_empty))
        })
    }
}