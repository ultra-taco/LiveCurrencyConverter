package alex.com.livecurrencyconverter.currency.database

import alex.com.livecurrencyconverter.currency.entity.QuoteEntity
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Dao
interface QuotesDao {

    @get:Query("SELECT * from quotes_table")
    val getQuotes: LiveData<List<QuoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuotes(entity: List<QuoteEntity>)

    @Query("DELETE FROM quotes_table")
    fun deleteAll()
}
