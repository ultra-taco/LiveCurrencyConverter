package alex.com.livecurrencyconverter.app

import alex.com.livecurrencyconverter.activities.components.CurrencyConverterComponent
import alex.com.livecurrencyconverter.activities.components.CurrencyConverterModule
import alex.com.livecurrencyconverter.activities.components.DaggerCurrencyConverterComponent
import alex.com.livecurrencyconverter.app.components.NetworkModule
import alex.com.livecurrencyconverter.app.components.AppComponent
import alex.com.livecurrencyconverter.app.components.AppModule
import alex.com.livecurrencyconverter.app.components.DaggerAppComponent

import android.app.Application
import com.facebook.stetho.Stetho

class LiveCurrencyConverterApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        val currencyConverterComponent: CurrencyConverterComponent by lazy {
            DaggerCurrencyConverterComponent.builder()
                .appComponent(appComponent)
                .currencyConverterModule(CurrencyConverterModule()).build()
        }
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