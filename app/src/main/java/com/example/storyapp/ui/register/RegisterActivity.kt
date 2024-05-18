package com.example.storyapp.ui.register

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
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        registerViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.registerProgress.visibility = View.VISIBLE
            } else {
                binding.registerProgress.visibility = View.GONE
            }
        }

        registerViewModel.errorReg.observe(this) { isError: String ->
            Toast.makeText(this, isError, Toast.LENGTH_SHORT).show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
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
    binding.signupButton.setOnClickListener {
        postRegister()

        // Observe the register result
        registerViewModel.registerResult.observe(this) { response ->
            if (response != null) {
                if (!response.error!!) {
                    showSuccessDialog()
                } else {
                    val errorMessage = response.message ?: "Error occurred"
                    showToast(errorMessage)
                }
            } else {
                showToast("Unexpected error")
            }
        }
    }
}

    private fun loginActivity() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    private fun showSuccessDialog() {
        val email = binding.edRegisterEmail.text.toString()
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
            setPositiveButton("Lanjut") { _, _ ->
                loginActivity()
                finish()
            }
            create()
            show()
        }
    }

    private fun postRegister() {
        binding.apply {
            registerViewModel.setRegister(
                edRegisterName.text.toString().trim(),
                edRegisterEmail.text.toString().trim(),
                edRegisterPassword.text.toString().trim()
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}