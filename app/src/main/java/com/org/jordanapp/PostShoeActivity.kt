package com.org.jordanapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.org.jordanapp.utils.MessageUtils
import java.util.UUID

class PostShoeActivity : AppCompatActivity() {

    private lateinit var etShoeName: EditText
    private lateinit var etShoePrice: EditText
    private lateinit var etShoeQuantity: EditText
    private lateinit var btnSelectImage: Button
    private lateinit var ivSelectedImage: ImageView
    private lateinit var btnPostShoe: Button


    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_shoe)

        supportActionBar?.title = "Jordan Shoes - Post Shoes"


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        etShoeName = findViewById(R.id.etShoeName)
        etShoePrice = findViewById(R.id.etShoePrice)
        etShoeQuantity = findViewById(R.id.etShoeQuantity)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        ivSelectedImage = findViewById(R.id.ivSelectedImage)
        btnPostShoe = findViewById(R.id.btnPostShoe)

        btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        btnPostShoe.setOnClickListener {
            uploadShoe()
        }
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            ivSelectedImage.setImageURI(selectedImageUri)
            ivSelectedImage.visibility = ImageView.VISIBLE
        }
    }

    private fun uploadShoe() {
        val name = etShoeName.text.toString().trim()
        val price = etShoePrice.text.toString().trim().toDoubleOrNull()
        val quantity = etShoeQuantity.text.toString().trim().toIntOrNull()

        if (name.isEmpty() || price == null || quantity == null || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all details and select an image.", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload image to Firebase Storage
        val storageRef = storage.reference.child("shoes_images/${UUID.randomUUID()}.jpg")
        storageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    saveShoeData(name, price, quantity, imageUrl)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveShoeData(name: String, price: Double, quantity: Int, imageUrl: String) {
        val shoe = hashMapOf(
            "name" to name,
            "price" to price,
            "quantity" to quantity,
            "imageUrl" to imageUrl
        )

        db.collection("shoes").add(shoe)
            .addOnSuccessListener {
                Toast.makeText(this, "Shoe posted successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to post shoe.", Toast.LENGTH_SHORT).show()
            }
    }
}