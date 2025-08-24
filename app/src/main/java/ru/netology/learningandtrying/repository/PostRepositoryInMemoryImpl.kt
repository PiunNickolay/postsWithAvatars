//package ru.netology.learningandtrying.repository
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import ru.netology.learningandtrying.dto.Post
//
//class PostRepositoryInMemoryImpl : PostRepository {
//    private var nextId = 1L
//    private var posts = listOf(
//        Post(
//            id = nextId++,
//            "Нетология. Университет интернет-профессий будущего",
//            "21 мая в 18:36",
//            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            false,
//            100,
//            100,
//            1000,
//            "https://rutube.ru/video/a329fd8e9b587186a0ea3699b775ce54/?r=wd"
//        ),
//        Post(
//            id = nextId++,
//            "Нетология. Университет интернет-профессий будущего",
//            "21 мая в 18:36",
//            "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
//            false,
//            1000,
//            1000,
//            10000
//        )
//    )
//
//    private val data = MutableLiveData(posts)
//
//    override fun get(): LiveData<List<Post>> = data
//
//    override fun likeById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(
//                likedByMe = !it.likedByMe,
//                likeCount = it.likeCount + if (it.likedByMe) -1 else 1
//            )
//        }
//        data.value = posts
//    }
//
//    override fun shareById(id: Long) {
//        posts = posts.map {
//            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
//        }
//        data.value = posts
//    }
//
//    override fun removeById(id: Long) {
//        posts = posts.filter { it.id != id }
//        data.value = posts
//    }
//
//    override fun save(post: Post) {
//        posts = if (post.id == 0L) {
//            listOf(post.copy(id = nextId++, author = "Me")) + posts
//        }else{
//            posts.map{ if (it.id != post.id) it else it.copy(content = post.content)}
//        }
//        data.value = posts
//    }
//}