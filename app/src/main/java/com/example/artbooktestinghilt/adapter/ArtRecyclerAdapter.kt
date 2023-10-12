package com.example.artbooktestinghilt.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.artbooktestinghilt.R
import com.example.artbooktestinghilt.roomdb.Art
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide: RequestManager
): RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() {

    class ArtViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    /**
     * Bu fonksiyon değişkeni, bir güncelleme olduğunda iki item arasındaki ilişkiyi takip eder.
     * [DiffUtil] sınıfı recyclerView'dan gelen bir widget'tır.
     */
    private val diffUtil = object : DiffUtil.ItemCallback<Art>() {
        /**
         * Bu method itemlerin aynı olup olmadığını kontrol eder.
         *
         * @param oldItem
         * @param newItem
         *
         * @return [Boolean]
         * @author furkanharmanci
         * @since 1.0
         */
        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
        /**
         * Bu method içeriklerin aynı olup olmadığını kontrol eder.
         *
         * @param oldItem
         * @param newItem
         *
         * @return [Boolean]
         * @author furkanharmanci
         * @since 1.0
         */
        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var arts: List<Art>
        /// mean: güncel listeyi getir
        get() = recyclerListDiffer.currentList
        /// mean: yeni listeyi buraya yerleştir
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.art_row,parent,false)
        return ArtViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.artRowImageView)
        val nameText = holder.itemView.findViewById<TextView>(R.id.artRowNameText)
        val artistNameText = holder.itemView.findViewById<TextView>(R.id.artistRowNameText)
        val yearText = holder.itemView.findViewById<TextView>(R.id.yearRowNameText)

        val art = arts[position]

        holder.itemView.apply {
            nameText.text = "Name ${art.name}"
            artistNameText.text = "Artist Name ${art.artistName}"
            yearText.text = "Year ${art.year}"
            glide.load(art.imageUrl).into(imageView)
        }

    }

    override fun getItemCount(): Int {
        return arts.size
    }
}