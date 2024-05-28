package com.example.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.StoryListBinding
import com.example.storyapp.ui.detail.DetailActivity

//class UserAdapter : PagingDataAdapter<ListStoryItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//
//    inner class MyViewHolder(private val binding: StoryListBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: ListStoryItem) {
//            Glide.with(binding.ivItemPhoto.context)
//                .load(item.photoUrl)
//                .into(binding.ivItemPhoto)
//            binding.tvUser.text = item.name
//            binding.tvItemName.text = item.description
//            binding.root.setOnClickListener {
//                onItemClickCallback.onItemClicked(item)
//            }
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = StoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = getItem(position)
//        if (item != null) {
//            holder.bind(item)
//            holder.itemView.setOnClickListener {
//                onItemClickCallback?.onItemClicked(item)
//                val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java).also {
//                    it.putExtra(DetailActivity.EXTRA_ID, item.id)
//                    it.putExtra(DetailActivity.EXTRA_NAME, item.name)
//                    it.putExtra(DetailActivity.EXTRA_DESC, item.description)
//                    it.putExtra(DetailActivity.EXTRA_PICT, item.photoUrl)
//                }
//                holder.itemView.context.startActivity(detailIntent)
//            }
//        }
////        holder.bind(item)
////        holder.itemView.setOnClickListener {
////            onItemClickCallback.onItemClicked(item)
////            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java).also {
////                it.putExtra(DetailActivity.EXTRA_ID, item.id)
////                it.putExtra(DetailActivity.EXTRA_NAME, item.name)
////                it.putExtra(DetailActivity.EXTRA_DESC, item.description)
////                it.putExtra(DetailActivity.EXTRA_PICT, item.photoUrl)
////            }
////            holder.itemView.context.startActivity(detailIntent)
////        }
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(data: ListStoryItem) {
//        }
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
//            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(
//                oldItem: ListStoryItem,
//                newItem: ListStoryItem
//            ): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}

class UserAdapter : PagingDataAdapter<ListStoryItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MyViewHolder(private val binding: StoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            Glide.with(binding.ivItemPhoto.context)
                .load(item.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvUser.text = item.name
            binding.tvItemName.text = item.description
            binding.root.setOnClickListener {
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
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(item)
                val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_ID, item.id)
                    it.putExtra(DetailActivity.EXTRA_NAME, item.name)
                    it.putExtra(DetailActivity.EXTRA_DESC, item.description)
                    it.putExtra(DetailActivity.EXTRA_PICT, item.photoUrl)
                }
                holder.itemView.context.startActivity(detailIntent)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
