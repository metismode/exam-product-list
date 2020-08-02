package com.metis.product.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.metis.product.R
import com.metis.product.databinding.ActivityProductBinding
import com.metis.product.model.Product
import com.metis.product.viewmodel.ProductViewModel

class ProductActivity : BaseActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupUI()
        setupViewModel()
        getProduct(getProductId())
    }

    private fun getProduct(id: Int) {
        viewModel.getProduct(id)
    }

    private fun getProductId() : Int {
        return if (intent.hasExtra("id")) {
            intent.getIntExtra("id", 0)
        }else{
            0
        }
    }

    private fun setupUI() {
        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {

        viewModel.product.observe(this, Observer<Product> {
            binding.status.visibility = View.GONE
            binding.loading.visibility = View.GONE
            it.let {
                viewModel.setDetail(it, binding, this)
            }
        })

        viewModel.isLoading.observe(this, Observer<Boolean> {
            val visibility = if (it) View.VISIBLE else View.GONE
            binding.status.visibility = View.GONE
            binding.loading.visibility = visibility
        })

        viewModel.isError.observe(this, Observer<Any> {
            binding.status.visibility = View.GONE
        })

        viewModel.isEmpty.observe(this, Observer<Boolean> {
            binding.status.visibility = View.VISIBLE
        })

        viewModel.isError.observe(this, Observer<Any> {
            binding.status.visibility = View.VISIBLE
            viewModel.setStatus(resources.getString(R.string.error))
        })

        viewModel.isEmpty.observe(this, Observer<Boolean> {
            binding.status.visibility = View.VISIBLE
            viewModel.setStatus(resources.getString(R.string.product_list_empty))
        })
    }
}