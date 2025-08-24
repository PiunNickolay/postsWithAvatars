package ru.netology.learningandtrying.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.learningandtrying.databinding.FragmentNewPostBinding
import ru.netology.learningandtrying.util.AndroidUtils
import ru.netology.learningandtrying.util.StringArg
import ru.netology.learningandtrying.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: PostViewModel by viewModels(ownerProducer = :: requireParentFragment)
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
       // val contentToEdit = intent.getStringExtra(Intent.EXTRA_TEXT)
     //   binding.content.setText(contentToEdit)
        binding.content.requestFocus()
        arguments?.textArg?.let(binding.content::setText)

        viewModel.postsCreated.observe(viewLifecycleOwner){
            AndroidUtils.hideKeyboard(requireView())
            viewModel.load()
            findNavController().navigateUp()
        }

        binding.ok.setOnClickListener{
            if(binding.content.text.isNotBlank()){
                val content = binding.content.text.toString()
                viewModel.changeContentAndSave(content)
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg by StringArg
    }
}