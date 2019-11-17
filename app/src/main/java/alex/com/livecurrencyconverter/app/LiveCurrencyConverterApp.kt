package alex.com.livecurrencyconverter.app

import alex.com.livecurrencyconverter.app.component.app.AppComponent
import alex.com.livecurrencyconverter.app.component.app.AppModule
import alex.com.livecurrencyconverter.app.component.app.DaggerAppComponent
import alex.com.livecurrencyconverter.app.component.network.NetworkModule
import alex.com.livecurrencyconverter.currency.component.CurrencyComponent
import alex.com.livecurrencyconverter.currency.component.CurrencyModule
import alex.com.livecurrencyconverter.currency.component.DaggerCurrencyComponent
import android.app.Application
import com.facebook.stetho.Stetho

class LiveCurrencyConverterApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        val currencyComponent: CurrencyComponent by lazy {
            DaggerCurrencyComponent.builder()
                .appComponent(appComponent)
                .currencyModule(CurrencyModule()).build()
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