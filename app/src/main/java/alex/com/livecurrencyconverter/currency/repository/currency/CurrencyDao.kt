package alex.com.livecurrencyconverter.currency.repository.currency

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Dao
interface CurrencyDao {

    @get:Query("SELECT * from currency_table")
    val getCurrencies: LiveData<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencies(entity: List<CurrencyEntity>)

    @Query("DELETE FROM currency_table")
    fun deleteAll()
}
