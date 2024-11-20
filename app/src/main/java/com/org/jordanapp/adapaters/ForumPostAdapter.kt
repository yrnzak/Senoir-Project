package com.org.jordanapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.org.jordanapp.R
import com.org.jordanapp.datamodel.ForumPost
import com.org.jordanapp.datamodel.Reply

class ForumPostAdapter(
    private val forumPostsList: ArrayList<ForumPost>,
    private val context: Context
) : RecyclerView.Adapter<ForumPostAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterUser: TextView = view.findViewById(R.id.postUser)
        val postTitle: TextView = view.findViewById(R.id.postTitle)
        val postContent: TextView = view.findViewById(R.id.postContent)
        val likeButton: ImageButton = view.findViewById(R.id.likeButton)
        val dislikeButton: ImageButton = view.findViewById(R.id.dislikeButton)
        val likeCount: TextView = view.findViewById(R.id.likeCount)
        val dislikeCount: TextView = view.findViewById(R.id.dislikeCount)
        val replyButton: TextView = view.findViewById(R.id.replyButton)
        val replySection: TextView = view.findViewById(R.id.replySection) // Added for showing replies
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = forumPostsList[position]

        // Set post data
        holder.posterUser.text = post.userEmail
        holder.postTitle.text = post.postTitle
        holder.postContent.text = post.postContent
        holder.likeCount.text = post.likes.toString()
        holder.dislikeCount.text = post.dislikes.toString()

        // Show replies (if any)
        holder.replySection.text = post.replies.joinToString("\n") { "${it.userName}: ${it.replyText}" }

        // Like functionality
        holder.likeButton.setOnClickListener {

            if (post.userEmail == currentUser?.email) {
                Toast.makeText(context, "You cannot like your own post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            post.postId?.let { postId ->
                db.collection("forumPosts").document(post.postId)
                    .update("likes", FieldValue.increment(1))
                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
            }
        }

        // Dislike functionality
        holder.dislikeButton.setOnClickListener {
            if (post.userEmail == currentUser?.email) {
                Toast.makeText(context, "You cannot like your own post", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            post.postId?.let { postId ->
                db.collection("forumPosts").document(post.postId)
                    .update("dislikes", FieldValue.increment(1))
                Toast.makeText(context, "Disliked", Toast.LENGTH_SHORT).show()
            }
        }

        // Reply functionality
        holder.replyButton.setOnClickListener {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            val input = android.widget.EditText(context)
            dialog.setTitle("Reply to Post")
            dialog.setView(input)

            dialog.setPositiveButton("Send") { _, _ ->
                val replyText = input.text.toString()
                if (replyText.isNotEmpty()) {
                    val reply = Reply(userId = currentUser?.uid ?: "", userName = currentUser?.displayName ?: "", replyText = replyText)
                    post.postId.let { postId ->
                        db.collection("forumPosts").document(post.postId)
                            .update("replies", FieldValue.arrayUnion(reply))
                            .addOnSuccessListener {
                                Toast.makeText(context, "Reply posted", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }

            dialog.setNegativeButton("Cancel", null)
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return forumPostsList.size
    }
}
