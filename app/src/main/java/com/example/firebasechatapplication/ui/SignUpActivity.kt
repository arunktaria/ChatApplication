package com.example.firebasechatapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Utils.ProgressBuilders
import com.example.firebasechatapplication.Repository.Repository
import com.example.firebasechatapplication.ViewModels.Factories.LoginViewModelFactory
import com.example.firebasechatapplication.ViewModels.LoginSignUpViewModel
import com.example.firebasechatapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    lateinit var emailtext: String
    lateinit var passwordtext: String
    lateinit var cpasswordtext: String
    val context = this
    lateinit var database:DatabaseReference
    val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference.child("user").child(auth.currentUser?.uid.toString())

        val repository = Repository()
        val viewmodel =
            ViewModelProvider(
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

        binding.cpassword.doOnTextChanged { text, start, before, count ->
        if (passwordtext.equals(text.toString()))
        {
            cpasswordtext=text.toString()
        }
        else
        {
            binding.cpassword.setError("password not matched!")
        }
        }


        binding.btnSignup.setOnClickListener {
            val ob = UserData()
            ob.email = binding.email.text.toString()
            ob.password = binding.password.text.toString()
            ob.username=binding.username.text.toString()
            viewmodel.signUp(auth, context, ob,database)
            ProgressBuilders.show(context,"please wait...")

        }

    }


}