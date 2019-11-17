package alex.com.livecurrencyconverter.currency.modules

import alex.com.livecurrencyconverter.currency.repository.currency.CurrencyRepository
import alex.com.livecurrencyconverter.currency.repository.quote.QuoteRepository
import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by Alex Doub on 11/13/2019.
 */

@Module
class CurrencyDataModule {
    @Provides
    internal fun provideCurrencyRepository(context: Context): CurrencyRepository {
        return CurrencyRepository(context)
    }

    @Provides
    internal fun provideQuoteRepository(context: Context): QuoteRepository {
        return QuoteRepository(context)
    }
}