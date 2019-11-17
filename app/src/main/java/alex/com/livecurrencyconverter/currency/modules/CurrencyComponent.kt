package alex.com.livecurrencyconverter.currency.modules

import alex.com.livecurrencyconverter.app.modules.AppComponent
import alex.com.livecurrencyconverter.currency.activity.CurrencyConverterActivity
import dagger.Component
import javax.inject.Scope

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class CurrencyScope

@CurrencyScope
@Component(
    dependencies = [AppComponent::class],
    modules = [CurrencyDataModule::class, CurrencyAPIModule::class]
)
interface CurrencyComponent {
    fun inject(activity: CurrencyConverterActivity)
}
