package alex.com.livecurrencyconverter.app

import alex.com.livecurrencyconverter.app.component.app.AppComponent
import alex.com.livecurrencyconverter.app.component.app.AppModule
import alex.com.livecurrencyconverter.app.component.app.DaggerAppComponent
import alex.com.livecurrencyconverter.app.component.network.NetworkModule
import alex.com.livecurrencyconverter.currency.component.CurrencyConverterComponent
import alex.com.livecurrencyconverter.currency.component.CurrencyConverterModule
import alex.com.livecurrencyconverter.currency.component.DaggerCurrencyConverterComponent
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