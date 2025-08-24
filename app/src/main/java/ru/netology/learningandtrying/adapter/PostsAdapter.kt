package ru.netology.learningandtrying.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.netology.learningandtrying.Counts
import ru.netology.learningandtrying.R
import ru.netology.learningandtrying.databinding.CardPostBinding
import ru.netology.learningandtrying.dto.Post


interface OnInteractionListener{
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onPost(post: Post)
}
class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) = with(binding) {
        root.setOnClickListener{
            onInteractionListener.onPost(post)
        }
        author.text = post.author
//        published.text = post.published
        content.text = post.content
        like.apply {
            isChecked = post.likedByMe
            text = post.likes.toString()
        }
        share.text = Counts.countFormat(post.shareCount)
        view.text = Counts.countFormat(post.viewCount)
        like.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        like.isClickable = true

        share.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        share.isClickable = true

        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.post_actions)
                setOnMenuItemClickListener {item->
                    when(item.itemId){
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }
                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
        menu.isClickable = true

        if (!post.video.isNullOrBlank()) {
            binding.videoContainer.visibility = View.VISIBLE
            binding.playButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, post.video!!.toUri())
                it.context.startActivity(intent)
            }
            playButton.isClickable = true
        } else {
            binding.videoContainer.visibility = View.GONE
        }
    }

}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean = oldItem == newItem


}