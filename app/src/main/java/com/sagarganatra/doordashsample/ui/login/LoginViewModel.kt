package com.sagarganatra.doordashsample.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sagarganatra.doordashsample.core.RxSchedulers
import com.sagarganatra.doordashsample.models.LoginRequest
import com.sagarganatra.doordashsample.models.TokenResponse
import com.sagarganatra.doordashsample.repository.LoginRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import timber.log.Timber

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val schedulers: RxSchedulers
) : ViewModel() {

    private var actionSubscription = Disposables.disposed()
    private var subscriptions = CompositeDisposable()
    private val loginLiveData = MutableLiveData<LoginState>()

    fun getLoginLiveData(): LiveData<LoginState> {
        return loginLiveData
    }

    fun attach(actionStream: Observable<LoginAction>) {
        actionSubscription.dispose()
        subscriptions = CompositeDisposable()
        actionSubscription = actionStream.subscribe(
            {handleLoginActions(it)},
            {error -> Timber.e(error)}
        )
    }

    private fun handleLoginActions(action: LoginAction) {
        when(action) {
            is LoginAction.Login -> {login(action.loginRequest)}
            LoginAction.CheckIfTokenExists -> {
                if(loginRepository.checkIfTokenExists()) {
                    loginLiveData.postValue(LoginState.NavigateNext(true))
                }
            }
        }
    }

    private fun login(loginRequest: LoginRequest) {
        loginLiveData.postValue(LoginState.Loading)
        subscriptions.add(loginRepository.getAuthToken(loginRequest)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {response -> loginLiveData.postValue(LoginState.LoginSuccess(response))},
                {
                    Timber.e(it.message)
                    loginLiveData.postValue(LoginState.Error("Login Failed. Please try again later")) }
            )
    )}


    sealed class LoginState {
        object Loading: LoginState()
        data class Error(
            val message: String
        ): LoginState()
        data class LoginSuccess (
            val tokenResponse: TokenResponse
        ): LoginState()
        data class NavigateNext(
            val navigateNext: Boolean
        ): LoginState()
    }

    sealed class LoginAction {
        data class Login(
           val loginRequest: LoginRequest
        ): LoginAction()
        object CheckIfTokenExists: LoginAction()
    }


    override fun onCleared() {
        super.onCleared()
        actionSubscription.dispose()
        subscriptions.dispose()
    }
}