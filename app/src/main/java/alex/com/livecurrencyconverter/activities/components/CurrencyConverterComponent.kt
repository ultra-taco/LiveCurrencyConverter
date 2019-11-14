package alex.com.livecurrencyconverter.activities.components

import alex.com.livecurrencyconverter.activities.CurrencyConverterActivity
import alex.com.livecurrencyconverter.app.components.AppComponent
import dagger.Component

/**
 * Created by Alex Doub on 11/13/2019.
 */

@CurrencyConverterScope
@Component(dependencies = [AppComponent::class], modules = [CurrencyConverterModule::class])
interface CurrencyConverterComponent {
    fun inject(activity: CurrencyConverterActivity)
}
