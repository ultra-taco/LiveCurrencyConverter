package alex.com.livecurrencyconverter.activities.components.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Alex Doub on 11/13/2019.
 */

interface CurrencyAPI {

    @GET("live")
    fun getLiveCurrencyList(@Query("api_key") apiKey: String): Observable<LiveAPIResponse>
}
