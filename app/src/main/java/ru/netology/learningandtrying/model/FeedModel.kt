package ru.netology.learningandtrying.model

import android.content.Context
import okio.IOException
import ru.netology.learningandtrying.R
import ru.netology.learningandtrying.dto.Post

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val error: Throwable? = null,
    val loading: Boolean = false,
    val empty: Boolean = false,
){
    val isError: Boolean = error != null

    fun errorToString(context: Context) : String = when(error){
        is IOException -> context.getString(R.string.network_error)
        else->context.getString(R.string.unknown_error)
    }
}