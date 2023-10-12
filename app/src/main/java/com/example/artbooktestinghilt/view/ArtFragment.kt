package com.example.artbooktestinghilt.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artbooktestinghilt.R
import com.example.artbooktestinghilt.adapter.ArtRecyclerAdapter
import com.example.artbooktestinghilt.databinding.FragmentArtsBinding
import com.example.artbooktestinghilt.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    val artRecyclerAdapter: ArtRecyclerAdapter
): Fragment(R.layout.fragment_arts) {

    private var fragmentBinding: FragmentArtsBinding? = null
    private lateinit var viewModel: ArtViewModel

    /**
     * Bu fonksiyon değişkeni recyclerView'daki itemlerin kaydırılmasından sorumludur.
     * [ItemTouchHelper] fonksiyonu reyclerView sınıfı içerisinde bir sınıftır.
     * @param [dragDirs] kaç kere sürüklenmesi gerektiğini
     * @param [swipeDirs] hangi yöne sürüklenmesi gerektiğini
     */
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        /**
         * Sürüklendiğinde ne yapılması gerektiğini açıklayan fonksiyon
         */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            /// silinecek itemin pozisyonunu belirtmek
            val layoutPosition = viewHolder.layoutPosition
            /// recyclerAdapter'in içindeki art listesinden o pozisyonu göstermek
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            /// seçilen itemi silmek
            viewModel.deleteArt(selectedArt)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ArtViewModel::class.java]

        val binding = FragmentArtsBinding.bind(view)
        fragmentBinding = binding

        subscribeToObservers()

        binding.recyclerViewArt.adapter = artRecyclerAdapter
        binding.recyclerViewArt.layoutManager = LinearLayoutManager(requireContext())

        /// sürükleme işlevini recyclerView'a bağlamak
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewArt)

        binding.fab.setOnClickListener {
            findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())
        }
    }

    private fun subscribeToObservers() {
        viewModel.artList.observe(viewLifecycleOwner, Observer {
            artRecyclerAdapter.arts = it
        })
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}