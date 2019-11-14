package alex.com.livecurrencyconverter.components.api

import alex.com.livecurrencyconverter.models.Currency
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Alex Doub on 11/13/2019.
 */

interface CurrencyAPI {

    @GET("live")
    fun getCurrencyList(): Observable<List<Currency>>
}
