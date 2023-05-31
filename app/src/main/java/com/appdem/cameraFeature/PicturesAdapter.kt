package com.appdem.cameraFeature

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdem.databinding.ItemPictureRecViewBinding

class PicturesAdapter(private var pictures: List<Bitmap>) :
    RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder>() {

    private lateinit var binding: ItemPictureRecViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturesViewHolder {
        binding =
            ItemPictureRecViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PicturesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PicturesViewHolder, position: Int) {
        val currentItem = pictures[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = pictures.size

    inner class PicturesViewHolder(private val binding: ItemPictureRecViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bitmapPicNote: Bitmap) {
            binding.ivPicNote.setImageBitmap(bitmapPicNote)
        }
    }
}