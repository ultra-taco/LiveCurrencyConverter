package alex.com.livecurrencyconverter.currency.api.response

open class BaseAPIResponse {
    // Success 
    val privacy: String? = null

    val source: String? = null
    val success: Boolean? = null
    val terms: String? = null
    val timestamp: Long? = null

    //Error
    val error: ErrorResponse? = null
}