package alex.com.livecurrencyconverter.currency.repository.quote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Dao
interface QuotesDao {

    @Query("SELECT * from quote_table")
    fun getQuotes(): Flow<List<QuoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(entity: List<QuoteEntity>)

    @Query("DELETE FROM quote_table")
    suspend fun deleteQuotes()
}
