package com.example.artbooktestinghilt.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.example.artbooktestinghilt.R
import com.example.artbooktestinghilt.adapter.ImageRecyclerAdapter
import com.example.artbooktestinghilt.databinding.FragmentImageApiBinding
import com.example.artbooktestinghilt.util.Status
import com.example.artbooktestinghilt.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageApiFragment @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter
): Fragment(R.layout.fragment_image_api) {

    private lateinit var viewModel: ArtViewModel
    private var fragmentBinding: FragmentImageApiBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]
        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        var job: Job? = null

        /// EditText'e her yazıldığında bu çağırılacak
        binding.searchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000) // 1 sn
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding.imageRecyclerView.adapter = imageRecyclerAdapter

        /// spanCount 3 -> 3 görsel yan yana gösterilecek
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)

        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }

                    imageRecyclerAdapter.images = urls ?: listOf()

                    fragmentBinding?.progressBar?.visibility = View.GONE
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                    fragmentBinding?.progressBar?.visibility = View.GONE
                }

                Status.LOADING -> {
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE
                }
            }
        })
    }
}