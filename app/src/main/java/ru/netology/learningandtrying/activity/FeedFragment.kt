package ru.netology.learningandtrying.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.learningandtrying.R
import ru.netology.learningandtrying.activity.NewPostFragment.Companion.textArg
import ru.netology.learningandtrying.adapter.OnInteractionListener
import ru.netology.learningandtrying.adapter.PostsAdapter
import ru.netology.learningandtrying.databinding.FragmentFeedBinding
import ru.netology.learningandtrying.dto.Post
import ru.netology.learningandtrying.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = :: requireParentFragment)
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val sharedIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(sharedIntent)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content })
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment,
                    bundleOf("postId" to post.id)
                    )
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner){state->
            adapter.submitList(state.posts)
            binding.errorGroup.isVisible = state.isError
            binding.errorText.text = state.errorToString(requireContext())
            binding.loading.isVisible = state.loading
            binding.empty.isVisible = state.empty
        }

        binding.fab.setOnClickListener {
            viewModel.cancelEdit()
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

//        viewModel.edited.observe(viewLifecycleOwner) {
//            if (it.id != 0L) {
//                newPostLauncher.launch(it.content)
//            }
//        }

        return binding.root
    }
}