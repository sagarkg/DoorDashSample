package com.sagarganatra.doordashsample.di.modules

import android.content.Context
import com.sagarganatra.doordashsample.App
import com.sagarganatra.doordashsample.utils.hasNetwork
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class HttpClientModule {

    @Provides
    fun provideHttpClientInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Named("cached")
    fun provideHttpClientWithCache(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .cache(cache)
            .build()
    }


    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .build()
    }


    @Provides
    fun provideCacheInterceptor(
        context: Context
    ): Interceptor {
        return Interceptor {
            val response = it.proceed(it.request())

            if(hasNetwork(context)!!) { // if has network max-age is 2 min
                response.newBuilder().removeHeader("pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age =" + 120)
                    .build()

            } else { // If no network. max-stale is 7 days
                response.newBuilder().removeHeader("pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale =" + 60 * 60 * 24 * 7)
                    .build()
            }

        }
    }

    @Provides
    fun provideHttpCache(app: App) : Cache {
        val sizeOfCache = (10 * 1024 * 1024).toLong() // 10 MB
        return Cache(File(app.cacheDir, "http"), sizeOfCache)
    }
}