package ru.netology.learningandtrying.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.learningandtrying.dao.PostDao
import ru.netology.learningandtrying.dto.Post
import ru.netology.learningandtrying.entity.PostEntity
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class PostRepositoryRoomImpl(private val dao: PostDao) : PostRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private companion object {
        const val BASE_URL = "http://10.0.2.2:9999"
        val jsonType = "application/json".toMediaType()
        val postsType: Type = object : TypeToken<List<Post>>() {}.type
    }

    override fun get(): List<Post> {
        val call = okHttpClient.newCall(
            Request.Builder()
                .url("$BASE_URL/api/slow/posts")
                .build()
        )
        val response = call.execute()

        val responseText = response.body?.string() ?: error("Response body is null")

        return gson.fromJson(responseText, postsType)
    }

    override fun likeById(id: Long) {
        thread {
            try{
                val post = dao.getById(id)?.toDto() ?: return@thread
                val request = Request.Builder()
                    .url("$BASE_URL/api/posts/$id/likes")
                    .method(
                        if (!post.likedByMe) "POST" else "DELETE",
                        if (!post.likedByMe) "".toRequestBody(null) else null
                    )
                    .build()
                val response = okHttpClient.newCall(request).execute()
                val body = response.body?.string() ?: return@thread
                val updatedPost = gson.fromJson(body, Post::class.java)

                dao.insert(PostEntity.fromDto(updatedPost))
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun shareById(id: Long) {
        TODO()
    }

    override fun removeById(id: Long) {
        TODO()
    }

    override fun save(post: Post): Post {
        val call = okHttpClient.newCall(
            Request.Builder()
                .url("$BASE_URL/api/posts")
                .post(gson.toJson(post).toRequestBody(jsonType))
                .build()
        )
        val response = call.execute()
        val responseText = response.body?.string() ?: error("Response body is null")

        val savedPost = gson.fromJson(responseText, Post::class.java)

        dao.insert(PostEntity.fromDto(savedPost))

        return savedPost
    }

}