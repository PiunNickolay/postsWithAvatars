package ru.netology.learningandtrying.repository

import androidx.lifecycle.LiveData
import ru.netology.learningandtrying.dto.Post

interface PostRepository {
    fun getAllAsync(callback: GetAllCallback)
    fun get(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post


    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

}