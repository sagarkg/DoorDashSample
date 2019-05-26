package com.sagarganatra.doordashsample.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxrelay2.PublishRelay
import com.sagarganatra.doordashsample.App
import com.sagarganatra.doordashsample.R
import com.sagarganatra.doordashsample.di.components.DaggerLoginComponent
import com.sagarganatra.doordashsample.di.components.LoginComponent
import com.sagarganatra.doordashsample.models.LoginRequest
import com.sagarganatra.doordashsample.ui.restaurantlist.RestaurantListActivity
import com.sagarganatra.doordashsample.utils.createLoadingDialog
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory
    private lateinit var loginComponent: LoginComponent
    private lateinit var loginViewModel: LoginViewModel
    private val actionStream: PublishRelay<LoginViewModel.LoginAction> = PublishRelay.create()
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        alertDialog = createLoadingDialog(resources.getString(R.string.singing_in))

        loginComponent = DaggerLoginComponent.builder()
            .appComponent((application as App).appComponent)
            .build()
        loginComponent.inject(this)

        loginViewModel = ViewModelProviders.of(this, loginViewModelFactory)
            .get(LoginViewModel::class.java)

        loginViewModel.getLoginLiveData().observe(this, Observer {
            if(it != null) this.handleLoginState(it)
        })

        // Send ActionStream to ViewModel
        loginViewModel.attach(actionStream)



        loginButton.setOnClickListener{
            // Add validations
            if(!userNameEditText.text.isNullOrEmpty() && !passwordEditText.text.isNullOrEmpty()) {
                actionStream.accept(LoginViewModel.LoginAction.Login(LoginRequest(
                    userNameEditText.text.toString(),
                    passwordEditText.text.toString())))
            } else {
                // Add message for username and password cannot be empty
                if(userNameEditText.text.isEmpty()) userNameEditText.error =
                    resources.getString(R.string.username_cannot_be_empty)
                if(passwordEditText.text.isEmpty()) passwordEditText.error =
                    resources.getString(R.string.password_cannot_be_empty)
            }
        }

        actionStream.accept(LoginViewModel.LoginAction.CheckIfTokenExists)
    }

    private fun handleLoginState(state: LoginViewModel.LoginState) {
        when(state) {
            LoginViewModel.LoginState.Loading -> {
                alertDialog.show()
            }
            is LoginViewModel.LoginState.Error -> {
                alertDialog.dismiss()
                // Show Error
            }
            is LoginViewModel.LoginState.LoginSuccess -> {
                alertDialog.dismiss()
                navigateNext()
            }
            is LoginViewModel.LoginState.NavigateNext -> {
                alertDialog.dismiss()
                // Navigate to next screen. Handle Navigate next
                navigateNext()
            }

        }
    }


    /**
     * This could be moved to a NavigationManager class which can be injected and called from ViewModel too.
     */
    private fun navigateNext() {
        val intent = Intent(this, RestaurantListActivity::class.java)
        startActivity(intent)
    }
}