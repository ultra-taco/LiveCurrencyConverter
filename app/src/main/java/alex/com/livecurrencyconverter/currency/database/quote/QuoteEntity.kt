package alex.com.livecurrencyconverter.currency.database.quote

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Alex Doub on 11/16/2019.
 */

@Entity(
    tableName = "quote_table",
    indices = [Index(value = ["currency"])]
)
class QuoteEntity(
    @PrimaryKey
    val currency: String,
    val value: Double
)

//@@TODO: MOVE ME TO REPO
fun Map<String, Double>.toQuotesList(): List<QuoteEntity> = map {
    QuoteEntity(
        it.key,
        it.value
    )
}
