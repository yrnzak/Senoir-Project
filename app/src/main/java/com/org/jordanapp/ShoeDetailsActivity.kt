package com.org.jordanapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.org.jordanapp.datamodel.Cart
import com.org.jordanapp.datamodel.CartItem
import com.org.jordanapp.datamodel.Shoe
import com.org.jordanapp.utils.MessageUtils

class ShoeDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shoe_details)

        supportActionBar?.title = "Shoe Details - More Details"


        // Retrieve the passed Shoe object
        val shoe = intent.getSerializableExtra("shoe") as? Shoe


        // Find views by ID
        val ivShoeImage = findViewById<ImageView>(R.id.ivShoeImageFull)
        val tvShoeName = findViewById<TextView>(R.id.tvShoeNameDetail)
        val tvShoePrice = findViewById<TextView>(R.id.tvShoePriceDetail)
        val tvShoeQuantity = findViewById<TextView>(R.id.tvShoeQuantityDetail)
        val btnAddToCart = findViewById<Button>(R.id.btnAddToCartDetail)


        // Set shoe details to the views
        shoe?.let {
            Glide.with(this).load(it.imageUrl).into(ivShoeImage)
            tvShoeName.text = it.name
            tvShoePrice.text = "Price: $${it.price}"
            tvShoeQuantity.text = "Quantity: ${it.quantity}"
        }

        // Handle "Add to Cart" button click
        btnAddToCart.setOnClickListener {
            Toast.makeText(this, "${shoe?.name} added to cart", Toast.LENGTH_SHORT).show()
            // Add logic to handle adding the shoe to the cart
            showAddToCartDialog(shoe)
        }

        val fabAddToWishlist = findViewById<FloatingActionButton>(R.id.fabAddToWishlist)
        fabAddToWishlist.setOnClickListener {

            // Create an Intent to open CartActivity
            val intent = Intent(this, CartActivity::class.java)

            // Start CartActivity
            startActivity(intent)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, login_activity::class.java))
                finish()
                return true
            }

            R.id.action_home -> {

                startActivity(Intent(this, HomeScreen::class.java))
                finish()
                return true
            }

            R.id.action_send_message -> {
                // Use the MessageUtils to show the send message dialog
                MessageUtils.showSendMessageDialog(this)
                return true
            }

            R.id.action_view_message -> {
                // Use the MessageUtils to show the send message dialog
                val intent = Intent(this, MessageActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.action_sent_message -> {
                // Use the MessageUtils to show the send message dialog
                val intent = Intent(this, MessageSentActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddToCartDialog(shoe: Shoe?) {
        // Inflate the dialog with the custom layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_to_cart, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val alertDialog = builder.create()

        // Access the dialog views
        val etNumberOfPairs = dialogView.findViewById<EditText>(R.id.etNumberOfPairs)
        val btnDialogAdd = dialogView.findViewById<Button>(R.id.btnDialogAdd)
        val btnDialogCancel = dialogView.findViewById<Button>(R.id.btnDialogCancel)

        // Handle Add button click
        btnDialogAdd.setOnClickListener {
            val numberOfPairs = etNumberOfPairs.text.toString().toIntOrNull()
            val shoename : String = shoe?.name ?: ""
            val shoePrice: Double = shoe?.price ?: 0.0


            if (numberOfPairs != null && numberOfPairs > 0) {

                // Create a cart item
                val cartItem = CartItem(shoename, shoePrice,numberOfPairs)

                // Add the item to the cart
                Cart.addItem(cartItem)

                // Show confirmation
                Toast.makeText(this, "$shoename added to cart!", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Cancel button click
        btnDialogCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}


