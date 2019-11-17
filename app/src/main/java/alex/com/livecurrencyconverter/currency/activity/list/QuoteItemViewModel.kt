package alex.com.livecurrencyconverter.currency.activity.list

import alex.com.livecurrencyconverter.currency.entity.QuoteEntity

/**
 * Created by Alex Doub on 11/16/2019.
 */

class QuoteItemViewModel (data: QuoteEntity) {

    companion object {
        private const val MAX_DIGITS = 8
    }

    val currency = data.currency.substring(0, 3) + " -> " + data.currency.substring(3)
    val value = if (data.value == 0.0) {
        "--"    // In case of bad conversion data
    } else {
        val stringValue = data.value.toString()
        val indexOfExponent = stringValue.indexOf("E")

        //Edge case -- the number is a massive exponent. Trim only the decimal component
        if (indexOfExponent > 0 ) {
            val exponentSubstring = stringValue.substringAfter("E")
            val startingLength = MAX_DIGITS - exponentSubstring.length
            stringValue.substringBefore("E").take(startingLength) + "E" + exponentSubstring
        } else {
            stringValue.take(MAX_DIGITS)
        }
    }
}