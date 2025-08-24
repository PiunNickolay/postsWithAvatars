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
//        val card = binding.post
//        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
//        val postId = arguments?.getLong("postId") ?: return binding.root
//
//        val onInteractionListener = object : OnInteractionListener {
//            override fun onLike(post: Post) {
//                viewModel.likeById(post.id)
//            }
//
//            override fun onShare(post: Post) {
//                val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                    type = "text/plain"
//                }
//                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                startActivity(chooser)
//                viewModel.shareById(post.id)
//            }
//
//            override fun onEdit(post: Post) {
//                viewModel.edit(post)
//                findNavController().navigate(
//                    R.id.action_postFragment_to_newPostFragment,
//                    Bundle().apply {
//                        putString("textArg", post.content)
//                    }
//                )
//            }
//
//            override fun onRemove(post: Post) {
//                viewModel.removeById(post.id)
//                findNavController().navigateUp()
//            }
//
//            override fun onPost(post: Post) {
//            }
//        }
//
//        val holder = PostViewHolder(card, onInteractionListener)
//
//        val posts = viewModel.data.observe(viewLifecycleOwner) { posts ->
//            val post = posts.find { it.id == postId }
//            if (post == null) {
//                Toast.makeText(
//                    requireContext(),
//                    getString(R.string.the_post_is_missing),
//                    Toast.LENGTH_LONG
//                ).show()
//                findNavController().navigateUp()
//                return@observe
//            }
//            holder.bind(post)
//        }
//
//        return binding.root
//    }
//}