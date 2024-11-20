package com.org.jordanapp.adapaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.org.jordanapp.R
import com.org.jordanapp.datamodel.CartItem

class CartAdapter(private val context: Context, private val cartItems: List<CartItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return cartItems.size
    }

    override fun getItem(position: Int): Any {
        return cartItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)

        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        val tvProductPrice = view.findViewById<TextView>(R.id.tvProductPrice)
        val tvProductQuantity = view.findViewById<TextView>(R.id.tvProductQuantity)
        val tvTotalCost = view.findViewById<TextView>(R.id.tvTotalCost)

        val cartItem = cartItems[position]

        tvProductName.text = cartItem.name
        tvProductPrice.text = "Price: $${cartItem.price}"
        tvProductQuantity.text = "Quantity: ${cartItem.quantity}"
        tvTotalCost.text = "Total: $${cartItem.price * cartItem.quantity}"

        return view
    }
}
