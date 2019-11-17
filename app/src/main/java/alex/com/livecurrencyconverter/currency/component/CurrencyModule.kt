package alex.com.livecurrencyconverter.currency.component

import alex.com.livecurrencyconverter.currency.api.CurrencyAPI
import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.currency.repository.currency.CurrencyRepository
import alex.com.livecurrencyconverter.currency.repository.quote.QuoteRepository
import alex.com.livecurrencyconverter.other.CurrencyConverterConstants
import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Module
class CurrencyModule {
    @Provides
    internal fun provideCurrencyAPIClient(retrofit: Retrofit): CurrencyAPIClient {
        val currencyAPI = retrofit.create(CurrencyAPI::class.java)
        return CurrencyAPIClient(CurrencyConverterConstants.API_KEY, currencyAPI)
    }

    @Provides
    internal fun provideCurrencyRepository(context: Context): CurrencyRepository {
        return CurrencyRepository(context)
    }

    @Provides
    internal fun provideQuoteRepository(context: Context): QuoteRepository {
        return QuoteRepository(context)
    }
}