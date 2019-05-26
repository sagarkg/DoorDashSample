package com.sagarganatra.doordashsample.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.repository.LoginRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory @Inject constructor(
    private val loginRepository: LoginRepository,
    private val schedulers: RxSchedulers
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository,
                schedulers
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}