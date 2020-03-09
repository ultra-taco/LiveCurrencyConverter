package alex.com.livecurrencyconverter.currency.repository.quote

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alex Doub on 11/16/2019.
 */

class QuoteRepository(context: Context, private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_QUOTES_LAST_SAVED_TIME = "quotes_last_saved_time"
    }

    private val quoteDao: QuotesDao = QuotesDatabase.getDatabase(context).quotesDao()

    fun getQuotes(): Flow<List<QuoteEntity>> {
        return quoteDao.getQuotes()
    }

    suspend fun insertQuotes(quotes: Map<String, Double>) {
        //Transform to db entity
        val entities = quotes.toList().map {
            QuoteEntity(it.first, it.second)
        }

        // Insert data & save timestamp
        quoteDao.insertQuotes(entities)
        sharedPreferences.edit()
            .putLong(KEY_QUOTES_LAST_SAVED_TIME, System.currentTimeMillis())
            .apply()
    }

    suspend fun deleteQuotes() {
        quoteDao.deleteQuotes()
        sharedPreferences.edit()
            .putLong(KEY_QUOTES_LAST_SAVED_TIME, 0L)
            .apply()
    }

    fun getLastSavedTime(): Long {
        return sharedPreferences.getLong(KEY_QUOTES_LAST_SAVED_TIME, 0L)
    }
}
