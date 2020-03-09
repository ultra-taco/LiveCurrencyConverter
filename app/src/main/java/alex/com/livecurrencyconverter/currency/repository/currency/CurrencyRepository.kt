package alex.com.livecurrencyconverter.currency.repository.currency

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alex Doub on 11/16/2019.
 */

class CurrencyRepository(context: Context, private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_CURRENCIES_LAST_SAVED_TIME = "currencies_last_saved_time"
    }

    private val currencyDao: CurrencyDao = CurrencyDatabase.getDatabase(context).currencyDao()

    fun getCurrencies(): Flow<List<CurrencyEntity>> {
        return currencyDao.getCurrencies()
    }

    suspend fun insertCurrencies(currencies: Map<String, String>) {
        //Transform to db entity
        val entities = currencies.map { CurrencyEntity(it.key, it.value) }

        // Insert data & save timestamp
        currencyDao.insertCurrencies(entities)
        sharedPreferences.edit()
            .putLong(KEY_CURRENCIES_LAST_SAVED_TIME, System.currentTimeMillis())
            .apply()
    }

    suspend fun deleteCurrencies() {
        currencyDao.deleteCurrencies()
        sharedPreferences.edit()
            .putLong(KEY_CURRENCIES_LAST_SAVED_TIME, 0L)
            .apply()
    }

    fun getLastSavedTime(): Long {
        return sharedPreferences.getLong(KEY_CURRENCIES_LAST_SAVED_TIME, 0L)
    }
}
