package ru.netology.learningandtrying.repository

import androidx.lifecycle.LiveData
import ru.netology.learningandtrying.dto.Post

interface PostRepository {
    fun get(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post) : Post

}