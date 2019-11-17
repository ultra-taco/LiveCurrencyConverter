package alex.com.livecurrencyconverter.currency.modules

import alex.com.livecurrencyconverter.currency.repository.currency.CurrencyRepository
import alex.com.livecurrencyconverter.currency.repository.quote.QuoteRepository
import alex.com.livecurrencyconverter.other.CurrencyConverterConstants
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Module
class CurrencyDataModule {
    @Provides
    internal fun provideCurrencySharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            CurrencyConverterConstants.KEY_CURRENCY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    internal fun provideCurrencyRepository(
        context: Context,
        sharedPreferences: SharedPreferences
    ): CurrencyRepository {
        return CurrencyRepository(context, sharedPreferences)
    }

    @Provides
    internal fun provideQuoteRepository(
        context: Context,
        sharedPreferences: SharedPreferences
    ): QuoteRepository {
        return QuoteRepository(context, sharedPreferences)
    }
}