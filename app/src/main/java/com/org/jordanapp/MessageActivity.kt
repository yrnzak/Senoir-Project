package com.org.jordanapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.org.jordanapp.adapters.MessageAdapter
import com.org.jordanapp.datamodel.Message
import com.org.jordanapp.utils.MessageUtils

class MessageActivity : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private val messages = ArrayList<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mymessage)

        supportActionBar?.title = "Inbox"

        messageListView = findViewById(R.id.messageListView)
        messageAdapter = MessageAdapter(this, messages)
        messageListView.adapter = messageAdapter

        // Fetch messages from Firestore
        fetchMessages()

        // Handle message click to show full details
        messageListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            showFullMessageDialog(messages[position])
        }
    }

    private fun fetchMessages() {
        val recipientEmail = currentUser?.email
        if (recipientEmail != null) {
            db.collection("messages")
                .whereEqualTo("recipientEmail", recipientEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    messages.clear()
                    for (document in querySnapshot) {
                        val message = document.toObject(Message::class.java)
                        messages.add(message)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showFullMessageDialog(message: Message) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Message from ${message.senderEmail}")
        dialogBuilder.setMessage(message.messageText)

        dialogBuilder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
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
}
