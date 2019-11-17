package alex.com.livecurrencyconverter.currency.database.currency

import alex.com.livecurrencyconverter.currency.database.quote.QuoteEntity
import alex.com.livecurrencyconverter.currency.database.quote.QuoteRepository
import alex.com.livecurrencyconverter.currency.database.quote.QuotesDao
import alex.com.livecurrencyconverter.currency.database.quote.QuotesDatabase
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created by Alex Doub on 11/16/2019.
 */

class CurrencyRepository(application: Application) {

    private val currencyDao: CurrencyDao = CurrencyDatabase.getDatabase(application).currencyDao()
    val currencies: LiveData<List<CurrencyEntity>> = currencyDao.getCurrencies

    fun save(currencies: Map<String, String>) {
        //Transform to db entity
        val data = currencies.map { CurrencyEntity(it.key, it.value) }

        InsertAsyncTask(currencyDao).execute(data)
    }

    fun clearData() {
        DeleteAsyncTask(currencyDao).execute()
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
