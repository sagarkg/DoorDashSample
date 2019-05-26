package com.sagarganatra.doordashsample.di.components

import com.sagarganatra.doordashsample.di.modules.LoginModule
import com.sagarganatra.doordashsample.di.modules.PerActivity
import com.sagarganatra.doordashsample.di.modules.RetrofitModule
import com.sagarganatra.doordashsample.ui.login.LoginActivity
import dagger.Component

@PerActivity
@Component(
    dependencies = [AppComponent::class],
    modules = [LoginModule::class, RetrofitModule::class]
)
interface LoginComponent{
    fun inject(loginActivity: LoginActivity)
}