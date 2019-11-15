package alex.com.livecurrencyconverter.currency.api

import alex.com.livecurrencyconverter.currency.api.response.CurrencyAPIResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Alex Doub on 11/13/2019.
 */

interface CurrencyAPI {

    @GET("live")
    fun getLiveCurrencyList(@Query("access_key") apiKey: String): Observable<CurrencyAPIResponse>
}
