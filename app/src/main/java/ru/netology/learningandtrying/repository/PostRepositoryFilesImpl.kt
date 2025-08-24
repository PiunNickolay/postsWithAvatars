//package ru.netology.learningandtrying.repository
//
//import android.content.Context
//import androidx.core.content.edit
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import ru.netology.learningandtrying.dto.Post
//
//class PostRepositoryFilesImpl(private val context: Context) : PostRepository {
//    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
//    private var nextId = 1L
//    private var posts = emptyList<Post>()
//        set(value){
//            field = value
//            data.value = value
//            sync()
//        }
//    private val data = MutableLiveData(posts)
//
//    init {
//        val file = context.filesDir.resolve(FILENAME)
//        if(file.exists()){
//            context.openFileInput(FILENAME).bufferedReader().use{
//                posts = gson.fromJson(it, type)
//                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
//            }
//        }
//    }
//
//    override fun get(): List<Post> = data
//
//    override fun likeById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                likedByMe = !it.likedByMe,
//                likeCount = it.likeCount + if (it.likedByMe) -1 else 1
//            )
//        }
//    }
//
//    override fun shareById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
//        }
//    }
//
//    override fun removeById(id: Long) {
//        posts = posts.filter { it.id != id }
//    }
//
//    override fun save(post: Post) {
//        posts = if (post.id == 0L) {
//            listOf(post.copy(id = nextId++, author = "Me")) + posts
//        } else {
//            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
//        }
//    }
//
//    private fun sync(){
//        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).bufferedWriter().use {
//            it.write(gson.toJson(posts))
//        }
//    }
//
//    companion object {
//        private const val FILENAME = "posts.json"
//        private val gson = Gson()
//        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
//    }
//}