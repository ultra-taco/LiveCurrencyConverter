package alex.com.livecurrencyconverter.currency.api

import alex.com.livecurrencyconverter.currency.api.response.CurrenciesAPIResponse
import alex.com.livecurrencyconverter.currency.api.response.QuotesAPIResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Alex Doub on 11/13/2019.
 */

interface CurrencyAPI {

    @GET("list")
    suspend fun getCurrencies(@Query("access_key") apiKey: String): CurrenciesAPIResponse

    @GET("live")
    suspend fun getLiveQuotes(@Query("access_key") apiKey: String): QuotesAPIResponse
}
