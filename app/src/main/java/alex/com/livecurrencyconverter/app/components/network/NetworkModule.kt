package alex.com.livecurrencyconverter.app.components.network

import alex.com.livecurrencyconverter.other.Constants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
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
        val gson = GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STRICT)
            .serializeNulls()
            .create()

        val factory = GsonConverterFactory.create(gson)

        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(factory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

}

