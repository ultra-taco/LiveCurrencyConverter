package alex.com.livecurrencyconverter.currency.database.quote

import alex.com.livecurrencyconverter.currency.database.currency.CurrencyDao
import alex.com.livecurrencyconverter.currency.database.currency.CurrencyEntity
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created by Alex Doub on 11/16/2019.
 */

class QuoteRepository(application: Application) {

    private val quoteDao: QuotesDao = QuotesDatabase.getDatabase(application).quotesDao()
    val quotes: LiveData<List<QuoteEntity>> = quoteDao.getQuotes

    fun save(quotes: Map<String, Double>) {
        //Transform to db entity
        val data = quotes.toList().map {
            QuoteEntity(it.first, it.second)
        }

        InsertAsyncTask(quoteDao).execute(data)
    }

    fun clearData() {
        DeleteAsyncTask(quoteDao).execute()
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: QuotesDao) :
        AsyncTask<List<QuoteEntity>, Void, Void>() {

        override fun doInBackground(vararg data: List<QuoteEntity>): Void? {
            mAsyncTaskDao.insertQuotes(data[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val mAsyncTaskDao: QuotesDao) :
        AsyncTask<Unit, Void, Void>() {

        override fun doInBackground(vararg data: Unit): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}