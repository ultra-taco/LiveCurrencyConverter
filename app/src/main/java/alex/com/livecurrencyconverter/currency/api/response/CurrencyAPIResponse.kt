package alex.com.livecurrencyconverter.currency.api.response

class CurrencyAPIResponse(
    // Success
    val privacy: String?,
    val quotes: Map<String, Double>?,
    val source: String?,
    val success: Boolean?,
    val terms: String?,
    val timestamp: Long?,

    //Error
    val error: ErrorResponse?
)