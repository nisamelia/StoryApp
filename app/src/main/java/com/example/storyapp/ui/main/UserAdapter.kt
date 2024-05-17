package com.example.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.StoryListBinding
import com.example.storyapp.ui.detail.DetailActivity

class UserAdapter : ListAdapter<ListStoryItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class MyViewHolder(val binding: StoryListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem){
            Glide.with(binding.imgStory.context)
                .load(item.photoUrl)
                .into(binding.imgStory)
            binding.tvUser.text = item.name
            binding.tvDesc.text = item.description
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java).also {
                it.putExtra(DetailActivity.EXTRA_ID, item.id)
                it.putExtra(DetailActivity.EXTRA_NAME, item.name)
                it.putExtra(DetailActivity.EXTRA_DESC, item.description)
                it.putExtra(DetailActivity.EXTRA_PICT, item.photoUrl)
            }
            holder.itemView.context.startActivity(detailIntent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem) {
        }
    }

    companion object{
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}