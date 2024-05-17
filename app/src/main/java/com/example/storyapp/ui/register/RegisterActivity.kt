package com.example.storyapp.ui.register

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
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var factory: ViewModelFactory
//    private val registerViewModel: RegisterViewModel by viewModels { factory }
    private val registerViewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(this)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        //TODO: INI DIUBAH
        registerViewModel.isLoading.observe(this){
            isLoading ->
            if (isLoading) {
                binding.registerProgress.visibility = View.VISIBLE
            } else {
                binding.registerProgress.visibility = View.GONE
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
        binding.signupButton.setOnClickListener {
//            val name = binding.edRegisterName.text.toString()
//            val email = binding.edRegisterEmail.text.toString()
//            val password = binding.edRegisterPassword.toString()
            postRegister()
            //TODO: INI DIGANTI
            registerViewModel.registerResult.observe(this) {
                response ->
                if (!response.error!!) {
                    showSuccessDialog()
                } else {
                    showErrorDialog()
                }
            }
//            AlertDialog.Builder(this).apply {
//                setTitle("Yeah!")
//                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
//                setPositiveButton("Lanjut") { _, _ ->
//                    loginActivity()
//                    finish()
//                }
//                create()
//                show()
//            }
        }
    }

//    private fun loginActivity() {
//        registerViewModel.registerResponse.observe(this@RegisterActivity) {
//            reponse ->
//            if (reponse.error == false) {
//                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//                finish()
//            }
//        }
//    }

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

    private fun showErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage("Login failed. Please check your credentials and try again.")
            setPositiveButton("OK") { _, _ -> }
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
//        registerViewModel.registerResponse.observe(this@RegisterActivity) {
//                reponse ->
//            if (reponse.error == false) {
//                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//                finish()
//            }
//        }
    }
}