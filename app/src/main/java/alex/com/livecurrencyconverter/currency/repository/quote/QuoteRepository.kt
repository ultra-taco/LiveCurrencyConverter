package alex.com.livecurrencyconverter.currency.repository.quote

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created by Alex Doub on 11/16/2019.
 */

class QuoteRepository(context: Context, private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_QUOTES_LAST_SAVED_TIME = "quotes_last_saved_time"
    }

    private val quoteDao: QuotesDao = QuotesDatabase.getDatabase(context).quotesDao()
    val quotes: LiveData<List<QuoteEntity>> = quoteDao.getQuotes

    fun save(quotes: Map<String, Double>) {
        //Transform to db entity
        val data = quotes.toList().map {
            QuoteEntity(it.first, it.second)
        }

        // Insert data & save timestamp
        InsertAsyncTask(quoteDao).execute(data)
        sharedPreferences.edit()
            .putLong(KEY_QUOTES_LAST_SAVED_TIME, System.currentTimeMillis())
            .apply()
    }

    fun clearData() {
        DeleteAsyncTask(quoteDao).execute()
        sharedPreferences.edit()
            .putLong(KEY_QUOTES_LAST_SAVED_TIME, 0L)
            .apply()
    }

    fun getLastSavedTime(): Long {
        return sharedPreferences.getLong(KEY_QUOTES_LAST_SAVED_TIME, 0L)
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
