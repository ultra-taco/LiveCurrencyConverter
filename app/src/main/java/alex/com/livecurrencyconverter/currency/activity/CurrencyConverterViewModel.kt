package alex.com.livecurrencyconverter.currency.activity

import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import alex.com.livecurrencyconverter.currency.database.currency.CurrencyRepository
import alex.com.livecurrencyconverter.currency.database.quote.QuoteEntity
import alex.com.livecurrencyconverter.currency.database.quote.QuoteRepository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyConverterViewModel(
    application: Application,
    private val currencyAPIClient: CurrencyAPIClient
) : ViewModel() {

    companion object {
        private const val DEFAULT_CURRENCY = "USD"    // API sets USD as the default currency
        private const val DEFAULT_AMOUNT = "1.00"
        private val DATA_STALE_DURATION = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES)

        private const val KEY_SHARED_PREFERENCES = "currency_preferences"
        private const val KEY_CURRENCIES_LAST_SAVED_AT = "currencies_last_saved_at"
        private const val KEY_QUOTES_LAST_SAVED_AT = "quotes_last_saved_at"
    }

    private val sharedPrefs = application.getSharedPreferences(
        KEY_SHARED_PREFERENCES,
        Context.MODE_PRIVATE
    )
    private val currencyRepo = CurrencyRepository(application)
    private val quotesRepo = QuoteRepository(application)

    // Observables
    val currenciesObservable = MutableLiveData<List<String>>()
    val quotesObservable = MutableLiveData<List<QuoteEntity>>()
    val isLoadingObservable = MutableLiveData<Boolean>()
    val amountObservable = MutableLiveData<String>().apply { value = DEFAULT_AMOUNT }

    // Events
    val showErrorEvent = MutableLiveData<String>()
    val showSnackbarEvent = MutableLiveData<String>()

    // Private data
    private val disposables = CompositeDisposable()
    private var sourceCurrency: String = DEFAULT_CURRENCY
    private var destinationCurrency: String? = null
    private var isLoadingCurrencies = MutableLiveData<Boolean>().apply { value = false }
    private var isLoadingQuotes = MutableLiveData<Boolean>().apply { value = false }

    init {
        observeCurrencyRepo()
        observeQuoteRepo()
        observeAmountChanges()
        observeIsLoading()
        refreshData()
    }

    fun refreshData() {
        refreshCurrencies()
        refreshQuotes()
        refreshIsLoading()
    }

    private fun observeAmountChanges() {
        amountObservable.observeForever { amount ->
            if (amount?.toDoubleOrNull() == null) {
                showSnackbarEvent.postValue("Input is not a number")
            }
            createAdjustedQuotes()
        }
    }

    private fun observeCurrencyRepo() {
        currencyRepo.currencies.observeForever { entities ->
            val strings = entities.map { it.currency }
            currenciesObservable.postValue(strings)
            isLoadingCurrencies.postValue(false)
        }
    }

    private fun refreshCurrencies() {
        val lastSavedTime = sharedPrefs.getLong(KEY_CURRENCIES_LAST_SAVED_AT, 0L)
        if (lastSavedTime + DATA_STALE_DURATION < System.currentTimeMillis()) {
            fetchCurrencies()
        }
    }

    private fun observeQuoteRepo() {
        quotesRepo.quotes.observeForever {
            createAdjustedQuotes()
        }
    }

    private fun refreshQuotes() {
        val lastSavedTime = sharedPrefs.getLong(KEY_QUOTES_LAST_SAVED_AT, 0L)
        if (lastSavedTime + DATA_STALE_DURATION < System.currentTimeMillis()) {
            fetchQuotes()
        }
    }

    private fun observeIsLoading() {
        isLoadingCurrencies.observeForever {
            refreshIsLoading()
        }
        isLoadingQuotes.observeForever {
            refreshIsLoading()
        }
    }

    private fun refreshIsLoading() {
        isLoadingObservable.postValue(isLoadingQuotes.value!! || isLoadingCurrencies.value!!)
    }

    //TODO: Save to preferences?
    fun setSourceCurrency(currency: String) {
        sourceCurrency = currency
        createAdjustedQuotes()
    }

    //TODO: Save to preferences?
    fun setDestinationCurrency(currency: String?) {
        destinationCurrency = currency
        createAdjustedQuotes()
    }

    fun clearData() {
        currencyRepo.clearData()
        quotesRepo.clearData()
        sharedPrefs.edit().clear().apply()
        showSnackbarEvent.value = "DB & Prefs cleared. Pull to refresh to fetch data from server"
    }

    private fun fetchCurrencies() {
        isLoadingCurrencies.postValue(true)
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

                            // Save to repo
                            currencyRepo.save(response.currencies)

                            // Save timestamp
                            sharedPrefs.edit()
                                .putLong(KEY_CURRENCIES_LAST_SAVED_AT, System.currentTimeMillis())
                                .apply()
                        }
                    }
                },
                onError = { throwable ->
                    showErrorEvent.postValue(throwable.localizedMessage)
                    isLoadingCurrencies.postValue(false)
                })
        disposables.add(subscription)
    }

    private fun fetchQuotes() {
        isLoadingQuotes.postValue(true)
        showSnackbarEvent.value = "Fetching quotes from server... "

        // Kick off request
        val subscription = currencyAPIClient.getQuotes()
            .subscribeBy(
                onNext = { response ->
                    when {
                        response.error != null -> throw IOException("Error fetching quotes: ${response.error.info}")
                        response.quotes == null -> throw IOException("Error fetching quotes: Network response body malformed")
                        else -> {
                            // Save to repo
                            quotesRepo.save(response.quotes)

                            // Save timestamp
                            sharedPrefs.edit()
                                .putLong(KEY_QUOTES_LAST_SAVED_AT, System.currentTimeMillis())
                                .apply()
                        }
                    }
                },
                onError = { throwable ->
                    showErrorEvent.postValue(throwable.localizedMessage)
                    isLoadingQuotes.postValue(false)
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

        // Assert repo is loaded
        val quotes = quotesRepo.quotes.value
        if (quotes == null || quotes.isEmpty()) {
            println("Quotes DB empty")
            quotesObservable.postValue(null)
            isLoadingQuotes.postValue(false)
            return
        }

        // (Debug) Assert repos are not corrupt or out of sync
        if (quotes.find { it.currency == DEFAULT_CURRENCY + sourceCurrency } == null) {
            println("Database is loaded but could not find this currency in quotes...")
            showErrorEvent.postValue("A database error has occurred, refreshing everything...")
            clearData()
            refreshData()
            return
        }

        // Find source conversion rate.
        var sourceConversionRate = 1.0
        if (sourceCurrency != DEFAULT_CURRENCY) {
            sourceConversionRate =
                quotes.find { it.currency == DEFAULT_CURRENCY + sourceCurrency }!!.value
        }

        // Update quotes according to selected source currency
        val amount = amountObservable.value?.toDoubleOrNull() ?: 0.0
        val adjustedQuotes = quotes.map {
            val newValue = amount * it.value / sourceConversionRate
            if (sourceCurrency == DEFAULT_CURRENCY) {
                QuoteEntity(
                    it.currency,
                    newValue
                )
            } else {
                val newCurrency = it.currency.replaceFirst(DEFAULT_CURRENCY, sourceCurrency)
                QuoteEntity(newCurrency, newValue)
            }
        }

        // Lastly, optionally filter to destination currency
        val filteredQuotes: List<QuoteEntity> = destinationCurrency?.let { destinationCurrency ->
            adjustedQuotes.filter { quote ->
                quote.currency.endsWith(destinationCurrency)
            }
        } ?: adjustedQuotes

        quotesObservable.postValue(filteredQuotes)
        isLoadingQuotes.postValue(false)
    }
}