package com.org.jordanapp.datamodel

data class ForumPost(
    var postId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userImageUrl: String = "",
    val postTitle: String = "",
    val postContent: String = "",
    val timestamp: Long = 0L,
    var likes: Int = 0,       // Added likes count
    var dislikes: Int = 0,    // Added dislikes count
    var replies: List<Reply> = emptyList()  // Added replies field
)
