package alex.com.livecurrencyconverter.app

import alex.com.livecurrencyconverter.app.modules.AppComponent
import alex.com.livecurrencyconverter.app.modules.AppModule
import alex.com.livecurrencyconverter.app.modules.DaggerAppComponent
import alex.com.livecurrencyconverter.app.modules.NetworkModule
import alex.com.livecurrencyconverter.currency.modules.CurrencyAPIModule
import alex.com.livecurrencyconverter.currency.modules.CurrencyComponent
import alex.com.livecurrencyconverter.currency.modules.CurrencyDataModule
import alex.com.livecurrencyconverter.currency.modules.DaggerCurrencyComponent
import android.app.Application
import com.facebook.stetho.Stetho

class LiveCurrencyConverterApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent

        // The currency component lives here so if any other activities/fragments would
        // use it, they would share the same data without duplicating the modules
        val currencyComponent: CurrencyComponent by lazy {
            DaggerCurrencyComponent.builder()
                .appComponent(appComponent)
                .currencyAPIModule(CurrencyAPIModule())
                .currencyDataModule(CurrencyDataModule())
                .build()
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