package com.org.jordanapp.datamodel

data class Message(
    val senderEmail: String = "",
    val recipientEmail: String = "",
    val messageText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
