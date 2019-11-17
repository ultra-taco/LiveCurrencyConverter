package alex.com.livecurrencyconverter.app.modules

import alex.com.livecurrencyconverter.other.CurrencyConverterConstants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Module
class NetworkModule {

    @Provides
    internal fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .readTimeout(10, SECONDS)
            .writeTimeout(10, SECONDS)
            .connectTimeout(10, SECONDS)
            .build()
    }

    @Provides
    internal fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gsonBuilder = GsonBuilder()
            .create()
        val gsonConverterFactory = GsonConverterFactory
            .create(gsonBuilder)

        return Retrofit.Builder()
            .client(client)
            .baseUrl(CurrencyConverterConstants.BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

}

