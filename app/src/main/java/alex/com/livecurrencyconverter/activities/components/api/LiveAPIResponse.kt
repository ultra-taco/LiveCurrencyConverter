package alex.com.livecurrencyconverter.activities.components.api

class LiveAPIResponse(
    val privacy: String,
    val quotes: Map<String, Double>,
    val source: String,
    val success: Boolean,
    val terms: String,
    val timestamp: Long
)