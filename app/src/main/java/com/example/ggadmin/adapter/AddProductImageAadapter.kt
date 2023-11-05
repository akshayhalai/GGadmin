package com.example.ggadmin.adapter

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ggadmin.databinding.ImageItemBinding

class AddProductImageAadapter(val list : ArrayList<Uri>) : RecyclerView.Adapter<AddProductImageAadapter.AddProductImageViewHolder>() {

    inner class AddProductImageViewHolder(val binding : ImageItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductImageViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  AddProductImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: AddProductImageViewHolder, position: Int) {
     holder.binding.itemImg.setImageURI(list[position])
    }
}