package com.example.storyapp.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ui.factory.StoryViewModelFactory
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.add.AddStoryActivity
import com.example.storyapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel> {
        StoryViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        binding.btnAdd.setOnClickListener {
            intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.listProgress.visibility = View.VISIBLE
            } else {
                binding.listProgress.visibility = View.GONE
            }

        }
        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                mainViewModel.setStories()
            }
        }

        mainViewModel.story.observe(this) { storyResponse ->
            if (!storyResponse.error) {
                adapter.submitList(storyResponse.listStory)
            } else {
                Toast.makeText(
                    this,
                    storyResponse.message ?: "Error fetching stories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        setupView()
        setupAction()

        adapter = UserAdapter()
        binding.rvStory.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {

            }
        })
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.materialToolbar.setOnMenuItemClickListener(object :
            MenuItem.OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.btnLogout -> {
                        mainViewModel.logout()
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        })
    }

}