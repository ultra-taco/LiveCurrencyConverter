package alex.com.livecurrencyconverter.app.modules

import android.content.Context
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Scope

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
internal annotation class AppScope

@AppScope
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun provideContext(): Context

    fun provideRetrofit(): Retrofit
}
