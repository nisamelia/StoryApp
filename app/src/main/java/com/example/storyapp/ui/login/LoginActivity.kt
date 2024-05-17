package com.example.storyapp.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.ui.factory.StoryViewModelFactory
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

        //TODO: INI DITAMBAH
        viewModel.isLoading.observe(this) {
            isLoading ->
            if (isLoading) {
                binding.loginProgress.visibility = View.VISIBLE
            } else {
                binding.loginProgress.visibility = View.GONE
            }
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

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            // Lakukan validasi email dan password di sini jika diperlukan
//            login()
            viewModel.saveLogin(email, password)
//            showSuccessDialog()
            // Amati hasil login dari ViewModel
            viewModel.loginResult.observe(this) { response ->
                if (!response.error!!) {
                    val token = response.loginResult?.token
                    if (token != null) {
                        val userModel = UserModel(email, token, true)
                        viewModel.saveSession(userModel)

                        // Tampilkan dialog sukses
                        showSuccessDialog()
                    }
//                    showSuccessDialog()
                } else {
                    // Tampilkan dialog error jika login gagal
                    showErrorDialog()
                }
            }
//            viewModel.saveLogin(email, password)
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage("You have successfully logged in.")
            setPositiveButton("Continue") { _, _ ->
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
            setTitle("Error")
            setMessage("Login failed. Please check your credentials and try again.")
            setPositiveButton("OK") { _, _ -> }
            create()
            show()
        }
    }


    private fun login(){
        binding.apply {
            viewModel.saveLogin(
                edLoginEmail.text.toString(),
                edLoginPassword.text.toString()
            )
        }
    }
}