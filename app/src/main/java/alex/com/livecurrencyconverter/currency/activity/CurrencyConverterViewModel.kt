package alex.com.livecurrencyconverter.currency.activity

import alex.com.livecurrencyconverter.currency.api.CurrencyAPIClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import java.io.IOException

/**
 * Created by Alex Doub on 11/13/2019.
 */

class CurrencyConverterViewModel(val currencyAPIClient: CurrencyAPIClient) : ViewModel() {

    // Observables
    val currencyData = BehaviorSubject.create<Map<String, Double>>()
    val statusText = MutableLiveData<String>()

    // Events
    val showErrorEvent = MutableLiveData<String>()
    val finishedLoadingEvent = MutableLiveData<Unit>()

    private val disposables = CompositeDisposable()

    init {
        //@@TODO: Load data from storage. API goes thru DB

        getData()
    }

    fun clearData() {
        //@@TODO: Provide DB, clear data
    }

    fun getData() {
        statusText.value = "Fetching data from server... "

        // Kick off request
        val subscription = currencyAPIClient.getLiveCurrencyList()
            .subscribeBy(
                onNext = { response ->
                    if (response.error != null) {
                        throw IOException("Error: ${response.error.info}")
                    } else if (response.quotes == null) {
                        throw IOException("Error: Network response body malformed")
                    }

                    currencyData.onNext(response.quotes)
                    statusText.postValue("Data loaded on ${response.timestamp}")
                    finishedLoadingEvent.postValue(null)
                },
                onError = { throwable ->
                    showErrorEvent.postValue(throwable.localizedMessage)
                    finishedLoadingEvent.postValue(null)
                })
        disposables.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}