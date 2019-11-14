package alex.com.livecurrencyconverter.components.app

import alex.com.livecurrencyconverter.components.api.APIClient
import alex.com.livecurrencyconverter.components.api.NetworkModule
import android.app.Application
import android.content.Context
import dagger.Component

/**
 * Created by Alex Doub on 11/13/2019.
 */

@AppScope
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun provideAPIClient(): APIClient

    fun provideContext(): Context

    fun provideApplication(): Application
}
