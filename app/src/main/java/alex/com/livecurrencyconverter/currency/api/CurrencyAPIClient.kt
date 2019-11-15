package alex.com.livecurrencyconverter.currency.api

import alex.com.livecurrencyconverter.currency.api.response.CurrencyAPIResponse
import io.reactivex.Observable

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyAPIClient(private val apiKey: String, private val currencyAPI: CurrencyAPI) {

    fun getLiveCurrencyList(): Observable<CurrencyAPIResponse> {
        return currencyAPI.getLiveCurrencyList(apiKey)
    }
}
