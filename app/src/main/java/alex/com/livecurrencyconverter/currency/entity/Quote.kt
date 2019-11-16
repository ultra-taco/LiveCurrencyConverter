package alex.com.livecurrencyconverter.currency.entity

/**
 * Created by Alex Doub on 11/16/2019.
 */

class Quote (val currency: String, val value: Double)

fun Map<String, Double>.toQuotesList(): List<Quote> = map { Quote(it.key, it.value) }