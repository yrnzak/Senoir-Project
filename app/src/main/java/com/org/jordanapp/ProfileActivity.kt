package com.org.jordanapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.org.jordanapp.adapaters.TransactionAdapter
import com.org.jordanapp.datamodel.Transaction
import com.org.jordanapp.utils.MessageUtils


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var ivProfilePicture: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserPhone: TextView
    private lateinit var btnLogout: Button
    private lateinit var lvTransactions: ListView // ListView for displaying transactions
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = "Jordan Shoes -Profile"

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        tvUserName = findViewById(R.id.tvUserName)
        tvUserEmail = findViewById(R.id.tvUserEmail)
        tvUserPhone = findViewById(R.id.tvUserPhone)

        lvTransactions = findViewById(R.id.lvTransactions) // Initialize ListView

        loadUserProfile()

        loadUserTransactions()


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

    private fun loadUserProfile() {
        val user = currentUser?.email
        if (user != null) {

            tvUserEmail.text = "Email Address: "+user
                        // Retrieve user details from Firestore
            db.collection("users")
                .whereEqualTo("email", user)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    for (document in querySnapshot) {
                        tvUserName.text = document.getString("firstName") + " " + document.getString("lastName")
                        tvUserEmail.text = document.getString("email")
                        tvUserPhone.text = document.getString("phone")
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load user details.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserTransactions() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid

            // Query Firestore for transactions by this user
            db.collection("transactions")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { result ->
                    val transactions = mutableListOf<Transaction>()

                    // Loop through the results and add each transaction to the list
                    for (document: QueryDocumentSnapshot in result) {
                        val transaction = document.toObject(Transaction::class.java)
                        transactions.add(transaction)
                    }

                    // Set the adapter to the ListView to display transactions
                    val adapter = TransactionAdapter(this, transactions)
                    lvTransactions.adapter = adapter
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load transactions.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
