package alex.com.livecurrencyconverter.activities.components.api

import io.reactivex.Observable

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyAPIClient(private val apiKey: String, private val currencyAPI: CurrencyAPI) {

    fun getLiveCurrencyList(): Observable<LiveAPIResponse> {
        return currencyAPI.getLiveCurrencyList(apiKey)
    }
}
