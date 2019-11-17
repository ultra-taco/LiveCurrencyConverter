package alex.com.livecurrencyconverter.currency.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Entity(
    tableName = "quotes_table",
    indices = [Index(value = ["currency"])]
)
class QuoteEntity(
    @PrimaryKey
    val currency: String,
    val value: Double
)

fun Map<String, Double>.toQuotesList(): List<QuoteEntity> = map { QuoteEntity(it.key, it.value) }
