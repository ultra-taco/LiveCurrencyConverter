package alex.com.livecurrencyconverter.currency.modules

import alex.com.livecurrencyconverter.currency.api.CurrencyAPI
import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.other.CurrencyConverterConstants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Module
class CurrencyAPIModule {
    @Provides
    internal fun provideCurrencyAPIClient(retrofit: Retrofit): CurrencyAPIClient {
        val currencyAPI = retrofit.create(CurrencyAPI::class.java)
        return CurrencyAPIClient(CurrencyConverterConstants.API_KEY, currencyAPI)
    }
}