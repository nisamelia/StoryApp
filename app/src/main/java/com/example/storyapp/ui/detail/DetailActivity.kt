package com.example.storyapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyapp.ui.factory.StoryViewModelFactory
import com.example.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_PICT = "extra_pict"
    }

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        StoryViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)

        val bundle = Bundle()
        bundle.putString(EXTRA_ID, id)

        detailViewModel.setDetail(id.toString())

        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.detailProgress.visibility = View.VISIBLE
            } else {
                binding.detailProgress.visibility = View.GONE
            }
        }
        detailViewModel.detail.observe(this) { storyResponse ->
            if (!storyResponse.error!!) {
                storyResponse.story?.let { story ->
                    binding.tvDetailName.text = story.name
                    binding.tvDetailDescription.text = story.description
                    Glide.with(binding.ivDetailPhoto.context)
                        .load(story.photoUrl)
                        .into(binding.ivDetailPhoto)
                }
            }
        }
    }
}