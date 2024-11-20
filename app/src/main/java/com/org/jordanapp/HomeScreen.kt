package com.org.jordanapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.org.jordanapp.adapaters.ShoeAdapter
import com.org.jordanapp.datamodel.Shoe
import com.org.jordanapp.utils.MessageUtils

class HomeScreen : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var shoesListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var shoesAdapter: ArrayAdapter<String>
    private var shoesList: ArrayList<Shoe> = ArrayList()

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        supportActionBar?.title = "Jordan Shoes -Home"

        db = FirebaseFirestore.getInstance()
        shoesListView = findViewById(R.id.lvShoes)
        searchEditText = findViewById(R.id.etSearch)


        // Find buttons by ID
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val fabForum = findViewById<FloatingActionButton>(R.id.fabForum)
        val fabPost = findViewById<FloatingActionButton>(R.id.fabPost)



        // Set onClickListeners
        fabCart.setOnClickListener {
            // Action for Cart button
            Toast.makeText(this, "Opening Cart", Toast.LENGTH_SHORT).show()
            // Example: Start Cart Activity
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        fabForum.setOnClickListener {
            // Action for Community Forum button
            Toast.makeText(this, "Opening Forum", Toast.LENGTH_SHORT).show()
            // Example: Start Forum Activity
            val intent = Intent(this, ForumActivity::class.java)
            startActivity(intent)
        }

        fabPost.setOnClickListener {
            // Action for Post button
            Toast.makeText(this, "Creating a Post", Toast.LENGTH_SHORT).show()
            // Example: Start Post Creation Activity
            val intent = Intent(this, PostShoeActivity::class.java)
            startActivity(intent)
        }

        loadShoesData()


        // Implement Search Functionality
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


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

    private fun loadShoesData() {
        db.collection("shoes").get().addOnSuccessListener { result: QuerySnapshot ->
            shoesList.clear()
            for (document in result) {
                val shoe = document.toObject(Shoe::class.java)
                println(shoe.toString())
                shoesList.add(shoe)
            }
            displayShoes()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayShoes() {
        val shoesAdapter = ShoeAdapter(this, shoesList)
        shoesListView.adapter = shoesAdapter
    }

    private fun filter(text: String) {
        val filteredShoes = shoesList.filter { it.name.contains(text, ignoreCase = true) }
        val filteredNames = filteredShoes.map { "${it.name} - $${it.price}" }
        shoesAdapter.clear()
        shoesAdapter.addAll(filteredNames)
        shoesAdapter.notifyDataSetChanged()
    }

}