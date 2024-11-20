package com.org.jordanapp


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.org.jordanapp.utils.MessageUtils
import java.util.UUID

class CreatePostActivity : AppCompatActivity() {

    private lateinit var etPostTitle: EditText
    private lateinit var etPostContent: EditText
    private lateinit var btnUploadImage: Button
    private lateinit var btnSubmitPost: Button

    private var selectedImageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        supportActionBar?.title = "Jordan Shoes -Sell Shoes"

        etPostTitle = findViewById(R.id.etPostTitle)
        etPostContent = findViewById(R.id.etPostContent)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        btnSubmitPost = findViewById(R.id.btnSubmitPost)

        // Handle image upload
        btnUploadImage.setOnClickListener {
            openImagePicker()
        }

        // Handle post submission
        btnSubmitPost.setOnClickListener {
            submitPost()
        }


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

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

    private fun submitPost() {
        val title = etPostTitle.text.toString().trim()
        val content = etPostContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload image if selected, then save post to Firestore
        if (selectedImageUri != null) {
            uploadImageAndSavePost(title, content)
        } else {
            savePostToFirestore(title, content, null)
        }
    }

    private fun uploadImageAndSavePost(title: String, content: String) {
        val fileName = UUID.randomUUID().toString()
        val storageRef = storage.reference.child("forum_images/$fileName")

        selectedImageUri?.let { uri ->
            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    savePostToFirestore(title, content, downloadUrl.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePostToFirestore(title: String, content: String, imageUrl: String?) {
        val currentUser = auth.currentUser // Get the current user

        val username  = currentUser?.displayName ?: "Anonymous"
        val useremail =  currentUser?.email ?: "Anonymous"
        val post = hashMapOf(
            "userName" to username, // Replace with actual user's name
            "userEmail" to useremail,
            "postTitle" to title,
            "postContent" to content,
            "userImageUrl" to imageUrl
        )

        db.collection("forumPosts").add(post).addOnSuccessListener {
            Toast.makeText(this, "Post submitted successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to submit post", Toast.LENGTH_SHORT).show()
        }
    }
}
