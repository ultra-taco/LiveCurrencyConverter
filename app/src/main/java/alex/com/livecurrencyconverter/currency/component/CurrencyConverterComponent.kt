package alex.com.livecurrencyconverter.currency.component

import alex.com.livecurrencyconverter.currency.activity.CurrencyConverterActivity
import alex.com.livecurrencyconverter.app.component.app.AppComponent
import dagger.Component

/**
 * Created by Alex Doub on 11/13/2019.
 */

@CurrencyConverterScope
@Component(dependencies = [AppComponent::class], modules = [CurrencyConverterModule::class])
interface CurrencyConverterComponent {
    fun inject(activity: CurrencyConverterActivity)
}
