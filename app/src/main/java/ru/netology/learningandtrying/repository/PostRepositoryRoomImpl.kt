package ru.netology.learningandtrying.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.learningandtrying.dao.PostDao
import ru.netology.learningandtrying.dto.Post
import ru.netology.learningandtrying.entity.PostEntity
import java.io.IOException
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

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {
        val request = Request.Builder()
            .url("$BASE_URL/api/slow/posts")
            .build()

        okHttpClient.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val posts =
                            response.body?.string() ?: throw RuntimeException("Body is null")
                        callback.onSuccess(gson.fromJson(posts, postsType))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }

    override fun likeById(id: Long) {
        val current = dao.getById(id)?.toDto() ?: return
        val request = Request.Builder()
            .url("$BASE_URL/api/posts/$id/likes")
            .method(
                if (!current.likedByMe) "POST" else "DELETE",
                if (!current.likedByMe) "".toRequestBody(null) else null
            )
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    val updated = gson.fromJson(body, Post::class.java)
                    dao.insert(PostEntity.fromDto(updated))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    override fun shareById(id: Long) {
        TODO()
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            .url("$BASE_URL/api/posts/$id")
            .delete()
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        dao.removeById(id)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("$BASE_URL/api/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    val saved = gson.fromJson(body, Post::class.java)
                    dao.insert(PostEntity.fromDto(saved))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        return post
    }

}