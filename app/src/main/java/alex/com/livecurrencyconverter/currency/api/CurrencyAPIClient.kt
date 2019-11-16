package alex.com.livecurrencyconverter.currency.api

import alex.com.livecurrencyconverter.currency.api.response.CurrenciesAPIResponse
import alex.com.livecurrencyconverter.currency.api.response.QuotesAPIResponse
import io.reactivex.Observable

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyAPIClient(private val apiKey: String, private val currencyAPI: CurrencyAPI) {

    fun getCurrencies(): Observable<CurrenciesAPIResponse> {
        return currencyAPI.getCurrencies(apiKey)
    }

    fun getQuotes(): Observable<QuotesAPIResponse> {
        return currencyAPI.getLiveQuotes(apiKey)
    }
}
