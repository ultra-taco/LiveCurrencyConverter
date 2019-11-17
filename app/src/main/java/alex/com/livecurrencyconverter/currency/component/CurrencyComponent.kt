package alex.com.livecurrencyconverter.currency.component

import alex.com.livecurrencyconverter.app.component.app.AppComponent
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
@Component(dependencies = [AppComponent::class], modules = [CurrencyModule::class])
interface CurrencyComponent {
    fun inject(activity: CurrencyConverterActivity)
}
