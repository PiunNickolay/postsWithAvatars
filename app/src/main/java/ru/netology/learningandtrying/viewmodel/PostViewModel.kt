    package ru.netology.learningandtrying.viewmodel

    import android.app.Application
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import ru.netology.learningandtrying.db.AppDb
    import ru.netology.learningandtrying.dto.Post
    import ru.netology.learningandtrying.model.FeedModel
    import ru.netology.learningandtrying.repository.PostRepository
    import ru.netology.learningandtrying.repository.PostRepositoryRoomImpl
    import ru.netology.learningandtrying.util.SingleLiveEvent
    import kotlin.concurrent.thread

    private val empty = Post(
        id = 0,
        author = "",
        content = "",
        published = 0L,
        likedByMe = false,
        likes = 0
    )

    class PostViewModel(application: Application) : AndroidViewModel(application) {
        private val repository: PostRepository = PostRepositoryRoomImpl(
            AppDb.getIstance(application).postDao
        )
        private val _data = MutableLiveData(FeedModel())
        val data: LiveData<FeedModel>
            get() = _data

        private val _postsCreated = SingleLiveEvent<Unit>()
        val postsCreated: LiveData<Unit>
            get() = _postsCreated

        init {
            load()
        }

        fun load() {
            thread {
                _data.postValue(FeedModel(loading = true))
                try {
                    val posts = repository.get()
                    FeedModel(posts = posts, empty = posts.isEmpty())
                } catch (e: Exception) {
                    FeedModel(error = e)
                }.also(_data::postValue)
            }
        }

        val edited = MutableLiveData(empty)
        val draft = MutableLiveData<String?>()
        fun likeById(id: Long) {
            thread {
                repository.likeById(id)
                val posts = repository.get()
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }
        }
        fun shareById(id: Long) = repository.shareById(id)
        fun removeById(id: Long) = repository.removeById(id)
        fun changeContentAndSave(text: String) {
            thread {
                edited.value?.let {
                    if (it.content != text) {
                        repository.save(it.copy(content = text))
                        _postsCreated.postValue(Unit)
                    }
                }
                edited.postValue(empty)
            }
        }

        fun edit(post: Post) {
            edited.value = post
        }

        fun cancelEdit() {
            edited.value = empty
        }
    }