package alex.com.livecurrencyconverter.components.api

import alex.com.livecurrencyconverter.models.Currency
import io.reactivex.Observable
import okhttp3.OkHttpClient

/**
 * Created by Alex Doub on 11/13/2019.
 */

class APIClient(private val client: OkHttpClient, private val currencyAPI: CurrencyAPI) {

    fun getPullRequests(): Observable<List<Currency>> {
        return currencyAPI.getCurrencyList()
    }
}
