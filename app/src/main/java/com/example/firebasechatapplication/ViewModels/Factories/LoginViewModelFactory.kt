package com.example.firebasechatapplication.ViewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.ViewModels.LoginSignUpViewModel
import com.example.firebasechatapplication.Repository.Repository

class LoginViewModelFactory(val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginSignUpViewModel(repository) as T
    }


}