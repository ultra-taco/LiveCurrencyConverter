package alex.com.livecurrencyconverter.currency.database.currency

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Entity(
    tableName = "currency_table",
    indices = [Index(value = ["currency"])]
)
class CurrencyEntity(
    @PrimaryKey
    val currency: String,
    val name: String
)
