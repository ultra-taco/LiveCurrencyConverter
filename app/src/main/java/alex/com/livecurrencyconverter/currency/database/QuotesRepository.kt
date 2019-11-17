package alex.com.livecurrencyconverter.currency.database

import alex.com.livecurrencyconverter.currency.entity.QuoteEntity
import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created by Alex Doub on 11/16/2019.
 */

class QuotesRepository(application: Application) {

    private val quotesDao: QuotesDao = QuotesDatabase.getDatabase(application).quotesDao()
    val quotes: LiveData<List<QuoteEntity>> = quotesDao.getQuotes

    fun save(quotes: Map<String, Double>) {
        //Transform to db entity
        val data = quotes.toList().map {
            QuoteEntity(it.first, it.second)
        }

        InsertAsyncTask(quotesDao).execute(data)
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: QuotesDao) :
        AsyncTask<List<QuoteEntity>, Void, Void>() {

        override fun doInBackground(vararg data: List<QuoteEntity>): Void? {
            mAsyncTaskDao.insertQuotes(data[0])
            return null
        }
    }
}
