package alex.com.livecurrencyconverter.currency.api

import alex.com.livecurrencyconverter.currency.api.response.CurrenciesAPIResponse
import alex.com.livecurrencyconverter.currency.api.response.QuotesAPIResponse

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyAPIClient(private val apiKey: String, private val currencyAPI: CurrencyAPI) {

    suspend fun getCurrencies(): CurrenciesAPIResponse {
        return currencyAPI.getCurrencies(apiKey)
    }

    suspend fun getQuotes(): QuotesAPIResponse {
        return currencyAPI.getLiveQuotes(apiKey)
    }
}
