package com.example.firebasechatapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Repository.Repository
import com.example.firebasechatapplication.ViewModels.Factories.LoginViewModelFactory
import com.example.firebasechatapplication.ViewModels.LoginSignUpViewModel
import com.example.firebasechatapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var emailtext: String
    lateinit var passwordtext: String
    val context = this
   lateinit var databaseReference:DatabaseReference
    val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference=FirebaseDatabase.getInstance().reference.child("users")
        auth = FirebaseAuth.getInstance()
        val repository = Repository()
        val viewmodel = ViewModelProvider(
            this,
            LoginViewModelFactory(repository)
        ).get(LoginSignUpViewModel::class.java)

        binding.email.doOnTextChanged { text, start, before, count ->
            if (emailPattern.matcher(text).matches()) {
                emailtext = text.toString()
            } else {
                binding.email.setError("Format Error")
            }

        }
        binding.password.doOnTextChanged { text, start, before, count ->
            if (text?.length!! > 6) {
                passwordtext = text.toString()
            } else {
                binding.password.setError("password must be >=6")
            }

        }
        binding.login.setOnClickListener {
            if (binding.email.text.toString().isEmpty() && binding.password.text.toString().isEmpty()) {

                binding.email.setError("Required!")
                binding.password.setError("Required!")
            } else if (binding.email.toString().equals("")) {
                binding.email.setError("Required!")
            } else if (binding.password.toString().equals("")) {
                binding.password.setError("Required!")
            }else {
                val ob = UserData()
                ob.email = binding.email.text.toString()
                ob.password = binding.password.text.toString()

                viewmodel.Login(auth, context, ob)
            }

        }

        binding.SignUptextview.setOnClickListener()
        {
            startActivity(Intent(LoginActivity@ this, SignUpActivity::class.java))
        }

    }

}
