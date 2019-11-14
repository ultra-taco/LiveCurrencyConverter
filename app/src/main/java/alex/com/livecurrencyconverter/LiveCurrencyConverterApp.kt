package alex.com.livecurrencyconverter

import alex.com.livecurrencyconverter.components.api.NetworkModule
import alex.com.livecurrencyconverter.components.app.AppComponent
import alex.com.livecurrencyconverter.components.app.AppModule
import alex.com.livecurrencyconverter.components.app.DaggerAppComponent
import android.app.Application
import com.facebook.stetho.Stetho

class LiveCurrencyConverterApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .build()
    }
}