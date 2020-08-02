package com.metis.product.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metis.product.model.Product
import com.metis.product.R
import com.metis.product.util.OnSingleClickListener
import com.metis.product.util.getProgressDrawable
import com.metis.product.util.loadImage
import kotlinx.android.synthetic.main.list_product.view.*

class ProductAdapter(private var productList: List<Product>, private val clickListener:(Product)->Unit) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.list_product,parent,false)

        return MyViewHolder(listItem,parent.context)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(productList[position],clickListener)
    }

    fun update(data:List<Product>){
        productList = data
        notifyDataSetChanged()
    }

    fun clear(){
        productList = emptyList()
        notifyDataSetChanged()
    }
}


class MyViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product, clickListener:(Product)->Unit){

        view.title.text = product.title
        view.price.text = product.price.toString()
        view.image.loadImage(product.image,getProgressDrawable(context))
        view.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                clickListener(product)
            }
        })

        if(product.isNewProduct){
            view.product_new.text = context.getString(R.string.product_new)
        }else{
            view.product_new.text = ""
        }

    }
}