package com.org.jordanapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.org.jordanapp.adapters.ForumPostAdapter
import com.org.jordanapp.datamodel.ForumPost
import com.org.jordanapp.utils.MessageUtils

class ForumActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var rvForumPosts: RecyclerView
    private var forumPostsList: ArrayList<ForumPost> = ArrayList()
    private lateinit var forumPostAdapter: ForumPostAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        supportActionBar?.title = "Jordan Shoes - Forum"

        db = FirebaseFirestore.getInstance()
        rvForumPosts = findViewById(R.id.rvForumPosts)

        // Pass 'this' as context to the adapter for interaction handling
        forumPostAdapter = ForumPostAdapter(forumPostsList, this)
        rvForumPosts.adapter = forumPostAdapter

        val fabAddPost = findViewById<FloatingActionButton>(R.id.fabAddPost)
        fabAddPost.setOnClickListener {
            // Navigate to CreatePostActivity to add a new forum post
            startActivity(Intent(this, CreatePostActivity::class.java))
        }

        loadForumPosts()
    }

    override fun onResume() {
        super.onResume()
        loadForumPosts() // Reload forum posts every time activity resumes
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

    // Function to load forum posts from Firestore
    private fun loadForumPosts() {
        db.collection("forumPosts").get().addOnSuccessListener { result: QuerySnapshot ->
            forumPostsList.clear()
            for (document in result) {
                val post = document.toObject(ForumPost::class.java)
                post.postId = document.id // Capture Firestore document ID for interaction use
                forumPostsList.add(post)
            }
            forumPostAdapter.notifyDataSetChanged() // Notify adapter of data changes
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load posts", Toast.LENGTH_SHORT).show()
        }
    }
}
