package alex.com.livecurrencyconverter.app.components.app

import alex.com.livecurrencyconverter.app.components.network.NetworkModule
import android.app.Application
import android.content.Context
import dagger.Component
import retrofit2.Retrofit

/**
 * Created by Alex Doub on 11/13/2019.
 */

@AppScope
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun provideContext(): Context

    fun provideApplication(): Application

    fun provideRetrofit(): Retrofit
}
