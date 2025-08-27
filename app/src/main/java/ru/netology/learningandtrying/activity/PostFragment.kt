//package ru.netology.learningandtrying.activity
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.PopupMenu
//import android.widget.Toast
//import androidx.core.net.toUri
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import ru.netology.learningandtrying.Counts
//import ru.netology.learningandtrying.R
//import ru.netology.learningandtrying.adapter.OnInteractionListener
//import ru.netology.learningandtrying.adapter.PostViewHolder
//import ru.netology.learningandtrying.databinding.FragmentPostBinding
//import ru.netology.learningandtrying.dto.Post
//import ru.netology.learningandtrying.viewmodel.PostViewModel
//
//class PostFragment : Fragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val binding = FragmentPostBinding.inflate(inflater, container, false)
//        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
//        val postId = arguments?.getLong("postId") ?: return binding.root
//
//        viewModel.data.observe(viewLifecycleOwner) { state -> val posts = state.posts
//            val post = posts.find { it.id == postId } ?: return@observe
//
//            binding.apply {
//
//
//                val url = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
//                Glide.with(binding.avatar)
//                    .load(url)
//                    .placeholder(R.drawable.ic_not_image)
//                    .error(R.drawable.ic_error)
//                    .timeout(10_000)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(binding.avatar)
//
//
//
//                author.text = post.author
//                published.text = post.published
//                content.text = post.content
//
//                like.isChecked = post.likedByMe
//                like.text = formatCount(post.likes)
//
//                when (post.attachment?.type) {
//                    VIDEO -> {
//                        attachment.visibility = View.VISIBLE
//
////                    val url = "http://10.0.2.2:9999/images/${"play.jpg"}"
////                    Glide.with(binding.attachment)
////                        .load(url)
////                        .placeholder(R.drawable.ic_not_image)
////                        .error(R.drawable.ic_error)
////                        .timeout(10_000)
////                        .into(binding.attachment)
//
//                        attachment.setOnClickListener {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.attachment.url))
//                            startActivity(intent)
//                        }
//                    }
//
//                    IMAGE -> {
//                        attachment.visibility = View.VISIBLE
//                        val url = "http://10.0.2.2:9999/images/${post.attachment.url}"
//                        Glide.with(binding.attachment).load(url)
//                            .placeholder(R.drawable.ic_not_image)
//                            .error(R.drawable.ic_error)
//                            .timeout(10_000)
//                            .into(binding.attachment)
//                    }
//
//                    null -> attachment.visibility = View.GONE
//                }
//
//
//
//                share.isChecked = post.sharedByMe
//                share.text = formatCount(post.shares)
//
//
//
//
//                like.setOnClickListener { viewModel.likeByID(post.id) }
//
//                share.setOnClickListener { val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                    type = "text/plain"
//                }
//                    val shareIntent =
//                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                    startActivity(shareIntent) }
//
//                menu.setOnClickListener {
//                    PopupMenu(it.context, it).apply {
//                        inflate(R.menu.menu_post)
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.remove -> {
//                                    viewModel.removeByID(post.id)
//                                    findNavController().navigateUp()
//
//                                    true
//                                }
//
//                                R.id.edit -> {
//                                    viewModel.edit(post)
//                                    findNavController().navigate(
//                                        R.id.action_postFragment_to_newPostFragment,
//                                        Bundle().apply { textArg = post.content })
//                                    true
//                                }
//
//                                else -> {
//                                    false
//                                }
//                            }
//                        }
//                    }.show()
//                }
//
//            }
//        }
//
//        return binding.root
//    }
//}