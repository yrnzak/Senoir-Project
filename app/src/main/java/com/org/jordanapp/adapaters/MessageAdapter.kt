package com.org.jordanapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.org.jordanapp.R
import com.org.jordanapp.datamodel.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(context: Context, private val messages: ArrayList<Message>) :
    ArrayAdapter<Message>(context, 0, messages) {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)

        // Reuse convertView if possible, otherwise inflate a new layout
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)

        val senderTextView = view.findViewById<TextView>(R.id.senderTextView)
        val timestampTextView = view.findViewById<TextView>(R.id.timestampTextView)
        val partialMessageTextView = view.findViewById<TextView>(R.id.partialMessageTextView)

        // Set the message data
        senderTextView.text = message?.senderEmail
        timestampTextView.text = dateFormat.format(Date(message?.timestamp ?: 0))

        // Show partial message (first 30 characters or full message if shorter)
        val partialMessage = if (message?.messageText?.length ?: 0 > 30) {
            "${message?.messageText?.substring(0, 30)}..."
        } else {
            message?.messageText
        }
        partialMessageTextView.text = HtmlCompat.fromHtml(partialMessage ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)

        return view
    }
}
