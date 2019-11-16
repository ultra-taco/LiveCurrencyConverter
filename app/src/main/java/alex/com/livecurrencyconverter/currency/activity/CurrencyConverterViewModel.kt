package alex.com.livecurrencyconverter.currency.activity

import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.currency.entity.Quote
import alex.com.livecurrencyconverter.currency.entity.toQuotesList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.IOException

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyConverterViewModel(private val currencyAPIClient: CurrencyAPIClient) : ViewModel() {

    companion object {
        private const val DEFAULT_CURRENCY = "USD"    // API sets USD as the default currency
        private const val DEFAULT_AMOUNT = "1.00"
    }

    // Observables
    val currenciesObservable = MutableLiveData<List<String>>()
    val quotesObservable = MutableLiveData<List<Quote>>()
    val isLoadingObservable = MutableLiveData<Boolean>()
    val amountObservable = MutableLiveData<String>().apply { value = DEFAULT_AMOUNT }

    // Events
    val showErrorEvent = MutableLiveData<String>()
    val showSnackbarEvent = MutableLiveData<String>()

    // Private data
    private val disposables = CompositeDisposable()
    private var baseQuotes: List<Quote> = emptyList()
    private var sourceCurrency: String = DEFAULT_CURRENCY
    private var destinationCurrency: String? = null

    init {
        observeAmountChanges()
        getCurrencyList()
        getQuotes()
    }

    private fun observeAmountChanges() {
        amountObservable.observeForever { amount ->
            if (amount?.toDoubleOrNull() == null) {
                showSnackbarEvent.postValue("Input is not a number")
            }
            createAdjustedQuotes()
        }
    }

    fun setSourceCurrency(currency: String) {
        sourceCurrency = currency
        createAdjustedQuotes()
    }

    fun setDestinationCurrency(currency: String?) {
        destinationCurrency = currency
        createAdjustedQuotes()
    }

    fun clearData() {
        //@@TODO: Provide DB, clear data
    }

    private fun getCurrencyList() {
        isLoadingObservable.postValue(true)
        showSnackbarEvent.value = "Fetching currencies from server... "

        // Kick off request
        val subscription = currencyAPIClient.getCurrencies()
            .subscribeBy(
                onNext = { response ->
                    when {
                        response.error != null -> throw IOException("Error fetching currencies: ${response.error.info}")
                        response.currencies == null -> throw IOException("Error fetching currencies: Network response body malformed")
                        response.currencies.isEmpty() -> throw IOException("Error fetching currencies: No data returned")
                        else -> {
                            val currencies = response.currencies.map { it.key }.sorted()
                            currenciesObservable.postValue(currencies)
                            showSnackbarEvent.postValue("Currencies loaded on ${response.timestamp}")
                            isLoadingObservable.postValue(false)
                        }
                    }
                },
                onError = { throwable ->
                    showErrorEvent.postValue(throwable.localizedMessage)
                    isLoadingObservable.postValue(false)
                })
        disposables.add(subscription)
    }

    fun getQuotes() {
        isLoadingObservable.postValue(true)
        showSnackbarEvent.value = "Fetching quotes from server... "

        // Kick off request
        val subscription = currencyAPIClient.getQuotes()
            .subscribeBy(
                onNext = { response ->
                    when {
                        response.error != null -> throw IOException("Error fetching quotes: ${response.error.info}")
                        response.quotes == null -> throw IOException("Error fetching quotes: Network response body malformed")
                        else -> {
                            baseQuotes = response.quotes.toQuotesList()
                            createAdjustedQuotes()

                            showSnackbarEvent.postValue("Quotes loaded on ${response.timestamp}")
                            isLoadingObservable.postValue(false)
                        }
                    }
                },
                onError = { throwable ->
                    showErrorEvent.postValue(throwable.localizedMessage)
                    isLoadingObservable.postValue(false)
                })
        disposables.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /**
     * Transform baseQuotes into an adjusted quote list based
     * on selected source, destination and amount.
     * */
    private fun createAdjustedQuotes() {

        // Find source conversion rate.
        val sourceConversionRate: Double = if (sourceCurrency == DEFAULT_CURRENCY) {
            1.0
        } else {
            baseQuotes.find { it.currency == DEFAULT_CURRENCY + sourceCurrency }!!.value
        }

        // Update quotes according to selected source currency
        val amount = amountObservable.value?.toDoubleOrNull() ?: 0.0
        val adjustedQuotes = baseQuotes.map {
            val newValue = amount * it.value / sourceConversionRate
            if (sourceCurrency == DEFAULT_CURRENCY) {
                Quote(it.currency, newValue)
            } else {
                val newCurrency = it.currency.replaceFirst(DEFAULT_CURRENCY, sourceCurrency)
                Quote(newCurrency, newValue)
            }
        }

        // Lastly, optionally filter to destination currency
        val filteredQuotes: List<Quote> = destinationCurrency?.let { destinationCurrency ->
            adjustedQuotes.filter { quote ->
                quote.currency.endsWith(destinationCurrency)
            }
        } ?: adjustedQuotes

        quotesObservable.postValue(filteredQuotes)
    }
}