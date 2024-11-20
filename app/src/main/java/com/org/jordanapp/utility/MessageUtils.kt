package com.org.jordanapp.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.org.jordanapp.R
import com.org.jordanapp.datamodel.Message
import java.util.Locale

object MessageUtils {

    fun showSendMessageDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.send_message_dialog_layout, null)
        val recipientEmailField = dialogView.findViewById<EditText>(R.id.etRecipientEmail)
        val messageTextField = dialogView.findViewById<EditText>(R.id.etMessageText)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Send Message")
            .setView(dialogView)
            .setPositiveButton("Send") { _, _ ->
                val recipientEmail = recipientEmailField.text.toString().trim()
                    .lowercase(Locale.ROOT)
                val messageText = messageTextField.text.toString().trim()

                if (recipientEmail.isNotEmpty() && messageText.isNotEmpty()) {
                    sendMessageToUser(context, recipientEmail, messageText)
                } else {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun sendMessageToUser(context: Context, recipientEmail: String, messageText: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val senderEmail = currentUser?.email ?: "Unknown"

        val message = Message(
            senderEmail = senderEmail,
            recipientEmail = recipientEmail,
            messageText = messageText
        )

        FirebaseFirestore.getInstance().collection("messages")
            .add(message)
            .addOnSuccessListener {
                Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
            }
    }
}
