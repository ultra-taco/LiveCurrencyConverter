package alex.com.livecurrencyconverter.currency.repository.currency

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created by Alex Doub on 11/16/2019.
 */

class CurrencyRepository(context: Context, private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_CURRENCIES_LAST_SAVED_TIME = "currencies_last_saved_time"
    }

    private val currencyDao: CurrencyDao = CurrencyDatabase.getDatabase(context).currencyDao()
    val currencies: LiveData<List<CurrencyEntity>> = currencyDao.getCurrencies

    fun save(currencies: Map<String, String>) {
        //Transform to db entity
        val data = currencies.map { CurrencyEntity(it.key, it.value) }

        // Insert data & save timestamp
        InsertAsyncTask(currencyDao).execute(data)
        sharedPreferences.edit()
            .putLong(KEY_CURRENCIES_LAST_SAVED_TIME, System.currentTimeMillis())
            .apply()
    }

    fun clearData() {
        DeleteAsyncTask(currencyDao).execute()
        sharedPreferences.edit()
            .putLong(KEY_CURRENCIES_LAST_SAVED_TIME, 0L)
            .apply()
    }

    fun getLastSavedTime(): Long {
        return sharedPreferences.getLong(KEY_CURRENCIES_LAST_SAVED_TIME, 0L)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: CurrencyDao) :
        AsyncTask<List<CurrencyEntity>, Void, Void>() {

        override fun doInBackground(vararg data: List<CurrencyEntity>): Void? {
            mAsyncTaskDao.insertCurrencies(data[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val mAsyncTaskDao: CurrencyDao) :
        AsyncTask<Unit, Void, Void>() {

        override fun doInBackground(vararg data: Unit): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}
