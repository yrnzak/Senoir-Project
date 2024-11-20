package com.org.jordanapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.org.jordanapp.adapaters.CartAdapter
import com.org.jordanapp.datamodel.Cart
import com.org.jordanapp.datamodel.CartItem
import com.org.jordanapp.utils.MessageUtils

class CartActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        supportActionBar?.title = "Shoe Details - Cart"

        // Fetch the cart items from your Cart class
        val cartItems = Cart.getItems()

        // Check if the cart is empty
        if (cartItems.isEmpty()) {
            showEmptyCartDialog()
        } else {
            // Set up the ListView with the CartAdapter
            val lvCartItems = findViewById<ListView>(R.id.lvCartItems)
            val adapter = CartAdapter(this, cartItems)
            lvCartItems.adapter = adapter
        }

        val fabAddToWishlist = findViewById<FloatingActionButton>(R.id.fabAddToWishlist)
        fabAddToWishlist.setOnClickListener {
            checkout(cartItems)  // Pass your cart items list here
        }
    }

    private fun showEmptyCartDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cart is Empty")
        builder.setMessage("Your cart is currently empty. Please add items to proceed.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            startActivity(Intent(this, HomeScreen::class.java))
            finish()
        }
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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

    // Updated checkout function
    fun checkout(cartItems: List<CartItem>) {
        for (item in cartItems) {
            val shoeName = item.name

            // Reference to the Firestore document by shoe name
            val shoeRef = db.collection("shoes").whereEqualTo("name", shoeName)

            // Fetch the current quantity of the shoe from Firestore
            shoeRef.get().addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val currentQuantity = document.getLong("quantity") ?: 0

                    // Calculate new quantity
                    val newQuantity = currentQuantity - item.quantity

                    // Update the quantity in Firestore
                    if (newQuantity >= 0) {
                        document.reference.update("quantity", newQuantity).addOnSuccessListener {
                            Toast.makeText(this, "${item.name} updated in inventory.", Toast.LENGTH_SHORT).show()

                            // Store transaction details in Firestore
                            storeTransaction(item)

                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to update ${item.name}: $e", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "${item.name} is out of stock.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Shoe ${item.name} not found in Firestore.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching ${item.name}: $e", Toast.LENGTH_SHORT).show()
            }
        }

        // Clear the cart after checkout
        Cart.clearItems()

        // Show shipment modal after checkout
        showShipmentModal()
    }

    // Function to store transaction in Firestore
    private fun storeTransaction(item: CartItem) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid ?: "unknown_user"  // Fallback to a default value if user ID is not available

        val transactionMap = mapOf(
            "userId" to userId,
            "itemName" to item.name,
            "quantity" to item.quantity,
            "transactionDate" to Timestamp.now(),  // Store as a timestamp
            "totalCost" to item.price * item.quantity
        )

        db.collection("transactions").add(transactionMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Transaction recorded successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to record transaction: $e", Toast.LENGTH_SHORT).show()
            }
    }


    // Function to show the shipment modal
    private fun showShipmentModal() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Checkout Complete")
        builder.setMessage("Your order has been placed. Shipment will be executed in a few days.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            startActivity(Intent(this, HomeScreen::class.java))
            finish()
        }
        builder.show()
    }
}
