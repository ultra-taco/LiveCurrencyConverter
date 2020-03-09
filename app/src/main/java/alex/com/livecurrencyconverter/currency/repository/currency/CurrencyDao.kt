package alex.com.livecurrencyconverter.currency.repository.currency

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Dao
interface CurrencyDao {

    @Query("SELECT * from currency_table")
    fun getCurrencies(): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(entity: List<CurrencyEntity>)

    @Query("DELETE FROM currency_table")
    suspend fun deleteCurrencies()
}
