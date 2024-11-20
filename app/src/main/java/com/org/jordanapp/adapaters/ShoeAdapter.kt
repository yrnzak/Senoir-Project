package com.org.jordanapp.adapaters


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.org.jordanapp.R
import com.org.jordanapp.ShoeDetailsActivity
import com.org.jordanapp.datamodel.Shoe

class ShoeAdapter(context: Context, private val shoesList: ArrayList<Shoe>) :
    ArrayAdapter<Shoe>(context, 0, shoesList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val shoe = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.shoe_list_item, parent, false)

        // Set up the views
        val ivShoeImage = view.findViewById<ImageView>(R.id.ivShoeImage)
        val tvShoeName = view.findViewById<TextView>(R.id.tvShoeName)
        val tvShoePrice = view.findViewById<TextView>(R.id.tvShoePrice)
        val tvShoeQuantity = view.findViewById<TextView>(R.id.tvShoeQuantity)
        val btnMore = view.findViewById<Button>(R.id.btnAddToCart)
        val btnViewMore = view.findViewById<Button>(R.id.btnViewMore)

        // Load the image using Glide
        Glide.with(context).load(shoe?.imageUrl).into(ivShoeImage)

        val sname = shoe?.name.toString()
        val price = "Price: $${shoe?.price}"

        val shoeDescriptions = ShoeDescriptions()
        val randomDescription = shoeDescriptions.getRandomDescription()


        // Set text details
        tvShoeName.text = shoe?.name
        tvShoePrice.text = "Price: $${shoe?.price}"
        tvShoeQuantity.text = "Quantity: ${shoe?.quantity}"

        // Handle "Add to Cart" button click
        btnMore.setOnClickListener {
            val intent = Intent(context, ShoeDetailsActivity::class.java)
            intent.putExtra("shoe", shoe) // Pass the shoe object to the details activity
            context.startActivity(intent)
        }

        btnViewMore.setOnClickListener {
            showShoeDetailsDialog(sname, price, randomDescription)
        }

        return view
    }


    // Function to show the shoe details dialog
    private fun showShoeDetailsDialog(shoeName: String, shoePrice: String, shoeDescription: String) {
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_shoe_details, null)

        // Find the TextViews in the dialog layout and set their values
        val tvDialogShoeName = dialogView.findViewById<TextView>(R.id.tvDialogShoeName)
        val tvDialogShoePrice = dialogView.findViewById<TextView>(R.id.tvDialogShoePrice)
        val tvDialogShoeDescription = dialogView.findViewById<TextView>(R.id.tvDialogShoeDescription)

        // Set the shoe details
        tvDialogShoeName.text = shoeName
        tvDialogShoePrice.text = shoePrice
        tvDialogShoeDescription.text = shoeDescription

        // Create and show the AlertDialog
        val dialogBuilder = AlertDialog.Builder(this.context)
            .setView(dialogView)
            .setTitle("Shoe Details")
            .setPositiveButton("OK", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
