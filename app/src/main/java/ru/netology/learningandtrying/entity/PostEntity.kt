package ru.netology.learningandtrying.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.learningandtrying.dto.Post


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shareCount: Int = 0,
    val viewCount: Int = 0,
    val video: String? = null
) {
    fun toDto() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes
    ).apply {
        shareCount = this@PostEntity.shareCount
        viewCount = this@PostEntity.viewCount
        video = this@PostEntity.video
    }

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.published,
            post.likedByMe,
            post.likes,
            post.shareCount,
            post.viewCount,
            post.video,
        )
    }
}
