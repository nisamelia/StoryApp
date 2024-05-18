package com.example.storyapp.ui.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.loginProgress.visibility = View.VISIBLE
            } else {
                binding.loginProgress.visibility = View.GONE
            }
        }

        viewModel.isError.observe(this) {isError ->
            Toast.makeText(this, isError, Toast.LENGTH_SHORT).show()
        }
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.saveLogin(email, password)
            viewModel.loginResult.observe(this) { response ->
                if (!response.error!!) {
                    val token = response.loginResult?.token
                    if (token != null) {
                        val userModel = UserModel(email, token, true)
                        viewModel.saveSession(userModel)
                        showSuccessDialog()
                    } else {
                        showErrorDialog()
                    }
                }
            }
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Sukses")
            setMessage("Yeay, Kamu Berhasil Masuk")
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setNegativeButton("Masuk Gagal") { _, _ ->
                Toast.makeText(context, "Masuk Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}