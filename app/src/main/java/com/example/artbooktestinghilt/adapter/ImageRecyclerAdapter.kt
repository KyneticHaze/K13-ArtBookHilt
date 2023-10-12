package com.example.artbooktestinghilt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.artbooktestinghilt.R
import javax.inject.Inject

class ImageRecyclerAdapter @Inject constructor(
    val glide: RequestManager
): RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var onItemClickListener: ((String) -> Unit)? = null

    /**
     * Bu fonksiyon değişkeni, bir güncelleme olduğunda iki item arasındaki ilişkiyi takip eder.
     * [DiffUtil] sınıfı recyclerView'dan gelen bir widget'tır.
     */
    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
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
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
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
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var images: List<String>
        /// mean: güncel listeyi getir
        get() = recyclerListDiffer.currentList
        /// mean: yeni listeyi buraya yerleştir
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_row, parent, false)
        return ImageViewHolder(view)
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.singleArtImageView)
        val url = images[position]

        holder.itemView.apply {
            glide
                .load(url)
                .into(imageView)

            setOnClickListener {
                onItemClickListener?.let {
                    it(url)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}