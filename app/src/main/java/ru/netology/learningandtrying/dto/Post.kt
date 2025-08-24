package ru.netology.learningandtrying.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
) {
    var shareCount: Int = 0
    var viewCount: Int = 0
    var video: String? = null
}