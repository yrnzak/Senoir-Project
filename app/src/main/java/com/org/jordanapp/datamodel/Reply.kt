package com.org.jordanapp.datamodel

data class Reply(
    val userId: String = "",
    val userName: String = "",
    val replyText: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
